package xiaojin.astralflux.util;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import xiaojin.astralflux.events.sourcesoul.SourceSoulModifyEvent;

import java.util.Map;

public final class ModEventHooks {

  public static <T extends LivingEntity> Map.Entry<Boolean, Double> sourceSoulConsumePre(T entity, double oldSourceSoulValue, double consumeValue) {
    var event = new SourceSoulModifyEvent.Pre<>(entity, oldSourceSoulValue, consumeValue);
    ModLoader.postEvent(event);
    return Map.entry(event.isCanceled(),
      event.isCanceled() ? event.getOldSourceSoulValue() : event.getModifyValue());
  }

  public static <T extends LivingEntity> void sourceSoulConsumePost(T entity, double oldSourceSoulValue, double consumeValue) {
    ModLoader.postEvent(new SourceSoulModifyEvent.Post<>(entity, oldSourceSoulValue, consumeValue));
  }
}
