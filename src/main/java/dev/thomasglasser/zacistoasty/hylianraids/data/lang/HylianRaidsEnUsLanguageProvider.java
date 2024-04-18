package dev.thomasglasser.zacistoasty.hylianraids.data.lang;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.HylianRaidsEntityTypes;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.block.HylianRaidsBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class HylianRaidsEnUsLanguageProvider extends LanguageProvider
{

	public HylianRaidsEnUsLanguageProvider(PackOutput output)
	{
		super(output, HylianRaids.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		add(HylianRaidsBlocks.SUMMONER.get(), "Summoner");

		add(HylianRaidsEntityTypes.GANON.get(), "Ganondorf");
		add(BuiltInRegistries.ENTITY_TYPE.getKey(HylianRaidsEntityTypes.GANON.get()).toLanguageKey("entity") + ".powered", "Ganon Beast");
	}
}
