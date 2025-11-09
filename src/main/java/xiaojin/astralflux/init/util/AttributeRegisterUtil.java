package xiaojin.astralflux.init.util;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModAttributes;

public abstract class AttributeRegisterUtil {

  @NotNull
  protected static <T extends Attribute> DeferredHolder<Attribute, T> register
    (String name, T attribute) {
    return ModAttributes.REGISTRY.register(name, () -> attribute);
  }

  @NotNull
  protected static DeferredHolder<Attribute, Attribute> register
    (String name, double defaultValue, double minValue, double maxValue) {
    return register(name, new RangedAttribute(AstralFlux.ID + ".attribute.name." + name, defaultValue, minValue, maxValue).setSyncable(true));
  }

  @NotNull
  protected static DeferredHolder<Attribute, Attribute> register
    (String name, double minValue, double maxValue) {
    return register(name, minValue, minValue, maxValue);
  }

  @NotNull
  protected static DeferredHolder<Attribute, Attribute> register
    (String name, boolean defaultValue) {
    return register(name, new BooleanAttribute(AstralFlux.ID + ".attribute.name." + name, defaultValue).setSyncable(true));
  }
}
