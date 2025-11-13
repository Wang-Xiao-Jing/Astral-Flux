package xiaojin.astralflux.init;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import xiaojin.astralflux.core.AstralFlux;
import xiaojin.astralflux.init.util.ModEntityDataSerializersRegisterUtil;

import java.util.Map;
import java.util.UUID;

public final class ModEntityDataSerializers extends ModEntityDataSerializersRegisterUtil {
  public static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = AstralFlux.modRegister(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS);

  public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<Map<Integer, Map.Entry<Integer, UUID>>>> AEGUS_BARRIER_SHIELD_MANAGER_SHIELDS = register("aegus_barrier_shield_manager_shields",
    EntityDataSerializer.forValueType(
      ByteBufCodecs.map(
        Int2ObjectOpenHashMap::new,
        ByteBufCodecs.INT,
        ModByteBufCodecs.<RegistryFriendlyByteBuf, Map.Entry<Integer, UUID>, Integer, UUID>entry(Map::entry, ByteBufCodecs.INT, UUIDUtil.STREAM_CODEC), 7)
    ));
}
