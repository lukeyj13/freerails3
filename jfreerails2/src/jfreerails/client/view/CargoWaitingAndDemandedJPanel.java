/*
 * CargoWaitingAndDemandedJPanel.java
 *
 * Created on 07 February 2004, 12:24
 */

package jfreerails.client.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import jfreerails.client.renderer.RenderersRoot;
import jfreerails.controller.ModelRoot;
import jfreerails.world.cargo.CargoType;
import jfreerails.world.cargo.ImmutableCargoBundle;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.station.StationModel;
import jfreerails.world.top.KEY;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.top.SKEY;
import jfreerails.world.train.WagonType;

/**
 * A JPanel that displays the cargo waiting and demanded at a station - used on
 * the select station popup window.
 * 
 * @author Luke
 */
public class CargoWaitingAndDemandedJPanel extends javax.swing.JPanel implements
        View {

    private static final long serialVersionUID = 3760559784860071476L;

    private ReadOnlyWorld world;

    private FreerailsPrincipal principal;

    public CargoWaitingAndDemandedJPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        stationName = new javax.swing.JLabel();
        waiting = new javax.swing.JLabel();
        waitingJTable = new javax.swing.JTable();
        demands = new javax.swing.JLabel();
        demandsJList = new javax.swing.JList();
        spacer = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(100, 200));
        jScrollPane1.setBorder(null);
        jScrollPane1
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        stationName.setText("Station Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel1.add(stationName, gridBagConstraints);

        waiting.setText("Waiting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(waiting, gridBagConstraints);

        waitingJTable.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.background"));
        waitingJTable.setFont(new java.awt.Font("Dialog", 0, 10));
        waitingJTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] { { "Mail", "4" }, { "Passengers", null } },
                new String[] { "Title 1", "Title 2" }));
        waitingJTable
                .setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        waitingJTable.setFocusable(false);
        waitingJTable.setRequestFocusEnabled(false);
        waitingJTable.setRowSelectionAllowed(false);
        waitingJTable.setShowHorizontalLines(false);
        waitingJTable.setShowVerticalLines(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(waitingJTable, gridBagConstraints);

        demands.setText("Demands");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(demands, gridBagConstraints);

        demandsJList.setBackground(javax.swing.UIManager.getDefaults()
                .getColor("Button.background"));
        demandsJList.setFont(new java.awt.Font("Dialog", 0, 10));
        demandsJList.setFocusable(false);
        demandsJList.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(demandsJList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(spacer, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

    }// GEN-END:initComponents

    public void setup(ModelRoot model, RenderersRoot vl, Action closeAction) {
        this.world = model.getWorld();
        this.principal = model.getPrincipal();
    }

    public void display(int newStationID) {
        StationModel station = (StationModel) world.get(principal,
                KEY.STATIONS, newStationID);
        this.stationName.setText(station.getStationName());
        final ImmutableCargoBundle cargoWaiting = (ImmutableCargoBundle) world
                .get(principal, KEY.CARGO_BUNDLES, station.getCargoBundleID());

        // count the number of cargo types waiting and demanded.
        final ArrayList<String> typeWaiting = new ArrayList<String>();
        final ArrayList<Integer> quantityWaiting = new ArrayList<Integer>();
        final List<String> typeDemanded = new ArrayList<String>();
        for (int i = 0; i < world.size(SKEY.CARGO_TYPES); i++) {
            CargoType cargoType = (CargoType) world.get(SKEY.CARGO_TYPES, i);
            int amountWaiting = cargoWaiting.getAmount(i);

            if (0 != amountWaiting) {
                typeWaiting.add(cargoType.getDisplayName());
                int carloads = amountWaiting
                        / WagonType.UNITS_OF_CARGO_PER_WAGON;
                quantityWaiting.add(new Integer(carloads));
            }
            if (station.getDemand().isCargoDemanded(i)) {
                typeDemanded.add(cargoType.getDisplayName());
            }
        }

        /*
         * The table shows the cargo waiting at the station. First column is
         * cargo type; second column is quantity in carloads.
         */
        TableModel tableModel = new AbstractTableModel() {

            private static final long serialVersionUID = 3760559784860071476L;

            public int getRowCount() {
                return typeWaiting.size();
            }

            public int getColumnCount() {
                return 2;
            }

            public Object getValueAt(int row, int column) {
                if (0 == column) {
                    return typeWaiting.get(row);
                }
                return quantityWaiting.get(row);
            }
        };
        this.waitingJTable.setModel(tableModel);

        /* The list shows the cargo demanded by the station. */
        this.demandsJList.setListData(typeDemanded.toArray());

        this.invalidate();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel demands;

    private javax.swing.JList demandsJList;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JPanel spacer;

    private javax.swing.JLabel stationName;

    private javax.swing.JLabel waiting;

    private javax.swing.JTable waitingJTable;
    // End of variables declaration//GEN-END:variables

}
