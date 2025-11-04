package xiaojin.astralflux.events.sourcesoul;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * 源魂修改事件
 */
public abstract class SourceSoulModifyEvent<T extends LivingEntity> extends LivingEvent implements IModBusEvent {
  private final double oldSourceSoulValue;

  public SourceSoulModifyEvent(T entity, double oldSourceSoul) {
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

  public static class Pre<T extends LivingEntity> extends SourceSoulModifyEvent<T> implements ICancellableEvent {
    private double modifyValue;

    public Pre(T entity, double oldSourceSoulValue, double modifyValue) {
      super(entity, oldSourceSoulValue);
      this.modifyValue = modifyValue;
    }

    public double getModifyValue() {
      return modifyValue;
    }

    public void setModifyValue(double modifyValue) {
      this.modifyValue = modifyValue;
    }
  }

  /**
   * 不可取消
   */
  public static class Post<T extends LivingEntity> extends SourceSoulModifyEvent<T> {
    private final double modifyValue;

    public Post(T entity, double oldSourceSoul, double modifyValue) {
      super(entity, oldSourceSoul);
      this.modifyValue = modifyValue;
    }

    public double getModifyValue() {
      return modifyValue;
    }
  }
}
