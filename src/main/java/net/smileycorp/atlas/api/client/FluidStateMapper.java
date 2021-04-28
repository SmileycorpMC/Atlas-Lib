package net.smileycorp.atlas.api.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.smileycorp.atlas.common.ModDefinitions;

@SideOnly(Side.CLIENT)
public class FluidStateMapper extends StateMapperBase {
	
	public final Fluid fluid;
	
	public FluidStateMapper(Fluid fluid) {
		this.fluid=fluid;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(new ResourceLocation(ModDefinitions.modid, fluid.getName() + ".atlasfluid"), getPropertyString(state.getProperties()));
    }

}
