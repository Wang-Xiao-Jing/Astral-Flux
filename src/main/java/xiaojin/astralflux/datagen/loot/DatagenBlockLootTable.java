package xiaojin.astralflux.datagen.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;

public class DatagenBlockLootTable extends BlockLootSubProvider {
  public DatagenBlockLootTable(HolderLookup.Provider lookupProvider) {
    super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
  }

  @Override
  protected void generate() {
  }

  @Override
  protected @NotNull Iterable<Block> getKnownBlocks() {
    return new ArrayList<>();
  }
}
