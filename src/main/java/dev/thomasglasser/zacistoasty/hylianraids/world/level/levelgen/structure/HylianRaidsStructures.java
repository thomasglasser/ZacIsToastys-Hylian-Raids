package dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.tags.HylianRaidsBiomeTags;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.pools.IceArenaPools;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.pools.NormalArenaPools;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.levelgen.structure.pools.SandArenaPools;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import static net.minecraft.data.worldgen.Structures.structure;

public class HylianRaidsStructures
{
    public static final ResourceKey<Structure> NORMAL_ARENA = createKey("normal_arena");
    public static final ResourceKey<Structure> ICE_ARENA = createKey("ice_arena");
    public static final ResourceKey<Structure> SAND_ARENA = createKey("sand_arena");

    private static ResourceKey<Structure> createKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, HylianRaids.modLoc(name));
    }

    public static void bootstrap(BootstapContext<Structure> context)
    {
        HolderGetter<Biome> holderGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> holderGetter2 = context.lookup(Registries.TEMPLATE_POOL);

        context.register(NORMAL_ARENA,
                new JigsawStructure(
                        structure(
                                holderGetter.getOrThrow(HylianRaidsBiomeTags.HAS_NORMAL_ARENA),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.BEARD_THIN
                        ),
                        holderGetter2.getOrThrow(NormalArenaPools.START),
                        7,
                        ConstantHeight.of(VerticalAnchor.absolute(0)),
                        false,
                        Heightmap.Types.WORLD_SURFACE_WG
                )
        );

        context.register(ICE_ARENA,
                new JigsawStructure(
                        structure(
                                holderGetter.getOrThrow(HylianRaidsBiomeTags.HAS_ICE_ARENA),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.BEARD_THIN
                        ),
                        holderGetter2.getOrThrow(IceArenaPools.START),
                        7,
                        ConstantHeight.of(VerticalAnchor.absolute(0)),
                        false,
                        Heightmap.Types.WORLD_SURFACE_WG
                )
        );

        context.register(SAND_ARENA,
                new JigsawStructure(
                        structure(
                                holderGetter.getOrThrow(HylianRaidsBiomeTags.HAS_SAND_ARENA),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.BEARD_THIN
                        ),
                        holderGetter2.getOrThrow(SandArenaPools.START),
                        7,
                        ConstantHeight.of(VerticalAnchor.absolute(0)),
                        false,
                        Heightmap.Types.WORLD_SURFACE_WG
                )
        );
    }
}
