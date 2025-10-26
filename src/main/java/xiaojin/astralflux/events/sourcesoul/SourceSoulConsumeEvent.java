package xiaojin.astralflux.events.sourcesoul;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;

/**
 * 源魂消耗事件
 */
public abstract class SourceSoulConsumeEvent<T extends LivingEntity & ISourceSoul> extends LivingEvent implements IModBusEvent {
  private final double oldSourceSoulValue;

  public SourceSoulConsumeEvent(T entity, double oldSourceSoul) {
    super(entity);
    this.oldSourceSoulValue = oldSourceSoul;
  }

  public double getOldSourceSoulValue() {
    return oldSourceSoulValue;
  }

  @Override
  public T getEntity() {
    return (T) super.getEntity();
  }

  public static class Pre<T extends LivingEntity & ISourceSoul> extends SourceSoulConsumeEvent<T> implements ICancellableEvent {
    private double consumeValue;

    public Pre(T entity, double oldSourceSoulValue, double consumeValue) {
      super(entity, oldSourceSoulValue);
      this.consumeValue = consumeValue;
    }

    public double getConsumeValue() {
      return consumeValue;
    }

    public void setConsumeValue(double consumeValue) {
      this.consumeValue = consumeValue;
    }
  }

  /**
   * 不可取消
   */
  public static class Post<T extends LivingEntity & ISourceSoul> extends SourceSoulConsumeEvent<T> {
    private final double consumeValue;

    public Post(T entity, double oldSourceSoul, double consumeValue) {
      super(entity, oldSourceSoul);
      this.consumeValue = consumeValue;
    }

    public double getConsumeValue() {
      return consumeValue;
    }
  }
}
