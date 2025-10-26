package xiaojin.astralflux.mixinimod;

import net.minecraft.world.entity.player.Player;

public interface IModPlayer {
  static IModPlayer of(Player player) {
    return (IModPlayer) player;
  }
}
