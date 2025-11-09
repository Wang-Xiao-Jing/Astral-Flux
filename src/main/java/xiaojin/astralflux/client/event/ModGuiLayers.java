package xiaojin.astralflux.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import xiaojin.astralflux.client.gui.layers.SourceSoulBarLayerDraw;
import xiaojin.astralflux.core.AstralFlux;


@EventBusSubscriber(value = Dist.CLIENT)
public final class ModGuiLayers {
  public static final ResourceLocation SOURCE_SOUL_BAR = AstralFlux.modRL("source_soul_bar");

  @SubscribeEvent
  public static void register(RegisterGuiLayersEvent event) {
    event.registerAbove(VanillaGuiLayers.AIR_LEVEL, SOURCE_SOUL_BAR, SourceSoulBarLayerDraw.INSTANCE);
  }

  @SubscribeEvent
  public static void register(RenderGuiLayerEvent.Pre event) {
    if (!event.getName().equals(VanillaGuiLayers.SELECTED_ITEM_NAME)) {
      return;
    }
    var instance = SourceSoulBarLayerDraw.INSTANCE;
    if (!instance.isDisplay()) {
      return;
    }
    var pose = event.getGuiGraphics().pose();
    pose.pushPose();
    pose.translate(0, -instance.getSourceSoulBarHeight(), 0);
  }

  @SubscribeEvent
  public static void register(RenderGuiLayerEvent.Post event) {
    if (!event.getName().equals(VanillaGuiLayers.SELECTED_ITEM_NAME)) {
      return;
    }
    if (!SourceSoulBarLayerDraw.INSTANCE.isDisplay()) {
      return;
    }
    var pose = event.getGuiGraphics().pose();
    pose.popPose();
  }
}
