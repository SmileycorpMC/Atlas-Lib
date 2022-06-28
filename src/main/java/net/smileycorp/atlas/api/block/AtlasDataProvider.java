package net.smileycorp.atlas.api.block;

import java.io.IOException;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class AtlasDataProvider implements DataProvider {

	protected final BlockStateProvider stateProvider;
	protected final LootTableProvider lootProvider;
	protected final RecipeProvider recipeProvider;

	public AtlasDataProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
		stateProvider = new BlockStateProvider(gen, modid, exFileHelper) {
			@Override protected void registerStatesAndModels() {}
		};
		lootProvider = new LootTableProvider(gen);
		recipeProvider = new RecipeProvider(gen) {};
	}

	@Override
	public void run(HashCache cache) throws IOException {
		stateProvider.run(cache);
		registerData();
		lootProvider.run(cache);
		recipeProvider.run(cache);
	}

	protected abstract void registerData();

	public void shapedBlock(ShapedBlock block) {
		//block
		stateProvider.simpleBlock(block.getBase());
		simpleItemBlock(block.getBase());
		stateProvider.stairsBlock(block.getStairs(), texture(block.getBase()));
		simpleItemBlock(block.getStairs());
		stateProvider.slabBlock(block.getSlab(), texture(block.getBase()), texture(block.getBase()));
		simpleItemBlock(block.getSlab());
		if (block.getWall() != null) {
			stateProvider.wallBlock(block.getWall(), texture(block.getBase()));
			stateProvider.itemModels().wallInventory(block.getWall().getRegistryName().getPath(), texture(block.getBase()));
		}
	}

	public void woodBlock(WoodBlock block) {
		//planks
		stateProvider.simpleBlock(block.getPlanks());
		simpleItemBlock(block.getPlanks());
		//recipeProvider.
		//plank stairs
		stateProvider.stairsBlock(block.getStairs(), texture(block.getPlanks()));
		simpleItemBlock(block.getStairs());
		//plank slab
		stateProvider.slabBlock(block.getSlab(), texture(block.getPlanks()), texture(block.getPlanks()));
		simpleItemBlock(block.getSlab());
		//stripped log
		stateProvider.logBlock(block.getStrippedLog());
		simpleItemBlock(block.getStrippedLog());
		//stripped wood
		stateProvider.axisBlock(block.getStrippedWood(), texture(block.getStrippedLog()), texture(block.getStrippedLog()));
		simpleItemBlock(block.getStrippedWood());
		//log
		stateProvider.logBlock(block.getLog());
		simpleItemBlock(block.getLog());
		//wood
		stateProvider.axisBlock(block.getWood(), texture(block.getLog()), texture(block.getLog()));
		simpleItemBlock(block.getWood());
		//leaves
		stateProvider.simpleBlock(block.getLeaves());
		simpleItemBlock(block.getLeaves());
		//sapling
		if (block.getSapling() != null) {
			stateProvider.models().cross(block.getSapling().getRegistryName().getPath(), texture(block.getSapling()));
			simpleItem(block.getSapling().asItem());
		}
		//fence
		stateProvider.fenceBlock(block.getFence(), texture(block.getPlanks()));
		stateProvider.itemModels().fenceInventory(block.getFence().getRegistryName().getPath(), texture(block.getPlanks()));
		//fence gate
		stateProvider.fenceGateBlock(block.getFenceGate(), texture(block.getPlanks()));
		simpleItemBlock(block.getFenceGate());
		//button
		stateProvider.buttonBlock(block.getButton(), texture(block.getPlanks()));
		stateProvider.itemModels().buttonInventory(block.getButton().getRegistryName().getPath(), texture(block.getPlanks()));
		//pressurePlate
		stateProvider.pressurePlateBlock(block.getPressurePlate(), texture(block.getPlanks()));
		simpleItemBlock(block.getPressurePlate());
		//door
		stateProvider.doorBlock(block.getDoor(), texture(block.getDoor(), "_bottom"), texture(block.getDoor(), "_top"));
		simpleItem(block.getDoor().asItem());
		//trapdoor
		stateProvider.trapdoorBlock(block.getTrapdoor(), texture(block.getTrapdoor()), true);
		simpleItemBlock(block.getTrapdoor());
		//sign
		stateProvider.signBlock((StandingSignBlock) block.getSignBlock(false), (WallSignBlock) block.getSignBlock(true), texture(block.getPlanks()));
		simpleItem(block.getSignItem());
		//boat
		simpleItem(block.getBoat());
	}

	protected ResourceLocation texture(Block block) {
		return texture(block, "");
	}

	protected ResourceLocation texture(Block block, String suffix) {
		ResourceLocation base = block.getRegistryName();
		return new ResourceLocation(base.getNamespace(), "block/" + base.getPath() + suffix);
	}

	protected void simpleItemBlock(Block block) {
		stateProvider.itemModels().withExistingParent(block.getRegistryName().getPath(), texture(block));
	}

	protected void simpleItem(Item item) {
		stateProvider.itemModels().withExistingParent(item.getRegistryName().getPath(), stateProvider.mcLoc("item/generated"));
	}

}
