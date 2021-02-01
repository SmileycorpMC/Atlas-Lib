package net.smileycorp.atlas.api.util;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.util.recipe.OreInput;
import net.smileycorp.atlas.api.util.recipe.OreStack;

public class OreDictUtils {
	
	public static OreInput[] toOreInput(ItemStack... stacks) {
		OreInput[] result = {};
		for (ItemStack stack : stacks) {
			ArrayUtils.add(result, new OreInput(stack));
		}
		return result;
	}
	
	public static OreInput[] toOreInput(OreStack... stacks) {
		OreInput[] result = {};
		for (OreStack stack : stacks) {
			ArrayUtils.add(result, new OreInput(stack));
		}
		return result;
	}
}
