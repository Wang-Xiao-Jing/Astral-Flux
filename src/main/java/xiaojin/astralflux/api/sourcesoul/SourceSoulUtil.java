package xiaojin.astralflux.api.sourcesoul;

import ctn.ctnapi.util.PayloadUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.common.payloads.SourceSoulData;
import xiaojin.astralflux.init.ModAttributes;

import javax.annotation.Nullable;

/**
 * 源魂 Source Soul
 */
public final class SourceSoulUtil {
  private static final String SOURCE_SOUL_VALUE_NBT = "source_soul_value";

  public static boolean isSourceSoul(LivingEntity entity) {
    return entity instanceof ISourceSoul ||
      entity.getPersistentData().contains(SOURCE_SOUL_VALUE_NBT) &&
        entity.getAttributes().hasAttribute(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 获取源魂值
   *
   * @param entity 实体
   * @return 源魂值
   */
  public static double getSourceSoulValue(LivingEntity entity) {
    return !isSourceSoul(entity) ? 0 : entity.getPersistentData().getDouble(SOURCE_SOUL_VALUE_NBT);
  }

  /**
   * 设置源魂值
   *
   * @param entity      实体
   * @param targetValue 目标值
   */
  public static void setSourceSoulValue(LivingEntity entity, double targetValue) {
    if (isSourceSoul(entity)) {
      double value = Math.clamp(targetValue, 0, entity.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL));
      entity.getPersistentData().putDouble(SOURCE_SOUL_VALUE_NBT, value);
    }
  }

  public static void increaseSourceSoulValue(LivingEntity entity, double value) {
    if (isSourceSoul(entity)) {
      setSourceSoulValue(entity, value + entity.getPersistentData().getDouble(SOURCE_SOUL_VALUE_NBT));
    }
  }

  /**
   * 修改最大源魂值
   *
   * @param entity     实体
   * @param upperLimit 上限
   * @param modifierId 修改器的ID
   */
  public static void setSourceSoulMaxValue(LivingEntity entity, double upperLimit, ResourceLocation modifierId) {
    if (isSourceSoul(entity)) {// 修改最大上限值
      var maxSourceSoul = getSourceSoulMaxAttributeInstance(entity);
      maxSourceSoul.addOrReplacePermanentModifier(new AttributeModifier(modifierId,
        upperLimit - maxSourceSoul.getValue(), AttributeModifier.Operation.ADD_VALUE));

      // 修改当前值以不让它超过上限
      setSourceSoulValue(entity, Math.min(maxSourceSoul.getValue(), getSourceSoulValue(entity)));
    }
  }

  /**
   * 获取最大源魂值
   *
   * @param entity 实体
   * @return 最大源魂值
   */
  public static double getSourceSoulMaxValue(LivingEntity entity) {
    return !isSourceSoul(entity) ? 0 : entity.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 获取最大源魂值属性实例
   *
   * @param entity 实体
   * @return 属性实例
   */
  @Nullable
  public static AttributeInstance getSourceSoulMaxAttributeInstance(LivingEntity entity) {
    return entity.getAttribute(ModAttributes.MAX_SOURCE_SOUL);
  }

  /**
   * 根据难度改变源魂值
   *
   * @param difficulty 难度
   * @param player     玩家
   */
  public static void difficultyChange(Difficulty difficulty, Player player) {
    SourceSoulUtil.setSourceSoulMaxValue(player, switch (difficulty) {
      case PEACEFUL, EASY -> 80;
      case NORMAL -> 100;
      case HARD -> 125;
    }, AstralFlux.modRL("difficulty_change"));
  }

  /**
   * 请手动同步
   * @param player
   */
  public static void synchronize(Player player) {
    if (player instanceof ServerPlayer serverPlayer) {
      PacketDistributor.sendToPlayer(serverPlayer, new SourceSoulData(ISourceSoul.of(serverPlayer).astralFlux$getSourceSoulValue()));
//      PayloadUtil.sendToClient();
    }
  }

  public static double getSourceSoulMaxValue(Player player) {
    return player.getAttributeValue(ModAttributes.MAX_SOURCE_SOUL);
  }

  public static void setSourceSoulMaxValue(Player player, double upperLimit, ResourceLocation modifierId) {
    // 修改最大上限值
    var maxSourceSoul = getSourceSoulMaxAttributeInstance(player);
    maxSourceSoul.addOrReplacePermanentModifier(new AttributeModifier(modifierId,
      upperLimit - maxSourceSoul.getValue(), AttributeModifier.Operation.ADD_VALUE));

    // 修改当前值以不让它超过上限
    ISourceSoul.of(player).astralFlux$setSourceSoulValue(Math.min(maxSourceSoul.getValue(), getSourceSoulValue(player)));
  }
}
