package com.acme.transpilador.model;

public class Token {

    private String tokenVisualg;
    private String tokenJava;
    private int qtd;

    public Token() {
    }

    public Token(String tokenVisualg, String tokenJava, int qtd) {
        this.tokenVisualg = tokenVisualg;
        this.tokenJava = tokenJava;
        this.qtd = qtd;
    }
    
    public String getTokenVisualg() {
        return tokenVisualg;
    }

    public void setTokenVisualg(String tokenVisualg) {
        this.tokenVisualg = tokenVisualg;
    }

    public String getTokenJava() {
        return tokenJava;
    }

    public void setTokenJava(String tokenJava) {
        this.tokenJava = tokenJava;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

}
