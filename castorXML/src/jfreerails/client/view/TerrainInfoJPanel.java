/*
 * TerrainInfoJPanel.java
 *
 * Created on 04 May 2003, 17:47
 */

package jfreerails.client.view;

import java.awt.Image;

import javax.swing.ImageIcon;

import jfreerails.client.renderer.RenderersRoot;
import jfreerails.world.cargo.CargoType;
import jfreerails.world.terrain.Consumption;
import jfreerails.world.terrain.Conversion;
import jfreerails.world.terrain.Production;
import jfreerails.world.terrain.TerrainType;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.top.SKEY;
import jfreerails.world.train.WagonType;

/**
 * This JPanel shows information on a terrain type.
 * 
 * @author Luke
 */
public class TerrainInfoJPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 3258131375164045363L;

    private RenderersRoot rr;

    private ReadOnlyWorld w;

    public TerrainInfoJPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {// GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        terrainImage = new javax.swing.JLabel();
        terrainName = new javax.swing.JLabel();
        terrainDescription = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        terrainImage.setIcon(new javax.swing.ImageIcon(getClass().getResource(
                "/jfreerails/client/graphics/terrain/City_0.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        add(terrainImage, gridBagConstraints);

        terrainName.setFont(new java.awt.Font("Dialog", 1, 14));
        terrainName.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        add(terrainName, gridBagConstraints);

        terrainDescription.setFont(new java.awt.Font("Dialog", 0, 12));
        terrainDescription
                .setText("<html>\n<p>Right-of-Way costs X per mile. </p>\n<table width=\"75%\" >\n  <tr> \n    <td><strong>Supplies:</strong></td>\n    <td>&nbsp;</td>\n  </tr>\n  <tr> \n    <td>Mail </td>\n    <td>2</td>\n  </tr>\n  <tr> \n    <td>Passangers</td>\n    <td>2</td>\n  </tr>\n  <tr> \n    <td> <strong>Demands</strong></td>\n    <td>&nbsp;</td>\n  </tr>\n  <tr> \n    <td>Mail</td>\n    <td>&nbsp;</td>\n  </tr>\n  <tr> \n    <td>Passengers</td>\n    <td>&nbsp;</td>\n  </tr>\n  <tr> \n    <td><strong>Converts</strong></td>\n    <td>&nbsp;</td>\n  </tr>\n  <tr> \n    <td>Livestock to Food</td>\n    <td>&nbsp;</td>\n  </tr>\n  <tr>\n    <td>Steel to Goods</td>\n    <td>&nbsp;</td>\n  </tr>\n</table>\n</html>");
        terrainDescription.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        add(terrainDescription, gridBagConstraints);

    }// GEN-END:initComponents

    public void setup(ReadOnlyWorld w, RenderersRoot vl) {
        this.w = w;
        this.rr = vl;
    }

    public void setTerrainType(int typeNumber) {

        TerrainType type = (TerrainType) w.get(SKEY.TERRAIN_TYPES, typeNumber);

        String row = "<p>Right-of-Way costs $" + type.getRightOfWay()
                + " per mile. </p>";
        String tableString = "";
        int cargosProduced = type.getProduction().size();
        int cargosConsumed = type.getConsumption().size();
        int cargosConverted = type.getConversion().size();
        if ((cargosProduced + cargosConsumed + cargosConverted) > 0) {
            // if the terrain type produces, consumes, or converts anything.
            tableString = "<table width=\"75%\" >";
            if (cargosProduced != 0) {
                tableString += "<tr> <td><strong>Supplies</strong></td> <td>&nbsp;</td> </tr>";
                for (int i = 0; i < cargosProduced; i++) {
                    Production p = type.getProduction().get(i);
                    CargoType c = (CargoType) w.get(SKEY.CARGO_TYPES, p
                            .getCargoType());
                    String supply = String.valueOf(p.getRate()
                            / WagonType.UNITS_OF_CARGO_PER_WAGON);
                    tableString += "<tr> <td>" + c.getDisplayName()
                            + " </td><td>" + supply + "</td></tr>";
                }
            }
            if (cargosConsumed != 0) {
                tableString += "<tr> <td><strong>Demands</strong></td> <td>&nbsp;</td> </tr>";
                for (int i = 0; i < cargosConsumed; i++) {
                    Consumption p = type.getConsumption().get(i);
                    CargoType c = (CargoType) w.get(SKEY.CARGO_TYPES, p
                            .getCargoType());
                    tableString += "<tr> <td>" + c.getDisplayName()
                            + " </td><td>&nbsp;</td></tr>";
                }
            }
            if (cargosConverted != 0) {
                tableString += "<tr> <td><strong>Converts</strong></td> <td>&nbsp;</td> </tr>";
                for (int i = 0; i < cargosConverted; i++) {
                    Conversion p = type.getConversion().get(i);
                    CargoType input = (CargoType) w.get(SKEY.CARGO_TYPES, p
                            .getInput());
                    CargoType output = (CargoType) w.get(SKEY.CARGO_TYPES, p
                            .getOutput());
                    tableString += "<tr> <td colspan=\"2\">"
                            + input.getDisplayName() + " to "
                            + output.getDisplayName() + "</td></tr>";
                }
            }
            tableString += "</table> ";
        }
        String labelString = "<html>" + row + tableString + "</html>";
        terrainDescription.setText(labelString);
        terrainName.setText(type.getDisplayName());

        Image tileIcon = rr.getTileViewWithNumber(typeNumber).getDefaultIcon();
        terrainImage.setIcon(new ImageIcon(tileIcon));

        repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel terrainDescription;

    private javax.swing.JLabel terrainImage;

    private javax.swing.JLabel terrainName;
    // End of variables declaration//GEN-END:variables

}
