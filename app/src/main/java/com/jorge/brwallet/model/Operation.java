package com.jorge.brwallet.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Operation implements Serializable {
    private long id;
    private String nome;
    private BigDecimal valor;
    private String date;
    private String currency;

    public Operation(long id, String nome, BigDecimal valor, String date, String currency){
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.currency = currency;
        this.date = date;
    }

    public long getId() { return id; }
    public String getName(){
        return nome;
    }
    public BigDecimal getValue(){
        return valor;
    }
    public String getDate() { return date; }
    public String getCurrency() { return currency; }


    public void setId(long id) { this.id = id; }
    public void setNome(String nome){ this.nome = nome; }
    public void setValor(BigDecimal valor){
        this.valor = valor;
    }
    public void setDate(String date) { this.date = date; }
    public void setCurrency(String currency) { this.currency = currency; }
}
