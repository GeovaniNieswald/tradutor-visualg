package com.acme.tradutor;

import com.acme.tradutor.model.Token;
import com.acme.tradutor.model.Variavel;
import java.util.ArrayList;
import java.util.List;

public class TraduzirParaJava {

    private String classe;
    private List<Token> tokens;

    private List<Variavel> listVariaveis;
    private boolean blocoVarExtra;

    public TraduzirParaJava() {
        this.classe = "";
        this.tokens = new ArrayList<>();
        this.blocoVarExtra = false;
    }

    public String traduzir(String vetCodigoOriginal[], boolean leDados, List<Token> tokens) {
        this.tokens = tokens;
        this.listVariaveis = new ArrayList<>();

        String codigoTraduzido = leDados ? "import java.util.Scanner;\n\n" : "";

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
                linhaTraduziada = "public class " + this.classe + " {";
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
                        linhaTraduziada = "\tpublic static void main(String args[]) {";
                        linhaTraduziada += leDados ? "\n\t\tScanner scan = new Scanner(System.in);" : "";
                        this.incrementarQtdToken("inicio", true, true);
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
                            String tipo = this.substituirTipoVariavel(nomesTipo[1].trim());
                            String nomes[] = nomesTipo[0].trim().split(",");

                            for (int j = 0; j < nomes.length; j++) {
                                String nome = nomes[j];

                                if (j == 0) {
                                    parametrosProcedimento += tipo + " " + nome.trim();
                                } else {
                                    parametrosProcedimento += ", " + tipo + " " + nome.trim();
                                }
                            }
                        }

                        linhaTraduziada = "\tprivate static void " + nomeProcedimento + "(" + parametrosProcedimento + ") {";

                        this.incrementarQtdToken("procedimento", true, true);
                    } else if (conteudoLinhaLimpa.startsWith("funcao ") || conteudoLinhaLimpa.startsWith("função ")) {
                        blocoVar = false;
                        blocoFuncao = true;

                        inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
                        fim = conteudoLinhaLimpa.indexOf("(");
                        String nomeFuncao = conteudoLinhaLimpa.substring(inicio, fim).trim();
                        String tiposFuncao[] = conteudoLinhaLimpa.split(":");
                        String tipoRetorno = this.substituirTipoVariavel(tiposFuncao[tiposFuncao.length - 1].trim());
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
                            String tipo = this.substituirTipoVariavel(nomesTipo[1].trim());
                            String nomes[] = nomesTipo[0].trim().split(",");

                            for (int j = 0; j < nomes.length; j++) {
                                String nome = nomes[j];

                                if (j == 0) {
                                    parametrosFuncao += tipo + " " + nome.trim();
                                } else {
                                    parametrosFuncao += ", " + tipo + " " + nome.trim();
                                }
                            }
                        }

                        linhaTraduziada = "\tprivate static " + tipoRetorno + " " + nomeFuncao + "(" + parametrosFuncao + ") {";

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
                        linhaTraduziada = this.substituirBlocoVar(conteudoLinha, false);
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

    private String substituirBlocoVar(String conteudoLinha, boolean procedimentoFuncao) {
        String nomesTipo[] = conteudoLinha.split(":");

        String nomes = nomesTipo[0].trim().trim();
        String tipo = nomesTipo[1].trim().toLowerCase().trim();
        String tipoTraduzido;

        String linhaTraduzida = procedimentoFuncao ? "\t" : "\tprivate static ";

        if (tipo.startsWith("vetor")) {
            String tamanhoTipoComentario[] = tipo.split(" de ");
            String tipoComentario[] = tamanhoTipoComentario[1].split("//");

            String tamanho = tamanhoTipoComentario[0].trim();
            tamanho = tamanho.replace(" ", "").replace("vetor", "").replace("[", "").replace("]", "");
            tipo = tipoComentario[0].trim();
            String comentario = "";

            if (tipoComentario.length > 1) {
                comentario = "//" + tipoComentario[1];
                this.incrementarQtdToken("//", true, true);
            }

            tipoTraduzido = this.substituirTipoVariavel(tipo);

            boolean ehMatriz = tamanho.contains(",");

            if (ehMatriz) {
                String tamanhoMatriz[] = tamanho.split(",");
                int tamanhoMat1 = Integer.parseInt(tamanhoMatriz[0].split("\\.\\.")[1]);
                int tamanhoMat2 = Integer.parseInt(tamanhoMatriz[1].split("\\.\\.")[1]);

                linhaTraduzida += tipoTraduzido + "[][] " + nomes + " = new " + tipoTraduzido + "[" + tamanhoMat1 + "]" + "[" + tamanhoMat2 + "]; " + comentario;
            } else {
                int tamanhoVet = Integer.parseInt(tamanho.split("\\.\\.")[1]);
                linhaTraduzida += tipoTraduzido + "[] " + nomes + " = new " + tipoTraduzido + "[" + tamanhoVet + "]; " + comentario;
            }
            this.incrementarQtdToken("vetor", true, true);
        } else {
            String tipoComentario[] = tipo.split("//");

            tipo = tipoComentario[0].trim();
            tipoTraduzido = this.substituirTipoVariavel(tipo);

            String comentario = "";
            if (tipoComentario.length > 1) {
                comentario = "//" + tipoComentario[1];
                this.incrementarQtdToken("//", true, true);
            }

            linhaTraduzida += tipoTraduzido;
            linhaTraduzida += " " + nomes + "; " + comentario;
        }

        Variavel v;
        String nomesVar[] = nomes.split(",");
        for (String var : nomesVar) {
            v = new Variavel(var.trim(), tipoTraduzido);
            this.listVariaveis.add(v);
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
                linhaTraduzida = this.substituirBlocoVar(conteudoLinha, true);
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
                linhaTraduzida = this.substituirBlocoVar(conteudoLinha, true);
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
            conteudoLinha = conteudoLinha.replace("maiusc(" + aux + ")", aux + ".toUpperCase()");
            this.incrementarQtdToken("maiusc", true, true);
        }
        while (conteudoLinha.contains("minusc(")) {
            inicio = conteudoLinha.indexOf("minusc(") + 7;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("minusc(" + aux + ")", aux + ".toLowerCase()");
            this.incrementarQtdToken("minusc", true, true);
        }
        while (conteudoLinha.contains("caracpnum(")) {
            inicio = conteudoLinha.indexOf("caracpnum(") + 10;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("caracpnum(" + aux + ")", "Integer.parseInt(" + aux + ")");
            this.incrementarQtdToken("caracpnum", true, true);
        }
        while (conteudoLinha.contains("compr(")) {
            inicio = conteudoLinha.indexOf("compr(") + 6;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("compr(" + aux + ")", aux + ".length()");
            this.incrementarQtdToken("compr", true, true);
        }
        while (conteudoLinha.contains("numpcarac(")) {
            inicio = conteudoLinha.indexOf("numpcarac(") + 10;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("numpcarac(" + aux + ")", "String.valueOf(" + aux + ")");
            this.incrementarQtdToken("numpcarac", true, true);
        }

        if (conteudoLinhaLimpa.startsWith("fimalgoritmo")) {
            linhaTraduzida = "\t}\n}";
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
            String variavel = conteudoLinhaLimpa.substring(inicio, fim).trim();

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
            String variavel = conteudoLinhaLimpa.substring(inicio).trim();

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
                linhaTraduzida += "System.out.println(\"\");";
            } else {
                inicio = conteudoLinha.indexOf("(");
                fim = conteudoLinha.lastIndexOf(")") + 1;
                String conteudo = conteudoLinha.substring(inicio, fim).replace(",", "+");
                linhaTraduzida += "System.out.println" + conteudo + ";";
            }

            this.incrementarQtdToken("escreval", true, true);
        } else if (conteudoLinhaLimpa.startsWith("escreva")) {
            if (conteudoLinhaLimpa.trim().equalsIgnoreCase("escreva")) {
                linhaTraduzida += "System.out.print(\"\");";
            } else {
                inicio = conteudoLinha.indexOf("(");
                fim = conteudoLinha.lastIndexOf(")") + 1;
                String conteudo = conteudoLinha.substring(inicio, fim).replace(",", "+");

                linhaTraduzida += "System.out.print" + conteudo + ";";
            }

            this.incrementarQtdToken("escreva", true, true);
        } else if (conteudoLinhaLimpa.startsWith("leia(") || conteudoLinhaLimpa.startsWith("leia (")) {
            inicio = conteudoLinha.indexOf("(") + 1;

            String variavel;
            String variavelCompleta;

            if (conteudoLinha.contains("[")) {
                fim = conteudoLinha.lastIndexOf("[");
                variavel = conteudoLinha.substring(inicio, fim).trim();
                fim = conteudoLinha.lastIndexOf("]") + 1;
                variavelCompleta = conteudoLinha.substring(inicio, fim).trim();
            } else {
                fim = conteudoLinha.lastIndexOf(")");
                variavel = conteudoLinha.substring(inicio, fim).trim();
                variavelCompleta = conteudoLinha.substring(inicio, fim).trim();
            }

            String tipoVariavel = this.qualTipoVariavel(variavel);

            switch (tipoVariavel) {
                case "int":
                    linhaTraduzida += variavelCompleta + " = Integer.parseInt(scan.nextLine());";
                    break;
                case "double":
                    linhaTraduzida += variavelCompleta + " = Double.parseDouble(scan.nextLine());";
                    break;
                case "String":
                    linhaTraduzida += variavelCompleta + " = scan.nextLine();";
                    break;
                case "boolean":
                    linhaTraduzida += variavelCompleta + " = Boolean.parseBoolean(scan.nextLine());";
                    break;
                default:
                    linhaTraduzida += variavelCompleta + " = scan.nextLine();";
                    break;
            }

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
    private String substituirTipoVariavel(String tipoOrigem) {
        tipoOrigem = tipoOrigem.trim().toLowerCase();
        String tipoDestino;

        switch (tipoOrigem) {
            case "inteiro":
                tipoDestino = "int";
                break;
            case "real":
                tipoDestino = "double";
                break;
            case "caractere":
            case "caracter":
                tipoDestino = "String";
                break;
            case "logico":
                tipoDestino = "boolean";
                break;
            default:
                tipoDestino = tipoOrigem;
                break;
        }

        this.incrementarQtdToken(tipoOrigem, true, true);

        return tipoDestino;
    }

    private String qualTipoVariavel(String variavel) {
        String tipoVariavel = "";

        for (Variavel v : listVariaveis) {
            if (v.getNome().equalsIgnoreCase(variavel)) {
                return v.getTipo();
            }
        }

        return tipoVariavel;
    }

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

    public void incrementarQtdToken(String token, boolean qtdVisualg, boolean qtdJava) {
        for (Token t : this.tokens) {
            if (t.getTokenVisualg().equalsIgnoreCase(token)) {
                if (qtdVisualg) {
                    t.setQtdVisualg(t.getQtdVisualg() + 1);
                }
                if (qtdJava) {
                    t.setQtdJava(t.getQtdJava() + 1);
                }

                break;
            }
        }
    }

}
