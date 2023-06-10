package net.smileycorp.atlas.api.block;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.PressurePlateBlock.Sensitivity;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.BoatRegistry;
import net.smileycorp.atlas.api.client.entity.AtlasBoatRenderer;
import net.smileycorp.atlas.api.item.AtlasBoatItem;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;



public class WoodBlock {

	protected final String name;
	protected final boolean isFlammable;

	protected final CreativeModeTab tab, decorations_tab;

	protected final TagKey<Item> logs_item_tag;
	protected final TagKey<Block> logs_block_tag;

	protected final AbstractTreeGrower tree_grower;
	protected final BlockSetType type;
	protected final WoodType wood_type;
	protected final BoatRegistry.Type boat_type;

	protected final Map<WoodShape, RegistryObject<Block>> BLOCKS = Maps.newHashMap();
	protected final Optional<RegistryObject<Item>> sign;

	@SuppressWarnings("deprecation")
	protected WoodBlock(WoodBlockBuilder builder, DeferredRegister<Item> items, DeferredRegister<Block> blocks) {
		name = builder.name;
		isFlammable = builder.is_flammable;
		tab = builder.tab;
		decorations_tab = builder.decorations_tab;
		logs_item_tag = ItemTags.create(new ResourceLocation(items.getRegistryName().getNamespace(), name + "_logs"));
		logs_block_tag = BlockTags.create(new ResourceLocation(blocks.getRegistryName().getNamespace(), name + "_logs"));
		tree_grower = builder.tree_grower;
		type = new BlockSetType(name);
		wood_type = WoodType.register(WoodType.register(new WoodType(name, type)));

		register(items, blocks, () -> new Block(builder.constructBaseProperties()), WoodShape.PLANKS);
		register(items, blocks, () -> new StairBlock(get(WoodShape.PLANKS).defaultBlockState(), builder.constructBaseProperties()), WoodShape.STAIRS);
		register(items, blocks, () -> new SlabBlock(builder.constructBaseProperties()), WoodShape.SLAB);
		register(items, blocks, () -> new RotatedPillarBlock(builder.constructBaseProperties().instrument(NoteBlockInstrument.BASS)), WoodShape.STRIPPED_LOG);
		register(items, blocks, () -> new RotatedPillarBlock(builder.constructBaseProperties().instrument(NoteBlockInstrument.BASS)), WoodShape.STRIPPED_WOOD);
		register(items, blocks, () -> new RotatedPillarBlock(builder.constructLogProperties().instrument(NoteBlockInstrument.BASS)) {
			@Override
			public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction tool, boolean simulate) {
				return tool == ToolActions.AXE_STRIP ? getStrippedLog().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
			}}, WoodShape.LOG);
		register(items, blocks, () -> new RotatedPillarBlock(builder.constructPropertiesFrom(Properties.of().mapColor(builder.bark_colour)).instrument(NoteBlockInstrument.BASS)){
			@Override
			public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction tool, boolean simulate) {
				return tool == ToolActions.AXE_STRIP ? getStrippedWood().defaultBlockState() : null;
			}}, WoodShape.WOOD);
		register(items, blocks, () -> new LeavesBlock(Properties.of().mapColor(builder.leaves_colour).strength(0.2F).randomTicks().sound(builder.leaves_sound).noOcclusion()
				.isValidSpawn(BlockUtils::jungleMob).isSuffocating(BlockUtils::never).isViewBlocking(BlockUtils::never)), WoodShape.LEAVES);
		if (builder.tree_grower != null) register(items, blocks, () -> new SaplingBlock(builder.tree_grower, Properties.of().sound(builder.leaves_sound).noCollission()
				.noOcclusion().randomTicks().instabreak().sound(SoundType.GRASS)), WoodShape.SAPLING);
		register(items, blocks, () -> new FenceBlock(builder.constructBaseProperties()), WoodShape.FENCE);
		register(items, blocks, () -> new FenceGateBlock(builder.constructBaseProperties(), wood_type), WoodShape.FENCE_GATE);
		register(items, blocks, () -> new ButtonBlock(builder.constructBaseProperties().noCollission(), type, 30, true), WoodShape.BUTTON);
		register(items, blocks, () -> new PressurePlateBlock(Sensitivity.EVERYTHING, builder.constructBaseProperties().noCollission(), type), WoodShape.PRESSURE_PLATE);
		register(items, blocks, () -> new DoorBlock(builder.constructBaseProperties().noOcclusion(), type), WoodShape.DOOR);
		register(items, blocks, () -> new TrapDoorBlock(builder.constructBaseProperties().noOcclusion(), type), WoodShape.TRAPDOOR);
		register(blocks, () -> new StandingSignBlock(builder.constructBaseProperties(), wood_type), WoodShape.SIGN);
		register(blocks, () -> new WallSignBlock(builder.constructBaseProperties(), wood_type), WoodShape.WALL_SIGN);
		sign = Optional.of(items.register(WoodShape.SIGN.getName(name),
				() -> new SignItem(new Item.Properties().stacksTo(16), get(WoodShape.SIGN), get(WoodShape.WALL_SIGN))));
		if (builder.has_boat) {
			boat_type = BoatRegistry.INSTANCE.register(name, builder.modid, items, builder.decorations_tab);
		} else {
			boat_type = null;
		}
	}

	protected void registerItem(DeferredRegister<Item> items, RegistryObject<Block> block, CreativeModeTab tab) {
		items.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}

	protected void register(DeferredRegister<Block> blocks, Supplier<Block> supplier, WoodShape shape) {
		RegistryObject<Block> block = blocks.register(shape.getName(name), supplier);
		BLOCKS.put(shape, block);
	}


	protected void register(DeferredRegister<Item> items, DeferredRegister<Block> blocks, Supplier<Block> supplier, WoodShape shape) {
		RegistryObject<Block> block = blocks.register(shape.getName(name), supplier);
		BLOCKS.put(shape, block);
		items.register(shape.getName(name), () -> new BlockItem(block.get(), new Item.Properties()));
	}

	@SubscribeEvent
	public void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == tab) {
			event.accept(getPlanks().asItem());
			event.accept(getStairs().asItem());
			event.accept(getSlab().asItem());
			event.accept(getStrippedLog().asItem());
			event.accept(getStrippedWood().asItem());
			event.accept(getLog());
			event.accept(getWood());
		} else if (event.getTab() == decorations_tab) {
			event.accept(getLeaves().asItem());
			if (BLOCKS.containsKey(WoodShape.SAPLING)) event.accept(getSapling().asItem());
			event.accept(getFence().asItem());
			event.accept(getFenceGate().asItem());
			event.accept(getButton().asItem());
			event.accept(getPressurePlate());
			event.accept(getDoor());
			event.accept(getTrapdoor());
			event.accept(getSignItem());
		}
	}

	public Block get(WoodShape shape) {
		return BLOCKS.containsKey(shape) ? BLOCKS.get(shape).get() : null;
	}

	public String getName() {
		return name;
	}

	public boolean isFlammable() {
		return isFlammable;
	}

	public TagKey<Item> getLogItemTag() {
		return logs_item_tag;
	}

	public TagKey<Block> getLogBlockTag() {
		return logs_block_tag;
	}

	public AbstractTreeGrower getTreeGrower() {
		return tree_grower;
	}

	public WoodType getWoodType() {
		return wood_type;
	}

	public Block getPlanks() {
		return get(WoodShape.PLANKS);
	}

	public StairBlock getStairs() {
		return (StairBlock) get(WoodShape.STAIRS);
	}

	public SlabBlock getSlab() {
		return (SlabBlock) get(WoodShape.SLAB);
	}

	public RotatedPillarBlock getLog() {
		return (RotatedPillarBlock) get(WoodShape.LOG);
	}

	public RotatedPillarBlock getStrippedLog() {
		return (RotatedPillarBlock) get(WoodShape.STRIPPED_LOG);
	}

	public RotatedPillarBlock getWood() {
		return (RotatedPillarBlock) get(WoodShape.WOOD);
	}

	public RotatedPillarBlock getStrippedWood() {
		return (RotatedPillarBlock) get(WoodShape.STRIPPED_WOOD);
	}

	public LeavesBlock getLeaves() {
		return (LeavesBlock) get(WoodShape.LEAVES);
	}

	public SaplingBlock getSapling() {
		return (SaplingBlock) get(WoodShape.SAPLING);
	}

	public FenceBlock getFence() {
		return (FenceBlock) get(WoodShape.FENCE);
	}

	public FenceGateBlock getFenceGate() {
		return (FenceGateBlock) get(WoodShape.FENCE_GATE);
	}

	public ButtonBlock getButton() {
		return (ButtonBlock) get(WoodShape.BUTTON);
	}

	public PressurePlateBlock getPressurePlate() {
		return (PressurePlateBlock) get(WoodShape.PRESSURE_PLATE);
	}

	public DoorBlock getDoor() {
		return (DoorBlock) get(WoodShape.DOOR);
	}

	public TrapDoorBlock getTrapdoor() {
		return (TrapDoorBlock) get(WoodShape.TRAPDOOR);
	}

	public SignBlock getSignBlock(boolean onWall) {
		return (SignBlock) (onWall ? get(WoodShape.WALL_SIGN) : get(WoodShape.SIGN));
	}

	public SignItem getSignItem() {
		return (SignItem) sign.get().orElse(null);
	}

	public AtlasBoatItem getBoat() {
		return (AtlasBoatItem) boat_type.getBoat();
	}

	public AtlasBoatItem getChestBoat() {
		return (AtlasBoatItem) boat_type.getChestBoat();
	}

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
		FuelHandler.INSTANCE.registerFuel(getSignItem(), 200);
	}

	public static enum WoodShape {
		PLANKS("planks"), STAIRS("stairs"), SLAB("slab"),
		STRIPPED_LOG("stripped", "log"), STRIPPED_WOOD("stripped", "wood"), LOG("log"), WOOD("wood"),
		LEAVES("", "leaves", true), SAPLING("", "sapling", true),
		FENCE("", "fence", true), FENCE_GATE("", "fence_gate", true), BUTTON("", "button", true), PRESSURE_PLATE("", "pressure_plate", true),
		DOOR("", "door", true), TRAPDOOR("", "trapdoor", true), SIGN("sign"), WALL_SIGN("wall_sign");

		private final String suffix, prefix;
		private final boolean is_decoration;

		private WoodShape(String suffix) {
			this("", suffix);
		}

		private WoodShape(String prefix, String suffix) {
			this(prefix, suffix, false);
		}

		private WoodShape(String prefix, String suffix, boolean isDecoration) {
			this.prefix = prefix;
			this.suffix = suffix;
			is_decoration = isDecoration;
		}

		public String getName(String name) {
			StringBuilder builder = new StringBuilder();
			if (!prefix.isBlank()) builder.append(prefix + "_");
			builder.append(name.toLowerCase());
			if (!suffix.isBlank()) {
				if(!(name.contains("wood") && suffix.equals("wood"))) builder.append("_" + suffix);
			}
			return builder.toString();
		}

		public CreativeModeTab getTab(WoodBlock wood_block) {
			return is_decoration ? wood_block.decorations_tab : wood_block.tab;
		}

	}

}
