package xiaojin.astralflux.api.sourcesoul;

import net.minecraft.world.entity.player.Player;

public interface ISourceSoul {
  static ISourceSoul of(Player player) {
    return (ISourceSoul) player;
  }

  double astralFlux$getSourceSoulValue();

  void astralFlux$setSourceSoulValue(double value);

  void astralFlux$increaseSourceSoulValue(double value);

  void astralFlux$saveSourceSoulValue();

  void astralFlux$loadSourceSoulValue();

  boolean astralFlux$isRecover();

  int astralFlux$getConsumeSourceSoulTick();

  void astralFlux$setConsumeSourceSoulTick(int consumeSourceSoulTick);
}
