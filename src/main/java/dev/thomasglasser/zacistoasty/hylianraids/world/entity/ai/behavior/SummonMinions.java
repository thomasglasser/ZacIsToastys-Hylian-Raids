package dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class SummonMinions<T extends LivingEntity> extends ExtendedBehaviour<T>
{
	protected EntityType<? extends LivingEntity> minionType;
	protected int numberPresent = 10;
	protected int nearbyMinions;

	public SummonMinions(EntityType<? extends LivingEntity> minionType)
	{
		super();
		this.minionType = minionType;
	}

	public SummonMinions<T> numberPresent(int number)
	{
		this.numberPresent = number;
		return this;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, T entity)
	{
		nearbyMinions = level.getEntities(EntityTypeTest.forClass(minionType.getBaseClass()), entity.getBoundingBox().inflate(64), e -> e.getType() == this.minionType).size();
		return nearbyMinions < numberPresent;
	}

	@Override
	protected void start(T entity)
	{
		for (int i = 0; i < numberPresent - nearbyMinions; i++)
		{
			LivingEntity minion = minionType.create(entity.level());
			if (minion != null)
			{
				minion.moveTo(entity.getX() + entity.getRandom().nextDouble() * 5 - 2.5, entity.getY(), entity.getZ() + entity.getRandom().nextDouble() * 5 - 2.5);
				entity.level().addFreshEntity(minion);
			}
		}
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements()
	{
		return List.of();
	}
}
