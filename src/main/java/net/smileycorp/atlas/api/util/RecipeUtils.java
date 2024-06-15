package net.smileycorp.atlas.api.util;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class RecipeUtils {

    public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2, boolean useNBT) {
    	if (stack1==null||stack2==null) return false;
    	if (stack2.getItem() == stack1.getItem() && (stack2.getDamageValue() == stack1.getDamageValue()))  {
    		DataComponentMap nbt1 = stack1.getComponents();
    		DataComponentMap nbt2 = stack2.getComponents();
    		if (nbt1 == null || nbt2 == null) return nbt1 == nbt2;
    		return nbt1.equals(nbt2)||!useNBT;
    	}
    	return false;
    }
    
    public static boolean compareItemStacksCanFit(ItemStack stack1, ItemStack stack2) {
    	if (stack1.isEmpty() || stack2.isEmpty()) return true;
		return compareItemStacks(stack1, stack2, true) &&
				stack1.getCount() + stack2.getCount() <= stack1.getMaxStackSize();
	}
    
    /*checks to see if stack2 is bigger than stack 1, if matchExactly is true, only checks that they are the same size*/
    public static boolean compareItemStacksWithSize(ItemStack stack1, ItemStack stack2, boolean matchExactly) {
		return compareItemStacks(stack1, stack2, true) &&
				(stack1.getCount() == stack2.getCount() || (!matchExactly && stack1.getCount()<stack2.getCount()));
	}

	public static boolean canResultsFitInSlots(List<ItemStack> outputList, List<ItemStack> slots, int amount) {
		for (ItemStack out : outputList) {
			amount = compareSlots(out, slots, amount);
			if (amount <= 0) return true;
		}
		return false;
	}
	
	private static int compareSlots(ItemStack out, List<ItemStack> slots, int amount) {
		for (ItemStack slot : slots) if (compareItemStacksCanFit(out, slot)) return amount--;
		for (ItemStack slot : slots) if (slot.isEmpty()) return amount--;
		return amount;
	}
}
