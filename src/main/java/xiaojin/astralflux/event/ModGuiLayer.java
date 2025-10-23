package xiaojin.astralflux.event;

import com.mojang.datafixers.util.Function3;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.client.gui.layers.SourceSoulBarLayerDraw;

import java.util.function.Supplier;

@EventBusSubscriber
public final class ModGuiLayer {
  public static final ResourceLocation TEST_LAYER = AstralFlux.modRL("source_soul_bar");

  @SubscribeEvent
  public static void registerGuiLayersEvent(RegisterGuiLayersEvent event) {
    register(event, VanillaGuiLayers.SELECTED_ITEM_NAME, TEST_LAYER, SourceSoulBarLayerDraw::new);
  }

  /**
   * 一个超级酷的注册方法
   */
  @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
  private static <T extends Layer> Supplier<T> register(RegisterGuiLayersEvent event, ResourceLocation other,
                                                        ResourceLocation id, Function3<GuiGraphics, DeltaTracker, Minecraft, T> layer) {
    final LayeredDraw.Layer layer1 = (guiGraphics, deltaTracker) -> layer.apply(guiGraphics, deltaTracker, Minecraft.getInstance());
    event.registerBelow(other, id, layer1);
    //noinspection unchecked
    return () -> (T) layer1;
  }
}
