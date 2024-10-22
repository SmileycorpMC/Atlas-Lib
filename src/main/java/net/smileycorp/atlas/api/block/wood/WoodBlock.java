package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.item.ItemBlockMeta;

import java.util.List;

public class WoodBlock<T extends Enum<T> & WoodEnum> {

	private final List<BlockBasePlank> planks = Lists.newArrayList();

	final List<Block> blocks = Lists.newArrayList();

	public WoodBlock(String modid, CreativeTabs tab, Class<T> types,  boolean isFlamable) {
		int size = types.getEnumConstants().length;
		for (int i = 0; i <= (size - 1) / 16; i++) {
		
		}
	}

	public ItemStack getPlankStack(T type, int amount) {
		return new ItemStack(planks.get(type.ordinal() / 16), amount, type.ordinal() % 16);
	}
	
}
