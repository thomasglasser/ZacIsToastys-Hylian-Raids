package dev.thomasglasser.zacistoasty.hylianraids.world.entity;

import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class HylianRaidsEntityEvents
{
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event)
	{
		event.put(HylianRaidsEntityTypes.GANON.get(), Ganon.createAttributes().build());
	}
}
