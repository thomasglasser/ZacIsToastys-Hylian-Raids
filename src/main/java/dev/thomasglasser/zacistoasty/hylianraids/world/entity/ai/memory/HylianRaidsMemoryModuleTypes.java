package dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.memory;

import com.mojang.serialization.Codec;
import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class HylianRaidsMemoryModuleTypes
{
	public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, HylianRaids.MOD_ID);

	public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Unit>> POWERED = register("powered", Codec.unit(Unit.INSTANCE));

	private static <U> DeferredHolder<MemoryModuleType<?>, MemoryModuleType<U>> register(String pIdentifier, Codec<U> pCodec) {
		return MEMORY_MODULE_TYPES.register(pIdentifier, () -> new MemoryModuleType<>(Optional.of(pCodec)));
	}
}
