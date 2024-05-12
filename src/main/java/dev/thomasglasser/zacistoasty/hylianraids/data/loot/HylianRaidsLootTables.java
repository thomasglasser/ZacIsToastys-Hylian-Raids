package dev.thomasglasser.zacistoasty.hylianraids.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HylianRaidsLootTables extends LootTableProvider
{
    public HylianRaidsLootTables(PackOutput packOutput)
    {
        super(packOutput, Set.of(), List.of(
                new SubProviderEntry(HylianRaidsChestLoot::new, LootContextParamSets.CHEST)));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext)
    {
        map.forEach((location, lootTable) -> lootTable.validate(validationcontext));
    }
}
