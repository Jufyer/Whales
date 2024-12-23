package org.jufyer.plugin.whales.listeners;

import org.jufyer.plugin.whales.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.AsyncStructureSpawnEvent;
import org.bukkit.generator.structure.StructureType;

import java.util.Random;

public class generateStructure implements Listener {
  @EventHandler
  public void onAsyncStructureSpawn(AsyncStructureSpawnEvent event) {
    if (event.getStructure().getStructureType().equals(StructureType.SHIPWRECK)) {
      if (new Random().nextDouble() < 0.5) {
      Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
        Location loc = new Location(event.getWorld(), event.getBoundingBox().getCenterX(), event.getBoundingBox().getMinY(), event.getBoundingBox().getCenterZ());
        placeBoneBlocks(loc);
      });
      event.setCancelled(true);

      }
    }
  }

  private void placeBoneBlocks(Location location) {
    // Finde den Bodenstandpunkt unterhalb des Ursprungs
    Location baseGroundLocation = getGroundLocation(location);

    int[][] offsets = {
      {-15, -21, 30}, {-1, -1, 5}, {-1, 0, -5}, {-1, 0, -1}, {-1, 0, 3},
      {-1, 1, -5}, {-1, 1, -4}, {-1, 1, -3}, {-1, 1, -2}, {-1, 1, -1},
      {0, 0, 1}, {0, 0, 5}, {0, 1, -1}, {0, 1, 3},
      {1, 0, -7}, {1, 0, -6}, {1, 0, -5}, {1, 0, -4}, {1, 0, -3}, {1, 0, -2}, {1, 0, -1}, {1, 0, 5}, {1, 0, 7}, {1, 0, 8},
      {1, 1, -7}, {1, 1, -6}, {1, 1, -5}, {1, 1, -4}, {1, 1, -3}, {1, 1, -2}, {1, 1, -1}, {1, 1, 1}, {1, 1, 3},
      {2, 0, -7}, {2, 0, -1}, {2, 0, 5}, {2, 0, 7}, {2, 0, 8}, {2, 0, 10},
      {2, 1, -7}, {2, 1, -6}, {2, 1, -5}, {2, 1, -4}, {2, 1, -3}, {2, 1, -1}, {2, 1, 1}, {2, 1, 3},
      {3, 0, -7}, {3, 0, -1}, {3, 0, 5}, {3, 0, 7}, {3, 0, 8}, {3, 0, 10}, {3, 0, 12},
      {3, 1, -7}, {3, 1, -6}, {3, 1, -5}, {3, 1, -4}, {3, 1, -3}, {3, 1, -2}, {3, 1, -1}, {3, 1, 1}, {3, 1, 3},
      {4, -1, 5}, {4, 0, -7}, {4, 0, -6}, {4, 0, -5}, {4, 0, -4}, {4, 0, -3}, {4, 0, -2}, {4, 0, -1}, {4, 0, 3},
      {4, 1, -7}, {4, 1, -6}, {4, 1, -5}, {4, 1, -4}, {4, 1, -3}, {4, 1, -2}, {4, 1, -1}, {4, 1, 1},
      {5, 0, 1}
    };


    // Baue die Struktur mit relativen Koordinaten
    for (int[] offset : offsets) {
      int offsetX = offset[0];
      int offsetY = offset[1];
      int offsetZ = offset[2];

      // Berechne die exakte Blockposition basierend auf dem Boden
      Location blockLocation = baseGroundLocation.clone().add(offsetX, offsetY, offsetZ);
      blockLocation.getBlock().setType(Material.BONE_BLOCK);
    }
  }

  /**
   * Findet die Bodenhöhe unter einer bestimmten Location.
   * Diese Methode berücksichtigt auch Wasser, damit Blöcke direkt am Meeresboden liegen.
   */
  private Location getGroundLocation(Location location) {
    Location groundLocation = location.clone();

    // Suche den Boden
    while (groundLocation.getY() > 0) {
      Block block = groundLocation.getBlock();
      if (!block.isEmpty() && !block.isLiquid()) {
        // Rücke einen Block nach oben, damit die Struktur direkt auf dem Boden sitzt
        groundLocation.add(0, 1, 0);
        break;
      }
      groundLocation.add(0, -1, 0);
    }

    return groundLocation;
  }

}
