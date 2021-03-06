/*
 * LoadGameJPanel.java
 *
 * Created on 30 August 2005, 21:48
 */

package jfreerails.client.view;

import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.Action;

import jfreerails.client.renderer.RenderersRoot;
import jfreerails.controller.Message2Server;
import jfreerails.controller.ModelRoot;
import jfreerails.controller.ModelRoot.Property;
import jfreerails.network.LoadGameMessage2Server;
import jfreerails.network.RefreshListOfGamesMessage2Server;
import jfreerails.world.common.ImStringList;

/**
 * 
 * @author Luke
 */
public class LoadGameJPanel extends javax.swing.JPanel implements View {

    private static final long serialVersionUID = -6810248272441137826L;

    private ImStringList lastFiles;

    /** Creates new form LoadGameJPanel */
    public LoadGameJPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
    // ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jList1
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1
                .addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        jList1ValueChanged(evt);
                    }
                });

        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(jScrollPane1, gridBagConstraints);

        jLabel1.setText("Please select a game to load.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(jLabel1, gridBagConstraints);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(okButton, gridBagConstraints);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(cancelButton, gridBagConstraints);

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(refreshButton, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshButtonActionPerformed
        Message2Server refreshGames = new RefreshListOfGamesMessage2Server(2);
        modelRoot.sendCommand(refreshGames);
    }// GEN-LAST:event_refreshButtonActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentShown

    }// GEN-LAST:event_formComponentShown

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
        if (null != close)
            close.actionPerformed(evt);
    }// GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonActionPerformed
        String filename = (String) jList1.getSelectedValue();
        Message2Server message2 = new LoadGameMessage2Server(1, filename);
        modelRoot.sendCommand(message2);

        if (null != close)
            close.actionPerformed(evt);
    }// GEN-LAST:event_okButtonActionPerformed

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jList1ValueChanged
        okButton.setEnabled(jList1.getSelectedIndex() != -1);
    }// GEN-LAST:event_jList1ValueChanged

    public void setup(ModelRoot m, RenderersRoot vl, Action closeAction) {
        this.close = closeAction;
        modelRoot = m;
        updateListOfFiles();
    }

    private void updateListOfFiles() {
        ImStringList files = (ImStringList) modelRoot
                .getProperty(Property.SAVED_GAMES_LIST);
        Object[] saves = new Object[files.size()];
        for (int i = 0; i < files.size(); i++) {
            saves[i] = files.get(i);
        }
        jList1.setListData(saves);
        okButton.setEnabled(jList1.getSelectedIndex() != -1);
        lastFiles = files;
    }

    @Override
    protected void paintComponent(Graphics g) {
        ImStringList files = (ImStringList) modelRoot
                .getProperty(Property.SAVED_GAMES_LIST);
        if (!lastFiles.equals(files)) {
            updateListOfFiles();
        }
        super.paintComponent(g);
    }

    ModelRoot modelRoot;
    ActionListener close;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton cancelButton;
    javax.swing.JLabel jLabel1;
    javax.swing.JList jList1;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JButton okButton;
    javax.swing.JButton refreshButton;
    // End of variables declaration//GEN-END:variables

}
