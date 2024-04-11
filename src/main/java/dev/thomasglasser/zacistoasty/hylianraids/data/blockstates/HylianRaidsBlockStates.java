package dev.thomasglasser.zacistoasty.hylianraids.data.blockstates;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.world.level.block.HylianRaidsBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.function.TriFunction;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HylianRaidsBlockStates extends BlockStateProvider
{
	protected final Map<Block, BlockStateGenerator> STATE_MAP = Maps.newHashMap();
	protected final Map<ResourceLocation, Supplier<JsonElement>> MODEL_MAP = Maps.newHashMap();
	private final BlockModelGenerators blockModelGenerators;
	private final PackOutput output;

	public HylianRaidsBlockStates(PackOutput output, ExistingFileHelper exFileHelper)
	{
		super(output, HylianRaids.MOD_ID, exFileHelper);
		this.blockModelGenerators = makeBlockModelGenerators();
		this.output = output;
	}

	@Override
	protected void registerStatesAndModels()
	{
		createTrialSpawner(HylianRaidsBlocks.SUMMONER.get());
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		if (blockModelGenerators != null)
		{
			return CompletableFuture.allOf(super.run(cache), generateAll(cache));
		}
		return super.run(cache);
	}

	private BlockModelGenerators makeBlockModelGenerators()
	{
		Consumer<BlockStateGenerator> consumer = (p_125120_) -> {
			Block block = p_125120_.getBlock();
			BlockStateGenerator blockstategenerator = STATE_MAP.put(block, p_125120_);
			if (blockstategenerator != null) {
				throw new IllegalStateException("Duplicate blockstate definition for " + block);
			}
		};
		Set<Item> set = Sets.newHashSet();
		BiConsumer<ResourceLocation, Supplier<JsonElement>> biconsumer = (p_125123_, p_125124_) -> {
			Supplier<JsonElement> supplier = MODEL_MAP.put(p_125123_, p_125124_);
			if (supplier != null) {
				throw new IllegalStateException("Duplicate model definition for " + p_125123_);
			}
		};
		Consumer<Item> consumer1 = set::add;
		return new BlockModelGenerators(consumer, biconsumer, consumer1);
	}

	private void createTrialSpawner(Block block) {
		TextureMapping texturemapping = TextureMapping.trialSpawner(block, "_side_inactive", "_top_inactive");
		TextureMapping texturemapping1 = TextureMapping.trialSpawner(block, "_side_active", "_top_active");
		TextureMapping texturemapping2 = TextureMapping.trialSpawner(block, "_side_active", "_top_ejecting_reward");
		ResourceLocation resourcelocation = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.create(block, texturemapping, blockModelGenerators.modelOutput);
		ResourceLocation resourcelocation1 = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES.createWithSuffix(block, "_active", texturemapping1, blockModelGenerators.modelOutput);
		ResourceLocation resourcelocation2 = ModelTemplates.CUBE_BOTTOM_TOP_INNER_FACES
				.createWithSuffix(block, "_ejecting_reward", texturemapping2, blockModelGenerators.modelOutput);
		blockModelGenerators.delegateItemModel(block, resourcelocation);
		blockModelGenerators.blockStateOutput
				.accept(MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BlockStateProperties.TRIAL_SPAWNER_STATE).generate(p_311520_ -> switch(p_311520_) {
					case INACTIVE, COOLDOWN -> Variant.variant().with(VariantProperties.MODEL, resourcelocation);
					case WAITING_FOR_PLAYERS, ACTIVE, WAITING_FOR_REWARD_EJECTION -> Variant.variant().with(VariantProperties.MODEL, resourcelocation1);
					case EJECTING_REWARD -> Variant.variant().with(VariantProperties.MODEL, resourcelocation2);
				})));
	}

	public CompletableFuture<?> generateAll(CachedOutput cache)
	{
		List<CompletableFuture<?>> futures = new ArrayList<>();
		for (Map.Entry<ResourceLocation, Supplier<JsonElement>> entry : MODEL_MAP.entrySet()) {
			futures.add(DataProvider.saveStable(cache, entry.getValue().get().getAsJsonObject(), getPath(entry.getKey())));
		}
		for (Map.Entry<Block, BlockStateGenerator> entry : STATE_MAP.entrySet()) {
			futures.add(saveBlockState(cache, entry.getValue().get().getAsJsonObject(), entry.getKey()));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{}));
	}

	private CompletableFuture<?> saveBlockState(CachedOutput cache, JsonObject stateJson, Block owner) {
		ResourceLocation blockName = Preconditions.checkNotNull(key(owner));
		Path outputPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
				.resolve(blockName.getNamespace()).resolve("blockstates").resolve(blockName.getPath() + ".json");
		return DataProvider.saveStable(cache, stateJson, outputPath);
	}

	protected Path getPath(ResourceLocation loc) {
		return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(loc.getNamespace()).resolve("models").resolve(loc.getPath() + ".json");
	}

	protected ResourceLocation key(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block);
	}
}
