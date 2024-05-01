package dev.thomasglasser.zacistoasty.hylianraids;

import com.mojang.logging.LogUtils;
import dev.thomasglasser.zacistoasty.hylianraids.client.HylianRaidsClientEvents;
import dev.thomasglasser.zacistoasty.hylianraids.core.HylianRaidsCoreEvents;
import dev.thomasglasser.zacistoasty.hylianraids.data.HylianRaidsDataGenerators;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.HylianRaidsEntityEvents;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.HylianRaidsEntityTypes;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.memory.HylianRaidsMemoryModuleTypes;
import dev.thomasglasser.zacistoasty.hylianraids.world.item.HylianRaidsItems;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.block.HylianRaidsBlocks;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HylianRaids.MOD_ID)
public class HylianRaids
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "hylianraids";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public HylianRaids(IEventBus modEventBus)
    {
        HylianRaidsBlocks.BLOCKS.register(modEventBus);
        HylianRaidsItems.ITEMS.register(modEventBus);
        HylianRaidsEntityTypes.ENTITY_TYPES.register(modEventBus);
        HylianRaidsMemoryModuleTypes.MEMORY_MODULE_TYPES.register(modEventBus);

        modEventBus.addListener(HylianRaidsDataGenerators::onGatherData);

        modEventBus.addListener(HylianRaidsCoreEvents::onCommonSetup);
        modEventBus.addListener(HylianRaidsEntityEvents::onEntityAttributeCreation);

        if (FMLEnvironment.dist.isClient())
        {
            modEventBus.addListener(HylianRaidsClientEvents::onClientSetup);
            modEventBus.addListener(HylianRaidsClientEvents::onRegisterRenderers);
        }
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
