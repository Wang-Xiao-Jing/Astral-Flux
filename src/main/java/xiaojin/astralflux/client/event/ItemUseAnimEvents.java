package xiaojin.astralflux.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import ctn.ctnapi.client.util.GraphicsPlaneRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import xiaojin.astralflux.core.AstralFlux;

@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ItemUseAnimEvents {
  @SubscribeEvent
  public static void renderArmEvent(RenderArmEvent event) {
    Minecraft minecraft = Minecraft.getInstance();
    ClientLevel level = minecraft.level;
    HumanoidArm arm = event.getArm();
    PoseStack poseStack = event.getPoseStack();
    AbstractClientPlayer player = event.getPlayer();
    MultiBufferSource multiBufferSource = event.getMultiBufferSource();
    ItemStack itemStack = player.getUseItem();
    InteractionHand itemHand = player.getUsedItemHand();
    int packedLight = event.getPackedLight();
  }

  @SubscribeEvent
  public static void renderArmEvent(RenderHandEvent event) {
    Minecraft minecraft = Minecraft.getInstance();
    ClientLevel level = minecraft.level;
    PoseStack poseStack = event.getPoseStack();
    LocalPlayer player = minecraft.player;
    MultiBufferSource multiBufferSource = event.getMultiBufferSource();
    ItemStack itemStack = event.getItemStack();
    InteractionHand itemHand = event.getHand();
    int packedLight = event.getPackedLight();
  }
}
