package net.smileycorp.atlas.api.client.colour;

import java.awt.Color;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemFoliageColour implements IItemColor {

	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (tintIndex==0) {
			return 0xFF74B265;
		}
		return Color.WHITE.getRGB();
	}

}
