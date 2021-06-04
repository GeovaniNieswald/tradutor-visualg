package com.acme.tradutor;

import com.acme.tradutor.model.Token;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String removerCaracteresEspeciais(String str) {
        return str.replaceAll("[^a-zZ-Z0-9]", "");
    }

    public static String primeiraLetraMaiuscula(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static List<Token> popularArrayTokens() {
        List<Token> tokens = new ArrayList<>();

        Token t = new Token("//", "//", "//", 0, 0, 0);
        tokens.add(t);

        t = new Token("algoritmo", "public class", "<?php", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimalgoritmo", "}", "?>", 0, 0, 0);
        tokens.add(t);

        t = new Token("var", "", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("inteiro", "int", "$", 0, 0, 0);
        tokens.add(t);

        t = new Token("vetor", "[]", "array()", 0, 0, 0);
        tokens.add(t);

        t = new Token("real", "double", "$", 0, 0, 0);
        tokens.add(t);

        t = new Token("caractere", "String", "$", 0, 0, 0);
        tokens.add(t);

        t = new Token("logico", "boolean", "$", 0, 0, 0);
        tokens.add(t);

        t = new Token("inicio", "public static void main(String args[])", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("se", "if", "if", 0, 0, 0);
        tokens.add(t);

        t = new Token("entao", "", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("senao", "else", "else", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimse", "}", "}", 0, 0, 0);
        tokens.add(t);

        t = new Token("enquanto", "while", "while", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimenquanto", "}", "}", 0, 0, 0);
        tokens.add(t);

        t = new Token("para", "for", "for", 0, 0, 0);
        tokens.add(t);

        t = new Token("ate", "<=", "<=", 0, 0, 0);
        tokens.add(t);

        t = new Token("passo", "", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("faca", "", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimpara", "}", "}", 0, 0, 0);
        tokens.add(t);

        t = new Token("escolha", "switch", "switch", 0, 0, 0);
        tokens.add(t);

        t = new Token("caso", "case", "case", 0, 0, 0);
        tokens.add(t);

        t = new Token("outrocaso", "default", "default", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimescolha", "}", "}", 0, 0, 0);
        tokens.add(t);

        t = new Token("interrompa", "break", "break", 0, 0, 0);
        tokens.add(t);

        t = new Token("escreval", "System.out.println()", "echo", 0, 0, 0);
        tokens.add(t);

        t = new Token("escreva", "System.out.print()", "echo", 0, 0, 0);
        tokens.add(t);

        t = new Token("int", "(int)", "(int)", 0, 0, 0);
        tokens.add(t);

        t = new Token("maiusc", ".toUpperCase()", "strtoupper()", 0, 0, 0);
        tokens.add(t);

        t = new Token("minusc", ".toLowerCase()", "strtolower()", 0, 0, 0);
        tokens.add(t);

        t = new Token("caracpnum", "Integer.parseInt()", "(int)", 0, 0, 0);
        tokens.add(t);

        t = new Token("compr", ".length()", "strlen()", 0, 0, 0);
        tokens.add(t);

        t = new Token("numpcarac", "String.valueOf()", "strval()", 0, 0, 0);
        tokens.add(t);

        t = new Token("nao", "!", "!", 0, 0, 0);
        tokens.add(t);

        t = new Token("falso", "false", "false", 0, 0, 0);
        tokens.add(t);

        t = new Token("verdadeiro", "true", "true", 0, 0, 0);
        tokens.add(t);

        t = new Token("mod", "%", "%", 0, 0, 0);
        tokens.add(t);

        t = new Token("e", "&&", "&&", 0, 0, 0);
        tokens.add(t);

        t = new Token("ou", "||", "||", 0, 0, 0);
        tokens.add(t);

        t = new Token("=", "==", "==", 0, 0, 0);
        tokens.add(t);

        t = new Token("<>", "!=", "!=", 0, 0, 0);
        tokens.add(t);

        t = new Token(":=", "=", "=", 0, 0, 0);
        tokens.add(t);

        t = new Token("<-", "=", "=", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimalgoritmo", "}", "?>", 0, 0, 0);
        tokens.add(t);

        t = new Token("leia", "new Scanner(System.in).nextLine()", "fgets(fopen (\"php://stdin\",\"r\"))", 0, 0, 0);
        tokens.add(t);

        t = new Token("procedimento", "static void nome(tipo parâmetro, ...)", "function nome(parâmetro, ...)", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimprocedimento", "}", "}", 0, 0, 0);
        tokens.add(t);

        t = new Token("funcao", "static tipo nome(tipo parâmetro, ...)", "function nome(parâmetro, ...)", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimfuncao", "}", "}", 0, 0, 0);
        tokens.add(t);

        t = new Token("retorne", "return", "return", 0, 0, 0);
        tokens.add(t);

        return tokens;
    }

}
