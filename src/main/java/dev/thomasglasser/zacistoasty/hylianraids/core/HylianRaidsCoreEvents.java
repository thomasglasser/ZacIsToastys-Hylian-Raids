package dev.thomasglasser.zacistoasty.hylianraids.core;

import com.google.common.collect.ImmutableSet;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.block.HylianRaidsBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class HylianRaidsCoreEvents
{
	public static void onCommonSetup(FMLCommonSetupEvent event)
	{
		registerBlockEntityAdditions();
	}

	private static void registerBlockEntityAdditions()
	{
		List<Block> trialSpawnerBlocks = new ArrayList<>(BlockEntityType.TRIAL_SPAWNER.validBlocks);
		trialSpawnerBlocks.add(HylianRaidsBlocks.SUMMONER.get());
		BlockEntityType.TRIAL_SPAWNER.validBlocks = ImmutableSet.copyOf(trialSpawnerBlocks);
	}
}
