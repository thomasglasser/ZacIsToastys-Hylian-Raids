package dev.thomasglasser.zacistoasty.hylianraids.world.level.block;

import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.world.item.HylianRaidsItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class HylianRaidsBlocks
{
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HylianRaids.MOD_ID);

	public static final DeferredBlock<TrialSpawnerBlock> SUMMONER = register("summoner", () -> new TrialSpawnerBlock(Block.Properties.ofFullCopy(Blocks.TRIAL_SPAWNER).requiredFeatures()));

	private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> supplier)
	{
		DeferredBlock<T> block = BLOCKS.register(name, supplier);
		HylianRaidsItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
		return block;
	}
}
