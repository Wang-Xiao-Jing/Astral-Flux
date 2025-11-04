package xiaojin.astralflux.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import xiaojin.astralflux.init.util.DataComponentUtil;

public final class ModDataComponent extends DataComponentUtil {
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BOOLEAN =
    register("boolean", Codec.BOOL, ByteBufCodecs.BOOL);
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> Integer =
    register("integer", Codec.INT, ByteBufCodecs.VAR_INT);
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> STRING =
    register("string", Codec.STRING, ByteBufCodecs.STRING_UTF8);
}
