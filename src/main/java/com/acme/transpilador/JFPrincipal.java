package com.acme.transpilador;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.io.FileUtils;

public class JFPrincipal extends javax.swing.JFrame {

    public JFPrincipal() {
        initComponents();
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

        jtaCodigoVisualg.setEditable(false);
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
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jScrollPane3)
                            .addGap(0, 0, 0)))
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
        jbTranspilar.setEnabled(false);
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

        int returnValue = jfc.showDialog(null, "Selecionar");

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            jtfArquivo.setText(selectedFile.getAbsolutePath());

            jbTranspilar.setEnabled(true);

            try {
                String content = FileUtils.readFileToString(selectedFile, StandardCharsets.ISO_8859_1);
                jtaCodigoVisualg.setText(content);
            } catch (IOException ex) {
                Logger.getLogger(JFPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jbSelecionarArquivoActionPerformed

    private void jbTranspilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbTranspilarActionPerformed
        String codigo = jtaCodigoVisualg.getText();

        String aux[] = codigo.split("\"");
        String nomeClasse = aux[1];

        codigo = codigo.replace("Algoritmo \"" + nomeClasse + "\"", "public class " + nomeClasse + " {");

        codigo = codigo.replace("Fimalgoritmo", "\t}\n}\n");

        jtaCodigoJava.setText(codigo);

        // TODO add your handling code here:
    }//GEN-LAST:event_jbTranspilarActionPerformed

    private void jbSalvarJavaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarJavaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbSalvarJavaActionPerformed

    private void jbSalvarPHPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSalvarPHPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbSalvarPHPActionPerformed

    private void jbMostrarTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbMostrarTabelaActionPerformed
        // TODO add your handling code here:
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
