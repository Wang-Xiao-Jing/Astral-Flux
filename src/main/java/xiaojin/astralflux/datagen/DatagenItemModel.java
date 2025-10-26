package xiaojin.astralflux.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.ModItems;

public class DatagenItemModel extends ItemModelProvider {
  public DatagenItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, AstralFlux.ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    basicItem(ModItems.AEGUS_BARRIER.value());
  }
}
