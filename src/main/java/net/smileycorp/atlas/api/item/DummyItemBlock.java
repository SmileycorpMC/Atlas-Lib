package net.smileycorp.atlas.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class DummyItemBlock extends Item {
	
	public final Block block;
	
	//Used to store blocks in itemstacks before the block items are registered and injected into itemstacks
	public DummyItemBlock(Block block) {
		this.block=block;
	}
	
}
