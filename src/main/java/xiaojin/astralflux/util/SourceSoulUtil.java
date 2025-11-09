package xiaojin.astralflux.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.core.AstralFluxEvents;
import xiaojin.astralflux.events.sourcesoul.SourceSoulModifyEvent;
import xiaojin.astralflux.init.ModAttributes;
import xiaojin.astralflux.init.ModAttachmentTypes;

import javax.annotation.Nullable;

/**
 * 源魂 Source Soul
 * <p>
 * 在该工具类的方法请使用限定static调用
 */
@SuppressWarnings("unused")
public final class SourceSoulUtil {
  /**
   * 受击暂停tick
   */
  public static final int HIT_PAUSE_RECOVERY_TICK = 10 * 20;
  /**
   * 消耗暂停tick
   */
  public static final int CONSUME_PAUSE_RECOVERY_TICK = 10 * 20;

  /**
   * 修改源魂
   * <p>
   * 建议使用该方法，此方法会触发{@link SourceSoulModifyEvent}事件
   *
   * @return 是否修改成功
   */
  public static boolean modify(LivingEntity entity, double consumeValue) {
    if (!isAttribute(entity)) {
      return false;
    }
    var oldSourceSoulValue = getValue(entity);
    var pre = AstralFluxEvents.sourceSoulModifyPre(entity, getValue(entity), consumeValue);
    var newConsumeValue = pre.getModifyValue();
    if (pre.isCanceled() || oldSourceSoulValue + newConsumeValue < 0) {
      return false;
    }
    setValue(entity, Math.clamp(getValue(entity) + newConsumeValue, 0, getMaxValue(entity)));
    AstralFluxEvents.sourceSoulModifyPost(entity, oldSourceSoulValue, newConsumeValue);
    return true;
  }

  /**
   * 判断是否带有源魂属性
   */
  public static boolean isAttribute(LivingEntity entity) {
    return entity instanceof Player ||
      entity.hasData(ModAttachmentTypes.SOURCE_SOUL) &&
        entity.getAttributes().hasAttribute(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 判断是否带有恢复源魂属性
   */
  public static boolean isRecoveryAttribute(LivingEntity entity) {
    return entity instanceof Player || entity.getAttributes().hasAttribute(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
  }

  /**
   * 判断是否带有暂停恢复tick
   */
  public static boolean isPauseRecoveryTick(final LivingEntity entity) {
    return entity instanceof Player || entity.hasData(ModAttachmentTypes.SOURCE_SOUL_PAUSE_RECOVERY_TICK);
  }

  /**
   * 获取源魂值
   */
  public static double getValue(LivingEntity entity) {
    return isAttribute(entity) ? entity.getData(ModAttachmentTypes.SOURCE_SOUL) : 0;
  }

  /**
   * 设置源魂值
   */
  public static void setValue(LivingEntity entity, double targetValue) {
    if (!isAttribute(entity)) {
      return;
    }
    entity.setData(ModAttachmentTypes.SOURCE_SOUL, targetValue);
  }

  /**
   * 在原来的基础上修改（增加）源魂值
   */
  public static void modifyValue(LivingEntity entity, double value) {
    if (!isAttribute(entity) || value == 0) {
      return;
    }
    setValue(entity, getValue(entity) + value);
  }

  /**
   * 获取源魂暂停恢复tick
   */
  public static int getPauseRecoveryTick(LivingEntity entity) {
    return isPauseRecoveryTick(entity) ? entity.getData(ModAttachmentTypes.SOURCE_SOUL_PAUSE_RECOVERY_TICK) : 0;
  }

  /**
   * 设置暂停恢复tick
   */
  public static void setPauseRecoveryTick(LivingEntity entity, int targetValue) {
    if (!isPauseRecoveryTick(entity)) {
      return;
    }
    entity.setData(ModAttachmentTypes.SOURCE_SOUL_PAUSE_RECOVERY_TICK, targetValue);
  }

  /**
   * 在原来的基础上修改（增加）暂停恢复tick
   */
  public static void modifyPauseRecoveryTick(LivingEntity entity, int value) {
    if (!isPauseRecoveryTick(entity)) {
      return;
    }
    setPauseRecoveryTick(entity, getPauseRecoveryTick(entity) + value);
  }

  /**
   * 修改基础最大源魂值到指定的值
   */
  public static void setMaxBaseValue(LivingEntity entity, double targetValue) {
    if (!isAttribute(entity)) {
      return;
    }
    //noinspection DataFlowIssue 因为以及判断过了
    setValue(entity, setAttributeBaseValue(entity, getMaxAttributeInstance(entity), targetValue));
  }

  /**
   * 修改源魂恢复基础值到指定的值
   */
  @SuppressWarnings("DataFlowIssue")
  public static void setRecoveryBaseValue(LivingEntity entity, double targetValue) {
    if (!isAttribute(entity)) {
      return;
    }
    setAttributeBaseValue(entity, getRecoveryValueAttributeInstance(entity), targetValue);
  }

  /**
   * 获取最大源魂值属性实例
   */
  @Nullable
  public static AttributeInstance getMaxAttributeInstance(LivingEntity entity) {
    return entity.getAttribute(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 获取最大源魂值属性实例
   */
  @NotNull
  @SuppressWarnings("DataFlowIssue")
  public static AttributeInstance getMaxAttributeInstance(Player player) {
    return player.getAttribute(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 获取源魂恢复速度属性实例
   */
  @Nullable
  public static AttributeInstance getRecoveryValueAttributeInstance(LivingEntity entity) {
    return entity.getAttribute(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
  }

  /**
   * 获取源魂恢复速度属性实例
   */
  @NotNull
  @SuppressWarnings("DataFlowIssue")
  public static AttributeInstance getRecoveryValueAttributeInstance(Player player) {
    return player.getAttribute(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
  }

  /**
   * 获取最大源魂值
   */
  public static double getMaxValue(LivingEntity entity) {
    return isAttribute(entity) ? entity.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL) : 0;
  }

  /**
   * 获取源魂恢复速度，单位每tick
   */
  public static double getRecoveryValue(LivingEntity entity) {
    return isRecoveryAttribute(entity) ? entity.getAttributeValue(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE) : 0;
  }

  /**
   * 是否允许源魂恢复
   */
  public static boolean allowSourceSoulRecover(LivingEntity entity) {
    return isAttribute(entity) &&
      isRecoveryAttribute(entity) &&
      getPauseRecoveryTick(entity) <= 0 &&
      getValue(entity) < getMaxValue(entity);
  }

  /**
   * 根据难度改变最大源魂值
   */
  public static void difficultyChange(Difficulty difficulty, Player player) {
    var maxSourceSoul = getMaxAttributeInstance(player);
    maxSourceSoul.addOrReplacePermanentModifier(new AttributeModifier(AstralFlux.modRL("world_difficulty_modification"),
      switch (difficulty) {
        case PEACEFUL, EASY -> 0;
        case NORMAL -> 20;
        case HARD -> 45;
      }, AttributeModifier.Operation.ADD_VALUE));
    // 修改当前值以不让它超过上限
    setValue(player, Math.min(maxSourceSoul.getValue(), getValue(player)));
  }

  private static double setAttributeBaseValue(LivingEntity entity, AttributeInstance attributeInstance, double targetValue) {
    attributeInstance.setBaseValue(targetValue);
    return Math.min(attributeInstance.getValue(), isAttribute(entity) ? getValue(entity) : 0);
  }
}
