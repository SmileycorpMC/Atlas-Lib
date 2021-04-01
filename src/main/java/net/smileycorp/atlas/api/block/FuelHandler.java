package net.smileycorp.atlas.api.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.IFuelHandler;

import net.smileycorp.atlas.api.util.RecipeUtils;

public class FuelHandler implements IFuelHandler {

	public static final FuelHandler INSTANCE = new FuelHandler();
	
	private Map<ItemStack, Integer> FUEL_MAP = new HashMap<ItemStack, Integer>();
	
	public ItemStack registerFuel(Item fuel, int burnTime) {
		return registerFuel(new ItemStack(fuel), burnTime);
	}
	
	public ItemStack registerFuel(Block fuel, int burnTime) {
		return registerFuel(new ItemStack(fuel), burnTime);
	}
	
	public ItemStack registerFuel(ItemStack fuel, int burnTime) {
		FUEL_MAP.put(fuel, burnTime);
		return fuel;
	}	
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		for (Entry<ItemStack, Integer> entry : FUEL_MAP.entrySet()) {
			if (RecipeUtils.compareItemStacks(fuel, entry.getKey(), false)) {
				return entry.getValue();
			}
		}
		return 0;
	}

}
