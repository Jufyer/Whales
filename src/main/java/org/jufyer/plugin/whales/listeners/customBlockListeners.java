package org.jufyer.plugin.whales.listeners;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class customBlockListeners implements Listener {

  private final Map<Player, Long> lastPlacedBlockTimes = new HashMap<>();
  private static final long COOLDOWN_TIME = 250;

  @EventHandler
  public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
    if (event.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
      ArmorStand as = (ArmorStand) event.getRightClicked();
      if (as.getEquipment().getHelmet() != null && as.getEquipment().getHelmet().getType() == Material.NAUTILUS_SHELL) {
        ItemMeta meta = as.getEquipment().getHelmet().getItemMeta();
        if (meta != null && meta.getCustomModelData() == 1234) {
          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    ItemStack item = event.getItem();
    if (item != null && item.hasItemMeta()) {
      ItemMeta meta = item.getItemMeta();
      if (meta != null && meta.getCustomModelData() == 1234) {
        if (event.getAction().isRightClick()) {
          Player player = event.getPlayer();
          long currentTime = System.currentTimeMillis();

          long lastPlacedTime = lastPlacedBlockTimes.getOrDefault(player, 0L);

          if (currentTime - lastPlacedTime >= COOLDOWN_TIME) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR && !event.getClickedBlock().isLiquid()) {
              if (event.getBlockFace().equals(BlockFace.UP)){
                Location loc = event.getClickedBlock().getLocation().add(0, 1, 0);
                createBlock(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.DOWN)){
                Location loc = event.getClickedBlock().getLocation().add(0, -1, 0);
                createBlock(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.WEST)){
                Location loc = event.getClickedBlock().getLocation().add(-1, 0, 0);
                createBlock(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.EAST)){
                Location loc = event.getClickedBlock().getLocation().add(1, 0, 0);
                createBlock(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }if (event.getBlockFace().equals(BlockFace.NORTH)){
                Location loc = event.getClickedBlock().getLocation().add(0, 0, -1);
                createBlock(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }else if (event.getBlockFace().equals(BlockFace.SOUTH)){
                Location loc = event.getClickedBlock().getLocation().add(0, 0, 1);
                createBlock(loc);
                lastPlacedBlockTimes.put(player, currentTime);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE){
                  event.getItem().setAmount(event.getItem().getAmount() - 1);
                }
              }
            }
          } else {
            event.setCancelled(true);
          }
        }
      }
    }
  }

  private void createBlock(Location loc) {
    ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Barnacles.getItemMeta();
    if (meta != null) {
      meta.setDisplayName("§rBarnacle Spike");
      meta.setCustomModelData(1234);
      Barnacles.setItemMeta(meta);
    }

    Location spawnLoc = loc.subtract(-0.5, 0, -0.5);
    ArmorStand as = loc.getWorld().spawn(spawnLoc, ArmorStand.class);
    as.setInvisible(true);
    as.setGravity(false);
    as.setBodyYaw(0);
    as.setRotation(0, 0);
    as.setCanMove(false);
    as.setCustomNameVisible(false);
    as.setPersistent(true);

    as.getEquipment().setHelmet(Barnacles);

    loc.getBlock().setType(Material.QUARTZ_BLOCK);
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (event.getBlock().getType().equals(Material.QUARTZ_BLOCK)) {
      Location blockLoc = event.getBlock().getLocation();
      for (ArmorStand as : blockLoc.getWorld().getEntitiesByClass(ArmorStand.class)) {
        if (as.getLocation().getBlockX() == blockLoc.getBlockX() &&
          as.getLocation().getBlockY() == blockLoc.getBlockY() &&
          as.getLocation().getBlockZ() == blockLoc.getBlockZ()) {
          as.remove();

          ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
          ItemMeta meta = Barnacles.getItemMeta();
          if (meta != null) {
            meta.setDisplayName("§rBarnacle Spike");
            meta.setCustomModelData(1234);
            Barnacles.setItemMeta(meta);
          }
          as.getWorld().dropItemNaturally(as.getLocation(), Barnacles);
        }
      }
    }
  }


  @EventHandler
  public void onBlockRedstone(BlockRedstoneEvent event) {
    Block block = event.getBlock();
    BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};

    for (BlockFace face : faces) {
      Block adjacentBlock = block.getRelative(face);
      if (event.getNewCurrent() > 0) {
        if (adjacentBlock.getType() == Material.QUARTZ_BLOCK) {
          Location blockLoc = adjacentBlock.getLocation();

          for (ArmorStand as : blockLoc.getWorld().getEntitiesByClass(ArmorStand.class)) {
            if (as.getLocation().getBlockX() == blockLoc.getBlockX() &&
              as.getLocation().getBlockY() == blockLoc.getBlockY() &&
              as.getLocation().getBlockZ() == blockLoc.getBlockZ()) {

              as.getEquipment().getHelmet().setAmount(0);

              ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
              ItemMeta meta = Barnacles.getItemMeta();
              if (meta != null) {
                meta.setDisplayName("§rBarnacle Spike Extended");
                meta.setCustomModelData(12345);
                Barnacles.setItemMeta(meta);
              }
              as.setInvisible(true);
              as.setGravity(false);
              as.setBodyYaw(0);
              as.setRotation(0, 0);
              as.setCanMove(false);
              as.setCustomNameVisible(false);
              as.setPersistent(true);

              as.getEquipment().setHelmet(Barnacles);

              for (Entity player : as.getNearbyEntities(0, 1, 0)){
                if (player instanceof  Player){
                  Player p = (Player) player;
                  p.damage(5);
                }
              }
            }
          }
        }
      }

      if (event.getNewCurrent() == 0) {
        Location blockLoc = adjacentBlock.getLocation();
        for (ArmorStand as : blockLoc.getWorld().getEntitiesByClass(ArmorStand.class)) {
          if (as.getLocation().getBlockX() == blockLoc.getBlockX() &&
            as.getLocation().getBlockY() == blockLoc.getBlockY() &&
            as.getLocation().getBlockZ() == blockLoc.getBlockZ()) {

            as.getEquipment().getHelmet().setAmount(0);

            ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
            ItemMeta meta = Barnacles.getItemMeta();
            if (meta != null) {
              meta.setDisplayName("§rBarnacle Spike");
              meta.setCustomModelData(1234);
              Barnacles.setItemMeta(meta);
            }
            as.setInvisible(true);
            as.setGravity(false);
            as.setBodyYaw(0);
            as.setRotation(0, 0);
            as.setCanMove(false);
            as.setCustomNameVisible(false);
            as.setPersistent(true);

            as.getEquipment().setHelmet(Barnacles);
          }
        }
      }
    }
  }
}
