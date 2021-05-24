package net.smileycorp.atlas.api.entity.ai;


import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIGoToPos extends EntityAIFindEntityNearest {
	
	
	protected final EntityLiving entity;
	protected final World world;
	protected final BlockPos pos;
	protected final PathNavigate pather;
	protected int timeToRecalcPath = 0;
	protected float waterCost;
	
	public EntityAIGoToPos(EntityLiving entity, BlockPos pos) {
		super(entity, EntityLiving.class);
		this.entity=entity;
		world=entity.world;
		this.pos=world.getTopSolidOrLiquidBlock(pos);
		pather=entity.getNavigator();
		this.setMutexBits(1);
	}
	
	@Override
	public void startExecuting() {
		waterCost = entity.getPathPriority(PathNodeType.WATER);
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return true;
	}
	
	@Override
	public void resetTask() {
        pather.clearPath();
        entity.setPathPriority(PathNodeType.WATER, this.waterCost);
    }
	
	@Override
	public void updateTask() {
	    if (--this.timeToRecalcPath <= 0)  {
	        this.timeToRecalcPath = 5;
	        pather.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 1f);
        }
    }

}
