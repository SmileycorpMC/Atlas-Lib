package net.smileycorp.atlas.api.client.colour;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class FoliageItemColour implements ItemColor {

	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		return tintIndex == 0 ? 0xFF74B265 : 0xFFFFFFFF;
	}

}
