package com.acme.tradutor;

import com.acme.tradutor.model.Token;
import com.acme.tradutor.model.Variavel;
import java.util.ArrayList;
import java.util.List;

public class TraduzirParaPython {

    private String classe;
    private List<Token> tokens;

    private List<Variavel> listVariaveis;
    private boolean blocoVarExtra;

    public TraduzirParaPython() {
        this.classe = "";
        this.tokens = new ArrayList<>();
        this.blocoVarExtra = false;
    }

    public String obterClasse() {
        return this.classe;
    }

    public List<Token> obterTokensAtt() {
        return this.tokens;
    }

    public String traduzir(String vetCodigoOriginal[], List<Token> tokens) {
        this.tokens = tokens;
        this.listVariaveis = new ArrayList<>();

        String codigoTraduzidoPython = "";
        // TODO: Fazer tradução para Python
        return codigoTraduzidoPython;
    }

    public void incrementarQtdToken(String token) {
        for (Token t : this.tokens) {
            if (t.getTokenVisualg().equalsIgnoreCase(token)) {
                t.setQtdPython(t.getQtdPython() + 1);
                break;
            }
        }
    }

}
