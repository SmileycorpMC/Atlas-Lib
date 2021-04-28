package net.smileycorp.atlas.api.client;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import com.google.common.collect.ImmutableList;

public class TextureAtlasLayered extends TextureAtlasSprite {
	
	protected final TextureAtlasSprite sprite0;
	protected final TextureAtlasSprite sprite1;
	
	public TextureAtlasLayered(ResourceLocation loc, TextureAtlasSprite sprite0, TextureAtlasSprite sprite1) {
		super(loc.toString());
		this.sprite0=sprite0;
		this.sprite1=sprite1;
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.<ResourceLocation>builder().addAll(super.getDependencies()).build();
	  }
	
	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }
	
	
	@Override
	public boolean load(IResourceManager manager, ResourceLocation location, Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
		
		textureGetter = RenderingUtils.defaultTextureGetter;
		
		try {
			int[][] data = Arrays.stream(sprite0.getFrameTextureData(0)).map(int[]::clone).toArray(int[][]::new);
			int[][] overlayData = sprite1.getFrameTextureData(0);
	        
	        for (int i = 0; i< data.length; i++) {
		 		for (int j = 0; j< data[i].length; j++) {
					int colour = data[i][j];
					Color colour2 = new Color(overlayData[i][j], true);
			        int r1 = (colour >> 16) & 0xFF;
			        int g1 = (colour >> 8) & 0xFF;
			        int b1 = (colour & 0xFF);
			       
			        int r2 = colour2.getRed();
			        int g2 = colour2.getBlue();
			        int b2 = colour2.getGreen();
			        float a2 = colour2.getAlpha()/255f;
			        
			        int r = Math.round((r1*(1-a2))+(r2*a2));
		        	int g = Math.round((g1*(1-a2))+(g2*a2));
		        	int b = Math.round((b1*(1-a2))+(b2*a2));
					data[i][j] = new Color(r, g, b, 255).getRGB();
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
	
	@SuppressWarnings("unused")
	private static int[] scaleToSmaller(int[] data, int width, int min) {
		if(width == min) { return data; }
		int scale = width / min;
		int[] output = new int[min*min];
		int j = 0;
		for(int i = 0; i < output.length; i++) {
			if(i%min == 0) {
				j += width;
			}
			output[i] = data[i*scale+j];
		}
		return output;
	}

}
