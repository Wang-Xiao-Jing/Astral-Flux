package xiaojin.astralflux.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;

public record SourceSoulData(double value) implements CustomPacketPayload {
  public static final Type<SourceSoulData> TYPE = new Type<>(AstralFlux.modRL("source_soul_value_data"));
  public static final StreamCodec<ByteBuf, SourceSoulData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.DOUBLE,
    SourceSoulData::value,
    SourceSoulData::new
  );

  /**
   * 发送到客户端
   */
  public static void toClient(final SourceSoulData data, final IPayloadContext context) {
    context.enqueueWork(() -> ISourceSoul.of(context.player()).astralFlux$setSourceSoulValue(data.value));
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
