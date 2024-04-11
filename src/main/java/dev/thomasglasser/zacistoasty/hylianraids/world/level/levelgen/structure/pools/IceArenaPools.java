package dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.pools;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import static net.minecraft.data.worldgen.Pools.EMPTY;

public class IceArenaPools
{
    public static final ResourceKey<StructureTemplatePool> START = HylianRaidsPools.createKey("ice_arena/base");

    public static void bootstrap(BootstapContext<StructureTemplatePool> bootstapContext) {
        HolderGetter<StructureTemplatePool> holderGetter = bootstapContext.lookup(Registries.TEMPLATE_POOL);

        Holder<StructureTemplatePool> holder = holderGetter.getOrThrow(EMPTY);

        bootstapContext.register(
                START,
                new StructureTemplatePool(
                        holder, ImmutableList.of(Pair.of(HylianRaidsPools.legacyElement(START.location()), 1)), StructureTemplatePool.Projection.RIGID
                )
        );
    }
}
