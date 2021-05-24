package net.smileycorp.atlas.api.entity.ai;


import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.google.common.base.Predicate;

public class EntityAIFindNearestTargetPredicate extends EntityAIFindEntityNearest {
	
	final Predicate<EntityLivingBase> targets;
	EntityLiving entity;
	
	public EntityAIFindNearestTargetPredicate(EntityLiving entity, Predicate<EntityLivingBase> predicate) {
		super(entity, EntityLiving.class);
		this.entity=entity;
		this.targets=predicate;
	}
	
	@Override
	public boolean shouldExecute() {
		if (super.shouldExecute()) {
	        double d0 = this.getFollowRange();
	        List<EntityLivingBase> list = entity.world.<EntityLivingBase>getEntitiesWithinAABB(EntityLiving.class, entity.getEntityBoundingBox().grow(d0, 4.0D, d0));
	        ReflectionHelper.setPrivateValue(EntityAIFindEntityNearest.class, this, list.get(0), "target");
	        	return true;
	    }
		return false;
    }
	

}
