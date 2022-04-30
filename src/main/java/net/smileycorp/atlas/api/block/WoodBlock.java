package net.smileycorp.atlas.api.block;

import java.util.function.Supplier;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.PressurePlateBlock.Sensitivity;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.BoatRegistry;
import net.smileycorp.atlas.api.client.entity.AtlasBoatRenderer;



public class WoodBlock {

	protected final String name;
	protected final AbstractTreeGrower treeGrower;
	protected final WoodType sign_type;
	protected final BoatRegistry.Type boat_type;

	protected final RegistryObject<Block> planks;
	protected final RegistryObject<Block> stairs;
	protected final RegistryObject<Block> slab;
	protected final RegistryObject<Block> log;
	protected final RegistryObject<Block> stripped_log;
	protected final RegistryObject<Block> wood;
	protected final RegistryObject<Block> stripped_wood;
	protected final RegistryObject<Block> leaves;
	protected final RegistryObject<Block> sapling;
	protected final RegistryObject<Block> fence;
	protected final RegistryObject<Block> fence_gate;
	protected final RegistryObject<Block> button;
	protected final RegistryObject<Block> pressure_plate;
	protected final RegistryObject<Block> door;
	protected final RegistryObject<Block> trapdoor;
	protected final RegistryObject<Block> standing_sign;
	protected final RegistryObject<Block> wall_sign;
	protected final RegistryObject<Item> sign;

	@SuppressWarnings("deprecation")
	protected WoodBlock(WoodBlockBuilder builder, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		name = builder.name;
		treeGrower = builder.treeGrower;
		sign_type = WoodType.register(WoodType.create(name));

		planks = register(blocks, () -> new Block(builder.constructBaseProperties()), "planks");
		registerItem(items, planks, builder.tab);
		stairs = register(blocks, () -> new StairBlock(planks.get().defaultBlockState(), builder.constructBaseProperties()), "stairs");
		registerItem(items, stairs, builder.tab);
		slab = register(blocks, () -> new SlabBlock(builder.constructBaseProperties()), "slab");
		registerItem(items, slab, builder.tab);
		stripped_log = register(blocks, () -> new RotatedPillarBlock(builder.constructBaseProperties()), "stripped", "log");
		registerItem(items, stripped_log, builder.tab);
		stripped_wood = register(blocks, () -> new RotatedPillarBlock(builder.constructPropertiesFrom(Properties.of(builder.woodMaterial, builder.barkColour))), "stripped", name.contains("wood") ? "" : "wood");
		registerItem(items, stripped_wood, builder.tab);
		log = register(blocks, () -> new RotatedPillarBlock(builder.constructLogProperties()) {
			@Override
			public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction tool) {
				return tool == ToolActions.AXE_STRIP ? stripped_log.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
			}}, "log");
		registerItem(items, log, builder.tab);
		wood = register(blocks, () -> new RotatedPillarBlock(builder.constructPropertiesFrom(Properties.of(builder.woodMaterial, builder.barkColour))){
			@Override
			public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction tool) {
				return tool == ToolActions.AXE_STRIP ? stripped_wood.get().defaultBlockState() : null;
			}}, name.contains("wood") ? "" : "wood");
		registerItem(items, wood, builder.tab);
		leaves = register(blocks, () -> new LeavesBlock(Properties.of(builder.leavesMaterial, builder.leavesColour).strength(0.2F).randomTicks().sound(builder.leavesSound).noOcclusion()
				.isValidSpawn(BlockUtils::jungleMob).isSuffocating(BlockUtils::never).isViewBlocking(BlockUtils::never)), "leaves");
		registerItem(items, leaves, builder.decorations_tab);
		sapling = builder.treeGrower == null ? null : register(blocks, () -> new SaplingBlock(builder.treeGrower, BlockBehaviour.Properties.of(Material.PLANT).noCollission()
				.noOcclusion().randomTicks().instabreak().sound(SoundType.GRASS)), "sapling");
		if (sapling!=null) registerItem(items, sapling, builder.decorations_tab);
		fence = register(blocks, () -> new FenceBlock(builder.constructBaseProperties()), "fence");
		registerItem(items, fence, builder.decorations_tab);
		fence_gate = register(blocks, () -> new FenceGateBlock(builder.constructBaseProperties()), "fence_gate");
		registerItem(items, fence_gate, builder.decorations_tab);
		button = register(blocks, () -> new WoodButtonBlock(builder.constructBaseProperties()), "button");
		registerItem(items, button, builder.decorations_tab);
		pressure_plate = register(blocks, () -> new PressurePlateBlock(Sensitivity.EVERYTHING, builder.constructBaseProperties()), "pressure_plate");
		registerItem(items, pressure_plate, builder.decorations_tab);
		door = register(blocks, () -> new DoorBlock(builder.constructBaseProperties().noOcclusion()), "door");
		registerItem(items, door, builder.decorations_tab);
		trapdoor = register(blocks, () -> new TrapDoorBlock(builder.constructBaseProperties().noOcclusion()), "trapdoor");
		registerItem(items, trapdoor, builder.decorations_tab);
		standing_sign = register(blocks, () -> new StandingSignBlock(builder.constructBaseProperties(), sign_type), "sign");
		wall_sign = register(blocks, () -> new WallSignBlock(builder.constructBaseProperties(), sign_type), "wall_sign");
		sign = register(items, () -> new SignItem(new Item.Properties().stacksTo(16).tab(builder.decorations_tab), standing_sign.get(), wall_sign.get()), "sign");
		if (builder.hasBoat) {
			boat_type = BoatRegistry.INSTANCE.register(name, builder.modid, items, builder.decorations_tab);
		} else {
			boat_type = null;
		}
	}

	protected void registerItem(DeferredRegister<Item> items, RegistryObject<Block> block, CreativeModeTab tab) {
		items.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}

	protected <T extends IForgeRegistryEntry<T>> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> supplier, String suffix) {
		return register(registry, supplier, "", suffix);
	}

	protected <T extends IForgeRegistryEntry<T>> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> supplier, String prefix, String suffix) {
		StringBuilder builder = new StringBuilder();
		if (!prefix.isBlank()) builder.append(prefix + "_");
		builder.append(name.toLowerCase());
		if (!suffix.isBlank()) builder.append("_" + suffix);
		return registry.register(builder.toString(), supplier);
	}

	public String getName() {
		return name;
	}

	public AbstractTreeGrower getTreeGrower() {
		return treeGrower;
	}

	public WoodType getSignType() {
		return sign_type;
	}

	public Block getPlanks() {
		return planks.get();
	}

	public Block getStairs() {
		return stairs.get();
	}

	public Block getSlab() {
		return slab.get();
	}

	public Block getLog() {
		return log.get();
	}

	public Block getStrippedLog() {
		return stripped_log.get();
	}

	public Block getWood() {
		return wood.get();
	}

	public Block getStrippedWood() {
		return stripped_wood.get();
	}

	public Block getLeaves() {
		return leaves.get();
	}

	public Block getSapling() {
		return sapling == null ? null : sapling.get();
	}

	public Block getFence() {
		return fence.get();
	}

	public Block getFenceGate() {
		return fence_gate.get();
	}

	public Block getButton() {
		return button.get();
	}

	public Block getPressurePlate() {
		return pressure_plate.get();
	}

	public Block getDoor() {
		return door.get();
	}

	public Block getTrapdoor() {
		return trapdoor.get();
	}

	public Block getSignBlock(boolean onWall) {
		return onWall ? wall_sign.get() : standing_sign.get();
	}

	public Item getSign() {
		return sign.get();
	}

	public Item getBoat() {
		return boat_type.getItem();
	}

	public void registerClient() {
		Sheets.addWoodType(sign_type);
		ItemBlockRenderTypes.setRenderLayer(getDoor(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(getTrapdoor(), RenderType.cutoutMipped());
		ItemBlockRenderTypes.setRenderLayer(getSapling(), RenderType.cutoutMipped());
		if (boat_type!=null) if (!AtlasBoatRenderer.isRegistered()) AtlasBoatRenderer.register();
	}

	public void registerStandardFuelValues() {
		FuelHandler.INSTANCE.registerFuel(getPlanks(), 300);
		FuelHandler.INSTANCE.registerFuel(getStairs(), 300);
		FuelHandler.INSTANCE.registerFuel(getSlab(), 150);
		FuelHandler.INSTANCE.registerFuel(getLog(), 300);
		FuelHandler.INSTANCE.registerFuel(getStrippedLog(), 300);
		FuelHandler.INSTANCE.registerFuel(getWood(), 300);
		FuelHandler.INSTANCE.registerFuel(getStrippedWood(), 300);
		FuelHandler.INSTANCE.registerFuel(getSapling(), 300);
		FuelHandler.INSTANCE.registerFuel(getFence(), 300);
		FuelHandler.INSTANCE.registerFuel(getFenceGate(), 300);
		FuelHandler.INSTANCE.registerFuel(getButton(), 100);
		FuelHandler.INSTANCE.registerFuel(getPressurePlate(), 300);
		FuelHandler.INSTANCE.registerFuel(getDoor(), 200);
		FuelHandler.INSTANCE.registerFuel(getTrapdoor(), 300);
		FuelHandler.INSTANCE.registerFuel(getSign(), 200);
	}

}
