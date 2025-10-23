package xiaojin.astralflux.init.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.AstralFlux;

import static xiaojin.astralflux.init.ModAttributes.REGISTRY;

@SuppressWarnings("SameParameterValue")
public class AttributeRegister {
  @NotNull
  protected static <T extends Attribute> DeferredHolder<Attribute, T> register(String name, T attribute) {
    return REGISTRY.register(name, () -> attribute);
  }

  @NotNull
  protected static DeferredHolder<Attribute, RangedAttribute> register(String name, double defaultValue, double minValue, double maxValue) {
    return register(name, new RangedAttribute(AstralFlux.ID + ".attribute.name." + name, defaultValue, minValue, maxValue));
  }

  @NotNull
  protected static DeferredHolder<Attribute, RangedAttribute> register(String name, double minValue, double maxValue) {
    return register(name, new RangedAttribute(AstralFlux.ID + ".attribute.name." + name, minValue, minValue, maxValue));
  }

  @NotNull
  protected static DeferredHolder<Attribute, BooleanAttribute> register(String name, boolean defaultValue) {
    return register(name, new BooleanAttribute(AstralFlux.ID + ".attribute.name." + name, defaultValue));
  }
}
