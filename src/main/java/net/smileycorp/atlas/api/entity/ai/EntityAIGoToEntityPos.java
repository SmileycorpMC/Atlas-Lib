package net.smileycorp.atlas.api.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntityAIGoToEntityPos extends EntityAIGoToPos {
	
	protected Entity target;
	
	public EntityAIGoToEntityPos(EntityLiving entity, Entity target) {
		super(entity, target.getPosition());
		this.target=target;
	}
	
	@Override
	public void updateTask() {
		if (target!=null) {
			pos = target.getPosition();
		}
	    super.updateTask();
    }
	
	@Override
	public boolean shouldContinueExecuting() {
		return target!=null;
	}

}
