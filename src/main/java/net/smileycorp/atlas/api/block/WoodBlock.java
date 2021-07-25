package net.smileycorp.atlas.api.block;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.PressurePlateBlock.Sensitivity;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WoodButtonBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.IForgeRegistry;



public class WoodBlock {
	
	protected final String name, modid;
	protected final CreativeModeTab tab, decorationsTab;
	protected final AbstractTreeGrower treeGrower;
	protected final WoodType sign_type;
	
	protected final Block planks;
	protected final StairBlock stairs;
	protected final SlabBlock slab;
	protected final RotatedPillarBlock log;
	protected final RotatedPillarBlock stripped_log;
	protected final Block wood;
	protected final Block stripped_wood;
	protected final LeavesBlock leaves;
	protected final SaplingBlock sapling;
	protected final FenceBlock fence;
	protected final FenceGateBlock fence_gate;
	protected final WoodButtonBlock button;
	protected final PressurePlateBlock pressure_plate;
	protected final DoorBlock door;
	protected final TrapDoorBlock trapdoor;
	protected final StandingSignBlock standing_sign;
	protected final WallSignBlock wall_sign;
	protected final SignItem sign;
	
	protected final List<Block> blocks;
	protected final List<Block> decorationBlocks;
	
	@SuppressWarnings("deprecation")
	protected WoodBlock(WoodBlockBuilder builder) {
		name = builder.name;
		modid = builder.modid;
		tab = builder.tab;
		decorationsTab = builder.decorationsTab;
		this.treeGrower = builder.treeGrower;
		sign_type = WoodType.register(WoodType.create(modid+"."+name));
		
		planks = init(new Block(builder.constructBaseProperties()), "planks");
		stairs = init(new StairBlock(planks.defaultBlockState(), builder.constructBaseProperties()), "stairs");
		slab = init(new SlabBlock(builder.constructBaseProperties()), "slab");
		log = init(new RotatedPillarBlock(builder.constructLogProperties()), "log");
		stripped_log = init(new RotatedPillarBlock(builder.constructBaseProperties()), "stripped", "log");
		wood = init(new Block(builder.constructPropertiesFrom(Properties.of(builder.woodMaterial, builder.barkColour))), "wood");
		stripped_wood = init(new Block(builder.constructPropertiesFrom(Properties.of(builder.woodMaterial, builder.barkColour))), "stripped", "wood");
		leaves = init(new LeavesBlock(Properties.of(builder.leavesMaterial, builder.leavesColour).strength(0.2F).randomTicks().sound(builder.leavesSound).noOcclusion()
				.isValidSpawn(BlockUtils::jungleMob).isSuffocating(BlockUtils::never).isViewBlocking(BlockUtils::never)), "leaves");
		if (builder.treeGrower == null) {
			sapling = null;
		} else {
			sapling = init(new SaplingBlock(builder.treeGrower, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)), "sapling");
		}
		fence = init(new FenceBlock(builder.constructBaseProperties()), "fence");
		fence_gate = init(new FenceGateBlock(builder.constructBaseProperties()), "fence_gate");
		button = init(new WoodButtonBlock(builder.constructBaseProperties()), "button");
		pressure_plate = init(new PressurePlateBlock(Sensitivity.EVERYTHING, builder.constructBaseProperties()), "pressure_plate");
		door = init(new DoorBlock(builder.constructBaseProperties()), "door");
		trapdoor = init(new TrapDoorBlock(builder.constructBaseProperties()), "trapdoor");
		standing_sign = init(new StandingSignBlock(builder.constructBaseProperties(), sign_type), "sign");
		wall_sign = init(new WallSignBlock(builder.constructBaseProperties(), sign_type), "wall_sign");
		sign = new SignItem(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_DECORATIONS), standing_sign, wall_sign);
		
		blocks = Arrays.asList(new Block[] {planks, stairs, slab, log, stripped_log, wood, stripped_wood});
		decorationBlocks = Arrays.asList(new Block[] {leaves, fence, fence_gate, button, pressure_plate, door, trapdoor, standing_sign, wall_sign});
		if (sapling!=null) decorationBlocks.add(sapling);
	}

	protected <T extends Block> T init(T block, String suffix) {
		return init(block, "", suffix);
	}

	protected <T extends Block> T init(T block, String prefix, String suffix) {
		StringBuilder builder = new StringBuilder();
		if (!prefix.isBlank()) builder.append(prefix + "_");
		builder.append(name.toLowerCase());
		if (!suffix.isBlank()) builder.append("_" + suffix);
		block.setRegistryName(new ResourceLocation(modid, builder.toString()));
		return block;
	}
	
	public String getName() {
		return name;
	}
	
	public String getModid() {
		return modid;
	}
	
	public ResourceLocation getRegistryName() {
		return new ResourceLocation(modid, name);
	}
	
	public CreativeModeTab getCreativeTab(boolean isDecoration) {
		return isDecoration ? tab : decorationsTab;
	}
	
	public AbstractTreeGrower getTreeGrower() {
		return treeGrower;
	}
	
	public WoodType getSignType() {
		return sign_type;
	}
	
	public Block getPlanks() {
		return planks;
	}
	
	public Block getStairs() {
		return stairs;
	}
	
	public Block getSlab() {
		return slab;
	}
	
	public Block getLog() {
		return log;
	}
	
	public Block getStrippedLog() {
		return stripped_log;
	}
	
	public Block getWood() {
		return wood;
	}
	
	public Block getStrippedWood() {
		return stripped_wood;
	}
	
	public Block getLeaves() {
		return leaves;
	}
	
	public Block getSapling() {
		return sapling;
	}
	
	public Block getFence() {
		return fence;
	}
	
	public Block getFenceGate() {
		return fence_gate;
	}
	
	public Block getButton() {
		return button;
	}
	
	public Block getPressurePlate() {
		return pressure_plate;
	}
	
	public Block getDoor() {
		return door;
	}
	
	public Block getTrapDoor() {
		return trapdoor;
	}
	
	public Block getSignBlock(boolean onWall) {
		return onWall ? wall_sign : standing_sign;
	}
	
	public Item getSign() {
		return sign;
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.registerAll(blocks.toArray(new Block[] {}));
		registry.registerAll(decorationBlocks.toArray(new Block[] {}));
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		for (Block block : blocks) {
			Item item = new BlockItem(block, new Item.Properties().tab(tab));
			item.setRegistryName(block.getRegistryName());
			registry.register(item);
		}
		for (Block block : decorationBlocks) {
			if (!(block instanceof SignBlock)) {
				Item item = new BlockItem(block, new Item.Properties().tab(decorationsTab));
				item.setRegistryName(block.getRegistryName());
				registry.register(item);
			}
		}
		registry.register(sign);
	}
	
	public void registerClient() {
		Sheets.addWoodType(sign_type);
	}
	
}
