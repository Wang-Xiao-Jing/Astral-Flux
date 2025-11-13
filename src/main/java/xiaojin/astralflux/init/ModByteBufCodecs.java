package xiaojin.astralflux.init;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public final class ModByteBufCodecs {

  public static <B extends ByteBuf, T extends Map.Entry<K, V>, K, V> StreamCodec<? super B, T> entry(BiFunction<K, V, ? extends T> entry, StreamCodec<? super B, K> key, StreamCodec<? super B, V> value) {
    return new StreamCodec<>() {
      @Override
      @NotNull
      public T decode(@NotNull B buf) {
        return entry.apply(key.decode(buf), value.decode(buf));
      }

      @Override
      public void encode(@NotNull B buf, @NotNull T pair) {
        key.encode(buf, pair.getKey());
        value.encode(buf, pair.getValue());
      }
    };
  }
}
