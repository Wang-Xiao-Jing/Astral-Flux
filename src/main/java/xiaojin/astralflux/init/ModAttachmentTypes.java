package xiaojin.astralflux.init;

import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xiaojin.astralflux.common.item.aegusbarrier.AegusBarrierShields;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.DateAttachmentTypeRegisterUtil;

public final class ModAttachmentTypes extends DateAttachmentTypeRegisterUtil {
  public static final DeferredRegister<AttachmentType<?>> REGISTRY = AstralFlux.modRegister(NeoForgeRegistries.ATTACHMENT_TYPES);

  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Double>> SOURCE_SOUL = register(
    "source_soul", AttachmentType
      .builder(() -> 0d)
      .serialize(Codec.DOUBLE)
      .sync(ByteBufCodecs.DOUBLE)
      .copyOnDeath());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<Integer>> SOURCE_SOUL_PAUSE_RECOVERY_TICK = register(
    "source_soul_pause_recovery_tick", AttachmentType
      .builder(() -> 0)
      .serialize(Codec.INT)
      .sync(ByteBufCodecs.INT)
      .copyOnDeath());
  public static final DeferredHolder<AttachmentType<?>, AttachmentType<AegusBarrierShields>> AEGUS_BARRIER_SHIELD = register(
    "aegus_barrier_shield", AttachmentType
      .builder(AegusBarrierShields::new)
      .sync(new AegusBarrierShields.Sync()));
}
