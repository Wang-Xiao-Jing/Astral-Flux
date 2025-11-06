package xiaojin.astralflux.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.EntityRegisterUtil;

public final class ModEntitys extends EntityRegisterUtil {
  public static final DeferredRegister<EntityType<?>> REGISTRY = AstralFlux.modRegister(BuiltInRegistries.ENTITY_TYPE);
}
