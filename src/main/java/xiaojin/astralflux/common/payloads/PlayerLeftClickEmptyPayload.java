package xiaojin.astralflux.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.events.PlayerLeftClickEmptyEvent;

public record PlayerLeftClickEmptyPayload(boolean isMainHand) implements CustomPacketPayload{
  public static final CustomPacketPayload.Type<PlayerLeftClickEmptyPayload> TYPE = new CustomPacketPayload.Type<>(AstralFlux.modRL("player_left_click_empty_payloads"));

  public static final StreamCodec<ByteBuf, PlayerLeftClickEmptyPayload> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.BOOL, PlayerLeftClickEmptyPayload::isMainHand,
    PlayerLeftClickEmptyPayload::new);

  public PlayerLeftClickEmptyPayload(InteractionHand interactionHand) {
    this(interactionHand == InteractionHand.MAIN_HAND);
  }

  /**
   * 发送到服务端
   */
  public static void toServer(final PlayerLeftClickEmptyPayload data, final IPayloadContext context) {
    context.enqueueWork(() -> trigger(context.player(), data.isMainHand() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));
  }

  public static void trigger(final Player player, final InteractionHand hand) {
    var event = new PlayerLeftClickEmptyEvent.Pre(player, hand);
    ModLoader.postEvent(event);
    if (event.isCanceled()){
      return;
    }
    new PlayerLeftClickEmptyEvent.Post(player, hand);
  }

  /**
   * 发送到客户端
   */
  public static void toClient(final PlayerLeftClickEmptyPayload data, final IPayloadContext context) {

  }

  @Override
  public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
