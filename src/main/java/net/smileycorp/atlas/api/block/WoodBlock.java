package net.smileycorp.atlas.api.block;


public class WoodBlock {
	
	/*public final PropertyString variant;
	private List<String> variants;
	
	final String name;
	final String modid;
	final BlockBaseLog log;
	final BlockBasePlank plank;
	final BlockBasePlankStairs stairs;
	final BlockBasePlankSlab slab;
	final BlockBasePlankSlab doubleSlab;
	
	Block[] blocks;

	public WoodBlock(String name, String modid, CreativeTabs tab, Block sapling, List<String> variants) {
		//super(name, modid, Material.WOOD, SoundType.WOOD, 3f, 5f, "axe", 0, tab);
		this.variants = variants.subList(0, 3);
		this.variant=new PropertyString("variant", new Predicate<String>(){
			@Override
			public boolean test(String variant) {
				return this.variants.contains(variant);
			}
		});
		this.name=name;
		this.modid=modid;
		log = new BlockBaseLog(name, modid, tab, variant);
		plank = new BlockBasePlank(name, modid, tab, variant);
		stairs = new BlockBasePlankStairs(plank, variant);
		slab = new BlockBasePlankSlab(plank, variant, false);
		doubleSlab = new BlockBasePlankSlab(plank, variant, true);
		blocks = new Block[]{log, plank, stairs, slab, doubleSlab};
		
	}
	
	public Block getPlank() {
		return plank;
	}
	
	public Block getStairs() {
		return stairs;
	}
	
	public Block getSlab() {
		return getSlab(false);
	}
	
	public Block getSlab(boolean getFull) {
		return getFull ? doubleSlab : slab;
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		slab.half=slab;
		doubleSlab.half=slab;
		registry.registerAll(blocks);
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		for (Block block : blocks) {
			if (block instanceof BlockSlabBase) continue;
			Item item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			item.setUnlocalizedName(block.getUnlocalizedName());
			registry.register(item);
		}
		Item item = new ItemSlab(slab, slab, doubleSlab);
		item.setRegistryName(slab.getRegistryName());
		item.setUnlocalizedName(slab.getUnlocalizedName());
		registry.register(item);
	}
	
	public void registerModels() {
		for (Block block : blocks) {
			if (block==doubleSlab) continue;
			final ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(block);
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(loc, "normal"));
		}
		ModelLoader.setCustomStateMapper(slab, new SlabStateMapper(slab));
		ModelLoader.setCustomStateMapper(doubleSlab, new CustomStateMapper(modid, name, "normal"));
	}
	
	public void registerRecipes() {
		OreDictionary.registerOre("logWood", log);
		OreDictionary.registerOre("plankWood", plank);
		OreDictionary.registerOre("slabWood", slab);
		OreDictionary.registerOre("stairWood", stairs);
		for (int i = 0; i < 4;) {
			String name = variants.get(i);
			GameRegistry.addShapelessRecipe(new ResourceLocation(modid, name +"_plank"), new ResourceLocation(modid, name), new ItemStack(plank, 4, i), Ingredient.fromStacks(new ItemStack(log, 1, i)));
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_stairs"), new ResourceLocation(modid, name), new ItemStack(stairs, 4, i), 
					new Object[]{"  #", " ##", "###", '#', new ItemStack(plank, 1, i)});
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, name+"_slab"), new ResourceLocation(modid, name), new ItemStack(slab, 6, i), 
					new Object[]{"###", '#', new ItemStack(plank, 1, i)});
		}
	}
	
	public static enum EnumWood implements IStringSerializable {
		;
		
		protected final String name;
		
		EnumWood(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

	}*/
}
