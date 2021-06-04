package com.acme.tradutor.model;

public class Token {

    private String tokenVisualg;
    private String tokenJava;
    private String tokenPython;
    private int qtdVisualg;
    private int qtdJava;
    private int qtdPython;

    public Token() {
    }

    public Token(String tokenVisualg, String tokenJava, String tokenPython, int qtdVisualg, int qtdJava, int qtdPython) {
        this.tokenVisualg = tokenVisualg;
        this.tokenJava = tokenJava;
        this.tokenPython = tokenPython;
        this.qtdVisualg = qtdVisualg;
        this.qtdJava = qtdJava;
        this.qtdPython = qtdPython;
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

    public String getTokenPython() {
        return tokenPython;
    }

    public void setTokenPython(String tokenPython) {
        this.tokenPython = tokenPython;
    }

    public int getQtdVisualg() {
        return qtdVisualg;
    }

    public void setQtdVisualg(int qtdVisualg) {
        this.qtdVisualg = qtdVisualg;
    }

    public int getQtdJava() {
        return qtdJava;
    }

    public void setQtdJava(int qtdJava) {
        this.qtdJava = qtdJava;
    }

    public int getQtdPython() {
        return qtdPython;
    }

    public void setQtdPython(int qtdPython) {
        this.qtdPython = qtdPython;
    }

}
