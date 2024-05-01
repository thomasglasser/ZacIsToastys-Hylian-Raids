package dev.thomasglasser.zacistoasty.hylianraids.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.thomasglasser.zacistoasty.hylianraids.HylianRaids;
import dev.thomasglasser.zacistoasty.hylianraids.world.entity.Ganon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GanonRenderer extends GeoEntityRenderer<Ganon>
{
	private final GeoModel<Ganon> poweredModel = new DefaultedEntityGeoModel<>(HylianRaids.modLoc("ganon_beast"));

	public GanonRenderer(EntityRendererProvider.Context renderManager)
	{
		super(renderManager, new DefaultedEntityGeoModel<>(HylianRaids.modLoc("ganondorf"))
		{
			@Override
			public Animation getAnimation(Ganon animatable, String name)
			{
				return null;
			}
		});
	}

	@Override
	public GeoModel<Ganon> getGeoModel()
	{
		if (animatable.isPowered())
		{
			if (scaleHeight != 1.0F || scaleWidth != 1.0F)
				withScale(1.0F);
			return poweredModel;
		}
		if (scaleHeight != 1.2F || scaleWidth != 1.2F)
			withScale(1.2F);
		return super.getGeoModel();
	}

	@Override
	protected float getDeathMaxRotation(Ganon animatable)
	{
		return 0;
	}

	// TODO: Remove once model bug fixed
	@Override
	public void actuallyRender(PoseStack poseStack, Ganon animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.pushPose();

		boolean shouldSit = animatable.isPassenger() && (animatable.getVehicle() != null && animatable.getVehicle().shouldRiderSit());
		float lerpBodyRot = Mth.rotLerp(partialTick, animatable.yBodyRotO, animatable.yBodyRot);
		float lerpHeadRot = Mth.rotLerp(partialTick, animatable.yHeadRotO, animatable.yHeadRot);
		float netHeadYaw = lerpHeadRot - lerpBodyRot;

		if (shouldSit && animatable.getVehicle() instanceof LivingEntity livingentity) {
			lerpBodyRot = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
			netHeadYaw = lerpHeadRot - lerpBodyRot;
			float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85, 85);
			lerpBodyRot = lerpHeadRot - clampedHeadYaw;

			if (clampedHeadYaw * clampedHeadYaw > 2500f)
				lerpBodyRot += clampedHeadYaw * 0.2f;

			netHeadYaw = lerpHeadRot - lerpBodyRot;
		}

		if (animatable.getPose() == Pose.SLEEPING) {
			Direction bedDirection = animatable.getBedOrientation();

			if (bedDirection != null) {
				float eyePosOffset = animatable.getEyeHeight(Pose.STANDING) - 0.1F;

				poseStack.translate(-bedDirection.getStepX() * eyePosOffset, 0, -bedDirection.getStepZ() * eyePosOffset);
			}
		}

		float ageInTicks = animatable.tickCount + partialTick;
		float limbSwingAmount = 0;
		float limbSwing = 0;

		applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);

		if (!shouldSit && animatable.isAlive() && animatable != null) {
			limbSwingAmount = animatable.walkAnimation.speed(partialTick);
			limbSwing = animatable.walkAnimation.position(partialTick);

			if (animatable.isBaby())
				limbSwing *= 3f;

			if (limbSwingAmount > 1f)
				limbSwingAmount = 1f;
		}

		if (!isReRender) {
			float headPitch = Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot());
			float motionThreshold = getMotionAnimThreshold(animatable);
			Vec3 velocity = animatable.getDeltaMovement();
			float avgVelocity = (float)(Math.abs(velocity.x) + Math.abs(velocity.z)) / 2f;
			AnimationState<Ganon> animationState = new AnimationState<>(animatable, limbSwing, limbSwingAmount, partialTick, avgVelocity >= motionThreshold && limbSwingAmount != 0);
			long instanceId = getInstanceId(animatable);

			animationState.setData(DataTickets.TICK, animatable.getTick(animatable));
			animationState.setData(DataTickets.ENTITY, animatable);
			animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, animatable != null && animatable.isBaby(), -netHeadYaw, -headPitch));
			getGeoModel().addAdditionalStateData(animatable, instanceId, animationState::setData);
			getGeoModel().handleAnimations(animatable, instanceId, animationState);
		}

		poseStack.translate(0, 0.01f, 0);

		this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

		if (animatable.isInvisibleTo(Minecraft.getInstance().player)) {
			if (Minecraft.getInstance().shouldEntityAppearGlowing(animatable)) {
				buffer = bufferSource.getBuffer(renderType = RenderType.outline(getTextureLocation(animatable)));
			}
			else {
				renderType = null;
			}
		}

		if (renderType != null)
		{

			updateAnimatedTextureFrame(animatable);

			for (GeoBone group : model.topLevelBones()) {
				renderRecursively(poseStack, animatable, group, renderType, bufferSource, buffer, isReRender, partialTick, packedLight,
						packedOverlay, red, green, blue, alpha);
			}
		}

		poseStack.popPose();
	}
}
