package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.atlas.api.util.RecipeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FuelHandler implements IFuelHandler {

	private static FuelHandler INSTANCE = null;
	
	private Map<ItemStack, Integer> FUEL_MAP = new HashMap<>();
	
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
	
	public static FuelHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FuelHandler();
			GameRegistry.registerFuelHandler(INSTANCE);
		}
		return INSTANCE;
	}

}
