package net.smileycorp.atlas.api.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class FlyingMoveControl extends EntityMoveHelper {

    private int ticksStuck = 0;
    
    public FlyingMoveControl(EntityLiving entity) {
        super(entity);
    }

    public void onUpdateMoveHelper() {
        if (action != Action.MOVE_TO) return;
        if (entity.posX == entity.prevPosX && entity.posY == entity.prevPosY && entity.posZ == entity.prevPosZ) {
            if (ticksStuck++ >= 40) {
                action = Action.WAIT;
                return;
            }
        } else ticksStuck = 0;
        double d0 = posX - entity.posX;
        double d1 = posY - entity.posY;
        double d2 = posZ - entity.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        d3 = MathHelper.sqrt(d3);
        if (d3 < entity.getEntityBoundingBox().getAverageEdgeLength()) {
            action = Action.WAIT;
            entity.motionX *= 0.5;
            entity.motionY *= 0.5;
            entity.motionZ *= 0.5;
        } else {
            double speed = this.speed * entity.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).getAttributeValue();
            entity.motionX += d0 / d3 * speed;
            entity.motionY += d1 / d3 * speed;
            entity.motionZ += d2 / d3 * speed;
            if (entity.getAttackTarget() == null) entity.rotationYaw = -((float)MathHelper.atan2(entity.motionX, entity.motionZ)) * 57.295776F;
            else {
                double d4 = entity.getAttackTarget().posX - entity.posX;
                double d5 = entity.getAttackTarget().posZ - entity.posZ;
                entity.rotationYaw = -((float)MathHelper.atan2(d4, d5)) * 57.295776F;
            }
            entity.renderYawOffset = entity.rotationYaw;
        }
    }
    
}
