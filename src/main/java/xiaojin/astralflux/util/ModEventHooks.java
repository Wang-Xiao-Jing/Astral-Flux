package xiaojin.astralflux.util;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;
import xiaojin.astralflux.events.sourcesoul.SourceSoulConsumeEvent;

import java.util.Map;

public final class ModEventHooks {

  public static <T extends LivingEntity & ISourceSoul> Map.Entry<Boolean, Double> sourceSoulConsumePre(T entity, double oldSourceSoulValue, double consumeValue) {
    var event = new SourceSoulConsumeEvent.Pre<>(entity, oldSourceSoulValue, consumeValue);
    ModLoader.postEvent(event);
    return Map.entry(event.isCanceled(),
      event.isCanceled() ? event.getOldSourceSoulValue() : event.getConsumeValue());
  }

  public static <T extends LivingEntity & ISourceSoul> void sourceSoulConsumePost(T entity, double oldSourceSoulValue, double consumeValue) {
    ModLoader.postEvent(new SourceSoulConsumeEvent.Post<>(entity, oldSourceSoulValue, consumeValue));
  }
}
