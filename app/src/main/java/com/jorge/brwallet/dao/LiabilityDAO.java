package com.jorge.brwallet.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jorge.brwallet.exceptions.DeleteWalletExceptionError;
import com.jorge.brwallet.exceptions.SaveWalletErrorException;
import com.jorge.brwallet.persistence.Banco;

import java.math.BigDecimal;

public class LiabilityDAO {

    private SQLiteDatabase db;
    private Banco banco;

    public LiabilityDAO (Context context){ banco = new Banco(context); }

    public long save(int id, String nome, BigDecimal valor, String data, String moeda) throws SaveWalletErrorException {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(Banco.FK_ID_WALLET, id);
        valores.put(Banco.LIABILITY_NAME, nome);
        valores.put(Banco.LIABILITY_VALUE, valor.toString());
        valores.put(Banco.LIABILITY_DATE, data);
        valores.put(Banco.LIABILITY_CURRENCY, moeda);

        resultado = db.insert(Banco.TABELA_LIABILITIES, null, valores);
        db.close();

        if(resultado == -1) {
            throw new SaveWalletErrorException();
        }

        return resultado;
    }

    public Cursor load(int id){
        Cursor cursor;
        String[] campos =  {Banco.LIABILITY_ID, Banco.LIABILITY_NAME, Banco.LIABILITY_VALUE, Banco.LIABILITY_CURRENCY, Banco.LIABILITY_DATE};
        String where = Banco.FK_ID_WALLET + " = " + id;
        db = banco.getReadableDatabase();
        cursor = db.query(Banco.TABELA_LIABILITIES, campos, where, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public long update(long id, String nome, BigDecimal valor, String data, String moeda) throws SaveWalletErrorException {
        ContentValues valores;
        String where;
        long result;

        db = banco.getWritableDatabase();

        where = Banco.LIABILITY_ID + "=" + id;

        valores = new ContentValues();
        valores.put(Banco.LIABILITY_NAME, nome);
        valores.put(Banco.LIABILITY_VALUE, valor.toString());
        valores.put(Banco.LIABILITY_DATE, data);
        valores.put(Banco.LIABILITY_CURRENCY, moeda);

        result = db.update(Banco.TABELA_LIABILITIES,valores,where,null);
        db.close();

        if(result == -1) {
            throw new SaveWalletErrorException();
        }
        return result;
    }

    public long delete(int id) throws DeleteWalletExceptionError {
        long result;

        String where = Banco.LIABILITY_ID + "=" + id;
        db = banco.getReadableDatabase();
        result = db.delete(Banco.TABELA_LIABILITIES,where,null);
        db.close();

        if(result == -1){
            throw new DeleteWalletExceptionError();
        }
        return result;
    }
}
