package com.jorge.brwallet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jorge.brwallet.exceptions.DeleteWalletExceptionError;
import com.jorge.brwallet.exceptions.LoadWalletErrorException;
import com.jorge.brwallet.exceptions.SaveWalletErrorException;
import com.jorge.brwallet.persistence.Banco;

public class WalletDAO {

    private SQLiteDatabase db;
    private Banco banco;

    public WalletDAO (Context context){
        banco = new Banco(context);
    }

    public long save(String name) throws SaveWalletErrorException, LoadWalletErrorException {
        ContentValues valores;
        String msg = null;
        long resultado = -1;

        if(!isWalletThere(name)){
            db = banco.getWritableDatabase();
            valores = new ContentValues();
            valores.put(banco.WALLET_NAME, name);

            resultado = db.insert(banco.TABELA_WALLET, null, valores);
            db.close();
        }else{
            msg = "CARTEIRA JA CRIADA!";
        }

        if(resultado == -1) {
            throw msg == null ? new SaveWalletErrorException() : new SaveWalletErrorException(msg);
        }

        return resultado;
    }

    public Cursor load(String name) throws SQLException {
        Cursor cursor;

        String[] campos =  {banco.ID_WALLET, banco.WALLET_NAME};
        String where = banco.WALLET_NAME + "=" + "'" + name + "'";
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA_WALLET, campos, where, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    private boolean isWalletThere (String name) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = this.load(name);
            if(cursor.getCount() == 0) {
                return result;
            } else
            result = cursor != null && name.equals(cursor.getString(1));
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public Cursor loadAll() {
        Cursor cursor;

        String[] campos =  {Banco.ID_WALLET, Banco.WALLET_NAME};

        db = banco.getReadableDatabase();
        cursor = db.query(Banco.TABELA_WALLET, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public long delete(int id) throws DeleteWalletExceptionError {
        long result;

        String where = Banco.ID_WALLET + "=" + id;
        db = banco.getReadableDatabase();
        result = db.delete(Banco.TABELA_WALLET,where,null);
        db.close();

        if(result == -1){
            throw new DeleteWalletExceptionError();
        }
        return result;
    }
}
