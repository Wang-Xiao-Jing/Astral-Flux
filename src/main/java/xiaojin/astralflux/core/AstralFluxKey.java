package xiaojin.astralflux.core;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.NotNull;

import static com.mojang.blaze3d.platform.InputConstants.KEY_F6;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = AstralFlux.ID, value = Dist.CLIENT)
public class AstralFluxKey {
  public static final String KEY_CATEGORIES = "key.categories." + AstralFlux.ID;
  /**
   * 切换源魂详细值类型
   */
  public static final KeyMapping SWITCH_DETAILS_SOURCE_SOUL_VALUE_TYPE = new KeyMapping(
    getDescription("switch_details_source_soul_value_type"),
    KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, KEY_F6,
    KEY_CATEGORIES);

  public static void init(RegisterKeyMappingsEvent event) {
    event.register(SWITCH_DETAILS_SOURCE_SOUL_VALUE_TYPE);
  }

  @SubscribeEvent
  public static void onKeyInput(InputEvent.Key event) {
    if (SWITCH_DETAILS_SOURCE_SOUL_VALUE_TYPE.isDown()) {
      AstralFluxConfig.CLIENT_CONFIG.detailsSourceSoulValueType.set(!AstralFluxConfig.CLIENT_CONFIG.detailsSourceSoulValueType.get());
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static @NotNull String getDescription(String name) {
    return "key." + AstralFlux.ID + "." + name;
  }
}
