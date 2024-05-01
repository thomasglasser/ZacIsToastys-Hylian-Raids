package dev.thomasglasser.zacistoasty.hylianraids.world.entity;

import dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.behavior.FloatAboveGround;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.behavior.SummonMinions;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.ai.memory.HylianRaidsMemoryModuleTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableRangedAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRetaliateTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;

public class Ganon extends Monster implements PowerableMob, RangedAttackMob, SmartBrainOwner<Ganon>, GeoEntity
{
	public static final RawAnimation DIE = RawAnimation.begin().thenPlay("misc.die");

	public static final EntityDataAccessor<Boolean> POWERED = SynchedEntityData.defineId(Ganon.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Byte> FIREBALLS_RETURNED = SynchedEntityData.defineId(Ganon.class, EntityDataSerializers.BYTE);

	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	protected Ganon(EntityType<? extends Ganon> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		setInvulnerable(true);
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 600)
				.add(Attributes.MOVEMENT_SPEED, 0.23)
				.add(Attributes.ATTACK_DAMAGE, 15);
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(POWERED, false);
		entityData.define(FIREBALLS_RETURNED, (byte)0);
	}

	@Override
	public boolean isPowered() {
		return entityData.get(POWERED);
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		if (!this.isSilent()) {
			level().levelEvent(null, 1016, this.blockPosition(), 0);
		}

		double d0 = this.getX();
		double d1 = this.getY() + 1;
		double d2 = this.getZ();
		double d3 = pTarget.getX() - d0;
		double d4 = pTarget.getY() - d1 + 1;
		double d5 = pTarget.getZ() - d2;
		LargeFireball fireball = new LargeFireball(this.level(), this, d3, d4, d5, 1);
		fireball.setPosRaw(d0, d1, d2);
		this.level().addFreshEntity(fireball);
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
				new NearbyPlayersSensor<>(),
				new HurtBySensor<>()
		);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(DefaultAnimations.genericWalkIdleController(this));
		controllers.add(DefaultAnimations.genericAttackAnimation(this, DefaultAnimations.ATTACK_SWING));
		controllers.add(new AnimationController<>(this, "Die", 5, state -> {
			if (isDeadOrDying())
				return state.setAndContinue(DIE);

			state.getController().forceAnimationReset();

			return PlayState.STOP;
		}));
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
				new InvalidateAttackTarget<>(),
				new SetWalkTargetToAttackTarget<Ganon>().startCondition(Ganon::isPowered),
				new MoveToWalkTarget<Ganon>().startCondition(Ganon::isPowered),
				new LookAtTargetSink(40, 300), 														// Look at the look target
				new FloatToSurfaceOfFluid<>());																					// Move to the current walk target
	}

	@Override
	public BrainActivityGroup<? extends Ganon> getIdleTasks()
	{
		return BrainActivityGroup.idleTasks(
				new FirstApplicableBehaviour<>(                // Run only one of the below behaviours, trying each one in order. Include explicit generic typing because javac is silly
						new TargetOrRetaliate<>().useMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER),                        // Set the attack target
						new SetPlayerLookTarget<>(),                    // Set the look target to a nearby player if available
						new SetRandomLookTarget<>()),
				new OneRandomBehaviour<>( 								// Run only one of the below behaviours, picked at random
						new SetRandomWalkTarget<>().speedModifier(1), 				// Set the walk target to a nearby random pathable location
						new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)))); // Don't walk anywhere
	}

	// Phase 1
	public BrainActivityGroup<? extends Ganon> getPlayTasks()
	{
		return new BrainActivityGroup<Ganon>(Activity.PLAY).priority(10).behaviours(
				new FloatAboveGround<>().blocksAboveGround(3),
				new SummonMinions<>(EntityType.WITHER_SKELETON),
				new AnimatableRangedAttack<>(40).attackRadius(16)
		)
				.requireAndWipeMemoriesOnUse(MemoryModuleType.ATTACK_TARGET);
	}

	// Phase 2
	@Override
	public BrainActivityGroup<? extends Ganon> getFightTasks()
	{
		return BrainActivityGroup.<Ganon>fightTasks(
				new InvalidateAttackTarget<>().invalidateIf((ganon, target) ->
				{
					LivingEntity hurtBy = BrainUtils.getMemory(this, MemoryModuleType.HURT_BY_ENTITY);
					return hurtBy != null && !hurtBy.isDeadOrDying() && target != hurtBy;
				}),
				new SetRetaliateTarget<>(),
				new TargetOrRetaliate<>().useMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER),                        // Set the attack target
				new AnimatableMeleeAttack<>(10)
				).requireAndWipeMemoriesOnUse(MemoryModuleType.ATTACK_TARGET, HylianRaidsMemoryModuleTypes.POWERED.get());
	}

	@Override
	public Map<Activity, BrainActivityGroup<? extends Ganon>> getAdditionalTasks()
	{
		return Map.of(
				Activity.PLAY, getPlayTasks()
		);
	}

	@Override
	protected Brain.@NotNull Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
	}

	@Override
	public List<Activity> getActivityPriorities()
	{
		return ObjectArrayList.of(Activity.FIGHT, Activity.PLAY, Activity.IDLE);
	}

	@Override
	public void tick()
	{
		super.tick();
		if (isPowered())
			BrainUtils.setMemory(this, HylianRaidsMemoryModuleTypes.POWERED.get(), Unit.INSTANCE);
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount)
	{
		refreshDimensions();
		if (!entityData.get(POWERED))
		{
			if (pSource.getDirectEntity() instanceof LargeFireball && !pSource.is(DamageTypeTags.IS_EXPLOSION))
			{
				entityData.set(FIREBALLS_RETURNED, (byte) (entityData.get(FIREBALLS_RETURNED) + 1));
			}
			if (entityData.get(FIREBALLS_RETURNED) >= 3)
			{
				entityData.set(POWERED, true);
			}
			return false;
		}
		return super.hurt(pSource, pAmount);
	}

	@Override
	public int getCurrentSwingDuration() {
		int base = 20;
		if (MobEffectUtil.hasDigSpeed(this)) {
			return base - (1 + MobEffectUtil.getDigSpeedAmplification(this));
		} else {
			return this.hasEffect(MobEffects.DIG_SLOWDOWN) ? base + (1 + this.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2 : base;
		}
	}
}
