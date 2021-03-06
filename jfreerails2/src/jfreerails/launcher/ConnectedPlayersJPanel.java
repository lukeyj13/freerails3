/*
 * ConnectedPlayersJPanel.java
 *
 * Created on 12 September 2004, 04:51
 */

package jfreerails.launcher;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jfreerails.network.FreerailsGameServer;

/**
 * A JPanel that shows the players currently logged in to the server.
 * 
 * @author Luke
 */
public class ConnectedPlayersJPanel extends javax.swing.JPanel implements
        PropertyChangeListener {

    private static final long serialVersionUID = 4049080453489111344L;

    FreerailsGameServer server = null;

    /** Creates new form ConnectedPlayersJPanel */
    public ConnectedPlayersJPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        title = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setLayout(new java.awt.GridBagLayout());

        title.setText("Connected Players");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 7, 7);
        add(title, gridBagConstraints);

        jList1.setModel(new javax.swing.AbstractListModel() {

            private static final long serialVersionUID = 1L;
            String[] strings = { "No players are logged on!" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

    }// GEN-END:initComponents

    void updateListOfPlayers() {
        if (null != server) {
            String[] playerNames = server.getPlayerNames();
            playerNames = playerNames.length == 0 ? new String[] { "No players are logged on!" }
                    : playerNames;
            setListOfPlayers(playerNames);
        }
    }

    void setListOfPlayers(String[] players) {
        jList1.setListData(players);
    }

    /** Called by the server when a player is added or removed. */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(FreerailsGameServer.CONNECTED_PLAYERS)) {
            if (EventQueue.isDispatchThread()) {
                updateListOfPlayers();
            } else {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        updateListOfPlayers();
                    }
                });
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JList jList1;

    javax.swing.JScrollPane jScrollPane1;

    javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables

}
