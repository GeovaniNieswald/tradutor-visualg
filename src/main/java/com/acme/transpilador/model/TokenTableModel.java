package com.acme.transpilador.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TokenTableModel extends AbstractTableModel {

    private final String[] colunas = new String[]{"Token Visualg", "Token Java", "Quantidade"};
    private List<Token> linhas = new ArrayList<>();

    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Token t = linhas.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return t.getTokenVisualg();
            case 1:
                return t.getTokenJava();
            case 2:
                return t.getQtd();
            default:
                throw new ArrayIndexOutOfBoundsException("Coluna " + columnIndex + " não existe");
        }
    }

    public void removeRow(int row) {
        linhas.remove(0);
        fireTableRowsDeleted(row, row);
    }

    public Token getUnidade(int index) {
        return linhas.get(index);
    }

    public void setDados(List<Token> tokens) {
        linhas = tokens;
    }

}
