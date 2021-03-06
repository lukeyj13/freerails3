/*
 * ClientOptionsJPanel.java
 *
 * Created on 20 December 2003, 15:57
 */

package jfreerails.launcher;

import java.awt.DisplayMode;
import java.net.InetSocketAddress;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jfreerails.client.view.DisplayModesComboBoxModels;
import jfreerails.controller.MyDisplayMode;
import jfreerails.controller.ScreenHandler;
import jfreerails.launcher.LauncherInterface.MSG_TYPE;

import org.apache.log4j.Logger;

/**
 * The Launcher panel that lets you choose fullscreen or windowed mode and the
 * screen resolution etc.
 * 
 * @author rtuck99@users.sourceforge.net
 * @author Luke Lindsay
 */
class ClientOptionsJPanel extends javax.swing.JPanel implements LauncherPanel {
    private static final long serialVersionUID = 3256721779883325748L;

    private static final Logger logger = Logger
            .getLogger(ClientOptionsJPanel.class.getName());

    private final LauncherInterface owner;

    private String[] names;

    private static final String INVALID_PORT = "A valid port value is between between 0 and 65535.";

    private final DocumentListener documentListener = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }

        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

        public void changedUpdate(DocumentEvent e) {
            validateInput();
        }

    };

    /**
     * If the user has opted to load a game, we need to limit the list of
     * players to participants in the game we are loading. Otherwise, any player
     * name is OK. Either, pass in a array of names or null if any name is OK.
     */
    void limitPlayerNames(String[] n) {
        this.names = n;
        if (names == null) {
            playerName.setVisible(true);
            playerNames.setVisible(false);
            playerName.setEditable(true);
        } else {
            if (names.length == 1) {
                playerName.setVisible(true);
                playerNames.setVisible(false);
                playerName.setText(n[0]);
                playerName.setEditable(false);
            } else {
                playerName.setVisible(false);
                playerNames.setVisible(true);
                ComboBoxModel model = new DefaultComboBoxModel(names);
                playerNames.setModel(model);
            }
        }
        this.revalidate();
    }

    String getPlayerName() {
        if (playerName.isVisible()) {
            return playerName.getText();
        }
        int index = playerNames.getSelectedIndex();
        if (index < 0)
            return null; // no selection.
        return names[index];

    }

    DisplayMode getDisplayMode() {
        if (this.fullScreenButton.isSelected()) {
            MyDisplayMode displayMode = ((MyDisplayMode) jList1
                    .getSelectedValue());
            if (logger.isDebugEnabled()) {
                logger.debug("The selected display mode is "
                        + displayMode.toString());
            }
            return displayMode.displayMode;
        }
        return null;
    }

    InetSocketAddress getRemoteServerAddress() {
        String portStr = remotePort.getText();
        if (portStr == null) {
            return null;
        }
        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            return null;
        }
        InetSocketAddress address;
        try {
            address = new InetSocketAddress(remoteIP.getText(), port);
        } catch (IllegalArgumentException e) {
            return null;
        }
        /*
         * cut and pasted InetSocketAddress isa = getRemoteServerAddress(); if
         * (isa == null) { infoText = "Please enter a valid remote server
         * address"; } else if (isa.isUnresolved()) { infoText = "Couldn't
         * resolve remote server address"; } else { isValid = true; }
         */
        return address;
    }

    public boolean validateInput() {
        /* Validate player name. */
        if (playerName.getText() == null || playerName.getText().equals("")) {
            owner.setInfoText("Please set a name for your player",
                    MSG_TYPE.ERROR);
            return false;
        }

        /* Validate host name. */
        if (remoteIP.getText() == null || remoteIP.getText().equals("")) {
            owner.setInfoText("Please enter a host name", MSG_TYPE.ERROR);
            return false;
        }

        /* Validate port. */
        try {
            int port = Integer.parseInt(remotePort.getText());
            if (port < 0 || port > 65535) {
                owner.setInfoText(INVALID_PORT, MSG_TYPE.ERROR);
                return false;
            }
        } catch (Exception e) {
            owner.setInfoText(INVALID_PORT, MSG_TYPE.ERROR);
            return false;
        }

        /*
         * Validate display-mode selection. Note, on some systems the display
         * mode can't be changed, in which case the list of selectable display
         * modes will have length 0.
         */
        if (fullScreenButton.isSelected() && jList1.getModel().getSize() > 0
                && jList1.getSelectedIndex() == -1) {
            owner.setInfoText("Select a display-mode.", MSG_TYPE.ERROR);
            return false;
        }

        /* Everything is ok. */
        owner.hideErrorMessages();

        owner.setProperty(LauncherInterface.SERVER_PORT_PROPERTY,
                this.remotePort.getText());
        owner.setProperty(LauncherInterface.PLAYER_NAME_PROPERTY,
                this.playerName.getText());
        owner.setProperty(LauncherInterface.SERVER_IP_ADDRESS_PROPERTY,
                this.remoteIP.getText());
        owner.setProperty(LauncherInterface.CLIENT_FULLSCREEN_PROPERTY, Boolean
                .toString(fullScreenButton.isSelected()));
        if (this.getDisplayMode() != null) {
            owner.setProperty(LauncherInterface.CLIENT_DISPLAY_PROPERTY,
                    new MyDisplayMode(this.getDisplayMode()).toString());
        }
        owner.saveProps();
        return true;
    }

    int getScreenMode() {
        if (this.fullScreenButton.isSelected()) {
            return ScreenHandler.FULL_SCREEN;
        } else if (this.windowedButton.isSelected()) {
            return ScreenHandler.WINDOWED_MODE;
        } else if (this.fixedSizeButton.isSelected()) {
            return ScreenHandler.FIXED_SIZE_WINDOWED_MODE;
        } else {
            throw new IllegalStateException();
        }
    }

    public void setControlsEnabled(boolean enabled) {
        windowedButton.setEnabled(enabled);
        fullScreenButton.setEnabled(enabled);
        fixedSizeButton.setEnabled(enabled);
        if (fullScreenButton.isSelected()) {
            jList1.setEnabled(enabled);
        }
    }

    private DisplayModesComboBoxModels listModel;

    void setRemoteServerPanelVisible(boolean b) {
        this.jPanel4.setVisible(b);
    }

    public ClientOptionsJPanel(LauncherInterface owner) {
        this.owner = owner;
        initComponents();
        validateInput();
        // Listen for changes in the server port text box.
        remotePort.getDocument().addDocumentListener(documentListener);
        remoteIP.getDocument().addDocumentListener(documentListener);
        playerName.getDocument().addDocumentListener(documentListener);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        playerName = new javax.swing.JTextField();
        playerNames = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        remoteIP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        remotePort = new javax.swing.JTextField();
        spacer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        windowedButton = new javax.swing.JRadioButton();
        fixedSizeButton = new javax.swing.JRadioButton();
        fullScreenButton = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel3.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Player Details"));
        jLabel1.setText("Player name:");
        jPanel3.add(jLabel1);

        playerName.setColumns(12);
        playerName.setText(owner
                .getProperty(LauncherInterface.PLAYER_NAME_PROPERTY));
        jPanel3.add(playerName);

        playerNames.setToolTipText("Select a player from the saved game.");
        jPanel3.add(playerNames);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jPanel3, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel4
                .setBorder(new javax.swing.border.TitledBorder(
                        new javax.swing.border.EtchedBorder(),
                        "Remote server address"));
        jPanel4.setEnabled(false);
        jLabel2.setText("IP Address:");
        jPanel4.add(jLabel2, new java.awt.GridBagConstraints());

        remoteIP.setColumns(15);
        remoteIP.setText(owner
                .getProperty(LauncherInterface.SERVER_IP_ADDRESS_PROPERTY));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(remoteIP, gridBagConstraints);

        jLabel3.setText("port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(jLabel3, gridBagConstraints);

        remotePort.setColumns(5);
        remotePort.setText(owner
                .getProperty(LauncherInterface.SERVER_PORT_PROPERTY));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(remotePort, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(spacer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(jPanel4, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.TitledBorder(
                new javax.swing.border.EtchedBorder(), "Select Display Mode"));
        jScrollPane1.setBorder(new javax.swing.border.BevelBorder(
                javax.swing.border.BevelBorder.LOWERED));

        String displayMode = owner
                .getProperty(LauncherInterface.CLIENT_DISPLAY_PROPERTY);
        String fullscreenProp = owner
                .getProperty(LauncherInterface.CLIENT_FULLSCREEN_PROPERTY);
        boolean fullscreen = false;
        if (displayMode != null && displayMode.trim().length() > 0) {
            fullscreen = Boolean.valueOf(fullscreenProp);
        }

        jList1
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (!fullscreen) {
            jList1.setEnabled(false);
        } else {
            jList1.setEnabled(true);
        }

        jList1
                .addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(
                            javax.swing.event.ListSelectionEvent evt) {
                        jList1ValueChanged(evt);
                    }
                });

        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2,
                javax.swing.BoxLayout.Y_AXIS));

        buttonGroup1.add(windowedButton);
        if (!fullscreen) {
            windowedButton.setSelected(true);
        }
        windowedButton.setText("Windowed");
        jPanel2.add(windowedButton);

        buttonGroup1.add(fixedSizeButton);
        fixedSizeButton.setText("Windowed (fixed size 640*480)");
        jPanel2.add(fixedSizeButton);

        buttonGroup1.add(fullScreenButton);
        if (fullscreen) {
            fullScreenButton.setSelected(true);
        }
        fullScreenButton.setText("Full screen");
        fullScreenButton
                .addChangeListener(new javax.swing.event.ChangeListener() {
                    public void stateChanged(javax.swing.event.ChangeEvent evt) {
                        fullScreenButtonStateChanged(evt);
                    }
                });

        jPanel2.add(fullScreenButton);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        listModel = new DisplayModesComboBoxModels();
        listModel.removeDisplayModesBelow(640, 480, 16);
        jList1.setModel(listModel);
        int pos = 0;
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).toString().equals(displayMode)) {
                pos = i;
                break;
            }
        }
        jList1.setSelectedIndex(pos);
        jList1.ensureIndexIsVisible(jList1.getSelectedIndex());
    }// GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jList1ValueChanged
        validateInput();
    }// GEN-LAST:event_jList1ValueChanged

    private void formComponentShown(java.awt.event.ComponentEvent evt) {// GEN-FIRST:event_formComponentShown
        validateInput();
    }// GEN-LAST:event_formComponentShown

    private void fullScreenButtonStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_fullScreenButtonStateChanged
        jList1.setEnabled(fullScreenButton.isSelected());
        validateInput();
    }// GEN-LAST:event_fullScreenButtonStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.ButtonGroup buttonGroup1;

    javax.swing.JRadioButton fixedSizeButton;

    javax.swing.JRadioButton fullScreenButton;

    javax.swing.JLabel jLabel1;

    javax.swing.JLabel jLabel2;

    javax.swing.JLabel jLabel3;

    javax.swing.JList jList1;

    javax.swing.JPanel jPanel1;

    javax.swing.JPanel jPanel2;

    javax.swing.JPanel jPanel3;

    javax.swing.JPanel jPanel4;

    javax.swing.JScrollPane jScrollPane1;

    javax.swing.JTextField playerName;

    javax.swing.JComboBox playerNames;

    javax.swing.JTextField remoteIP;

    javax.swing.JTextField remotePort;

    javax.swing.JPanel spacer;

    javax.swing.JRadioButton windowedButton;
    // End of variables declaration//GEN-END:variables

}
