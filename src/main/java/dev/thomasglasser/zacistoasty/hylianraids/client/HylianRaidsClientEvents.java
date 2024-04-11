package dev.thomasglasser.zacistoasty.hylianraids.client;

import dev.thomasglasser.zacistoasty.hylianraids.world.level.block.HylianRaidsBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class HylianRaidsClientEvents
{
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		ItemBlockRenderTypes.setRenderLayer(HylianRaidsBlocks.SUMMONER.get(), RenderType.cutout());
	}
}
