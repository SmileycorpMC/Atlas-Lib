package net.smileycorp.atlas.api.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomStateMapper extends StateMapperBase {
	
	public final String modid;
	public final String name;
	public final String state;
	
	public CustomStateMapper(String modid, String name) {
		this.modid=modid;
		this.name=name;
		this.state=null;
	}
	
	public CustomStateMapper(String modid, String name, String state) {
		this.modid=modid;
		this.name=name;
		this.state=state;
	}
	
	public CustomStateMapper(String name) {
		this.modid=null;
		this.name=name;
		this.state=null;
	}
	
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		String modid = this.modid;
		String statename = this.state;
		if (modid==null) modid=state.getBlock().getRegistryName().getResourceDomain();
		if (statename==null) statename = getPropertyString(state.getProperties());
        return new ModelResourceLocation(new ResourceLocation(modid, name), statename);
    }

}
