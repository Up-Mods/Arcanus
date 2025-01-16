package dev.cammiescorner.arcanus.fabric.common.entities.goals;

import dev.cammiescorner.arcanus.fabric.common.entities.Summon;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class FollowCasterGoal<T extends Mob & Summon> extends Goal {
	private static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
	private static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
	private static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
	private final T summon;
	private LivingEntity owner;
	private final LevelReader level;
	private final double speedModifier;
	private final PathNavigation navigation;
	private int timeToRecalcPath;
	private final float stopDistance;
	private final float startDistance;
	private float oldWaterCost;
	private final boolean canFly;

	public FollowCasterGoal(T summon, double speedModifier, float startDistance, float stopDistance, boolean canFly) {
		this.summon = summon;
		this.level = summon.level();
		this.speedModifier = speedModifier;
		this.navigation = summon.getNavigation();
		this.startDistance = startDistance;
		this.stopDistance = stopDistance;
		this.canFly = canFly;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));

		if(!(summon.getNavigation() instanceof GroundPathNavigation) && !(summon.getNavigation() instanceof FlyingPathNavigation))
			throw new IllegalArgumentException("Unsupported mob type for FollowCasterGoal");
	}

	public boolean canUse() {
		LivingEntity livingEntity = summon.getCaster();

		if(livingEntity == null)
			return false;
		else if(livingEntity.isSpectator())
			return false;
		else if(unableToMove())
			return false;
		else if(summon.distanceToSqr(livingEntity) < (double) (startDistance * startDistance))
			return false;
		else
			owner = livingEntity;

		return true;
	}

	public boolean canContinueToUse() {
		if(navigation.isDone())
			return false;
		else if(unableToMove())
			return false;
		else
			return !(summon.distanceToSqr(owner) <= (double) (stopDistance * stopDistance));
	}

	private boolean unableToMove() {
		return summon.isPassenger() || summon.isLeashed();
	}

	public void start() {
		timeToRecalcPath = 0;
		oldWaterCost = summon.getPathfindingMalus(BlockPathTypes.WATER);
		summon.setPathfindingMalus(BlockPathTypes.WATER, 0f);
	}

	public void stop() {
		owner = null;
		navigation.stop();
		summon.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);
	}

	public void tick() {
		summon.getLookControl().setLookAt(owner, 10f, (float) summon.getMaxHeadXRot());

		if(--timeToRecalcPath <= 0) {
			timeToRecalcPath = adjustedTickDelay(10);

			if(summon.distanceToSqr(owner) >= 144)
				teleportToOwner();
			else
				navigation.moveTo(owner, speedModifier);
		}
	}

	private void teleportToOwner() {
		BlockPos blockPos = owner.blockPosition();

		for(int i = 0; i < 10; ++i) {
			int j = randomIntInclusive(-MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING, MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING);
			int k = randomIntInclusive(-MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING, MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING);
			int l = randomIntInclusive(-MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING, MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING);
			boolean bl = maybeTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);

			if(bl)
				return;
		}

	}

	private boolean maybeTeleportTo(int x, int y, int z) {
		if(Math.abs((double) x - owner.getX()) < MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING && Math.abs((double) z - owner.getZ()) < MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING) {
			return false;
		}
		else if(!canTeleportTo(new BlockPos(x, y, z))) {
			return false;
		}
		else {
			summon.moveTo((double) x + 0.5, y, (double) z + 0.5, summon.getYRot(), summon.getXRot());
			navigation.stop();
			return true;
		}
	}

	private boolean canTeleportTo(BlockPos pos) {
		BlockPathTypes blockPathTypes = WalkNodeEvaluator.getBlockPathTypeStatic(level, pos.mutable());
		if(blockPathTypes != BlockPathTypes.WALKABLE) {
			return false;
		}
		else {
			BlockState blockState = level.getBlockState(pos.below());

			if(!canFly && blockState.getBlock() instanceof LeavesBlock) {
				return false;
			}
			else {
				BlockPos blockPos = pos.subtract(summon.blockPosition());
				return level.noCollision(summon, summon.getBoundingBox().move(blockPos));
			}
		}
	}

	private int randomIntInclusive(int min, int max) {
		return summon.getRandom().nextInt(max - min + 1) + min;
	}
}
