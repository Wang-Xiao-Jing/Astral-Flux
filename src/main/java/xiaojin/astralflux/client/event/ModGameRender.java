package xiaojin.astralflux.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import xiaojin.astralflux.client.renderer.item.AegusBarrierShieldRenderer;
import xiaojin.astralflux.common.item.aegusbarrier.AegusBarrierItem;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModItems;


@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public final class ModGameRender {

  @SubscribeEvent
  public static void levelRender(final RenderLevelStageEvent event) {
    final var stage = event.getStage();
    final var minecraft = Minecraft.getInstance();
    final var level = minecraft.level;
    final var frustum = event.getFrustum();
    final var pose = event.getPoseStack();
    final var camera = event.getCamera();
    final var partialTick = event.getPartialTick();
    if (stage == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
      AegusBarrierShieldRenderer.INSTANCE.levelRender(
        minecraft, level, frustum, pose, camera, partialTick);
    }
  }

  @SubscribeEvent
  public static void playerRender(final RenderPlayerEvent.Pre event) {
    Minecraft minecraft = Minecraft.getInstance();
    ClientLevel level = minecraft.level;
    Player player = event.getEntity();
    ItemStack itemStack = player.getWeaponItem();
    PlayerRenderer renderer = event.getRenderer();
    float partialTick = event.getPartialTick();
    PoseStack poseStack = event.getPoseStack();
    MultiBufferSource multiBufferSource = event.getMultiBufferSource();
    int packedLight = event.getPackedLight();
    ItemRenderer itemRenderer = minecraft.getItemRenderer();
    ItemDisplayContext displayContext = ItemDisplayContext.NONE;
    PlayerModel<AbstractClientPlayer> model = renderer.getModel();
  }


  @SubscribeEvent
  public static void playerRender(final RenderPlayerEvent.Post event) {
    Minecraft minecraft = Minecraft.getInstance();
    ClientLevel level = minecraft.level;
    Player player = event.getEntity();
    ItemStack itemStack = player.getWeaponItem();
    PlayerRenderer renderer = event.getRenderer();
    float partialTick = event.getPartialTick();
    PoseStack poseStack = event.getPoseStack();
    MultiBufferSource multiBufferSource = event.getMultiBufferSource();
    int packedLight = event.getPackedLight();
    ItemRenderer itemRenderer = minecraft.getItemRenderer();
    ItemDisplayContext displayContext = ItemDisplayContext.NONE;

//    if (itemStack.isEmpty() || !player.isUsingItem() || !itemStack.is(ModItems.AEGUS_BARRIER)) {
//      return;
//    }

//    poseStack.pushPose();
//    poseStack.translate(0,0,0);
//    itemRenderer.renderStatic(itemStack, displayContext, packedLight, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, level, 0);
//    poseStack.popPose();
  }
 }
