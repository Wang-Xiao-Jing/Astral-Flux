package xiaojin.astralflux.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.data.LanguageProvider;
import xiaojin.astralflux.AstralFlux;
import xiaojin.astralflux.init.ModAttributes;
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
    add("itemGroup.astralflux_tab", "流光轻逝");
    add(ModItems.AEGUS_BARRIER.value(), "埃癸斯壁垒");
//    add(ModAttributes.SOURCE_SOUL.get(), "源魂值");
    add(ModAttributes.MAX_SOURCE_SOUL.value(), "最大源魂值");
    add(ModAttributes.SOURCE_SOUL_RECOVERY.value(), "源魂恢复速度");
  }

  private void add(Attribute attribute, String value) {
    add(attribute.getDescriptionId(), value);
  }
}
