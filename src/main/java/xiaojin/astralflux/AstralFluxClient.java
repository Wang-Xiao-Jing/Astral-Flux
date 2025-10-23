package xiaojin.astralflux;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static xiaojin.astralflux.AstralFlux.ID;

@Mod(value = AstralFlux.ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ID, value = Dist.CLIENT)
public class AstralFluxClient {
  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    AstralFlux.LOGGER.info("HELLO FROM CLIENT SETUP");
  }
}
