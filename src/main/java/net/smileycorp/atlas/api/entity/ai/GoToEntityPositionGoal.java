package net.smileycorp.atlas.api.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;

public class GoToEntityPositionGoal extends GoToPositionGoal {

	protected Entity target;

	public GoToEntityPositionGoal(MobEntity entity, Entity target) {
		super(entity, target.blockPosition());
		this.target=target;
	}

	@Override
	public void tick() {
		if (target!=null) {
			pos = target.blockPosition();
		}
	    super.tick();
    }

	@Override
	public boolean canContinueToUse() {
		return target!=null;
	}

}
