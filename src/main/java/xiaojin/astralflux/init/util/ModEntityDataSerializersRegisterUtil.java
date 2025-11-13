package xiaojin.astralflux.init.util;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import xiaojin.astralflux.init.ModEntityDataSerializers;

public abstract class ModEntityDataSerializersRegisterUtil {
  protected static <T> DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<T>> register(String name, EntityDataSerializer<T> serializer) {
    return ModEntityDataSerializers.REGISTRY.register(name, () -> serializer);
  }
}
