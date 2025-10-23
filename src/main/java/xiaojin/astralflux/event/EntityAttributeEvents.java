package xiaojin.astralflux.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.init.ModAttributes;


@EventBusSubscriber(modid = AstralFlux.ID)
public final class EntityAttributeEvents {
  @SubscribeEvent
  public static void entityAttribute(EntityAttributeCreationEvent event) {
  }

  @SubscribeEvent
  public static void addAttribute(EntityAttributeModificationEvent event) {
    event.add(EntityType.PLAYER, ModAttributes.SOURCE_SOUL_RECOVERY);
    event.add(EntityType.PLAYER, ModAttributes.MAX_SOURCE_SOUL);
  }
}
