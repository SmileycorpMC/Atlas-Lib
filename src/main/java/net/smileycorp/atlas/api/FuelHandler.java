package net.smileycorp.atlas.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

import java.util.HashMap;
import java.util.Map;

public class FuelHandler {

	public static FuelHandler INSTANCE = new FuelHandler();

	private Map<Item, Integer> FUEL_MAP = new HashMap<>();

	public ItemStack registerFuel(Block fuel, int burnTime) {
		if (fuel == null) return ItemStack.EMPTY;
		return registerFuel(fuel.asItem(), burnTime);
	}

	public ItemStack registerFuel(Item fuel, int burnTime) {
		if (fuel == null) return ItemStack.EMPTY;
		FUEL_MAP.put(fuel, burnTime);
		return new ItemStack(fuel);
	}

	public int getBurnTime(ItemStack fuel) {
		return FUEL_MAP.containsKey(fuel.getItem()) ? FUEL_MAP.get(fuel.getItem()) : 0;
	}

	@SubscribeEvent
	public void BurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
		int burnTime = getBurnTime(event.getItemStack());
		if (burnTime > 0) event.setBurnTime(burnTime);
	}

}
