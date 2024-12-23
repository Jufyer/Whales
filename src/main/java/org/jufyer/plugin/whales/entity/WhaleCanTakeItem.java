package org.jufyer.plugin.whales.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.level.Level;
import org.bukkit.inventory.ItemStack;

public abstract class WhaleCanTakeItem extends Dolphin {
  public WhaleCanTakeItem(EntityType<? extends Dolphin> type, Level world) {
    super(type, world);
  }

  public boolean canTakeItem(ItemStack stack) {
    return false;
  }
}
