package xiaojin.astralflux.init.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.UnaryOperator;

import static xiaojin.astralflux.init.ModDataComponentTypes.REGISTRY;

public abstract class DataComponentTypeRegisterUtil {

  protected static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register
    (String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    return register(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec));
  }

  protected static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register
    (String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
    return REGISTRY.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }
}
