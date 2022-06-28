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

	protected final String name, modid;
	protected final CreativeModeTab tab;
	protected CreativeModeTab decorations_tab;
	protected Material wood_material = Material.WOOD;
	protected Material leaves_material = Material.GRASS;
	protected MaterialColor plank_colour = MaterialColor.WOOD;
	protected MaterialColor bark_colour = MaterialColor.PODZOL;
	protected MaterialColor leaves_colour = MaterialColor.GRASS;
	protected AbstractTreeGrower tree_grower = null;
	protected SoundType wood_sound = SoundType.WOOD;
	protected SoundType leaves_sound = SoundType.GRASS;
	protected float block_hardness = 2f;
	protected float explosion_resistance = 5f;
	protected boolean has_boat = false;
	protected boolean is_flammable = true;

	private WoodBlockBuilder(String name, String modid, CreativeModeTab tab, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		this.items = items;
		this.blocks = blocks;
		this.name = name;
		this.modid = modid;
		this.tab = tab;
		decorations_tab = tab;
	}

	public static WoodBlockBuilder of(String name, String modid, CreativeModeTab tab, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		return new WoodBlockBuilder(name, modid, tab, items, blocks);
	}

	public WoodBlockBuilder decorationsTab(CreativeModeTab tab) {
		decorations_tab = tab;
		return this;
	}

	public WoodBlockBuilder woodMaterial(Material mat) {
		wood_material = mat;
		return this;
	}

	public WoodBlockBuilder leavesMaterial(Material mat) {
		leaves_material = mat;
		return this;
	}

	public WoodBlockBuilder plankColour(MaterialColor colour) {
		plank_colour = colour;
		return this;
	}

	public WoodBlockBuilder barkColour(MaterialColor colour) {
		bark_colour = colour;
		return this;
	}

	public WoodBlockBuilder leavesColour(MaterialColor colour) {
		leaves_colour = colour;
		return this;
	}

	public WoodBlockBuilder treeGrower(AbstractTreeGrower tree) {
		tree_grower = tree;
		return this;
	}

	public WoodBlockBuilder woodSound(SoundType sound) {
		wood_sound = sound;
		return this;
	}

	public WoodBlockBuilder leavesSound(SoundType sound) {
		leaves_sound = sound;
		return this;
	}

	public WoodBlockBuilder blockHardness(float hardness) {
		block_hardness=hardness;
		return this;
	}

	public WoodBlockBuilder explosionResistance(float resistance) {
		explosion_resistance = resistance;
		return this;
	}

	public WoodBlockBuilder enableBoat() {
		has_boat = true;
		return this;
	}

	public WoodBlockBuilder isFireproof() {
		is_flammable = false;
		return this;
	}


	public WoodBlock build() {
		return new WoodBlock(this, items, blocks);
	}

	public Properties constructBaseProperties() {
		return constructPropertiesFrom(Properties.of(wood_material, plank_colour));
	}

	public Properties constructLogProperties() {
		return constructPropertiesFrom(Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? plank_colour : bark_colour));
	}

	public Properties constructPropertiesFrom(Properties base) {
		return base.sound(wood_sound).strength(block_hardness, explosion_resistance).requiresCorrectToolForDrops();
	}




}