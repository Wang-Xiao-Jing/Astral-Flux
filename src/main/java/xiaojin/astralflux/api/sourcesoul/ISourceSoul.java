package xiaojin.astralflux.api.sourcesoul;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import xiaojin.astralflux.util.ModEventHooks;

public interface ISourceSoul {
  static ISourceSoul of(Player player) {
    return (ISourceSoul) player;
  }

  // 这些方法 仅适用Mixin注入时重写使用 //

  double astralFlux$getSourceSoulValue();

  void astralFlux$setSourceSoulValue(double value);

  void astralFlux$modifySourceSoulValue(double value);

  void astralFlux$saveSourceSoulValue();

  void astralFlux$loadSourceSoulValue();

  boolean astralFlux$allowSourceSoulRecover();

  int astralFlux$getPauseSourceSoulRecoveryTick();

  void astralFlux$setPauseSourceSoulRecoveryTick(int tick);

  /**
   * 获取源魂值
   */
  default double getSourceSoulValue() {
    return astralFlux$getSourceSoulValue();
  }

  /**
   * 消耗源魂值
   * <p>
   * 正常消耗请调用此方法以适配源魂事件
   * <p>
   * 如果继承的类不是 LivingEntity或其子类 则需要重写
   */
  default <T extends LivingEntity & ISourceSoul> void sourceSoulConsume(double consumeValue) {
    double oldSourceSoulValue = getSourceSoulValue();
    var event = ModEventHooks.sourceSoulConsumePre((T) this,
      oldSourceSoulValue, consumeValue);
    var newValue = event.getValue();
    if (event.getKey() || newValue <= 0) {
      return;
    }
    modifySourceSoulValue(-newValue);
    ModEventHooks.sourceSoulConsumePost((T) this, oldSourceSoulValue, newValue);
  }

  /**
   * 设置源魂值
   */
  default void setSourceSoulValue(double value) {
    astralFlux$setSourceSoulValue(value);
  }

  /**
   * 修改（增加）源魂值
   */
  default void modifySourceSoulValue(double value) {
    astralFlux$modifySourceSoulValue(value);
  }

  /**
   * 保存源魂值
   */
  default void saveSourceSoulValue() {
    astralFlux$saveSourceSoulValue();
  }

  /**
   * 加载源魂值
   */
  default void loadSourceSoulValue() {
    astralFlux$loadSourceSoulValue();
  }

  /**
   * 是否允许恢复
   */
  default boolean allowSourceSoulRecover() {
    return astralFlux$allowSourceSoulRecover();
  }

  /**
   * 获取暂停源魂恢复的计时tick
   */
  default int getPauseSourceSoulRecoveryTick() {
    return astralFlux$getPauseSourceSoulRecoveryTick();
  }

  /**
   * 设置暂停源魂恢复的计时tick
   */
  default void setPauseSourceSoulRecoveryTick(int tick) {
    astralFlux$setPauseSourceSoulRecoveryTick(tick);
  }
}
