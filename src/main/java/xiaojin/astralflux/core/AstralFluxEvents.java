package xiaojin.astralflux.core;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.ModLoader;
import xiaojin.astralflux.events.sourcesoul.SourceSoulModifyEvent;

public final class AstralFluxEvents {
  /**
   * 源魂修改事件 Pre
   */
  public static <T extends LivingEntity> SourceSoulModifyEvent.Pre<T> sourceSoulModifyPre(T entity, double oldSourceSoulValue, double newSourceSoulValue) {
    var pre = new SourceSoulModifyEvent.Pre<>(entity, oldSourceSoulValue, newSourceSoulValue);
    ModLoader.postEvent(pre);
    return pre;
  }

  /**
   * 源魂修改事件 Post
   */
  public static <T extends LivingEntity> SourceSoulModifyEvent.Post<T> sourceSoulModifyPost(T entity, double oldSourceSoulValue, double consumeValue) {
    var post = new SourceSoulModifyEvent.Post<>(entity, oldSourceSoulValue, consumeValue);
    ModLoader.postEvent(post);
    return post;
  }
}
