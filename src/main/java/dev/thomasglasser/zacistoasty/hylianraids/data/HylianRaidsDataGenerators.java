package dev.thomasglasser.zacistoasty.hylianraids.data;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.data.blockstates.HylianRaidsBlockStates;
import dev.thomasglasser.zacistoasty.hylianraids.data.lang.HylianRaidsEnUsLanguageProvider;
import dev.thomasglasser.zacistoasty.hylianraids.data.tags.HylianRaidsBiomeTagsProvider;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.HylianRaidsStructures;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.placement.HylianRaidsStructureSets;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.pools.HylianRaidsPools;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class HylianRaidsDataGenerators
{
	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.TEMPLATE_POOL, HylianRaidsPools::bootstrap)
			.add(Registries.STRUCTURE, HylianRaidsStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, HylianRaidsStructureSets::bootstrap);

	public static void onGatherData(GatherDataEvent event)
	{
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		boolean includeClient = event.includeClient();
		boolean includeServer = event.includeServer();

		// Server
		DatapackBuiltinEntriesProvider datapackEntries = new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of(HylianRaids.MOD_ID));
		generator.addProvider(includeServer, datapackEntries);
		lookupProvider = datapackEntries.getRegistryProvider();
		generator.addProvider(includeServer, new HylianRaidsBiomeTagsProvider(output, lookupProvider, existingFileHelper));

		// Client
		generator.addProvider(includeClient, new HylianRaidsBlockStates(output, existingFileHelper));
		generator.addProvider(includeClient, new HylianRaidsEnUsLanguageProvider(output));
	}
}
