package net.smileycorp.atlas.api.entity.ai;

import java.util.EnumSet;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GoToPositionGoal extends Goal  {


	protected final MobEntity entity;
	protected final World world;
	protected BlockPos pos;
	protected final PathNavigator pather;
	protected int timeToRecalcPath = 0;
	protected float waterCost;

	public GoToPositionGoal(MobEntity entity, BlockPos pos) {
		super();
		this.entity=entity;
		world=entity.level;
		this.pos=pos;
		pather=entity.getNavigation();
		setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void start() {
		waterCost = entity.getPathfindingMalus(PathNodeType.WATER);
	}

	@Override
	public boolean canContinueToUse() {
		return true;
	}

	@Override
	public void stop() {
        pather.stop();
        entity.setPathfindingMalus(PathNodeType.WATER, waterCost);
    }

	@Override
	public void tick() {
	    if (--this.timeToRecalcPath <= 0)  {
	        this.timeToRecalcPath = 5;
	        pather.createPath(pos, 1);
        }
    }

}
