package net.smileycorp.atlas.api.client.colour;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ItemFoliageColour implements IItemColor {

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if (tintIndex==0) {
			return 0xFF74B265;
		}
		return Color.WHITE.getRGB();
	}

}
