package dev.thomasglasser.zacistoasty.hylianraids.world.entity;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HylianRaidsEntityTypes
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, HylianRaids.MOD_ID);

	public static final DeferredHolder<EntityType<?>, EntityType<Ganon>> GANON = ENTITY_TYPES.register("ganon", () -> EntityType.Builder.of(Ganon::new, MobCategory.MONSTER)
			.sized(0.6F, 1.95F) // Second Phase Size
			.build("ganon"));
}
