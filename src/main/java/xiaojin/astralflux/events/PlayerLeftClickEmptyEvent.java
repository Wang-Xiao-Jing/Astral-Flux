package xiaojin.astralflux.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerLeftClickEmptyEvent extends PlayerInteractEvent implements IModBusEvent {
  public PlayerLeftClickEmptyEvent(final Player player,
                                   final InteractionHand hand) {
    super(player, hand, BlockPos.ZERO, null);
  }

  @Override
  @Nullable
  public BlockPos getPos() {
    return null;
  }

  @Override
  @Nullable
  public Direction getFace() {
    return null;
  }

  public static class Pre extends PlayerLeftClickEmptyEvent implements ICancellableEvent {
    public Pre(final Player player, final InteractionHand hand) {
      super(player, hand);
    }
  }

  /**
   * 不可取消
   */
  public static class Post extends PlayerLeftClickEmptyEvent {
    public Post(final Player player, final InteractionHand hand) {
      super(player, hand);
    }
  }
}
