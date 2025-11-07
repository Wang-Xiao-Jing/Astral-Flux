package xiaojin.astralflux.init.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xiaojin.astralflux.core.AstralFlux;

import java.util.function.Supplier;

import static xiaojin.astralflux.init.ModEntitys.REGISTRY;

public abstract class EntityRegisterUtil {

  protected static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, EntityType.Builder<T> builder) {
    return register(name, () -> builder.build(AstralFlux.modRLText(name)));
  }

  private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register
    (String name, Supplier<EntityType<T>> entityType) {
    return REGISTRY.register(name, entityType);
  }
}
