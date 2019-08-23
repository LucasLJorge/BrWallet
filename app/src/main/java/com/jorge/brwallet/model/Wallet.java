package com.jorge.brwallet.model;

import java.util.ArrayList;

public class Wallet {

    private String name;
    private int id;

    private ArrayList<Asset> assets = new ArrayList<Asset>();
    private ArrayList<Liability> liabilities = new ArrayList<Liability>();

    public Wallet(int id, String name){
        this.id = id;
        this.name = name;
    }

    public void addAsset (Asset asset){
        if(asset!=null){
            assets.add(asset);
        }
    }

    public void addLiability (Liability liability){
        if(liability!=null){
            liabilities.add(liability);
        }
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}