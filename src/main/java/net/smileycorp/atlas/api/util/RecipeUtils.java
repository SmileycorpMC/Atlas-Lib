package net.smileycorp.atlas.api.util;

import java.util.List;

import net.minecraft.item.ItemStack;

public class RecipeUtils {

    public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
    	return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
    
    public static boolean compareItemStacksCanFit(ItemStack stack1, ItemStack stack2) {
		if (compareItemStacks(stack1, stack2)) {
			if (stack1.getCount()+stack2.getCount()<=stack1.getMaxStackSize()) {
				return true;
			}
		}
    	return false;
	}
    
    public static boolean compareItemStacksWithSize(ItemStack stack1, ItemStack stack2) {
		if (compareItemStacks(stack1, stack2)) {
			if (stack1.getCount()==stack2.getCount()) {
				return true;
			}
		}
    	return false;
	}

	public static boolean canResultsFitInSlots(List<ItemStack> outputList, List<ItemStack> slots, int amount) {
		for (ItemStack out : outputList) {
			amount = compareSlots(out, slots, amount);
			if (amount<=0) {
				return true;
			}
		}
		return false;
	}
	
	private static int compareSlots(ItemStack out, List<ItemStack> slots, int amount) {
		for (ItemStack slot : slots) {
			if (compareItemStacksWithSize(out, slot)) {
				return amount--;
			}
		}
		for (ItemStack slot : slots) {
			if (slot.isEmpty()) {
				return amount--;
			}
		}
		return amount;
	}
}
