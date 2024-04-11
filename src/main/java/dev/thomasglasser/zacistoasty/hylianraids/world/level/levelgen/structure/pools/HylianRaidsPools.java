package dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.pools;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.function.Function;

import static net.minecraft.data.worldgen.Pools.EMPTY;

public class HylianRaidsPools
{
    public static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, HylianRaids.modLoc(name));
    }

    public static void bootstrap(BootstapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> holderGetter = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> holder = holderGetter.getOrThrow(EMPTY);
        context.register(EMPTY, new StructureTemplatePool(holder, ImmutableList.of(), StructureTemplatePool.Projection.RIGID));

        NormalArenaPools.bootstrap(context);
        IceArenaPools.bootstrap(context);
        SandArenaPools.bootstrap(context);
    }

    public static Function<StructureTemplatePool.Projection, SinglePoolElement> singleElement(ResourceLocation rl) {
        return projection -> new SinglePoolElement(Either.left(rl),
                Holder.direct(new StructureProcessorList(List.of())), projection);
    }

    public static Function<StructureTemplatePool.Projection, SinglePoolElement> singleElement(ResourceLocation rl, Holder<StructureProcessorList> structureProcessorList) {
        return projection -> new SinglePoolElement(Either.left(rl),
                structureProcessorList, projection);
    }

    public static Function<StructureTemplatePool.Projection, LegacySinglePoolElement> legacyElement(ResourceLocation rl) {
        return projection -> new LegacySinglePoolElement(Either.left(rl),
                Holder.direct(new StructureProcessorList(List.of())), projection);
    }
}
