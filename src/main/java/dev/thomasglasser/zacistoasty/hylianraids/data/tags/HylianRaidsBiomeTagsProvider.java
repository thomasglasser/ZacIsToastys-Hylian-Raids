package dev.thomasglasser.zacistoasty.hylianraids.data.tags;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.tags.HylianRaidsBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class HylianRaidsBiomeTagsProvider extends BiomeTagsProvider
{
	public HylianRaidsBiomeTagsProvider(PackOutput p_255800_, CompletableFuture<HolderLookup.Provider> p_256205_, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(p_255800_, p_256205_, HylianRaids.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		tag(HylianRaidsBiomeTags.HAS_ICE_ARENA)
				.add(Biomes.SNOWY_PLAINS)
				.add(Biomes.ICE_SPIKES)
				.add(Biomes.SNOWY_TAIGA)
				.add(Biomes.SNOWY_BEACH)
				.add(Biomes.FROZEN_PEAKS)
				.add(Biomes.JAGGED_PEAKS)
				.add(Biomes.SNOWY_SLOPES)
				.add(Biomes.GROVE);

		tag(HylianRaidsBiomeTags.HAS_SAND_ARENA)
				.add(Biomes.DESERT);

		tag(HylianRaidsBiomeTags.HAS_NORMAL_ARENA)
				.add(Biomes.STONY_SHORE)
				.add(Biomes.SWAMP)
				.add(Biomes.MANGROVE_SWAMP)
				.add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
				.add(Biomes.WINDSWEPT_HILLS)
				.add(Biomes.WINDSWEPT_FOREST)
				.add(Biomes.TAIGA)
				.add(Biomes.PLAINS)
				.add(Biomes.MEADOW)
				.add(Biomes.BEACH)
				.add(Biomes.FOREST)
				.add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
				.add(Biomes.FLOWER_FOREST)
				.add(Biomes.BIRCH_FOREST)
				.add(Biomes.DARK_FOREST)
				.add(Biomes.SAVANNA_PLATEAU)
				.add(Biomes.SAVANNA)
				.add(Biomes.JUNGLE)
				.add(Biomes.BADLANDS)
				.add(Biomes.WOODED_BADLANDS)
				.add(Biomes.JAGGED_PEAKS)
				.add(Biomes.STONY_PEAKS)
				.add(Biomes.OLD_GROWTH_PINE_TAIGA)
				.add(Biomes.OLD_GROWTH_BIRCH_FOREST)
				.add(Biomes.SUNFLOWER_PLAINS)
				.add(Biomes.SPARSE_JUNGLE)
				.add(Biomes.BAMBOO_JUNGLE)
				.add(Biomes.ERODED_BADLANDS)
				.add(Biomes.WINDSWEPT_SAVANNA)
				.add(Biomes.CHERRY_GROVE);
	}
}
