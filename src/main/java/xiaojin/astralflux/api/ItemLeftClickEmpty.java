package xiaojin.astralflux.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ItemLeftClickEmpty {
  /**
   * 左键点击
   */
  void leftClick(ItemStack stack, Player player);
}
