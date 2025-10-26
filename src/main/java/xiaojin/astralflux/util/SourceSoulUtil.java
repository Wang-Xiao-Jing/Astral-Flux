package xiaojin.astralflux.util;

import ctn.ctnapi.util.PayloadUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.api.sourcesoul.ISourceSoul;
import xiaojin.astralflux.common.payloads.SourceSoulData;
import xiaojin.astralflux.init.ModAttributes;

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
   * 源魂值NBT
   */
  private static final String SOURCE_SOUL_VALUE_NBT = "source_soul_value";

  /**
   * 判断是否带有源魂属性
   */
  public static boolean isAttribute(LivingEntity entity) {
    return entity instanceof ISourceSoul ||
      entity.getPersistentData().contains(SOURCE_SOUL_VALUE_NBT) &&
        entity.getAttributes().hasAttribute(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 判断是否带有源魂属性
   */
  public static boolean isRecoveryAttribute(LivingEntity entity) {
    return entity instanceof ISourceSoul || entity.getAttributes().hasAttribute(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
  }

  /**
   * 获取源魂值
   */
  public static double getValue(LivingEntity entity) {
    return !isAttribute(entity) ? 0 : entity.getPersistentData().getDouble(SOURCE_SOUL_VALUE_NBT);
  }

  /**
   * 设置源魂值
   */
  public static void setValue(LivingEntity entity, double targetValue) {
    if (isAttribute(entity)) {
      double value = Math.clamp(targetValue, 0, entity.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL));
      entity.getPersistentData().putDouble(SOURCE_SOUL_VALUE_NBT, value);
    }
  }

  /**
   * 增加源魂值
   */
  public static void increaseValue(LivingEntity entity, double value) {
    if (isAttribute(entity)) {
      setValue(entity, value + entity.getPersistentData().getDouble(SOURCE_SOUL_VALUE_NBT));
    }
  }

  /**
   * 修改基础最大源魂值到指定的值
   */
  @SuppressWarnings("DataFlowIssue")
  public static void setMaxBaseValue(LivingEntity entity, double targetValue) {
    if (isAttribute(entity)) {
      setValue(entity, setAttributeBaseValue(entity, getMaxAttributeInstance(entity), targetValue));
    }
  }

  /**
   * 修改基础最大源魂值到指定的值
   */
  public static void setMaxBaseValue(Player player, double targetValue) {
    ISourceSoul.of(player).setSourceSoulValue(setAttributeBaseValue(player, getMaxAttributeInstance(player), targetValue));
  }

  /**
   * 修改源魂恢复基础值到指定的值
   */
  @SuppressWarnings("DataFlowIssue")
  public static void setRecoveryBaseValue(LivingEntity entity, double targetValue) {
    if (isAttribute(entity)) {
      setAttributeBaseValue(entity, getRecoveryValueAttributeInstance(entity), targetValue);
    }
  }

  /**
   * 修改源魂恢复基础值到指定的值
   */
  public static void setRecoveryBaseValue(Player player, double targetValue) {
    setAttributeBaseValue(player, getRecoveryValueAttributeInstance(player), targetValue);
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
    return !isAttribute(entity) ? 0 : entity.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 获取最大源魂值
   */
  public static double getMaxValue(Player player) {
    return player.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 获取源魂恢复速度，单位每tick
   */
  public static double getRecoveryValue(LivingEntity entity) {
    return !isRecoveryAttribute(entity) ? 0 : entity.getAttributeValue(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
  }

  /**
   * 获取源魂恢复速度，单位每tick
   */
  public static double getRecoveryValue(Player player) {
    return player.getAttributeValue(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE);
  }

  /**
   * 根据难度改变最大源魂值
   *
   * @param difficulty 难度
   * @param player     玩家
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
    ISourceSoul.of(player).setSourceSoulValue(Math.min(maxSourceSoul.getValue(), getValue(player)));
  }

  /**
   * 同步源魂值
   * <p>
   * 请手动同步
   */
  public static void synchronize(Player player) {
    if (player instanceof ServerPlayer serverPlayer) {
      PayloadUtil.sendToClient(serverPlayer, new SourceSoulData(ISourceSoul.of(serverPlayer).getSourceSoulValue()));
    }
  }

  private static double setAttributeBaseValue(LivingEntity entity, AttributeInstance attributeInstance, double targetValue) {
    attributeInstance.setBaseValue(targetValue);
    return Math.min(attributeInstance.getValue(),
      (entity instanceof ISourceSoul iSourceSoul) ?
        iSourceSoul.getSourceSoulValue() :
        getValue(entity));
  }
}
