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

import jfreerails.client.view.DialogueBoxController;
import jfreerails.client.view.RHSJTabPane;
import jfreerails.client.view.TerrainInfoJPanel;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.track.FreerailsTile;

public class TerrainHelper {

	
    public static void showTerrainInfo(int x, int y, TerrainInfoJPanel terrainInfo, ReadOnlyWorld world, DialogueBoxController dialogueBoxController) {
    	showTerrainInfo(x, y, terrainInfo, world);
        dialogueBoxController.showContent(terrainInfo);
    }
    
    
    public static void showTerrainInfo(int x, int y, TerrainInfoJPanel terrainInfo, ReadOnlyWorld world) {
        FreerailsTile tile = (FreerailsTile) world.getTile(x, y);
        int terrainType = tile.getTerrainTypeID();
        terrainInfo.setTerrainType(terrainType);
    }
    
    public static void showTerrainInfo(int x, int y, RHSJTabPane rhControlPanel, ReadOnlyWorld world) {
    	showTerrainInfo(x, y, rhControlPanel.getTerrainInfoPanel(), world);
    	rhControlPanel.selectTerrainInfoPanel();
    }


	public static void showTerrainInfo(int x, int y,
			ReadOnlyWorld world) {
		// TODO Auto-generated method stub
		
	}
}
