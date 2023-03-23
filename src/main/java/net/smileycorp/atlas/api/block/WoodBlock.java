package net.smileycorp.atlas.api.block;

import java.util.function.Supplier;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
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
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.BoatRegistry;
import net.smileycorp.atlas.api.client.entity.AtlasBoatRenderer;
import net.smileycorp.atlas.api.item.AtlasBoatItem;


public class WoodBlock {

	protected final String name;
	protected final AbstractTreeGrower treeGrower;
	protected final BlockSetType type;
	protected final WoodType wood_type;
	protected final BoatRegistry.Type boat_type;
	protected final CreativeModeTab tab, decorations_tab;

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
		type = new BlockSetType(name);
		wood_type = WoodType.register(WoodType.register(new WoodType(name, type)));

		planks = register(blocks, () -> new Block(builder.constructBaseProperties()), "planks");
		registerItem(items, planks);
		stairs = register(blocks, () -> new StairBlock(planks.get().defaultBlockState(), builder.constructBaseProperties()), "stairs");
		registerItem(items, stairs);
		slab = register(blocks, () -> new SlabBlock(builder.constructBaseProperties()), "slab");
		registerItem(items, slab);
		stripped_log = register(blocks, () -> new RotatedPillarBlock(builder.constructBaseProperties()), "stripped", "log");
		registerItem(items, stripped_log);
		stripped_wood = register(blocks, () -> new RotatedPillarBlock(builder.constructPropertiesFrom(Properties.of(builder.woodMaterial, builder.barkColour))), "stripped", name.contains("wood") ? "" : "wood");
		registerItem(items, stripped_wood);
		log = register(blocks, () -> new RotatedPillarBlock(builder.constructLogProperties()) {
			@Override
			public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction tool, boolean simulate) {
				return tool == ToolActions.AXE_STRIP ? stripped_log.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
			}}, "log");
		registerItem(items, log);
		wood = register(blocks, () -> new RotatedPillarBlock(builder.constructPropertiesFrom(Properties.of(builder.woodMaterial, builder.barkColour))){
			@Override
			public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction tool, boolean simulate) {
				return tool == ToolActions.AXE_STRIP ? stripped_wood.get().defaultBlockState() : null;
			}}, name.contains("wood") ? "" : "wood");
		registerItem(items, wood);
		leaves = register(blocks, () -> new LeavesBlock(Properties.of(builder.leavesMaterial, builder.leavesColour).strength(0.2F).randomTicks().sound(builder.leavesSound).noOcclusion()
				.isValidSpawn(BlockUtils::jungleMob).isSuffocating(BlockUtils::never).isViewBlocking(BlockUtils::never)), "leaves");
		registerItem(items, leaves);
		sapling = builder.treeGrower == null ? null : register(blocks, () -> new SaplingBlock(builder.treeGrower, BlockBehaviour.Properties.of(Material.PLANT).noCollission()
				.noOcclusion().randomTicks().instabreak().sound(SoundType.GRASS)), "sapling");
		if (sapling!=null) registerItem(items, sapling);
		fence = register(blocks, () -> new FenceBlock(builder.constructBaseProperties()), "fence");
		registerItem(items, fence);
		fence_gate = register(blocks, () -> new FenceGateBlock(builder.constructBaseProperties(), wood_type), "fence_gate");
		registerItem(items, fence_gate);
		button = register(blocks, () -> new ButtonBlock(builder.constructBaseProperties().noCollission(), type, 30, true), "button");
		registerItem(items, button);
		pressure_plate = register(blocks, () -> new PressurePlateBlock(Sensitivity.EVERYTHING, builder.constructBaseProperties().noCollission(), type), "pressure_plate");
		registerItem(items, pressure_plate);
		door = register(blocks, () -> new DoorBlock(builder.constructBaseProperties().noOcclusion(), type), "door");
		registerItem(items, door);
		trapdoor = register(blocks, () -> new TrapDoorBlock(builder.constructBaseProperties().noOcclusion(), type), "trapdoor");
		registerItem(items, trapdoor);
		standing_sign = register(blocks, () -> new StandingSignBlock(builder.constructBaseProperties(), wood_type), "sign");
		wall_sign = register(blocks, () -> new WallSignBlock(builder.constructBaseProperties(), wood_type), "wall_sign");
		sign = register(items, () -> new SignItem(new Item.Properties().stacksTo(16), standing_sign.get(), wall_sign.get()), "sign");
		if (builder.hasBoat) {
			boat_type = BoatRegistry.INSTANCE.register(name, builder.modid, items, builder.decorations_tab);
		} else {
			boat_type = null;
		}
		tab = builder.tab;
		decorations_tab = builder.decorations_tab;
		MinecraftForge.EVENT_BUS.register(this);
	}

	protected void registerItem(DeferredRegister<Item> items, RegistryObject<Block> block) {
		items.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}

	protected <T> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> supplier, String suffix) {
		return register(registry, supplier, "", suffix);
	}

	@SubscribeEvent
	public void addCreative(CreativeModeTabEvent.BuildContents event) {
		if (event.getTab() == tab) {
			event.m_246326_(planks.get().asItem());
			event.m_246326_(stairs.get().asItem());
			event.m_246326_(slab.get().asItem());
			event.m_246326_(stripped_log.get().asItem());
			event.m_246326_(stripped_wood.get().asItem());
			event.m_246326_(log.get());
			event.m_246326_(wood.get());
		} else if (event.getTab() == decorations_tab) {
			event.m_246326_(leaves.get().asItem());
			if (sapling != null) event.m_246326_(sapling.get().asItem());
			event.m_246326_(fence.get().asItem());
			event.m_246326_(fence_gate.get().asItem());
			event.m_246326_(button.get().asItem());
			event.m_246326_(pressure_plate.get());
			event.m_246326_(door.get());
			event.m_246326_(trapdoor.get());
			event.m_246326_(sign.get());
		}
	}

	protected <T> RegistryObject<T> register(DeferredRegister<T> registry, Supplier<T> supplier, String prefix, String suffix) {
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
		return wood_type;
	}

	public Block getPlanks() {
		return planks.get();
	}

	public StairBlock getStairs() {
		return (StairBlock) stairs.get();
	}

	public SlabBlock getSlab() {
		return (SlabBlock) slab.get();
	}

	public RotatedPillarBlock getLog() {
		return (RotatedPillarBlock) log.get();
	}

	public RotatedPillarBlock getStrippedLog() {
		return (RotatedPillarBlock) stripped_log.get();
	}

	public RotatedPillarBlock getWood() {
		return (RotatedPillarBlock) wood.get();
	}

	public RotatedPillarBlock getStrippedWood() {
		return (RotatedPillarBlock) stripped_wood.get();
	}

	public LeavesBlock getLeaves() {
		return (LeavesBlock) leaves.get();
	}

	public SaplingBlock getSapling() {
		return sapling == null ? null : (SaplingBlock) sapling.get();
	}

	public FenceBlock getFence() {
		return (FenceBlock) fence.get();
	}

	public FenceGateBlock getFenceGate() {
		return (FenceGateBlock) fence_gate.get();
	}

	public ButtonBlock getButton() {
		return (ButtonBlock) button.get();
	}

	public PressurePlateBlock getPressurePlate() {
		return (PressurePlateBlock) pressure_plate.get();
	}

	public DoorBlock getDoor() {
		return (DoorBlock) door.get();
	}

	public TrapDoorBlock getTrapdoor() {
		return (TrapDoorBlock) trapdoor.get();
	}

	public SignBlock getSignBlock(boolean onWall) {
		return (SignBlock) (onWall ? wall_sign.get() : standing_sign.get());
	}

	public SignItem getSign() {
		return (SignItem) sign.get();
	}

	public AtlasBoatItem getBoat() {
		return (AtlasBoatItem) boat_type.getBoat();
	}

	@SuppressWarnings("removal")
	public void registerClient() {
		Sheets.addWoodType(wood_type);
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
