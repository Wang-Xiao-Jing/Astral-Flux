package xiaojin.astralflux.client.renderer.armpose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class AegusBarrierArmPose implements IClientItemExtensions {
  public static final HumanoidModel.ArmPose AEGUS_BARRIER_ITEM_ARMPOSE = HumanoidModel.ArmPose.valueOf("ASTRALFLUX_AEGUS_BARRIER_ITEM_ARMPOSE");

  @Override
  public HumanoidModel.@Nullable ArmPose getArmPose(final LivingEntity entityLiving, final InteractionHand hand, final ItemStack itemStack) {
    return AEGUS_BARRIER_ITEM_ARMPOSE;
  }

  @Override
  public boolean applyForgeHandTransform(final PoseStack poseStack, final LocalPlayer player, final HumanoidArm humanoidArm, final ItemStack itemInHand, final float partialTick, final float equipProcess, final float swingProcess) {
    boolean playerUsingItem = player.isUsingItem();
    if (!playerUsingItem) {
      boolean flag3 = humanoidArm == HumanoidArm.RIGHT;
      float f5 = -0.4F * Mth.sin(Mth.sqrt(swingProcess) * (float) Math.PI);
      float f6 = 0.2F * Mth.sin(Mth.sqrt(swingProcess) * (float) (Math.PI * 2));
      float f10 = -0.2F * Mth.sin(swingProcess * (float) Math.PI);
      int l = flag3 ? 1 : -1;
      poseStack.translate((float) l * f5, f6, f10);
    }

    applyItemArmTransform(poseStack, humanoidArm, equipProcess);

    if (!playerUsingItem) {
      this.applyItemArmAttackTransform(poseStack, humanoidArm, swingProcess);
    }

    int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;

    float f = (float)player.getUseItemRemainingTicks() - partialTick + 1.0F;
    float f1 = f / (float) itemInHand.getUseDuration(player);

    if (!playerUsingItem) {
      f1 = 1f;
    }

    if (playerUsingItem && (f1 <= 0.2 || f1 >= 1)) {
      return false;
    }

    float useTime = (float) (System.currentTimeMillis() % 10000) / 500.0F / (Math.clamp(f1, 0.7F, 1F) * 2f);
    float pos = 2.5f / 16f;

    poseStack.translate((float) -i * 0.55F, 0, 0);
    poseStack.mulPose(Axis.YP.rotationDegrees(90));
    poseStack.translate(pos, pos, 0);
    poseStack.scale(0.5f, 0.5f, 0.5f);
    poseStack.mulPose(Axis.YP.rotationDegrees(useTime * 360.0F / 2.0F));
    poseStack.translate(-pos, -pos, 0);

    return !playerUsingItem;
  }

  public void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProg) {
    int i = hand == HumanoidArm.RIGHT ? 1 : -1;
    poseStack.translate((float)i * 0.56F, -0.52F + equippedProg * -0.6F, -0.72F);
  }

  public void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress) {
    int i = hand == HumanoidArm.RIGHT ? 1 : -1;
    float f = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
    poseStack.mulPose(Axis.YP.rotationDegrees((float)i * (45.0F + f * -20.0F)));
    float f1 = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
    poseStack.mulPose(Axis.ZP.rotationDegrees((float)i * f1 * -20.0F));
    poseStack.mulPose(Axis.XP.rotationDegrees(f1 * -80.0F));
    poseStack.mulPose(Axis.YP.rotationDegrees((float)i * -45.0F));
  }
}
