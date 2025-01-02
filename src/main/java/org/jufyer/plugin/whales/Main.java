package org.jufyer.plugin.whales;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jufyer.plugin.whales.entity.Whale;
import org.jufyer.plugin.whales.listeners.customBlockListeners;
import org.jufyer.plugin.whales.listeners.generateStructure;
import org.jufyer.plugin.whales.listeners.rightClickListener;
import org.jufyer.plugin.whales.listeners.spawnListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements CommandExecutor {
  private static Main instance;

  public static Main getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    Bukkit.getPluginManager().registerEvents(new spawnListener(), this);
    Bukkit.getPluginManager().registerEvents(new rightClickListener(), this);
    Bukkit.getPluginManager().registerEvents(new generateStructure(), this);
    Bukkit.getPluginManager().registerEvents(new customBlockListeners(), this);

    getCommand("spawnWhale").setExecutor(this);

    //Custom Recipe:
    ItemStack Barnacles = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta meta = Barnacles.getItemMeta();
    if (meta != null) {
      meta.setDisplayName("§rBarnacle Spike");
      meta.setCustomModelData(12345);
      Barnacles.setItemMeta(meta);
    }

    ItemStack Barnacle = new ItemStack(Material.NAUTILUS_SHELL);
    ItemMeta Bmeta = Barnacle.getItemMeta();
    Bmeta.setDisplayName("§rBarnacle");
    Bmeta.setCustomModelData(123);
    Barnacle.setItemMeta(Bmeta);

    ShapedRecipe barnacles_spike = new ShapedRecipe(Barnacles);
    barnacles_spike.shape(" B ", "BIB", " R ");

    barnacles_spike.setIngredient('B', new RecipeChoice.ExactChoice(Barnacle)); // Custom Item
    barnacles_spike.setIngredient('I', Material.IRON_INGOT);
    barnacles_spike.setIngredient('R', Material.REDSTONE);

    getServer().addRecipe(barnacles_spike);

  }

  @Override
  public void onDisable(){

  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    Player player = (Player) sender;
    boolean isOP = player.isOp();

    if (isOP){
      new Whale(player.getLocation());
    }

    return false;
  }
}
