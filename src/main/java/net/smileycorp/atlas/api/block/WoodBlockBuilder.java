package net.smileycorp.atlas.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;

public class WoodBlockBuilder {
	
	protected DeferredRegister<Item> items;
	protected DeferredRegister<Block> blocks;
	
	protected final String name;
	protected final CreativeModeTab tab;
	protected CreativeModeTab decorations_tab;
	protected Material woodMaterial = Material.WOOD;
	protected Material leavesMaterial = Material.GRASS;
	protected MaterialColor plankColour = MaterialColor.WOOD;
	protected MaterialColor barkColour = MaterialColor.PODZOL;
	protected MaterialColor leavesColour = MaterialColor.GRASS;
	protected AbstractTreeGrower treeGrower = null;
	protected SoundType woodSound = SoundType.WOOD;
	protected SoundType leavesSound = SoundType.GRASS;
	protected float blockHardness = 2f;
	protected float explosionResistance = 5f;
	
	private WoodBlockBuilder(String name, CreativeModeTab tab, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		this.items = items;
		this.blocks = blocks;
		this.name = name;
		this.tab = tab;
		this.decorations_tab = tab;
	}
	
	public static WoodBlockBuilder of(String name, CreativeModeTab tab, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		return new WoodBlockBuilder(name, tab, items, blocks);
	}
	
	public WoodBlockBuilder decorationsTab(CreativeModeTab tab) {
		decorations_tab = tab;
		return this;
	}
	
	public WoodBlockBuilder woodMaterial(Material mat) {
		woodMaterial = mat;
		return this;
	}
	
	public WoodBlockBuilder leavesMaterial(Material mat) {
		leavesMaterial = mat;
		return this;
	}
	
	public WoodBlockBuilder plankColour(MaterialColor colour) {
		plankColour = colour;
		return this;
	}
	
	public WoodBlockBuilder barkColour(MaterialColor colour) {
		barkColour = colour;
		return this;
	}
	
	public WoodBlockBuilder leavesColour(MaterialColor colour) {
		leavesColour = colour;
		return this;
	}
	
	public WoodBlockBuilder treeGrower(AbstractTreeGrower tree) {
		treeGrower = tree;
		return this;
	}
	
	public WoodBlockBuilder woodSound(SoundType sound) {
		woodSound = sound;
		return this;
	}
	
	public WoodBlockBuilder leavesSound(SoundType sound) {
		leavesSound = sound;
		return this;
	}
	
	public WoodBlockBuilder blockHardness(float hardness) {
		blockHardness=hardness;
		return this;
	}
	
	public WoodBlockBuilder explosionResistance(float resistance) {
		explosionResistance = resistance;
		return this;
	}
	
	public WoodBlock build() {
		return new WoodBlock(this, items, blocks);
	}
	
	public Properties constructBaseProperties() {
		return constructPropertiesFrom(Properties.of(woodMaterial, plankColour));
	}
	
	public Properties constructLogProperties() {
		return constructPropertiesFrom(Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? plankColour : barkColour));
	}

	public Properties constructPropertiesFrom(Properties base) {
		return base.sound(woodSound).strength(blockHardness, explosionResistance).requiresCorrectToolForDrops();
	}

	
	
	
}