package net.smileycorp.atlas.api.client;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureAtlasGreyscale extends TextureAtlasSprite {
	
	final ResourceLocation resource;
	
	public TextureAtlasGreyscale(ResourceLocation resource) {
		super(resource.toString()+"_grey");
		this.resource=resource;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
	return ImmutableList.<ResourceLocation>builder().addAll(super.getDependencies()).add(resource).build();
	  }
	
	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }
	
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		try {
			TextureAtlasSprite sprite = textureGetter.apply(resource);
			
			this.copyFrom(sprite);
			int[][] spriteData = sprite.getFrameTextureData(0);
			int[][] data = Arrays.stream(spriteData).map(int[]::clone).toArray(int[][]::new);
		 	for (int i = 0; i< spriteData.length; i++) {
		 		for (int j = 0; j< spriteData[i].length; j++) {
					int colour = spriteData[i][j];
					int r = (colour >> 16) & 0xFF;
			        int g = (colour >> 8) & 0xFF;
			        int b = (colour & 0xFF);
			        float rr = (float) Math.pow(r / 255.0, 2.2);
			        float gg = (float) Math.pow(g / 255.0, 2.2);
			        float bb = (float) Math.pow(b / 255.0, 2.2);
			        float l = 0.2126f * rr + 0.7152f * gg + 0.0722f * bb;
			        System.out.println(l);
			        int grey = (int) (255.0 * Math.pow(l, 1.0 / 2.2));
					data[i][j] = new Color(grey, grey, grey, 255).getRGB();
		 		}
		 	}
			if (framesTextureData.size()<1) {
				framesTextureData.add(data);
			} else {
				framesTextureData.set(0, data);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
    }

}
