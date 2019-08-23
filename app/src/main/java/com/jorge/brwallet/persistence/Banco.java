package com.jorge.brwallet.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Banco extends SQLiteOpenHelper {
    public static final String NOME_BANCO = "banco.brwallet";
    public static final String TABELA_WALLET = "carteira";
    public static final String ID_WALLET = "_id_wallet";
    public static final String WALLET_NAME = "nome";

    public static final String FK_ID_WALLET = "_id";

    public static final String TABELA_ASSETS = "ativos";
    public static final String ASSET_ID = "_id_asset";
    public static final String ASSET_NAME = "nome_ativo";
    public static final String ASSET_VALUE = "valor_ativo";
    public static final String ASSET_CURRENCY = "moeda_ativo";
    public static final String ASSET_DATE = "data_ativo";

    public static final String TABELA_LIABILITIES = "passivos";
    public static final String LIABILITY_ID = "_id_liability";
    public static final String LIABILITY_NAME = "nome_passivo";
    public static final String LIABILITY_VALUE = "valor_passivo";
    public static final String LIABILITY_CURRENCY = "moeda_passivo";
    public static final String LIABILITY_DATE = "data_passivo";

    public static final int VERSAO = 1;

    public Banco(Context context){
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         CREATE TABLE TABELA_WALLET (
            ID integer primary key autoincrement,
            NOME text
            )
        */
        StringBuilder sb = new StringBuilder().
            append("CREATE TABLE ").
            append(TABELA_WALLET).
            append("(").
            append(ID_WALLET).
            append(" integer primary key autoincrement,").
            append(WALLET_NAME).
            append(" text").
            append(");");

        /*
         CREATE TABLE TABELA_ASSETS (
            ASSET_ID integer primary key autoincrement,
            FK_ID_WALLET integer,
            ASSET_NAME text,
            ASSET_VALUE numeric,
            ASSET_CURRENCY text,
            ASSET_DATE text,
            FOREIGN KEY(FK_ID_WALLET) REFERENCES TABELA_WALLET(ID_WALLET)
            );
        */
        StringBuilder sb2 = new StringBuilder().
            append(" CREATE TABLE ").
            append(TABELA_ASSETS).
            append("(").
            append(ASSET_ID).
            append(" integer primary key autoincrement,").
            append(FK_ID_WALLET).
            append(" integer,").
            append(ASSET_NAME).
            append(" text,").
            append(ASSET_VALUE).
            append(" text,").
            append(ASSET_CURRENCY).
            append(" text,").
            append(ASSET_DATE).
            append(" text,").
            append(" FOREIGN KEY (").
            append(FK_ID_WALLET).
            append(") REFERENCES ").
            append(TABELA_WALLET).
            append("(").append(ID_WALLET).
            append("));");

        /*
         CREATE TABLE TABELA_LIABILITIES (
            LIABILITY_ID integer primary key autoincrement,
            FK_ID_WALLET integer,
            LIABILITY_NAME text,
            LIABILITY_VALUE numeric,
            LIABILITY_CURRENCY text,
            LIABILITY_DATE text,
            FOREIGN KEY(FK_ID_WALLET) REFERENCES TABELA_WALLET(ID_WALLET)
            );
        */
        StringBuilder sb3 = new StringBuilder().
            append(" CREATE TABLE ").append(TABELA_LIABILITIES).append("(").
            append(LIABILITY_ID).append(" integer primary key autoincrement,").
            append(FK_ID_WALLET).append(" integer,").
            append(LIABILITY_NAME).append(" text,").
            append(LIABILITY_VALUE).append(" text,").
            append(LIABILITY_CURRENCY).append(" text,").
            append(LIABILITY_DATE).append(" text,").
            append(" FOREIGN KEY (").
            append(FK_ID_WALLET).
            append(") REFERENCES ").
            append(TABELA_WALLET).
            append("(").append(ID_WALLET).
            append("));");

        db.execSQL(sb.toString());
        db.execSQL(sb2.toString());
        db.execSQL(sb3.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_WALLET);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ASSETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_LIABILITIES);
        onCreate(db);
    }
}
