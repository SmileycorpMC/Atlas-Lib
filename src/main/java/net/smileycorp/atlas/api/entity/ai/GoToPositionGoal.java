package net.smileycorp.atlas.api.entity.ai;


import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class GoToPositionGoal extends Goal  {
	
	
	protected final Mob entity;
	protected final Level world;
	protected BlockPos pos;
	protected final PathNavigation pather;
	protected int timeToRecalcPath = 0;
	protected float waterCost;
	
	public GoToPositionGoal(Mob entity, BlockPos pos) {
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
		waterCost = entity.getPathfindingMalus(BlockPathTypes.WATER);
	}
	
	@Override
	public boolean canContinueToUse() {
		return true;
	}
	
	@Override
	public void stop() {
        pather.stop();
        entity.setPathfindingMalus(BlockPathTypes.WATER, waterCost);
    }
	
	@Override
	public void tick() {
	    if (--this.timeToRecalcPath <= 0)  {
	        this.timeToRecalcPath = 5;
	        pather.createPath(pos, 1);
        }
    }

}
