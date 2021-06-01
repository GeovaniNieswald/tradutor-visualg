package com.acme.transpilador;

import com.acme.transpilador.model.Token;
import com.acme.transpilador.model.Variavel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;

public class JFPrincipal extends javax.swing.JFrame {

    private List<Variavel> variaveis;
    private List<Token> tokens;
    private String classe;

    public JFPrincipal() {
        initComponents();
    }

    private String transpilarParaJava(String vetCodigoOriginal[], boolean leDados) {
        String codigoTranspiladoJava = leDados ? "import java.util.Scanner;\n\n" : "";

        int numeroLinha = 0;
        boolean blocoVar = false;
        boolean blocoProcedimento = false;
        boolean blocoFuncao = false;
        String conteudoLinhaLimpa;
        String linhaTranspilada;

        for (var conteudoLinha : vetCodigoOriginal) {
            conteudoLinhaLimpa = conteudoLinha.trim().toLowerCase();

            if (numeroLinha == 0) {
                String nomeClasse[] = conteudoLinha.split("\"");
                this.classe = this.primeiraLetraMaiuscula(this.removerCaracteresEspeciais(nomeClasse[1]));
                linhaTranspilada = "public class " + this.classe + " {";
                this.incrementarQtdToken("algoritmo");
            } else {
                if (conteudoLinhaLimpa.startsWith("//") || conteudoLinhaLimpa.isEmpty()) {
                    linhaTranspilada = "\t" + conteudoLinha;
                    this.incrementarQtdToken("//");
                } else {
                    if (conteudoLinhaLimpa.startsWith("var") && blocoProcedimento == false && blocoFuncao == false) {
                        blocoVar = true;
                        linhaTranspilada = "";
                        this.incrementarQtdToken("var");
                    } else if (conteudoLinhaLimpa.startsWith("inicio") && blocoProcedimento == false && blocoFuncao == false) {
                        blocoVar = false;
                        linhaTranspilada = "\tpublic static void main(String args[]) {";
                        linhaTranspilada += leDados ? "\n\t\tScanner scan = new Scanner(System.in);" : "";
                        this.incrementarQtdToken("inicio");
                    } else if (conteudoLinhaLimpa.startsWith("procedimento ")) {
                        blocoVar = false;
                        blocoProcedimento = true;

                        // TODO: Transpilar primeira linha do procedimento
                        linhaTranspilada = "\tstatic void ";

                        this.incrementarQtdToken("procedimento");
                    } else if (conteudoLinhaLimpa.startsWith("funcao ") || conteudoLinhaLimpa.startsWith("função ")) {
                        blocoVar = false;
                        blocoFuncao = true;

                        // TODO: Transpilar primeira linha da função
                        linhaTranspilada = "\tstatic ";

                        this.incrementarQtdToken("funcao");
                    } else if (conteudoLinhaLimpa.startsWith("fimprocedimento")) {
                        blocoProcedimento = false;
                        linhaTranspilada = "\t}";
                        this.incrementarQtdToken("fimprocedimento");
                    } else if (conteudoLinhaLimpa.startsWith("fimfuncao") || conteudoLinhaLimpa.startsWith("fimfunção")) {
                        blocoFuncao = false;
                        linhaTranspilada = "\t}";
                        this.incrementarQtdToken("fimfuncao");
                    } else if (blocoVar) {
                        linhaTranspilada = this.substituirVariaveisJava(conteudoLinha);
                    } else if (blocoProcedimento) {
                        linhaTranspilada = this.substituirProcedimentoJava(conteudoLinha);
                    } else if (blocoFuncao) {
                        linhaTranspilada = this.substituirFuncaoJava(conteudoLinha);
                    } else {
                        linhaTranspilada = this.substituirBlocoInicio(conteudoLinha, conteudoLinhaLimpa);
                    }
                }
            }

            codigoTranspiladoJava += linhaTranspilada + "\n";
            numeroLinha++;
        }

        return codigoTranspiladoJava;
    }

    private String transpilarParaPHP(String vetCodigoOriginal[]) {
        String codigoTranspiladoPHP = "";
        // TODO: Fazer transpilação para PHP
        return codigoTranspiladoPHP;
    }

    private String substituirVariaveisJava(String conteudoLinha) {
        String nomesTipo[] = conteudoLinha.split(":");

        String nomes = nomesTipo[0].trim().toLowerCase().trim();
        String tipo = nomesTipo[1].trim().toLowerCase().trim();
        String tipoTranspilado;

        String linhaTranspilada = "\tprivate static ";

        if (tipo.startsWith("vetor")) {
            String tamanhoTipoComentario[] = tipo.split(" de ");
            String tipoComentario[] = tamanhoTipoComentario[1].split("//");

            String tamanho = tamanhoTipoComentario[0].trim();
            tamanho = tamanho.replace(" ", "").replace("vetor", "").replace("[", "").replace("]", "");
            tipo = tipoComentario[0].trim();
            String comentario = "";

            if (tipoComentario.length > 1) {
                comentario = "//" + tipoComentario[1];
                this.incrementarQtdToken("//");
            }

            tipoTranspilado = this.substituirTipoVariavelJava(tipo);

            boolean ehMatriz = tamanho.contains(",");

            if (ehMatriz) {
                String tamanhoMatriz[] = tamanho.split(",");
                int tamanhoMat1 = Integer.parseInt(tamanhoMatriz[0].split("\\.\\.")[1]);
                int tamanhoMat2 = Integer.parseInt(tamanhoMatriz[1].split("\\.\\.")[1]);

                linhaTranspilada += tipoTranspilado + "[][] " + nomes + " = new " + tipoTranspilado + "[" + tamanhoMat1 + "]" + "[" + tamanhoMat2 + "]; " + comentario;
            } else {
                int tamanhoVet = Integer.parseInt(tamanho.split("\\.\\.")[1]);
                linhaTranspilada += tipoTranspilado + "[] " + nomes + " = new " + tipoTranspilado + "[" + tamanhoVet + "]; " + comentario;
            }
            this.incrementarQtdToken("vetor");
        } else {
            String tipoComentario[] = tipo.split("//");

            tipo = tipoComentario[0].trim();
            tipoTranspilado = this.substituirTipoVariavelJava(tipo);
            String comentario = tipoComentario.length > 1 ? "//" + tipoComentario[1] : "";

            linhaTranspilada += tipoTranspilado;
            linhaTranspilada += " " + nomes + "; " + comentario;
        }

        Variavel v;
        String nomesVar[] = nomes.split(",");
        for (String var : nomesVar) {
            v = new Variavel(var.trim(), tipoTranspilado);
            this.variaveis.add(v);
        }

        return linhaTranspilada;
    }

    private String substituirTipoVariavelJava(String tipoOrigem) {
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
                tipoDestino = "String";
                break;
            case "logico":
                tipoDestino = "boolean";
                break;
            default:
                tipoDestino = tipoOrigem;
                break;
        }

        this.incrementarQtdToken(tipoOrigem);

        return tipoDestino;
    }

    private String substituirProcedimentoJava(String conteudoLinha) {
        String linhaTranspilada = "\t";
        // TODO: Transpilar bloco Procedimento
        return linhaTranspilada;
    }

    private String substituirFuncaoJava(String conteudoLinha) {
        String linhaTranspilada = "\t";
        // TODO: Transpilar bloco Função
        return linhaTranspilada;
    }

    private String substituirBlocoInicio(String conteudoLinha, String conteudoLinhaLimpa) {
        String linhaTranspilada = "\t";
        String espacoEmBranco = "";
        int inicio;
        int fim;
        String aux;

        char caracteres[] = conteudoLinha.toCharArray();
        for (var caracter : caracteres) {
            if (caracter == ' ') {
                espacoEmBranco += " ";
            } else {
                break;
            }
        }

        linhaTranspilada += espacoEmBranco;

        while (conteudoLinha.contains("int(")) {
            inicio = conteudoLinha.indexOf("int(") + 4;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("int(" + aux + ")", "(int) " + aux);
            this.incrementarQtdToken("int");
        }
        while (conteudoLinha.contains("maiusc(")) {
            inicio = conteudoLinha.indexOf("maiusc(") + 7;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("maiusc(" + aux + ")", aux + ".toUpperCase()");
            this.incrementarQtdToken("maiusc");
        }
        while (conteudoLinha.contains("minusc(")) {
            inicio = conteudoLinha.indexOf("minusc(") + 7;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("minusc(" + aux + ")", aux + ".toLowerCase()");
            this.incrementarQtdToken("minusc");
        }
        while (conteudoLinha.contains("caracpnum(")) {
            inicio = conteudoLinha.indexOf("caracpnum(") + 10;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("caracpnum(" + aux + ")", "Integer.parseInt(" + aux + ")");
            this.incrementarQtdToken("caracpnum");
        }
        while (conteudoLinha.contains("compr(")) {
            inicio = conteudoLinha.indexOf("compr(") + 6;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("compr(" + aux + ")", aux + ".length()");
            this.incrementarQtdToken("compr");
        }
        while (conteudoLinha.contains("numpcarac(")) {
            inicio = conteudoLinha.indexOf("numpcarac(") + 10;
            fim = conteudoLinha.indexOf(")", inicio);
            aux = conteudoLinha.substring(inicio, fim).trim();
            conteudoLinha = conteudoLinha.replace("numpcarac(" + aux + ")", "String.valueOf(" + aux + ")");
            this.incrementarQtdToken("numpcarac");
        }

        if (conteudoLinhaLimpa.startsWith("fimalgoritmo")) {
            linhaTranspilada = "\t}\n}";
            this.incrementarQtdToken("fimalgoritmo");
        } else if (conteudoLinhaLimpa.startsWith("se ")) {
            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            fim = conteudoLinhaLimpa.indexOf(" entao");

            String condicao = this.substituirComandos(conteudoLinhaLimpa.substring(inicio, fim));

            linhaTranspilada += "if (" + condicao + ") {";
            this.incrementarQtdToken("se");
        } else if (conteudoLinhaLimpa.startsWith("senao") || conteudoLinhaLimpa.startsWith("senão")) {
            linhaTranspilada += "} else {";
            this.incrementarQtdToken("senao");
        } else if (conteudoLinhaLimpa.startsWith("fimse")) {
            linhaTranspilada += "}";
            this.incrementarQtdToken("fimse");
        } else if (conteudoLinhaLimpa.startsWith("enquanto")) {
            inicio = conteudoLinha.indexOf("(");
            fim = conteudoLinha.lastIndexOf(")") + 1;

            String condicao = this.substituirComandos(conteudoLinha.substring(inicio, fim));

            linhaTranspilada += "while " + condicao + " {";
            this.incrementarQtdToken("enquanto");
        } else if (conteudoLinhaLimpa.startsWith("fimenquanto")) {
            linhaTranspilada += "}";
            this.incrementarQtdToken("fimenquanto");
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

            this.incrementarQtdToken("para");
            this.incrementarQtdToken("ate");

            if (conteudoLinhaLimpa.contains(" passo ")) {
                inicio = conteudoLinhaLimpa.indexOf(" passo ") + 7;
                fim = conteudoLinhaLimpa.indexOf(" ", inicio) + 1;
                String passo = conteudoLinhaLimpa.substring(inicio, fim).trim();

                linhaTranspilada += "for (" + variavel + " = " + valor1 + "; " + variavel + " <= " + valorN + "; " + variavel + " += " + passo + ") {";
                this.incrementarQtdToken("passo");
            } else {
                linhaTranspilada += "for (" + variavel + " = " + valor1 + "; " + variavel + " <= " + valorN + "; " + variavel + "++) {";
            }
            this.incrementarQtdToken("faca");
        } else if (conteudoLinhaLimpa.startsWith("fimpara")) {
            linhaTranspilada += "}";
            this.incrementarQtdToken("fimpara");
        } else if (conteudoLinhaLimpa.startsWith("escolha")) {
            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            String variavel = conteudoLinhaLimpa.substring(inicio).trim();

            linhaTranspilada += "switch (" + variavel + ") {";
            this.incrementarQtdToken("escolha");
        } else if (conteudoLinhaLimpa.startsWith("caso")) {
            inicio = conteudoLinhaLimpa.indexOf(" ") + 1;
            String variaveis[] = conteudoLinhaLimpa.substring(inicio).trim().split(",");

            linhaTranspilada = "";
            for (var v : variaveis) {
                linhaTranspilada += "\t" + espacoEmBranco + "case " + v.trim() + ":\n";
            }

            linhaTranspilada = linhaTranspilada.substring(0, linhaTranspilada.length() - 1);
            this.incrementarQtdToken("caso");
        } else if (conteudoLinhaLimpa.startsWith("outrocaso")) {
            linhaTranspilada += "default:";
            this.incrementarQtdToken("outrocaso");
        } else if (conteudoLinhaLimpa.startsWith("fimescolha")) {
            linhaTranspilada += "}";
            this.incrementarQtdToken("fimescolha");
        } else if (conteudoLinhaLimpa.startsWith("interrompa")) {
            linhaTranspilada += "break;";
            this.incrementarQtdToken("interrompa");
        } else if (conteudoLinhaLimpa.startsWith("escreval")) {
            inicio = conteudoLinha.indexOf("(");
            fim = conteudoLinha.lastIndexOf(")") + 1;
            String conteudo = conteudoLinha.substring(inicio, fim).replace(",", "+");

            linhaTranspilada += "System.out.println" + conteudo + ";";
            this.incrementarQtdToken("escreval");
        } else if (conteudoLinhaLimpa.startsWith("escreva")) {
            inicio = conteudoLinha.indexOf("(");
            fim = conteudoLinha.lastIndexOf(")") + 1;
            String conteudo = conteudoLinha.substring(inicio, fim).replace(",", "+");

            linhaTranspilada += "System.out.print" + conteudo + ";";
            this.incrementarQtdToken("escreva");
        } else if (conteudoLinhaLimpa.startsWith("leia(")) {
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
                    linhaTranspilada += variavelCompleta + " = Integer.parseInt(scan.nextLine());";
                    break;
                case "double":
                    linhaTranspilada += variavelCompleta + " = Double.parseDouble(scan.nextLine());";
                    break;
                case "String":
                    linhaTranspilada += variavelCompleta + " = scan.nextLine();";
                    break;
                case "boolean":
                    linhaTranspilada += variavelCompleta + " = Boolean.parseBoolean(scan.nextLine());";
                    break;
                default:
                    linhaTranspilada += variavelCompleta + " = scan.nextLine();";
                    break;
            }

            this.incrementarQtdToken("leia");
        } else {
            if (conteudoLinhaLimpa.contains(":=")) {
                conteudoLinha = conteudoLinha.replace(":=", "=");
                this.incrementarQtdToken(":=");
            }
            if (conteudoLinhaLimpa.contains("<-")) {
                conteudoLinha = conteudoLinha.replace("<-", "=");
                this.incrementarQtdToken("<-");
            }
            if (conteudoLinhaLimpa.contains("verdadeiro")) {
                conteudoLinha = conteudoLinha.replace("verdadeiro", "true");
                this.incrementarQtdToken("verdadeiro");
            }
            if (conteudoLinhaLimpa.contains("falso")) {
                conteudoLinha = conteudoLinha.replace("falso", "false");
                this.incrementarQtdToken("falso");
            }
            if (conteudoLinhaLimpa.contains("<>")) {
                conteudoLinha = conteudoLinha.replace("<>", "!=");
                this.incrementarQtdToken("<>");
            }
            if (conteudoLinhaLimpa.contains("não") || conteudoLinhaLimpa.contains("nao")) {
                conteudoLinha = conteudoLinha.replace("nao", "!").replace("não", "!");
                this.incrementarQtdToken("nao");
            }
            if (conteudoLinhaLimpa.contains(" mod ")) {
                conteudoLinha = conteudoLinha.replace(" mod ", " % ");
                this.incrementarQtdToken("mod");
            }

            linhaTranspilada += conteudoLinha.trim() + ";";
        }

        return linhaTranspilada;
    }

    private String qualTipoVariavel(String variavel) {
        String tipoVariavel = "";

        for (Variavel v : variaveis) {
            if (v.getNome().equalsIgnoreCase(variavel)) {
                return v.getTipo();
            }
        }

        return tipoVariavel;
    }

    private String substituirComandos(String comando) {
        if (comando.contains("verdadeiro")) {
            comando = comando.replace("verdadeiro", "true");
            this.incrementarQtdToken("verdadeiro");
        }
        if (comando.contains("falso")) {
            comando = comando.replace("falso", "false");
            this.incrementarQtdToken("falso");
        }
        if (comando.contains("=")) {
            comando = comando.replace("=", "==");
            this.incrementarQtdToken("=");
        }
        if (comando.contains("<>")) {
            comando = comando.replace("<>", "!=");
            this.incrementarQtdToken("<>");
        }
        if (comando.contains(" e ")) {
            comando = comando.replace(" e ", " && ");
            this.incrementarQtdToken("e");
        }
        if (comando.contains(" ou ")) {
            comando = comando.replace(" ou ", " || ");
            this.incrementarQtdToken("ou");
        }
        if (comando.contains(" não ") || comando.contains(" nao ")) {
            comando = comando.replace(" nao ", "!").replace(" não ", "!");
            this.incrementarQtdToken("nao");
        }
        if (comando.contains(" mod ")) {
            comando = comando.replace(" mod ", " % ");
            this.incrementarQtdToken("mod");
        }

        return comando;
    }

    private String removerCaracteresEspeciais(String str) {
        return str.replaceAll("[^a-zZ-Z0-9]", "");
    }

    private String primeiraLetraMaiuscula(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private void incrementarQtdToken(String token) {
        for (Token t : tokens) {
            if (t.getTokenVisualg().equalsIgnoreCase(token)) {
                t.setQtd(t.getQtd() + 1);
                break;
            }
        }
    }

    private void popularArrayTokens() {
        this.tokens = new ArrayList<>();

        Token t = new Token("//", "//", 0);
        this.tokens.add(t);

        t = new Token("algoritmo", "public class", 0);
        this.tokens.add(t);

        t = new Token("fimalgoritmo", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("var", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("inteiro", "int", 0);
        this.tokens.add(t);

        t = new Token("vetor", "[]", 0);
        this.tokens.add(t);

        t = new Token("real", "double", 0);
        this.tokens.add(t);

        t = new Token("caractere", "String", 0);
        this.tokens.add(t);

        t = new Token("logico", "boolean", 0);
        this.tokens.add(t);

        t = new Token("inicio", "public static void main(String args[])", 0);
        this.tokens.add(t);

        t = new Token("se", "if", 0);
        this.tokens.add(t);

        t = new Token("entao", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("senao", "else", 0);
        this.tokens.add(t);

        t = new Token("fimse", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("enquanto", "while", 0);
        this.tokens.add(t);

        t = new Token("fimenquanto", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("para", "for", 0);
        this.tokens.add(t);

        t = new Token("ate", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("passo", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("faca", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("fimpara", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("escolha", "switch", 0);
        this.tokens.add(t);

        t = new Token("caso", "case", 0);
        this.tokens.add(t);

        t = new Token("outrocaso", "default", 0);
        this.tokens.add(t);

        t = new Token("fimescolha", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("interrompa", "break", 0);
        this.tokens.add(t);

        t = new Token("escreval", "System.out.println()", 0);
        this.tokens.add(t);

        t = new Token("escreva", "System.out.print()", 0);
        this.tokens.add(t);

        t = new Token("int", "(int)", 0);
        this.tokens.add(t);

        t = new Token("maiusc", ".toUpperCase()", 0);
        this.tokens.add(t);

        t = new Token("minusc", ".toLowerCase()", 0);
        this.tokens.add(t);

        t = new Token("caracpnum", "Integer.parseInt()", 0);
        this.tokens.add(t);

        t = new Token("compr", ".length()", 0);
        this.tokens.add(t);

        t = new Token("numpcarac", "String.valueOf()", 0);
        this.tokens.add(t);

        t = new Token("nao", "!", 0);
        this.tokens.add(t);

        t = new Token("falso", "false", 0);
        this.tokens.add(t);

        t = new Token("verdadeiro", "true", 0);
        this.tokens.add(t);

        t = new Token("mod", "%", 0);
        this.tokens.add(t);

        t = new Token("e", "&&", 0);
        this.tokens.add(t);

        t = new Token("ou", "||", 0);
        this.tokens.add(t);

        t = new Token("=", "==", 0);
        this.tokens.add(t);

        t = new Token("<>", "!=", 0);
        this.tokens.add(t);

        t = new Token(":=", "=", 0);
        this.tokens.add(t);

        t = new Token("<-", "=", 0);
        this.tokens.add(t);

        t = new Token("fimalgoritmo", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("leia", "new Scanner(System.in).nextLine()", 0);
        this.tokens.add(t);

        t = new Token("procedimento", "static void nome(tipo parâmetro, ...)", 0);
        this.tokens.add(t);

        t = new Token("fimprocedimento", "sem correspondência", 0);
        this.tokens.add(t);

        t = new Token("funcao", "static tipo nome(tipo parâmetro, ...)", 0);
        this.tokens.add(t);

        t = new Token("fimfuncao", "sem correspondência", 0);
        this.tokens.add(t);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfArquivo = new javax.swing.JTextField();
        jbSelecionarArquivo = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaCodigoVisualg = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaCodigoJava = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtaCodigoPHP = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jbSalvarJava = new javax.swing.JButton();
        jbSalvarPHP = new javax.swing.JButton();
        jbTranspilar = new javax.swing.JButton();
        jbMostrarTabela = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Transpilador Visualg para Java-PHP");
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Transpilador Visualg para Java-PHP");

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Arquivo");

        jtfArquivo.setEditable(false);
        jtfArquivo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtfArquivo.setFocusable(false);

        jbSelecionarArquivo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jbSelecionarArquivo.setText("Selecionar");
        jbSelecionarArquivo.setFocusPainted(false);
        jbSelecionarArquivo.setFocusable(false);
        jbSelecionarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelecionarArquivoActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel2.setText("Código em Visualg");

        jtaCodigoVisualg.setColumns(20);
        jtaCodigoVisualg.setRows(5);
        jtaCodigoVisualg.setPreferredSize(null);
        jScrollPane2.setViewportView(jtaCodigoVisualg);

        jtaCodigoJava.setEditable(false);
        jtaCodigoJava.setColumns(20);
        jtaCodigoJava.setRows(5);
        jScrollPane1.setViewportView(jtaCodigoJava);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel3.setText("Código em Java");

        jtaCodigoPHP.setEditable(false);
        jtaCodigoPHP.setColumns(20);
        jtaCodigoPHP.setRows(5);
        jScrollPane3.setViewportView(jtaCodigoPHP);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel6.setText("Código em PHP");

        jbSalvarJava.setText("Salvar");
        jbSalvarJava.setEnabled(false);
        jbSalvarJava.setFocusPainted(false);
        jbSalvarJava.setFocusable(false);
        jbSalvarJava.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSalvarJava.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalvarJavaActionPerformed(evt);
            }
        });

        jbSalvarPHP.setText("Salvar");
        jbSalvarPHP.setCursor(new java.awt.Cursor(java.awt.Cursor.W_RESIZE_CURSOR));
        jbSalvarPHP.setEnabled(false);
        jbSalvarPHP.setFocusPainted(false);
        jbSalvarPHP.setFocusable(false);
        jbSalvarPHP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSalvarPHPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                        .addComponent(jScrollPane3))
                    .addComponent(jLabel6)
                    .addComponent(jLabel3))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbSalvarJava)
                    .addComponent(jbSalvarPHP)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jbSalvarJava, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addGap(5, 5, 5)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbSalvarPHP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jbTranspilar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jbTranspilar.setText("Transpilar");
        jbTranspilar.setFocusPainted(false);
        jbTranspilar.setFocusable(false);
        jbTranspilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbTranspilarActionPerformed(evt);
            }
        });

        jbMostrarTabela.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jbMostrarTabela.setText("Mostrar Tabela com Tokens");
        jbMostrarTabela.setEnabled(false);
        jbMostrarTabela.setFocusPainted(false);
        jbMostrarTabela.setFocusable(false);
        jbMostrarTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbMostrarTabelaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jtfArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addComponent(jbSelecionarArquivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 310, Short.MAX_VALUE)
                        .addComponent(jbTranspilar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jbMostrarTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbSelecionarArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbTranspilar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbMostrarTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jbSelecionarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelecionarArquivoActionPerformed
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Selecione um arquivo");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Algoritmo Visualg", "alg", "ALG");
        jfc.addChoosableFileFilter(filter);

        int returnValue = jfc.showDialog(this, "Selecionar");

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            jtfArquivo.setText(selectedFile.getAbsolutePath());

            try {
                String content = FileUtils.readFileToString(selectedFile, StandardCharsets.ISO_8859_1);
                jtaCodigoVisualg.setText(content);
            } catch (IOException ex) {
                Logger.getLogger(JFPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jbSelecionarArquivoActionPerformed

    private void jbTranspilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTranspilarActionPerformed
        this.variaveis = new ArrayList<>();
        this.popularArrayTokens();

        String codigo = jtaCodigoVisualg.getText();

        if (!codigo.trim().isEmpty()) {
            boolean leDados = codigo.contains("leia(");
            String vetCodigoOriginal[] = codigo.split("\\n");

            String codigoTranspiladoJava = this.transpilarParaJava(vetCodigoOriginal, leDados);
            String codigoTranspiladoPHP = this.transpilarParaPHP(vetCodigoOriginal);

            jtaCodigoJava.setText(codigoTranspiladoJava);
            jtaCodigoPHP.setText(codigoTranspiladoPHP);

            jbMostrarTabela.setEnabled(true);
            jbSalvarJava.setEnabled(true);
        }
    }//GEN-LAST:event_jbTranspilarActionPerformed

    private void jbSalvarJavaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarJavaActionPerformed
        String codigoJava = jtaCodigoJava.getText();

        if (!codigoJava.trim().isEmpty()) {
            String arquivo = this.classe + ".java";

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Java", "java");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecione um local para salvar");
            fileChooser.setSelectedFile(new File(arquivo));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                try {
                    try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                        byte[] strToBytes = codigoJava.getBytes();
                        outputStream.write(strToBytes);
                    }

                    JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }//GEN-LAST:event_jbSalvarJavaActionPerformed

    private void jbSalvarPHPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarPHPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbSalvarPHPActionPerformed

    private void jbMostrarTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMostrarTabelaActionPerformed
        JFTabelaTokens frame = new JFTabelaTokens(tokens);
        frame.setVisible(true);
    }//GEN-LAST:event_jbMostrarTabelaActionPerformed

    public static void main(String args[]) {
        try {
            for (var info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals("Windows")) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new JFPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbMostrarTabela;
    private javax.swing.JButton jbSalvarJava;
    private javax.swing.JButton jbSalvarPHP;
    private javax.swing.JButton jbSelecionarArquivo;
    private javax.swing.JButton jbTranspilar;
    private javax.swing.JTextArea jtaCodigoJava;
    private javax.swing.JTextArea jtaCodigoPHP;
    private javax.swing.JTextArea jtaCodigoVisualg;
    private javax.swing.JTextField jtfArquivo;
    // End of variables declaration//GEN-END:variables
}
