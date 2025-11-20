package xiaojin.astralflux.datagen;

import net.minecraft.client.KeyMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.LanguageProvider;
import xiaojin.astralflux.common.commands.SourceSoulCommands;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.core.AstralFluxConfig;
import xiaojin.astralflux.core.AstralFluxKey;
import xiaojin.astralflux.init.ModAttributes;
import xiaojin.astralflux.init.ModEntityTypes;
import xiaojin.astralflux.init.ModItems;

public class DatagenI18ZhCn extends LanguageProvider {
  public DatagenI18ZhCn(PackOutput output) {
    super(output, AstralFlux.ID, "zh_cn");
  }

  public static String translationKey(String string) {
    return AstralFlux.ID + ".configgui." + string;
  }

  public static String commentKey(String string) {
    return AstralFlux.ID + ".configgui." + string + ".tooltip";
  }

  @Override
  protected void addTranslations() {
    add("itemGroup." + AstralFlux.ID + "_tab", "流光轻逝");
    add(AstralFluxKey.KEY_CATEGORIES, "流光轻逝");
    add(ModItems.AEGUS_BARRIER.value(), "埃癸斯壁垒");
    add(ModAttributes.MAX_SOURCE_SOUL.value(), "最大源魂值");
    add(ModAttributes.SOURCE_SOUL_RECOVERY_VALUE.value(), "源魂恢复速度");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.SET_KEY, SourceSoulCommands.ProcessType.VALUE.getName()), "已设置%s的源魂值为：%d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.SET_KEY, SourceSoulCommands.ProcessType.MAX_VALUE.getName()), "已设置%s的最大源魂基础值为：%d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.SET_KEY, SourceSoulCommands.ProcessType.RECOVERY_SPEED.getName()), "已设置%s的源魂恢复基础值为：每tick %d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.GET_KEY, SourceSoulCommands.ProcessType.VALUE.getName()), "%s的源魂值为：%d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.GET_KEY, SourceSoulCommands.ProcessType.MAX_VALUE.getName()), "%s的最大源魂值为：%d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.GET_KEY, SourceSoulCommands.ProcessType.RECOVERY_SPEED.getName()), "%s的源魂恢复值为：每tick %d点");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.RESET_KEY, SourceSoulCommands.ProcessType.VALUE.getName()), "已重置%s的源魂值为：%d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.RESET_KEY, SourceSoulCommands.ProcessType.MAX_VALUE.getName()), "已重置%s的最大源魂基础值为：%d");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.RESET_KEY, SourceSoulCommands.ProcessType.RECOVERY_SPEED.getName()), "已重置%s的源魂恢复基础值为：每tick %d点");
    add(SourceSoulCommands.getFormattedKey(SourceSoulCommands.RESET_KEY), "已重置%s的源魂属性");
    add("astralflux.configuration.gui", "GUI");
    add("astralflux.configuration.gui.button", "详细");
    add("astralflux.configuration.section.astralflux.client.toml.title", "客户端配置");
    add("astralflux.configuration.section.astralflux.client.toml", "客户端配置");
    add("astralflux.configuration.title", "流光轻逝配置");
    add(AstralFluxConfig.CLIENT_CONFIG.detailsSourceSoulValueType, "是否显示源魂详细值");
    add(AstralFluxConfig.CLIENT_CONFIG.detailsModifySourceSoulValueSkipWords, "是否显示修改源魂值跳字");
    add(AstralFluxKey.SWITCH_DETAILS_SOURCE_SOUL_VALUE_TYPE, "切换源魂详细值类型");
    add(ModEntityTypes.AEGUS_BARRIER_SHIELD_ENTITY.get(), "埃癸斯壁垒盾面");
    add(ModEntityTypes.AEGUS_BARRIER_SHIELD_MANAGER_ENTITY.get(), "埃癸斯壁垒");
  }

  private void add(Attribute attribute, String value) {
    add(attribute.getDescriptionId(), value);
  }

  private void add(ModConfigSpec.ConfigValue<?> configValue, String value) {
    add(AstralFluxConfig.getTranslation(configValue.getPath().toArray(String[]::new)), value);
  }

  private void add(ModConfigSpec.ConfigValue<?> configValue, String value, String tooltipValue) {
    add(configValue, value);
    add(AstralFluxConfig.getTranslation(configValue.getPath().toArray(String[]::new)) + ".tooltip", value);
  }

  private void add(KeyMapping keyMapping, String value) {
    add(keyMapping.getName(), value);
  }
}
