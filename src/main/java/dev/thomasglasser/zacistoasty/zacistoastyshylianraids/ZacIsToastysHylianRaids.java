package dev.thomasglasser.zacistoasty.zacistoastyshylianraids;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ZacIsToastysHylianRaids.MODID)
public class ZacIsToastysHylianRaids
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "zacistoastyshylianraids";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ZacIsToastysHylianRaids(IEventBus modEventBus)
    {

    }
}
