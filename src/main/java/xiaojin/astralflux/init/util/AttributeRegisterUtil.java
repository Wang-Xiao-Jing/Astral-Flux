package xiaojin.astralflux.init.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;

public abstract class AttributeRegisterUtil {
  public static final DeferredRegister<Attribute> REGISTRY = AstralFlux.modRegister(BuiltInRegistries.ATTRIBUTE);

  @NotNull
  protected static <T extends Attribute> DeferredHolder<Attribute, T> register
    (String name, T attribute) {
    return REGISTRY.register(name, () -> attribute);
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
