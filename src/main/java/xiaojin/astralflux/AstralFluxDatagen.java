package xiaojin.astralflux;

import com.mojang.text2speech.Narrator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import xiaojin.astralflux.datagen.DatagenDatapackBuiltinEntries;
import xiaojin.astralflux.datagen.DatagenI18ZhCn;
import xiaojin.astralflux.datagen.DatagenItemModel;
import xiaojin.astralflux.datagen.DatagenParticle;
import xiaojin.astralflux.datagen.loot.DatagenLootTableProvider;

/**
 * 数据生成主类
 */
@EventBusSubscriber
@SuppressWarnings("all")
public class AstralFluxDatagen {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    final var generator = event.getGenerator();
    final var output = generator.getPackOutput();
    final var exFileHelper = event.getExistingFileHelper();
    final var lookupProvider = event.getLookupProvider();

    // 服务端数据生成
    buildServer(event, generator, new DatagenDatapackBuiltinEntries(output, lookupProvider));
    buildServer(event, generator, new DatagenLootTableProvider(output, lookupProvider));

    // 客户端数据生成
    buildClient(event, generator, new DatagenI18ZhCn(output));
    buildClient(event, generator, new DatagenItemModel(output, exFileHelper));
    buildClient(event, generator, new DatagenParticle(output, exFileHelper));

    Narrator.LOGGER.info("Gather data finish");
  }

  private static <T extends DataProvider> @NotNull T buildClient(GatherDataEvent event,
                                                                 DataGenerator generator,
                                                                 T provider) {
    return generator.addProvider(event.includeClient(), provider);
  }

  private static <T extends DataProvider> @NotNull T buildServer(GatherDataEvent event,
                                                                 DataGenerator generator,
                                                                 T provider) {
    return generator.addProvider(event.includeServer(), provider);
  }
}
