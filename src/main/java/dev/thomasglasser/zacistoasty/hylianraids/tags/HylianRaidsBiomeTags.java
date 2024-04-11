package dev.thomasglasser.zacistoasty.hylianraids.tags;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class HylianRaidsBiomeTags
{
	public static final TagKey<Biome> HAS_NORMAL_ARENA = register("has_normal_arena");
	public static final TagKey<Biome> HAS_ICE_ARENA = register("has_ice_arena");
	public static final TagKey<Biome> HAS_SAND_ARENA = register("has_sand_arena");

	private static TagKey<Biome> register(String name)
	{
		return TagKey.create(Registries.BIOME, HylianRaids.modLoc(name));
	}
}
