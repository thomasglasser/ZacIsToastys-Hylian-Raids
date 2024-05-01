package dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.behavior;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class FloatAboveGround<T extends LivingEntity> extends ExtendedBehaviour<T>
{
	protected int blocksAboveGround = 1;

	public FloatAboveGround<T> blocksAboveGround(int blocksAboveGround)
	{
		this.blocksAboveGround = blocksAboveGround;
		return this;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, T entity)
	{
		return entity.onGround() || !level.getBlockState(entity.blockPosition().below(blocksAboveGround)).isAir();
	}

	@Override
	protected boolean shouldKeepRunning(T entity)
	{
		return checkExtraStartConditions((ServerLevel) entity.level(), entity);
	}

	@Override
	protected void tick(T entity)
	{
		entity.resetFallDistance();
		entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.1, 0));
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements()
	{
		return List.of();
	}
}
