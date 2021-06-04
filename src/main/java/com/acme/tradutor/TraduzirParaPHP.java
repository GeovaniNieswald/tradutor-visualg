package com.acme.tradutor;

import com.acme.tradutor.model.Token;
import java.util.ArrayList;
import java.util.List;

public class TraduzirParaPHP {

    private String classe;
    private List<Token> tokens;

    private boolean blocoVarExtra;

    public TraduzirParaPHP() {
        this.classe = "";
        this.tokens = new ArrayList<>();
        this.blocoVarExtra = false;
    }

    public String traduzir(String vetCodigoOriginal[], boolean leDados, List<Token> tokens) {
        this.tokens = tokens;

        String codigoTraduzido = "";

        int numeroLinha = 0;
        boolean blocoVar = false;
        boolean blocoProcedimento = false;
        boolean blocoFuncao = false;
        String conteudoLinhaLimpa;
        String linhaTraduziada;

        int inicio;
        int fim;

        for (String conteudoLinha : vetCodigoOriginal) {
            conteudoLinhaLimpa = conteudoLinha.trim().toLowerCase();

            if (numeroLinha == 0) {
                String nomeClasse[] = conteudoLinha.split("\"");
                this.classe = Utils.primeiraLetraMaiuscula(Utils.removerCaracteresEspeciais(nomeClasse[1]));
                linhaTraduziada = "<?php";
                linhaTraduziada += leDados ? "\n\t$handle = fopen (\"php://stdin\",\"r\");" : "";
                this.incrementarQtdToken("algoritmo", true, true);
            } else {
                if (conteudoLinhaLimpa.startsWith("//") || conteudoLinhaLimpa.isEmpty()) {
                    linhaTraduziada = "\t" + conteudoLinha;
                    this.incrementarQtdToken("//", true, true);
                } else {
                    if (conteudoLinhaLimpa.startsWith("var") && blocoProcedimento == false && blocoFuncao == false) {
                        blocoVar = true;
                        linhaTraduziada = "";
                        this.incrementarQtdToken("var", true, false);
                    } else if (conteudoLinhaLimpa.startsWith("inicio") && blocoProcedimento == false && blocoFuncao == false) {
                        blocoVar = false;
                        linhaTraduziada = "";
                        this.incrementarQtdToken("inicio", true, false);
                    } else if (conteudoLinhaLimpa.startsWith("procedimento ")) {
                        blocoVar = false;
                        blocoProcedimento = true;

                        inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
                        fim = conteudoLinhaLimpa.indexOf("(");
                        String nomeProcedimento = conteudoLinhaLimpa.substring(inicio, fim).trim();
                        String parametrosProcedimento = "";

                        inicio = conteudoLinhaLimpa.indexOf("(") + 1;
                        fim = conteudoLinhaLimpa.lastIndexOf(")");
                        String parametros[] = conteudoLinhaLimpa.substring(inicio, fim).trim().split(";");

                        for (int i = 0; i < parametros.length; i++) {
                            String parametro = parametros[i];

                            if (i > 0) {
                                parametrosProcedimento += ", ";
                            }

                            String[] nomesTipo = parametro.split(":");
                            String nomes[] = nomesTipo[0].trim().split(",");

                            for (int j = 0; j < nomes.length; j++) {
                                String nome = nomes[j];

                                if (j == 0) {
                                    parametrosProcedimento += "$" + nome.trim();
                                } else {
                                    parametrosProcedimento += ", $" + nome.trim();
                                }
                            }
                        }

                        linhaTraduziada = "\tfunction " + nomeProcedimento + "(" + parametrosProcedimento + ") {";

                        this.incrementarQtdToken("procedimento", true, true);
                    } else if (conteudoLinhaLimpa.startsWith("funcao ") || conteudoLinhaLimpa.startsWith("função ")) {
                        blocoVar = false;
                        blocoFuncao = true;

                        inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
                        fim = conteudoLinhaLimpa.indexOf("(");
                        String nomeFuncao = conteudoLinhaLimpa.substring(inicio, fim).trim();
                        String parametrosFuncao = "";

                        inicio = conteudoLinhaLimpa.indexOf("(") + 1;
                        fim = conteudoLinhaLimpa.lastIndexOf(")");
                        String parametros[] = conteudoLinhaLimpa.substring(inicio, fim).trim().split(";");

                        for (int i = 0; i < parametros.length; i++) {
                            String parametro = parametros[i];

                            if (i > 0) {
                                parametrosFuncao += ", ";
                            }

                            String[] nomesTipo = parametro.split(":");
                            String nomes[] = nomesTipo[0].trim().split(",");

                            for (int j = 0; j < nomes.length; j++) {
                                String nome = nomes[j];

                                if (j == 0) {
                                    parametrosFuncao += "$" + nome.trim();
                                } else {
                                    parametrosFuncao += ", $" + nome.trim();
                                }
                            }
                        }

                        linhaTraduziada = "\tfunction " + nomeFuncao + "(" + parametrosFuncao + ") {";

                        this.incrementarQtdToken("funcao", true, true);
                    } else if (conteudoLinhaLimpa.startsWith("fimprocedimento")) {
                        blocoProcedimento = false;
                        linhaTraduziada = "\t}";
                        this.incrementarQtdToken("fimprocedimento", true, true);
                    } else if (conteudoLinhaLimpa.startsWith("fimfuncao") || conteudoLinhaLimpa.startsWith("fimfunção")) {
                        blocoFuncao = false;
                        linhaTraduziada = "\t}";
                        this.incrementarQtdToken("fimfuncao", true, true);
                    } else if (blocoVar) {
                        linhaTraduziada = this.substituirBlocoVar(conteudoLinha);
                    } else if (blocoProcedimento) {
                        linhaTraduziada = this.substituirBlocoProcedimento(conteudoLinha, conteudoLinhaLimpa);
                    } else if (blocoFuncao) {
                        linhaTraduziada = this.substituirBlocoFuncao(conteudoLinha, conteudoLinhaLimpa);
                    } else {
                        linhaTraduziada = this.substituirBlocoInicio(conteudoLinha, conteudoLinhaLimpa);
                    }
                }
            }

            codigoTraduzido += linhaTraduziada + "\n";
            numeroLinha++;
        }

        return codigoTraduzido;
    }

    private String substituirBlocoVar(String conteudoLinha) {
        String nomesTipo[] = conteudoLinha.split(":");

        String nomes = nomesTipo[0].trim().trim();
        String tipo = nomesTipo[1].trim().toLowerCase().trim();

        String linhaTraduzida = "";

        if (tipo.startsWith("vetor")) {
            String tamanhoTipoComentario[] = tipo.split(" de ");
            String tipoComentario[] = tamanhoTipoComentario[1].split("//");

            tipo = tipoComentario[0].trim();

            String comentario = "";
            if (tipoComentario.length > 1) {
                comentario = "//" + tipoComentario[1];
                this.incrementarQtdToken("//", true, true);
            }

            String vetNomes[] = nomes.split(",");
            for (String n : vetNomes) {
                if (vetNomes.length > 1) {
                    linhaTraduzida += "\t$" + n.trim() + " = array();" + comentario + "\n";
                } else {
                    linhaTraduzida += "\t$" + n.trim() + " = array();" + comentario;
                }
            }

            this.incrementarQtdToken("vetor", true, true);
            this.incrementarQtdToken(tipo, true, true);
        } else {
            String tipoComentario[] = tipo.split("//");
            tipo = tipoComentario[0].trim();

            String comentario = "";
            if (tipoComentario.length > 1) {
                comentario = "//" + tipoComentario[1];
                this.incrementarQtdToken("//", true, true);
            }

            String vetNomes[] = nomes.split(",");
            for (String n : vetNomes) {
                if (vetNomes.length > 1) {
                    linhaTraduzida += "\t$" + n.trim() + ";" + comentario + "\n";
                } else {
                    linhaTraduzida += "\t$" + n.trim() + ";" + comentario;
                }
            }

            this.incrementarQtdToken(tipo, true, true);
        }

        return linhaTraduzida;
    }

    private String substituirBlocoProcedimento(String conteudoLinha, String conteudoLinhaLimpa) {
        String linhaTraduzida;

        if (conteudoLinhaLimpa.startsWith("//") || conteudoLinhaLimpa.isEmpty()) {
            linhaTraduzida = "\t" + conteudoLinha;
            this.incrementarQtdToken("//", true, true);
        } else {
            if (conteudoLinhaLimpa.startsWith("var")) {
                this.blocoVarExtra = true;
                linhaTraduzida = "";
                this.incrementarQtdToken("var", true, false);
            } else if (conteudoLinhaLimpa.startsWith("inicio")) {
                this.blocoVarExtra = false;
                linhaTraduzida = "";
                this.incrementarQtdToken("inicio", true, false);
            } else if (this.blocoVarExtra) {
                linhaTraduzida = this.substituirBlocoVar(conteudoLinha);
            } else {
                linhaTraduzida = this.substituirBlocoInicio(conteudoLinha, conteudoLinhaLimpa);
            }
        }

        return linhaTraduzida;
    }

    private String substituirBlocoFuncao(String conteudoLinha, String conteudoLinhaLimpa) {
        String linhaTraduzida;

        if (conteudoLinhaLimpa.startsWith("//") || conteudoLinhaLimpa.isEmpty()) {
            linhaTraduzida = "\t" + conteudoLinha;
            this.incrementarQtdToken("//", true, true);
        } else {
            if (conteudoLinhaLimpa.startsWith("var")) {
                this.blocoVarExtra = true;
                linhaTraduzida = "";
                this.incrementarQtdToken("var", true, false);
            } else if (conteudoLinhaLimpa.startsWith("inicio")) {
                this.blocoVarExtra = false;
                linhaTraduzida = "";
                this.incrementarQtdToken("inicio", true, false);
            } else if (this.blocoVarExtra) {
                linhaTraduzida = this.substituirBlocoVar(conteudoLinha);
            } else {
                linhaTraduzida = this.substituirBlocoInicio(conteudoLinha, conteudoLinhaLimpa);
            }
        }

        return linhaTraduzida;
    }

    private String substituirBlocoInicio(String conteudoLinha, String conteudoLinhaLimpa) {
        String linhaTraduzida = "\t";
        String espacoEmBranco = "";
        int inicio;
        int fim;
        String aux;

        char caracteres[] = conteudoLinha.toCharArray();
        for (char caracter : caracteres) {
            if (caracter == ' ') {
                espacoEmBranco += " ";
            } else {
                break;
            }
        }

        linhaTraduzida += espacoEmBranco;

        while (conteudoLinha.contains("int(")) {
            inicio = conteudoLinha.indexOf("int(") + 4;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("int(" + aux + ")", "(int) " + aux);
            this.incrementarQtdToken("int", true, true);
        }
        while (conteudoLinha.contains("maiusc(")) {
            inicio = conteudoLinha.indexOf("maiusc(") + 7;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("maiusc(" + aux + ")", "strtoupper(" + aux + ")");
            this.incrementarQtdToken("maiusc", true, true);
        }
        while (conteudoLinha.contains("minusc(")) {
            inicio = conteudoLinha.indexOf("minusc(") + 7;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("minusc(" + aux + ")", "strtolower(" + aux + ")");
            this.incrementarQtdToken("minusc", true, true);
        }
        while (conteudoLinha.contains("caracpnum(")) {
            inicio = conteudoLinha.indexOf("caracpnum(") + 10;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("caracpnum(" + aux + ")", "(int) " + aux);
            this.incrementarQtdToken("caracpnum", true, true);
        }
        while (conteudoLinha.contains("compr(")) {
            inicio = conteudoLinha.indexOf("compr(") + 6;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("compr(" + aux + ")", "strlen(" + aux + ")");
            this.incrementarQtdToken("compr", true, true);
        }
        while (conteudoLinha.contains("numpcarac(")) {
            inicio = conteudoLinha.indexOf("numpcarac(") + 10;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("numpcarac(" + aux + ")", "strval(" + aux + ")");
            this.incrementarQtdToken("numpcarac", true, true);
        }

        if (conteudoLinhaLimpa.startsWith("fimalgoritmo")) {
            linhaTraduzida = "?>";
            this.incrementarQtdToken("fimalgoritmo", true, true);
        } else if (conteudoLinhaLimpa.startsWith("se ")) {
            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            fim = conteudoLinhaLimpa.indexOf(" entao");

            String condicao = this.substituirComandos(conteudoLinhaLimpa.substring(inicio, fim));

            linhaTraduzida += "if (" + condicao + ") {";
            this.incrementarQtdToken("se", true, true);
        } else if (conteudoLinhaLimpa.startsWith("senao") || conteudoLinhaLimpa.startsWith("senão")) {
            linhaTraduzida += "} else {";
            this.incrementarQtdToken("senao", true, true);
        } else if (conteudoLinhaLimpa.startsWith("fimse")) {
            linhaTraduzida += "}";
            this.incrementarQtdToken("fimse", true, true);
        } else if (conteudoLinhaLimpa.startsWith("enquanto")) {
            inicio = conteudoLinha.indexOf("(");
            fim = conteudoLinha.lastIndexOf(")") + 1;

            String condicao = this.substituirComandos(conteudoLinha.substring(inicio, fim));

            linhaTraduzida += "while " + condicao + " {";
            this.incrementarQtdToken("enquanto", true, true);
        } else if (conteudoLinhaLimpa.startsWith("fimenquanto")) {
            linhaTraduzida += "}";
            this.incrementarQtdToken("fimenquanto", true, true);
        } else if (conteudoLinhaLimpa.startsWith("para")) {
            conteudoLinhaLimpa = conteudoLinhaLimpa.replace("até", "ate");

            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            fim = conteudoLinhaLimpa.indexOf(" ", inicio) + 1;
            String variavel = "$" + conteudoLinhaLimpa.substring(inicio, fim).trim();

            inicio = conteudoLinhaLimpa.indexOf(" de ") + 4;
            fim = conteudoLinhaLimpa.indexOf(" ", inicio) + 1;
            String valor1 = conteudoLinhaLimpa.substring(inicio, fim).trim();

            inicio = conteudoLinhaLimpa.indexOf(" ate ") + 5;
            fim = conteudoLinhaLimpa.indexOf(" ", inicio) + 1;
            String valorN = conteudoLinhaLimpa.substring(inicio, fim).trim();

            this.incrementarQtdToken("para", true, true);
            this.incrementarQtdToken("ate", true, true);

            if (conteudoLinhaLimpa.contains(" passo ")) {
                inicio = conteudoLinhaLimpa.indexOf(" passo ") + 7;
                fim = conteudoLinhaLimpa.indexOf(" ", inicio) + 1;
                String passo = conteudoLinhaLimpa.substring(inicio, fim).trim();

                linhaTraduzida += "for (" + variavel + " = " + valor1 + "; " + variavel + " <= " + valorN + "; " + variavel + " += " + passo + ") {";
                this.incrementarQtdToken("passo", true, true);
            } else {
                linhaTraduzida += "for (" + variavel + " = " + valor1 + "; " + variavel + " <= " + valorN + "; " + variavel + "++) {";
            }
            this.incrementarQtdToken("faca", true, true);
        } else if (conteudoLinhaLimpa.startsWith("fimpara")) {
            linhaTraduzida += "}";
            this.incrementarQtdToken("fimpara", true, true);
        } else if (conteudoLinhaLimpa.startsWith("escolha")) {
            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            String variavel = "$" + conteudoLinhaLimpa.substring(inicio).trim();

            linhaTraduzida += "switch (" + variavel + ") {";
            this.incrementarQtdToken("escolha", true, true);
        } else if (conteudoLinhaLimpa.startsWith("caso")) {
            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            String variaveis[] = conteudoLinhaLimpa.substring(inicio).trim().split(",");

            linhaTraduzida = "";
            for (String v : variaveis) {
                linhaTraduzida += "\t" + espacoEmBranco + "case " + v.trim() + ":\n";
            }

            linhaTraduzida = linhaTraduzida.substring(0, linhaTraduzida.length() - 1);
            this.incrementarQtdToken("caso", true, true);
        } else if (conteudoLinhaLimpa.startsWith("outrocaso")) {
            linhaTraduzida += "default:";
            this.incrementarQtdToken("outrocaso", true, true);
        } else if (conteudoLinhaLimpa.startsWith("fimescolha")) {
            linhaTraduzida += "}";
            this.incrementarQtdToken("fimescolha", true, true);
        } else if (conteudoLinhaLimpa.startsWith("interrompa")) {
            linhaTraduzida += "break;";
            this.incrementarQtdToken("interrompa", true, true);
        } else if (conteudoLinhaLimpa.startsWith("escreval")) {
            if (conteudoLinhaLimpa.trim().equalsIgnoreCase("escreval")) {
                linhaTraduzida += "echo \"\\n\";";
            } else {
                inicio = conteudoLinha.indexOf("(");
                fim = conteudoLinha.lastIndexOf(")") + 1;
                String conteudo = conteudoLinha.substring(inicio, fim).replace(",", ".");
                linhaTraduzida += "echo" + conteudo + " . \"\\n\";";
            }

            this.incrementarQtdToken("escreval", true, true);
        } else if (conteudoLinhaLimpa.startsWith("escreva")) {
            if (conteudoLinhaLimpa.trim().equalsIgnoreCase("escreva")) {
                linhaTraduzida += "echo \"\";";
            } else {
                inicio = conteudoLinha.indexOf("(");
                fim = conteudoLinha.lastIndexOf(")") + 1;
                String conteudo = conteudoLinha.substring(inicio, fim).replace(",", ".");

                linhaTraduzida += "echo " + conteudo + ";";
            }

            this.incrementarQtdToken("escreva", true, true);
        } else if (conteudoLinhaLimpa.startsWith("leia(") || conteudoLinhaLimpa.startsWith("leia (")) {
            inicio = conteudoLinha.indexOf("(") + 1;

            String variavel;
            if (conteudoLinha.contains("[")) {
                fim = conteudoLinha.lastIndexOf("]") + 1;
                variavel = conteudoLinha.substring(inicio, fim).trim();
            } else {
                fim = conteudoLinha.lastIndexOf(")");
                variavel = conteudoLinha.substring(inicio, fim).trim();
            }

            linhaTraduzida += "$" + variavel + " = fgets($handle);";

            this.incrementarQtdToken("leia", true, true);
        } else {
            if (conteudoLinhaLimpa.contains("retorne ")) {
                conteudoLinha = conteudoLinha.replace("retorne ", "return ");
                this.incrementarQtdToken("retorne", true, true);
            } else if (conteudoLinhaLimpa.contains(":=")) {
                conteudoLinha = conteudoLinha.replace(":=", "=");
                this.incrementarQtdToken(":=", true, true);
            }
            if (conteudoLinhaLimpa.contains("<-")) {
                conteudoLinha = conteudoLinha.replace("<-", "=");
                this.incrementarQtdToken("<-", true, true);
            }
            if (conteudoLinhaLimpa.contains("verdadeiro")) {
                conteudoLinha = conteudoLinha.replace("verdadeiro", "true");
                this.incrementarQtdToken("verdadeiro", true, true);
            }
            if (conteudoLinhaLimpa.contains("falso")) {
                conteudoLinha = conteudoLinha.replace("falso", "false");
                this.incrementarQtdToken("falso", true, true);
            }
            if (conteudoLinhaLimpa.contains("<>")) {
                conteudoLinha = conteudoLinha.replace("<>", "!=");
                this.incrementarQtdToken("<>", true, true);
            }
            if (conteudoLinhaLimpa.contains("não") || conteudoLinhaLimpa.contains("nao")) {
                conteudoLinha = conteudoLinha.replace("nao", "!").replace("não", "!");
                this.incrementarQtdToken("nao", true, true);
            }
            if (conteudoLinhaLimpa.contains(" mod ")) {
                conteudoLinha = conteudoLinha.replace(" mod ", " % ");
                this.incrementarQtdToken("mod", true, true);
            }

            linhaTraduzida += conteudoLinha.trim() + ";";
        }

        return linhaTraduzida;
    }

    // -----------------
    //
    public String obterClasse() {
        return this.classe;
    }

    public List<Token> obterTokensAtt() {
        return this.tokens;
    }

    // -----------------
    //
    private String substituirComandos(String comando) {
        if (comando.contains("verdadeiro")) {
            comando = comando.replace("verdadeiro", "true");
            this.incrementarQtdToken("verdadeiro", true, true);
        }
        if (comando.contains("falso")) {
            comando = comando.replace("falso", "false");
            this.incrementarQtdToken("falso", true, true);
        }
        if (comando.contains("=")) {
            comando = comando.replace("=", "==");
            this.incrementarQtdToken("=", true, true);
        }
        if (comando.contains("<>")) {
            comando = comando.replace("<>", "!=");
            this.incrementarQtdToken("<>", true, true);
        }
        if (comando.contains(" e ")) {
            comando = comando.replace(" e ", " && ");
            this.incrementarQtdToken("e", true, true);
        }
        if (comando.contains(" ou ")) {
            comando = comando.replace(" ou ", " || ");
            this.incrementarQtdToken("ou", true, true);
        }
        if (comando.contains(" não ") || comando.contains(" nao ")) {
            comando = comando.replace(" nao ", "!").replace(" não ", "!");
            this.incrementarQtdToken("nao", true, true);
        }
        if (comando.contains(" mod ")) {
            comando = comando.replace(" mod ", " % ");
            this.incrementarQtdToken("mod", true, true);
        }

        return comando;
    }

    public void incrementarQtdToken(String token, boolean qtdVisualg, boolean qtdPHP) {
        for (Token t : this.tokens) {
            if (t.getTokenVisualg().equalsIgnoreCase(token)) {
                if (qtdVisualg) {
                    t.setQtdVisualg(t.getQtdVisualg() + 1);
                }
                if (qtdPHP) {
                    t.setQtdPHP(t.getQtdPHP() + 1);
                }

                break;
            }
        }
    }

}
