/*
 * Created on 01-Jun-2003
 *
 */
package jfreerails.client.view;

import java.awt.Graphics;

import javax.swing.Action;
import javax.swing.JLabel;

import jfreerails.client.renderer.RenderersRoot;
import jfreerails.controller.ModelRoot;
import jfreerails.world.common.Money;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.top.ReadOnlyWorld;

/**
 * This JLabel shows the amount of cash available.
 * 
 * @author Luke
 * 
 */
public class CashJLabel extends JLabel implements View {
    private static final long serialVersionUID = 3257853181542412341L;

    private ReadOnlyWorld w;

    private FreerailsPrincipal principal;

    public CashJLabel() {
        this.setText("          ");
    }

    public void setup(ModelRoot model, RenderersRoot vl, Action closeAction) {
        this.w = model.getWorld();
        principal = model.getPrincipal();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (null != w) {
            Money m = w.getCurrentBalance(principal);
            String s = m.toString();
            this.setText("$" + s);
        }

        super.paintComponent(g);
    }
}