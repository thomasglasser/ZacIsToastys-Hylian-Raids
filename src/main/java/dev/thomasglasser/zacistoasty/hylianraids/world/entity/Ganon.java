package dev.thomasglasser.zacistoasty.hylianraids.world.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;

public class Ganon extends Monster implements PowerableMob, RangedAttackMob, SmartBrainOwner<Ganon>, GeoEntity
{
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	protected Ganon(EntityType<? extends Ganon> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	@Override
	public boolean isPowered() {
		return this.getHealth() <= this.getMaxHealth() / 2.0F;
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		if (!this.isSilent()) {
			this.level().levelEvent(null, 1024, this.blockPosition(), 0);
		}

		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		double d3 = pTarget.getX() - d0;
		double d4 = pTarget.getY() - d1;
		double d5 = pTarget.getZ() - d2;
		WitherSkull witherskull = new WitherSkull(this.level(), this, d3, d4, d5);
		witherskull.setOwner(this);
		witherskull.setDangerous(true);
		witherskull.setPosRaw(d0, d1, d2);
		this.level().addFreshEntity(witherskull);
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose)
	{
		if (isPowered())
			return EntityDimensions.scalable(1.5625F, 5.4375F);
		return super.getDimensions(pPose);
	}

	@Override
	protected Component getTypeName()
	{
		if (isPowered())
			return Component.translatable(((TranslatableContents)super.getTypeName().getContents()).getKey() + ".powered");
		return super.getTypeName();
	}

	@Override
	public List<? extends ExtendedSensor<? extends Ganon>> getSensors()
	{
		return List.of(
				new NearbyPlayersSensor<>()
		);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		// TODO
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	@Override
	public BrainActivityGroup<? extends Ganon> getCoreTasks()
	{
		return BrainActivityGroup.coreTasks(
				new SetWalkTargetToAttackTarget<>(),
				new MoveToWalkTarget<>(),
				new LookAtTargetSink(40, 300), 														// Look at the look target
				new FloatToSurfaceOfFluid<>());																					// Move to the current walk target
	}

	@Override
	public BrainActivityGroup<? extends Ganon> getIdleTasks()
	{
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<>(                // Run only one of the below behaviours, trying each one in order. Include explicit generic typing because javac is silly
						new TargetOrRetaliate<>(),                        // Set the attack target
						new SetPlayerLookTarget<>(),                    // Set the look target to a nearby player if available
						new SetRandomLookTarget<>()),
				new OneRandomBehaviour<>( 								// Run only one of the below behaviours, picked at random
						new SetRandomWalkTarget<>().speedModifier(1), 				// Set the walk target to a nearby random pathable location
						new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)))); // Don't walk anywhere
	}

	// Phase 1
	public BrainActivityGroup<? extends Ganon> getRestTasks()
	{
		return BrainActivityGroup.empty();
	}

	// Phase 2
	@Override
	public BrainActivityGroup<? extends Ganon> getFightTasks()
	{
		return BrainActivityGroup.empty();
	}

	@Override
	public Map<Activity, BrainActivityGroup<? extends Ganon>> getAdditionalTasks()
	{
		return Map.of(
				Activity.REST, getRestTasks()
		);
	}
}
