package xiaojin.astralflux.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.AttributeRegister;

public final class ModAttributes extends AttributeRegister {
  public static final DeferredRegister<Attribute> REGISTRY = AstralFlux.modRegister(BuiltInRegistries.ATTRIBUTE);

  public static final Holder<Attribute> MAX_SOURCE_SOUL =
    register("entity.max_source_soul", 80, 0, 10240);
  public static final Holder<Attribute> SOURCE_SOUL_RECOVERY_VALUE =
    register("entity.source_soul_recovery_value", 0.5d / 20d, 0, 10240);
}
