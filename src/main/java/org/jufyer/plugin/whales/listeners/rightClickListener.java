package org.jufyer.plugin.whales.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class rightClickListener implements Listener {
  private static final int MAX_LOOT_ITEMS = 5;

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    if (event.getPlayer().getActiveItem() != null && event.getRightClicked().getName().equals("with")) {
      if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.SHEARS) {
        event.getRightClicked().setCustomName("out");

        Location loc = event.getRightClicked().getLocation();

        ItemStack barnacles = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta meta = barnacles.getItemMeta();
        meta.setDisplayName("§rBarnacle");
        meta.setCustomModelData(123);
        barnacles.setItemMeta(meta);
        loc.getWorld().dropItemNaturally(loc, barnacles);

        NamespacedKey fishingLootKey = LootTables.FISHING.getKey();
        LootTable fishingLootTable = Bukkit.getLootTable(fishingLootKey);
        if (fishingLootTable != null) {
          List<ItemStack> lootList = new ArrayList<>();
          Random random = new Random();

          for (int i = 0; i < MAX_LOOT_ITEMS; i++) {
            LootContext lootContext = new LootContext.Builder(loc)
              .lootedEntity(event.getRightClicked())
              .killer(event.getPlayer())
              .build();

            Collection<ItemStack> loot = fishingLootTable.populateLoot(random, lootContext);
            lootList.addAll(loot);

            if (lootList.size() >= MAX_LOOT_ITEMS) {
              break;
            }
          }

          while (lootList.size() > MAX_LOOT_ITEMS) {
            lootList.remove(random.nextInt(lootList.size()));
          }

          for (ItemStack item : lootList) {
            loc.getWorld().dropItemNaturally(loc, item);
          }
        }
      }
    }
  }
}
