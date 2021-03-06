package jfreerails.client.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;

import jfreerails.client.common.ModelRootImpl;
import jfreerails.client.common.ModelRootListener;
import jfreerails.client.renderer.RenderersRoot;
import jfreerails.controller.ModelRoot;
import jfreerails.world.common.ImPoint;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.track.FreerailsTile;

/**
 * The tabbed panel that sits in the lower right hand corner of the screen.
 * 
 * @author rob
 */
public class RHSJTabPane extends JTabbedPane implements ModelRootListener {
    private static final long serialVersionUID = 3906926798502965297L;

    private final TerrainInfoJPanel terrainInfoPanel;


	private final StationInfoJPanel stationInfoPanel;

	private final TrainListJPanel trainListPanel;

    private final BuildTrackJPanel buildTrackPanel;

    private ReadOnlyWorld world;

    private int trainListIndex;
    private int stationInfoIndex;
    private int terrainInfoIndex;
    
    public RHSJTabPane() {
        /*
         * Dont accept keyboard focus since we want to leave it with the main
         * map view.
         */
        setFocusable(false);

        ImageIcon trainListIcon;
        ImageIcon buildTrackIcon;
        ImageIcon stationInfoIcon;
        
        /* set up trainsJTabbedPane */
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        terrainInfoPanel = new TerrainInfoJPanel();

        trainListPanel = new TrainListJPanel(true);

        buildTrackPanel = new BuildTrackJPanel();
        trainListPanel.removeButtons();

        URL terrainInfoIconUrl = getClass().getResource(
                "/jfreerails/client/graphics/icons/terrain_info.png");
        ImageIcon terrainInfoIcon = new ImageIcon(terrainInfoIconUrl);

        URL buildTrackIconUrl = getClass().getResource(
                "/jfreerails/client/graphics/icons/track_new.png");
        buildTrackIcon = new ImageIcon(buildTrackIconUrl);
        
        URL trainListIconUrl = getClass().getResource(
                "/jfreerails/client/graphics/icons/train_list.png");
        trainListIcon = new ImageIcon(trainListIconUrl);
        
        URL stationListIconUrl = getClass().getResource(
                "/jfreerails/client/graphics/icons/train_list.png");
        stationInfoIcon = new ImageIcon(stationListIconUrl);
        
        // Note titles set to null so only the icon appears at the top of the
        // top.
        JScrollPane terrainInfoJScrollPane = new JScrollPane(terrainInfoPanel);
        terrainInfoJScrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        addTab(null, terrainInfoIcon, terrainInfoJScrollPane, "Terrain Info");
        this.terrainInfoIndex= this.getTabCount()-1;
        
        stationInfoPanel = new StationInfoJPanel();
        stationInfoPanel.removeCloseButton();
        // Don't show the station info tab until it has been rewritten to take
        // up less space.
         JScrollPane stationInfoJScrollPane = new
         JScrollPane(stationInfoPanel);
         stationInfoJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
         addTab(null, stationInfoIcon, stationInfoJScrollPane, "Station Info");
         this.stationInfoIndex= this.getTabCount()-1;

        trainListPanel.setTrainViewHeight(20);
        addTab(null, buildTrackIcon, buildTrackPanel, "Build Track");
        addTab(null, trainListIcon, trainListPanel, "Train List");
        this.trainListIndex = this.getTabCount() - 1;

        /* These values were picked by trial and error! */
        this.setMinimumSize(new Dimension(250, 200));

    }

    public void setup(final ActionRoot actionRoot, RenderersRoot vl,
            final ModelRootImpl modelRoot) {
        world = modelRoot.getWorld();
        terrainInfoPanel.setup(world, vl);
        stationInfoPanel.setup(modelRoot, vl, null);

        ActionListener showTrain = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = trainListPanel.getSelectedTrainID();
                actionRoot.getDialogueBoxController().showTrainOrders(id);
            }
        };

        trainListPanel.setShowTrainDetailsActionListener(showTrain);
        trainListPanel.setup(modelRoot, vl, null);
        modelRoot.addPropertyChangeListener(this);

        buildTrackPanel.setup(modelRoot, actionRoot, vl, null);
    }

    /**
     * Updates the Terrain Info Panel if the specfied PropertyChangeEvent was
     * triggered by the cursor moving.
     */
    public void propertyChange(ModelRoot.Property prop, Object before,
            Object after) {
        if (prop.equals(ModelRoot.Property.CURSOR_POSITION)) {
            ImPoint p = (ImPoint) after;
            terrainInfoPanel.setTerrainType(((FreerailsTile) world.getTile(p.x,
                    p.y)).getTerrainTypeID());
        }
    }

    public void setTrainTabEnabled(boolean enabled) {
        this.setEnabledAt(this.trainListIndex, enabled);
    }

//    public void setStationTabEnabled(boolean enabled) {
//        this.setEnabledAt(this.stationInfoIndex, enabled);
//    }
//    
//    public void setTerrainTabEnabled(boolean enabled) {
//        this.setEnabledAt(this.terrainInfoIndex, enabled);
//    }
    
    public void selectTerrainInfoPanel() {
    	this.setEnabledAt(terrainInfoIndex, true);
    	this.setSelectedIndex(terrainInfoIndex);
    }
    
    public void selectStationInfoPanel() {
    	this.setEnabledAt(stationInfoIndex, true);
    	this.setSelectedIndex(stationInfoIndex);
    }
    
    public StationInfoJPanel getStationInfoPanel() {
		return stationInfoPanel;
	}
    
    public TerrainInfoJPanel getTerrainInfoPanel() {
		return terrainInfoPanel;
	}


}