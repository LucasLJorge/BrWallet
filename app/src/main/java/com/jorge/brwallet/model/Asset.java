package com.jorge.brwallet.model;

import java.math.BigDecimal;

public class Asset extends Operation {
    public Asset(long id, String nome, BigDecimal valor, String date, String currency){
        super(id, nome, valor, date, currency);
    }
}
