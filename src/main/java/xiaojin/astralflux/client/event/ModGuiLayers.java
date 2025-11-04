package xiaojin.astralflux.client.event;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import xiaojin.astralflux.client.gui.layers.SourceSoulBarLayerDraw;
import xiaojin.astralflux.core.AstralFlux;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public final class ModGuiLayers {
  public static final ResourceLocation SOURCE_SOUL_BAR = AstralFlux.modRL("source_soul_bar");

  @SubscribeEvent
  public static void register(RegisterGuiLayersEvent event) {
    event.registerAbove(VanillaGuiLayers.SELECTED_ITEM_NAME, SOURCE_SOUL_BAR, SourceSoulBarLayerDraw.INSTANCE);
  }
}
