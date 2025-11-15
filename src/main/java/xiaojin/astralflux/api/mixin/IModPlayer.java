package xiaojin.astralflux.api.mixin;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import xiaojin.astralflux.api.abs.AbstractAegusBarrierShieldManager;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@NonExtendable
public interface IModPlayer {
  @Nonnull
  static IModPlayer of(@Nonnull final Player player) {
    return (IModPlayer) player;
  }

  @Internal
  @CheckForNull
  default AbstractAegusBarrierShieldManager astralFlux$getShieldManager() {
    throw new UnsupportedOperationException();
  }

  @Internal
  default void astralFlux$setShieldManager(
    @Nullable final AbstractAegusBarrierShieldManager shieldManager) {
    throw new UnsupportedOperationException();
  }
}
