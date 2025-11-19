package xiaojin.astralflux.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

@SuppressWarnings("unused")
public final class ModClientEnumParams {
  public static final EnumProxy<HumanoidModel.ArmPose> AEGUS_BARRIER_ITEM_ARMPOSE = new EnumProxy<>(
    HumanoidModel.ArmPose.class,
    false,
    (IArmPoseTransformer) (humanoidModel, livingEntity, humanoidArm) -> {
      if (!livingEntity.isUsingItem()) {
        return;
      }
      boolean isLeftArm = humanoidArm == HumanoidArm.LEFT;
      ModelPart arm = isLeftArm ? humanoidModel.leftArm : humanoidModel.rightArm;
      arm.xRot = -90 * Mth.DEG_TO_RAD;
      arm.yRot = -0.1F + humanoidModel.head.yRot;
    }
  );
}
