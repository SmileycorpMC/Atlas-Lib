package net.smileycorp.atlas.api.client.colour;

import java.awt.Color;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public class FoliageItemColour implements ItemColor {

	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (tintIndex==0) {
			return 0xFF74B265;
		}
		return Color.WHITE.getRGB();
	}

}
