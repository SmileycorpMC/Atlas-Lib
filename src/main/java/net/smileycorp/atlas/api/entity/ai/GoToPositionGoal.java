package net.smileycorp.atlas.api.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.PathType;

import java.util.EnumSet;

public class GoToPositionGoal extends Goal  {

	protected final Mob entity;
	protected BlockPos pos;
	protected final PathNavigation pather;
	protected final double speed;
	protected int timeToRecalcPath = 0;
	protected float waterCost;

	public GoToPositionGoal(Mob entity, BlockPos pos) {
		this(entity, pos, 1);
	}

	public GoToPositionGoal(Mob entity, BlockPos pos, double speed) {
		super();
		this.entity = entity;
		this.pos = pos;
		pather = entity.getNavigation();
		this.speed = speed;
		setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void start() {
		waterCost = entity.getPathfindingMalus(PathType.WATER);
	}

	@Override
	public boolean canContinueToUse() {
		return true;
	}

	@Override
	public void stop() {
		pather.stop();
		entity.setPathfindingMalus(PathType.WATER, waterCost);
	}

	@Override
	public void tick() {
		if (--timeToRecalcPath <= 0)  {
			timeToRecalcPath = 10;
			pather.moveTo(pather.createPath(pos, 1), speed);
		}
	}

}
