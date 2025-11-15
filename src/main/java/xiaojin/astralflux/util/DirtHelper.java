package xiaojin.astralflux.util;

import net.minecraft.world.level.Level;
import net.neoforged.fml.LogicalSide;

public class DirtHelper {
  public static void runDirt(final boolean isClient, Runnable client, Runnable server) {
    if (isClient) {
      client.run();
    } else {
      server.run();
    }
  }

  public static void runDirt(final Level world, Runnable client, Runnable server) {
    runDirt(world.isClientSide, client, server);
  }

  public static void runDirt(final LogicalSide logicalSide, Runnable client, Runnable server) {
    runDirt(logicalSide.isClient(), client, server);
  }
}

