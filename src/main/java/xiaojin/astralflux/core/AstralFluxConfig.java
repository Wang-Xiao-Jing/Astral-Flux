package xiaojin.astralflux.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class AstralFluxConfig {
  // 客户端配置 //
  public static final ClientConfig CLIENT_CONFIG;
  public static final ModConfigSpec CLIENT_CONFIG_SPEC;

  static {
    // 客户端配置 //
    var clientPair = configure(ClientConfig::new);
    CLIENT_CONFIG = clientPair.getLeft();
    CLIENT_CONFIG_SPEC = clientPair.getRight();
  }

  /**
   * 客户端配置
   */
  public static class ClientConfig {
    public final ModConfigSpec.ConfigValue<Boolean> detailsSourceSoulValueType;
    public final ModConfigSpec.ConfigValue<Boolean> detailsModifySourceSoulValueSkipWords;

    public ClientConfig(ModConfigSpec.Builder builder) {
      this.detailsSourceSoulValueType = define(builder, false,
        "gui.details_source_soul_value_type",
        "是否显示源魂详细值");
      this.detailsModifySourceSoulValueSkipWords = define(builder, false,
        "gui.details_modify_source_soul_value_skip_words",
        "是否显示修改源魂值跳字");
    }
  }

  @SuppressWarnings("SameParameterValue")
  private static ModConfigSpec.@NotNull BooleanValue define(ModConfigSpec.Builder builder, boolean defaultValue, String key, String... comment) {
    return builder.comment(comment)
      .translation(getTranslation(key))
      .define(key, defaultValue);
  }

  public static @NotNull String getTranslation(String... keys) {
    if (keys.length == 0) {
      return AstralFlux.ID + ".config";
    }
    var builder = new StringBuilder();
    for (String key : keys) {
      builder.append(".");
      builder.append(key);
    }
    return AstralFlux.ID + ".config" + builder;
  }

  private static <T> @NotNull Pair<T, ModConfigSpec> configure(Function<ModConfigSpec.Builder, T> consumer) {
    return new ModConfigSpec.Builder().configure(consumer);
  }
}
