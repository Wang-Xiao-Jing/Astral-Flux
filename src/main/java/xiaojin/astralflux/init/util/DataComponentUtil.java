package xiaojin.astralflux.init.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.core.AstralFlux;

import java.util.function.UnaryOperator;

public abstract class DataComponentUtil {
  public static final DeferredRegister<DataComponentType<?>> ITEM_COMPONENTS = AstralFlux.modRegister(BuiltInRegistries.DATA_COMPONENT_TYPE);

  protected static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register
    (String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    return register(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec));
  }

  protected static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register
    (String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
    return ITEM_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }
}
