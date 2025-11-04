package xiaojin.astralflux.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModAttributes;
import xiaojin.astralflux.util.SourceSoulUtil;

public class SourceSoulCommands {
  public static final DoubleArgumentType FOUR_COLOR_ARG = DoubleArgumentType.doubleArg(0, 10240);
  public static final String SET_KEY = "set_source_soul";
  public static final String GET_KEY = "get_source_soul";
  public static final String RESET_KEY = "reset_source_soul";
  public static final String SUGGESTIONS = "source_soul.suggestions.";
  public static final String FILL_SET_KEY = SUGGESTIONS + "set";
  public static final String FILL_GET_KEY = SUGGESTIONS + "get";
  public static final String FILL_VALUE_KEY = SUGGESTIONS + "value";

  public static void processSourceSoul(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("sourceSoul")
      .requires(source -> source.hasPermission(2))
      .then(Commands.argument("target", EntityArgument.player())
        .then(Commands.literal("set")
          .then(logic(ProcessType.VALUE, true))
          .then(logic(ProcessType.MAX_VALUE, true))
          .then(logic(ProcessType.RECOVERY_SPEED, true)))
        .then(Commands.literal("get")
          .then(logic(ProcessType.VALUE, false))
          .then(logic(ProcessType.MAX_VALUE, false))
          .then(logic(ProcessType.RECOVERY_SPEED, false)))
        .then(Commands.literal("reset").executes(context -> {
          var player = EntityArgument.getPlayer(context, "target");
          SourceSoulUtil.setValue(player, 0);
          SourceSoulUtil.setMaxBaseValue(player, ModAttributes.MAX_SOURCE_SOUL.value().getDefaultValue());
          SourceSoulUtil.setRecoveryBaseValue(player, ModAttributes.SOURCE_SOUL_RECOVERY_VALUE.value().getDefaultValue());
          context.getSource().sendSuccess(() ->
            Component.translatable(getFormattedKey(RESET_KEY), player.getName()), true);
          return 1;
        }))
        .then(Commands.literal("reset")
          .then(reset(ProcessType.VALUE))
          .then(reset(ProcessType.MAX_VALUE))
          .then(reset(ProcessType.RECOVERY_SPEED))
        )
      )
    );
  }

  private static LiteralArgumentBuilder<CommandSourceStack> reset(ProcessType processType) {
    final var name = processType.getName();
    return Commands.literal(name).executes(context -> {
      var player = EntityArgument.getPlayer(context, "target");
      double value = 0;
      switch (processType) {
        case VALUE -> SourceSoulUtil.setValue(player, 0);
        case MAX_VALUE ->
          SourceSoulUtil.setMaxBaseValue(player, value = ModAttributes.MAX_SOURCE_SOUL.value().getDefaultValue());
        case RECOVERY_SPEED ->
          SourceSoulUtil.setRecoveryBaseValue(player, value = ModAttributes.SOURCE_SOUL_RECOVERY_VALUE.value().getDefaultValue());
      }
      final double finalValue = value;
      context.getSource().sendSuccess(() ->
        Component.translatable(getFormattedKey(RESET_KEY, name), player.getName(), finalValue), true);
      return 1;
    });
  }

  private static LiteralArgumentBuilder<CommandSourceStack> logic(ProcessType processType, boolean isSet) {
    final var name = processType.getName();
    Command<CommandSourceStack> commandSourceStackCommand = context -> {
      double value = 0;
      if (isSet) {
        value = DoubleArgumentType.getDouble(context, "value");
      }
      var player = EntityArgument.getPlayer(context, "target");
      switch (processType) {
        case VALUE -> {
          if (isSet) {
            SourceSoulUtil.setValue(player, value);
          } else {
            value = SourceSoulUtil.getValue(player);
          }
        }
        case MAX_VALUE -> {
          if (isSet) {
            SourceSoulUtil.setMaxBaseValue(player, value);
          } else {
            value = SourceSoulUtil.getMaxValue(player);
          }
        }
        case RECOVERY_SPEED -> {
          if (isSet) {
            SourceSoulUtil.setRecoveryBaseValue(player, value);
          } else {
            value = SourceSoulUtil.getRecoveryValue(player);
          }
        }
      }
      final double finalValue = value;
      context.getSource().sendSuccess(() ->
        Component.translatable(getFormattedKey(isSet ? SET_KEY : GET_KEY, name), player.getName(), finalValue), true);
      return 1;
    };
    var literal = Commands.literal(name);
    literal = isSet ?
      literal.then(Commands.argument("value", FOUR_COLOR_ARG).executes(commandSourceStackCommand)) :
      literal.executes(commandSourceStackCommand);
    return literal;
  }

  public static @NotNull String getFormattedKey(String key, String key2) {
    return "%s.commands.%s.%s".formatted(AstralFlux.ID, key, key2);
  }

  public static @NotNull String getFormattedKey(String key) {
    return "%s.commands.%s".formatted(AstralFlux.ID, key);
  }

  //TODO
  private static void fillSelectorSuggestions(SuggestionsBuilder builder) {
    builder.suggest("set", Component.translatable(FILL_SET_KEY));
    builder.suggest("get", Component.translatable(FILL_GET_KEY));
    for (var processType : ProcessType.values()) {
      var name = processType.getName();
      builder.suggest(name, Component.translatable(SUGGESTIONS + name));
    }
    builder.suggest("value", Component.translatable(FILL_VALUE_KEY));
  }

  public enum ProcessType {
    VALUE("value"),
    MAX_VALUE("maxValue"),
    RECOVERY_SPEED("recoveryValue"),
    ;

    private final String name;

    ProcessType(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
