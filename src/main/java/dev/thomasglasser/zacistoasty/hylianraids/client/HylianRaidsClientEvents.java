package dev.thomasglasser.zacistoasty.hylianraids.client;

import dev.thomasglasser.zacistoasty.hylianraids.client.renderer.GanonRenderer;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.HylianRaidsEntityTypes;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.block.HylianRaidsBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class HylianRaidsClientEvents
{
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		ItemBlockRenderTypes.setRenderLayer(HylianRaidsBlocks.SUMMONER.get(), RenderType.cutout());
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerEntityRenderer(HylianRaidsEntityTypes.GANON.get(), GanonRenderer::new);
	}
}
