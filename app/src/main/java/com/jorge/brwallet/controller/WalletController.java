package com.jorge.brwallet.controller;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jorge.brwallet.dao.AssetDAO;
import com.jorge.brwallet.dao.LiabilityDAO;
import com.jorge.brwallet.dao.WalletDAO;
import com.jorge.brwallet.exceptions.DeleteWalletExceptionError;
import com.jorge.brwallet.exceptions.LoadWalletErrorException;
import com.jorge.brwallet.exceptions.SaveWalletErrorException;
import com.jorge.brwallet.model.Wallet;
import com.jorge.brwallet.model.Asset;
import com.jorge.brwallet.model.Liability;
import com.jorge.brwallet.model.Operation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

public class WalletController extends AppCompatActivity {

    private ArrayList<Liability> liabilities;
    private ArrayList<Asset> assets;
    private ArrayList<Operation> operations;

    private BigDecimal totalPassivos = new BigDecimal(0);
    private BigDecimal totalAtivos = new BigDecimal(0);
    private BigDecimal totalGeral = new BigDecimal(0);

    private static WalletController instance;
    private Wallet currentWallet;
    private AssetDAO assetDAO;
    private LiabilityDAO liabilityDAO;
    private WalletDAO walletDAO;

    private WalletController(Context context){
        assetDAO = new AssetDAO(context);
        liabilityDAO = new LiabilityDAO(context);
        walletDAO = new WalletDAO(context);
        currentWallet = new Wallet(0, null);
        liabilities = new ArrayList<Liability>();
        assets = new ArrayList<Asset>();
        operations = new ArrayList<Operation>();
    }

    public static synchronized WalletController getInstance(Context context){
        if (instance == null)
            instance = new WalletController(context);
        return instance;
    }
/*
* ArrayList<Asset> assets = WalletController.getInstance(getBaseContext()).getWalletAssets();
        ArrayList<Liability> liabilities = WalletController.getInstance(getBaseContext()).getWalletLiabilities();

        BigDecimal totalAssets = new BigDecimal(0);
        for (int i = 0; i < assets.size(); i++){
            totalAssets = assets.get(i).getValue();
        }

        BigDecimal totalLiabilities = new BigDecimal(0);
        for (int i = 0; i < liabilities.size(); i++){
            totalLiabilities = liabilities.get(i).getValue();
        }

        textTotalAssets.setText(totalAssets.toString());
        textTotalLiabilities.setText(totalLiabilities.toString());
        textTotalGeral.setText(totalAssets.subtract(totalLiabilities).toString());
* */


    public BigDecimal getTotalAssets(Context context){
        ArrayList<Asset> assets = WalletController.getInstance(context).getWalletAssets();

        BigDecimal totalAssets = new BigDecimal(0);
        for (int i = 0; i < assets.size(); i++){
            totalAssets = totalAssets.add(assets.get(i).getValue());
        }
        return totalAssets;
    }

    public BigDecimal getTotalLiabilities(Context context) {
        ArrayList<Liability> liabilities = WalletController.getInstance(context).getWalletLiabilities();

        BigDecimal totalLiabilities = new BigDecimal(0);
        for (int i = 0; i < liabilities.size(); i++){
            totalLiabilities = totalLiabilities.add(liabilities.get(i).getValue());
        }
        return totalLiabilities;
    }

    public BigDecimal getWalletBalance(Context context) {
        BigDecimal totalGeral = new BigDecimal(0);

        totalGeral = getTotalAssets(context).subtract(getTotalLiabilities(context));

        return totalGeral;
    }

    public boolean addAsset (Context context, TextInputEditText nome, TextInputEditText valor, TextInputEditText data, String moeda){
        //Inicia valores
        boolean result = false;

        //Executa Validacao e altera no banco
        try {
            //se valido, altera
            if(checkCurrency(valor)){
                Asset asset = new Asset(1L, nome.getText().toString(), new BigDecimal(treatCurrency(valor)), data.getText().toString(), moeda);
                currentWallet.addAsset(asset);
                long daoResult = assetDAO.save(currentWallet.getId(), asset.getName(), asset.getValue(), asset.getDate(), asset.getCurrency());
                result = daoResult == -1? false : true;
            }

        } catch (SaveWalletErrorException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean addLiability (Context context, TextInputEditText nome, TextInputEditText valor, TextInputEditText data, String moeda){
        //Inicia valores
        boolean result = false;
        liabilityDAO = new LiabilityDAO(context);

        //Executa Validacao e altera no banco
        try {
            //se valido, altera
            if(checkCurrency(valor)){
                Liability liability = new Liability(1L, nome.getText().toString(), new BigDecimal(treatCurrency(valor)), data.getText().toString(), moeda);
                currentWallet.addLiability(liability);
                long daoResult = liabilityDAO.save(currentWallet.getId(), liability.getName(), liability.getValue(), liability.getDate(), liability.getCurrency());
                result = daoResult == -1? false : true;
            }

        } catch (SaveWalletErrorException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean checkCurrency(TextInputEditText valor){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String custoRegEx = "";
        boolean isCurrencyRight = true;

        try {
            custoRegEx = valor.getText().toString().replaceAll("[^0-9,.]", "");
            custoRegEx = custoRegEx.replaceAll(",", ".");
            custoRegEx = nf.format(new BigDecimal(custoRegEx));
            custoRegEx = custoRegEx.replaceAll("[^0-9,]", "");
            custoRegEx = custoRegEx.replaceAll(",", ".");

            Log.d("CustoRegEx", custoRegEx);

        } catch (NumberFormatException e){
            e.printStackTrace();
            isCurrencyRight = false;
            valor.setText("");
        }catch(Exception e) {
            e.printStackTrace();
            isCurrencyRight = false;
        }
        return isCurrencyRight;
    }

    public String treatCurrency(TextInputEditText valor){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String custoRegEx = "";

        try {
            custoRegEx = valor.getText().toString().replaceAll("[^0-9,.]", "");
            custoRegEx = custoRegEx.replaceAll(",", ".");
            custoRegEx = nf.format(new BigDecimal(custoRegEx));
            custoRegEx = custoRegEx.replaceAll("[^0-9,]", "");
            custoRegEx = custoRegEx.replaceAll(",", ".");

            Log.d("CustoRegEx", custoRegEx);

        } catch (NumberFormatException e){
            e.printStackTrace();
            valor.setText("");
        }catch(Exception e) {
            e.printStackTrace();
        }
        return custoRegEx;
    }

    public ArrayList getWalletAssets() {
        Cursor cursor;
        ArrayList<Operation> result = new ArrayList<>();

        cursor = assetDAO.load(currentWallet.getId());
        if(cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {

                Asset asset = new Asset(
                        Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        new BigDecimal(cursor.getString(2)),
                        cursor.getString(4),
                        cursor.getString(3)
                );

                result.add(asset);
                cursor.moveToNext();
            }
        }

        return result;
    }

    public ArrayList getWalletLiabilities() {
        Cursor cursor;
        ArrayList<Operation> result = new ArrayList<>();

        cursor = liabilityDAO.load(currentWallet.getId());
        if(cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {

                Liability liability = new Liability(
                        Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        new BigDecimal(cursor.getString(2)),
                        cursor.getString(4),
                        cursor.getString(3)
                );

                result.add(liability);
                cursor.moveToNext();
            }
        }

        return result;
    }

    public ArrayList getOperations(){
        Cursor cursor;
        ArrayList<Operation> result = new ArrayList<>();

        cursor = assetDAO.load(currentWallet.getId());
        if(cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {

                Asset asset = new Asset(
                        Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        new BigDecimal(cursor.getString(2)),
                        cursor.getString(4),
                        cursor.getString(3)
                );

                result.add(asset);
                cursor.moveToNext();
            }
        }

        cursor = liabilityDAO.load(currentWallet.getId());
        if(cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {

                Liability liability = new Liability(
                        Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        new BigDecimal(cursor.getString(2)),
                        cursor.getString(4),
                        cursor.getString(3)
                );

                result.add(liability);
                cursor.moveToNext();
            }
        }

        return result;
    }

    public boolean editLiability(Context context, long id, String name, TextInputEditText inputText, String date, String currency) throws SaveWalletErrorException{
        boolean result = false;
        liabilityDAO = new LiabilityDAO(context);

        //Executa Validacao e altera no banco
            //se valido, altera
            if(checkCurrency(inputText)) {
                Liability liability = new Liability(id, name, new BigDecimal(inputText.getText().toString()), date, currency);
                currentWallet.addLiability(liability);
                long daoResult = liabilityDAO.update(liability.getId(), liability.getName(), liability.getValue(), liability.getDate(), liability.getCurrency());
                result = daoResult == -1 ? false : true;
            }
        return result;
    }

    public boolean editAsset(Context context, long id, String name, TextInputEditText inputText, String date, String currency) throws SaveWalletErrorException{
        boolean result = false;
        assetDAO = new AssetDAO(context);

        //Executa Validacao e altera no banco
            //se valido, altera
            if(checkCurrency(inputText)){
                Asset asset = new Asset(id, name, new BigDecimal(inputText.getText().toString()), date, currency);
                currentWallet.addAsset(asset);
                long daoResult = assetDAO.update(asset.getId(), asset.getName(), asset.getValue(), asset.getDate(), asset.getCurrency());
                result = daoResult == -1? false : true;
            }
        return result;
    }

    public boolean deleteAsset(int id) throws DeleteWalletExceptionError {
        return assetDAO.delete(id) != -1;
    }

    public boolean deleteLiability(int id) throws DeleteWalletExceptionError {
        return liabilityDAO.delete(id) != -1;
    }

    public boolean saveWallet(String name) throws SaveWalletErrorException, LoadWalletErrorException {
        boolean result = walletDAO.save(name) != -1;
        if(result)
            load(name);
        return result;
    }

    public boolean load(String name) throws LoadWalletErrorException {
        boolean result;
        Cursor cursor = walletDAO.load(name);

        if(cursor != null && name.equals(cursor.getString(1))) {
            currentWallet = new Wallet(cursor.getInt(0), cursor.getString(1));
            result = true;
        } else {
            throw new LoadWalletErrorException();
        }
        return result;
    }

    public ArrayList<Wallet> getWalletList() {
        Cursor cursor = walletDAO.loadAll();
        ArrayList<Wallet> wallets = new ArrayList();

        for(int i=0;i<cursor.getCount();i++){
            wallets.add(new Wallet(cursor.getInt(0),cursor.getString(1)));
            cursor.moveToNext();
        }

        return wallets;
    }

    public boolean deleteWallet() throws DeleteWalletExceptionError {
        boolean result;

            if(walletDAO.delete(currentWallet.getId()) == 0){
                result = false;
            }else{
                currentWallet = null;
                result = true;
            }

        return result;
    }


    public File exportWallet(Context context, String path) {
        ArrayList<Asset> assets = new ArrayList<>(WalletController.getInstance(context).getWalletAssets());
        ArrayList<Liability> liabilities = new ArrayList<>(WalletController.getInstance(context).getWalletLiabilities());

        try {
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
            buffWrite.append("TOTAL DE ATIVOS: " + WalletController.getInstance(context).getTotalAssets(context).toString() + "\n");
            for (int i = 0; i < assets.size(); i++){
                buffWrite.append(String.valueOf(assets.get(i).getId()));
                buffWrite.append(" ");
                buffWrite.append(assets.get(i).getName());
                buffWrite.append(" ");
                buffWrite.append(assets.get(i).getDate());
                buffWrite.append(" ");
                buffWrite.append(assets.get(i).getCurrency());
                buffWrite.append(assets.get(i).getValue().toString());
                buffWrite.append("\n");
            }

            buffWrite.append("TOTAL DE PASSIVOS: " + WalletController.getInstance(context).getTotalLiabilities(context).toString() + "\n");
            for (int i = 0; i < liabilities.size(); i++){
                buffWrite.append(String.valueOf(liabilities.get(i).getId()));
                buffWrite.append(" ");
                buffWrite.append(liabilities.get(i).getName());
                buffWrite.append(" ");
                buffWrite.append(liabilities.get(i).getDate());
                buffWrite.append(" ");
                buffWrite.append(liabilities.get(i).getCurrency());
                buffWrite.append(liabilities.get(i).getValue().toString());
                buffWrite.append("\n");
            }
            buffWrite.append("TOTAL: " + WalletController.getInstance(context).getWalletBalance(context));
            buffWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(path);
    }

    public String getWalletName() {
        return currentWallet.getName();
    }
}
