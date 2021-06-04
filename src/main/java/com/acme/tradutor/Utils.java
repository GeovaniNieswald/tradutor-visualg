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

        Token t = new Token("//", "//", "#", 0, 0, 0);
        tokens.add(t);

        t = new Token("algoritmo", "public class", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimalgoritmo", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("var", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("inteiro", "int", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("vetor", "[]", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("real", "double", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("caractere", "String", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("logico", "boolean", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("inicio", "public static void main(String args[])", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("se", "if", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("entao", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("senao", "else", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimse", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("enquanto", "while", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimenquanto", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("para", "for", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("ate", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("passo", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("faca", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimpara", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("escolha", "switch", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("caso", "case", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("outrocaso", "default", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimescolha", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("interrompa", "break", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("escreval", "System.out.println()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("escreva", "System.out.print()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("int", "(int)", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("maiusc", ".toUpperCase()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("minusc", ".toLowerCase()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("caracpnum", "Integer.parseInt()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("compr", ".length()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("numpcarac", "String.valueOf()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("nao", "!", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("falso", "false", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("verdadeiro", "true", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("mod", "%", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("e", "&&", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("ou", "||", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("=", "==", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("<>", "!=", "", 0, 0, 0);
        tokens.add(t);

        t = new Token(":=", "=", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("<-", "=", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimalgoritmo", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("leia", "new Scanner(System.in).nextLine()", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("procedimento", "static void nome(tipo parâmetro, ...)", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimprocedimento", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("funcao", "static tipo nome(tipo parâmetro, ...)", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("fimfuncao", "sem correspondência", "", 0, 0, 0);
        tokens.add(t);

        t = new Token("retorne", "return", "", 0, 0, 0);
        tokens.add(t);

        return tokens;
    }

}
