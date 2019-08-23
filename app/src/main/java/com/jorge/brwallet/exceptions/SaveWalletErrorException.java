package com.jorge.brwallet.exceptions;

public class SaveWalletErrorException extends Exception {

    public SaveWalletErrorException(){
        super ("ERRO AO SALVAR CARTEIRA");
    }

    public SaveWalletErrorException(String s) {
        super(s);
    }
}