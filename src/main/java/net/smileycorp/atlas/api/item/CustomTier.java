package net.smileycorp.atlas.api.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class CustomTier implements Tier {

	private final int durability, level, enchantability;
	private final float speed, damage;
	private final Supplier<Ingredient> repairIngredient;

	public CustomTier(int durability, float speed, float damage, int level, int enchantability, Supplier<Ingredient> repairIngredient) {
		this.durability = durability;
		this.speed = speed;
		this.damage = damage;
		this.level = level;
		this.enchantability = enchantability;
		this.repairIngredient = repairIngredient;
	}

	@Override
	public int getUses() {
		return durability;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return damage;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairIngredient.get();
	}

}
