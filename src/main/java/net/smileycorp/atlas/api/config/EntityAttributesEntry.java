package net.smileycorp.atlas.api.config;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraftforge.common.config.Configuration;

public class EntityAttributesEntry {

    private double movementSpeed;
    private double flyingSpeed;
    private double followRange;
    private double attackDamage;
    private double maxHealth;
    private double armor;
    private double armorToughness;
    private double knockbackResistance;

    public EntityAttributesEntry(Configuration config, String name, double movementSpeed, double followRange, double attackDamage, double maxHealth, double armor, double armorToughness, double knockbackResistance) {
        this(config, name, movementSpeed, followRange, attackDamage, maxHealth, armor, armorToughness, knockbackResistance, 0);
    }

    public EntityAttributesEntry(Configuration config, String name, double movementSpeed, double followRange, double attackDamage, double maxHealth, double armor, double armorToughness, double knockbackResistance, double flyingSpeed) {
        this.movementSpeed = config.get(name, "movementSpeed", movementSpeed, "Movement Speed").getDouble();
        this.followRange = config.get(name, "followRange", followRange, "Follow Range").getDouble();
        this.attackDamage = attackDamage == 0 ? 0 : config.get(name, "attackDamage", attackDamage, "Attack Damage").getDouble();
        this.maxHealth = config.get(name, "maxHealth", maxHealth, "Max Health").getDouble();
        this.armor = config.get(name, "armor", armor, "Armor").getDouble();
        this.armorToughness = config.get(name, "armorToughness", armorToughness, "Armor Toughness").getDouble();
        this.knockbackResistance = config.get(name, "knockbackResistance", knockbackResistance, "Knockback Resistance").getDouble();
        this.flyingSpeed = flyingSpeed == 0 ? 0 : config.get(name, "flyingSpeed", flyingSpeed, "Flying Speed").getDouble();
    }

    public void applyAttributes(EntityLivingBase entity) {
        AbstractAttributeMap map = entity.getAttributeMap();
        setAttribute(map, SharedMonsterAttributes.MOVEMENT_SPEED, movementSpeed);
        setAttribute(map, SharedMonsterAttributes.FOLLOW_RANGE, followRange);
        if (attackDamage != 0) setAttribute(map, SharedMonsterAttributes.ATTACK_DAMAGE, attackDamage);
        setAttribute(map, SharedMonsterAttributes.MAX_HEALTH, maxHealth);
        setAttribute(map, SharedMonsterAttributes.ARMOR, armor);
        setAttribute(map, SharedMonsterAttributes.ARMOR_TOUGHNESS, armorToughness);
        setAttribute(map, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, knockbackResistance);
        if (flyingSpeed != 0) setAttribute(map, SharedMonsterAttributes.FLYING_SPEED, flyingSpeed);
    }

    private void setAttribute(AbstractAttributeMap map, IAttribute attribute, double value) {
        if (map.getAttributeInstance(attribute) == null) map.registerAttribute(attribute);
        map.getAttributeInstance(attribute).setBaseValue(value);
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public double getFlyingSpeed() {
        return flyingSpeed;
    }

    public double getFollowRange() {
        return followRange;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getArmor() {
        return armor;
    }

    public double getArmorToughness() {
        return armorToughness;
    }

    public double getKnockbackResistance() {
        return knockbackResistance;
    }
    
}
