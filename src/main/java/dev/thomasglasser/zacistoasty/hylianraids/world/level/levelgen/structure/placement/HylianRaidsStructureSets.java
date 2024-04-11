package dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.placement;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.HylianRaidsStructures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class HylianRaidsStructureSets
{
    public static final ResourceKey<StructureSet> NORMAL_ARENA = register("normal_arena");
    public static final ResourceKey<StructureSet> ICE_ARENA = register("ice_arena");
    public static final ResourceKey<StructureSet> SAND_ARENA = register("sand_arena");

    public static void bootstrap(BootstapContext<StructureSet> context) {
        HolderGetter<Structure> holderGetter = context.lookup(Registries.STRUCTURE);

        // Fun Easter egg: the number used is the word "raids" numerically
        context.register(NORMAL_ARENA,
                new StructureSet(holderGetter.getOrThrow(HylianRaidsStructures.NORMAL_ARENA), new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 1819419))
        );

        context.register(ICE_ARENA,
                new StructureSet(holderGetter.getOrThrow(HylianRaidsStructures.ICE_ARENA), new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 1819420))
        );

        context.register(SAND_ARENA,
                new StructureSet(holderGetter.getOrThrow(HylianRaidsStructures.SAND_ARENA), new RandomSpreadStructurePlacement(64, 16, RandomSpreadType.LINEAR, 1819421))
        );
    }

    private static ResourceKey<StructureSet> register(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, HylianRaids.modLoc(name));
    }
}
