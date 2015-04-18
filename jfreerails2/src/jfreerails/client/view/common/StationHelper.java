/**
 * FreeRails 2 - A railroad strategy game Copyright (C) 2007 Roland Spatzenegger
 * (c@npg.net)
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA. 
 */

package jfreerails.client.view.common;

import jfreerails.client.common.ModelRootImpl;
import jfreerails.client.view.DialogueBoxController;
import jfreerails.client.view.RHSJTabPane;
import jfreerails.client.view.StationInfoJPanel;
import jfreerails.controller.ModelRoot;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.station.StationModel;
import jfreerails.world.top.KEY;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.track.FreerailsTile;
import jfreerails.world.track.TrackRule;

public class StationHelper {

    public static Integer findStationNumberAtPoint (int x, int y, ModelRoot modelRoot, ReadOnlyWorld world) {
    	Integer stationNumber = null;
    	
        FreerailsTile tile = (FreerailsTile) world.getTile(x, y);
        TrackRule trackRule = tile.getTrackPiece().getTrackRule();
        FreerailsPrincipal principal = modelRoot.getPrincipal();
        if (trackRule.isStation()
                && tile.getTrackPiece().getOwnerID() == world.getID(principal)) {

            for (int i = 0; i < world.size(principal, KEY.STATIONS); i++) {
                StationModel station = (StationModel) world.get(principal,
                        KEY.STATIONS, i);

                if (null != station && station.x == x && station.y == y) {
                	stationNumber = i;
                    return stationNumber;
                }
            }

            throw new IllegalStateException("Couldn't find station at " + x
                    + ", " + y);
        }
        return stationNumber;
    }

   
    private static void showStation (StationInfoJPanel stationInfoPanel, int stationNumber) {
    	stationInfoPanel.setStation(stationNumber);
    }

    public static void showStationInfo(int x, int y, RHSJTabPane rhControlPanel, ModelRoot modelRoot) {
    	Integer stationNumber = StationHelper.findStationNumberAtPoint (x, y, 
    			modelRoot, modelRoot.getWorld());
        if (stationNumber != null) {
        	// TODO clean
        	//this.showStationInfo(stationNumber);
//            showStationOrTerrain(this.stationInfo, stationNumber);
            showStation(rhControlPanel.getStationInfoPanel(), stationNumber);
            rhControlPanel.selectStationInfoPanel();
        }
        else {
        	TerrainHelper.showTerrainInfo(x, y, rhControlPanel, modelRoot.getWorld());
        }
        //dialogueBoxController.showTerrainInfo(x, y);
    }
    
    public static void showStationOrTerrainInfo(int x, int y, StationInfoJPanel stationInfo, DialogueBoxController dialogueBoxController, ModelRootImpl modelRoot) {
//    	Integer stationNumber = StationHelper.findStationNumberAtPoint (x, y, 
//    			modelRoot, modelRoot.getWorld());
//        if (stationNumber != null) {
//        	// TODO clean
//        	//this.showStationInfo(stationNumber);
////            showStationOrTerrain(this.stationInfo, stationNumber);
//            showStation(stationInfo, stationNumber);
//        }
//        showStationInfo (x, y, stationInfo, modelRoot);
        dialogueBoxController.showTerrainInfo(x, y);
    }
    
    public static void showStationOrTerrainInfo(int x, int y, RHSJTabPane rhControlPanel, ModelRoot modelRoot) {
        showStationInfo (x, y, rhControlPanel, modelRoot);
//        rhControlPanel.getTerrainInfoPanel().showTerrainInfo(x, y);
        
        //TerrainHelper.showTerrainInfo(x, y, rhControlPanel, modelRoot.getWorld());
    }
    
}

