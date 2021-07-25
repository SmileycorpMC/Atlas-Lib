package net.smileycorp.atlas.api.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad.Builder;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.item.IMetaItem;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class RenderingUtils {
	
	public static final Function<ResourceLocation, TextureAtlasSprite> defaultTextureGetter = resource -> {
	    return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resource.toString());
	};
	
	public static void setMetaModel(String modid, Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta,
				new ModelResourceLocation(modid + ":items/"
				+ item.getRegistryName().toString().substring(modid.length()+1),
			((IMetaItem)item).byMeta(meta)));
	}
	
	public static void replaceRegisteredModel(ModelResourceLocation location, IRegistry<ModelResourceLocation, IBakedModel> registry, Class<? extends IBakedModel> clazz) {
	    try {
	      IModel model = ModelLoaderRegistry.getModel(location);
	      IBakedModel baked = registry.getObject(location);
	      IBakedModel finalModel = ReflectionHelper.findConstructor(clazz, IBakedModel.class, IModel.class).newInstance(baked, model);
	      registry.putObject(location, finalModel);
	    } catch(Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public static void renderCubeQuad(BufferBuilder buffer, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, World world, int luminance, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			renderPlanarQuad(buffer, facing, x, y, z, layer, colour, texture, world, luminance, pos);
		}
	}
	
	public static void renderPlanarQuad(BufferBuilder buffer, EnumFacing facing, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, World world, int luminance, BlockPos pos) {
		Vector4f[] plane = PlanarQuadRenderer.getQuadsFor(facing);
		Vector3f offset = (layer == 0 ? new Vector3f(0, 0, 0) : PlanarQuadRenderer.getOffsetFor(facing, x, y, z, layer));
		int rgba = colour.getRGB();
		for (int i = 0; i < 4; ++i) {
			Vector4f quadPos = plane[i];
			
			float r = ((rgba & 0xFF0000) >> 16) / 255F;
			float g = ((rgba & 0xFF00) >> 8) / 255F;
			float b = (rgba& 0xFF) / 255F;
			float a = ((rgba & 0xFF000000) >> 24) / 255F;
			
			float u = i < 2 ? texture.getMaxU() - (1F / 16F / 10000) : texture.getMinU() + (1F / 16F / 10000);
			float v = i == 1 || i == 2 ? texture.getMaxV() - (1F / 16F / 10000) : texture.getMinV() + (1F / 16F / 10000);
			
			int light = world.getCombinedLight(pos.offset(facing), luminance);
			
			buffer.pos(quadPos.x + offset.x, quadPos.y + offset.y, quadPos.z + offset.z).color(r, g, b, a).tex(u, v).lightmap(light >> 16 & 0xFFFF, light & 0xFFFF).endVertex();
		}
	}
	
	public static List<BakedQuad> getQuadsForCube(Color colour, String spritename) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		for (EnumFacing facing : EnumFacing.VALUES) {
			quads.addAll(getQuadsForPlane(facing, colour, spritename));
		}
		return quads;
	}
	
	public static List<BakedQuad> getQuadsForPlane(EnumFacing facing, Color colour, String spritename) {
		List<BakedQuad> quads = new ArrayList<BakedQuad>();
		FaceBakery bakery = new FaceBakery();
		Vector3f[] vecs = PlanarQuadRenderer.get3FQuadsFor(facing);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(spritename);
		BlockPartFace face = new BlockPartFace(facing.getOpposite(), 0, spritename, new BlockFaceUV(new float[]{0, 0, 16, 16}, 0));
		quads.add(bakery.makeBakedQuad(vecs[0], vecs[1], face, sprite, facing, ModelRotation.X0_Y0, null, true, true));
		return quads;
	}
	
	/*public static List<BakedQuad> getQuadsForModel(List<BakedQuad> quads, Color colour, TextureAtlasSprite sprite) {
		List<BakedQuad> newquads = new ArrayList<BakedQuad>();
		for (BakedQuad quad : quads) {
			EnumFacing facing = quad.getFace();
			Builder builder = new UnpackedBakedQuad.Builder(quad.getFormat());
			builder.setQuadOrientation(facing);
			builder.setTexture(sprite);
			Vector4f[] vectors = PlanarQuadRenderer.getQuadsFor(facing);
			for (int i  = 0; i < vectors.length; i++) {
				Vector4f vector = vectors[i];
				float u = i < 2 ? sprite.getMaxU() - (1F / 16F / 10000) : sprite.getMinU() + (1F / 16F / 10000);
				float v = i == 1 || i == 2 ? sprite.getMaxV() - (1F / 16F / 10000) : sprite.getMinV() + (1F / 16F / 10000);
				putQuadData(builder, vector, colour, u, v, facing, sprite);
			}
			newquads.add(builder.build());
		}
		return newquads;
	}*/
	
	private static void putQuadData(WoodBlockBuilder quad, Vector4f vector, Color colour, float u, float v, EnumFacing facing, TextureAtlasSprite sprite) {
		VertexFormat format = quad.getVertexFormat();
		
		Vec3i normal = facing.getDirectionVec();
		float light = LightUtil.diffuseLight(normal.getX(), normal.getY(), normal.getZ());
		
		for (int i = 0; i < format.getElementCount(); i++) {
		      switch (format.getElement(i).getUsage()) {
		      	case POSITION:
		      		quad.put(i, vector.getX(), vector.getY(), vector.getZ(), 1f);
		      		break;	
		      	case UV:
					quad.put(i, sprite.getInterpolatedU(u * 16), sprite.getInterpolatedV(v * 16), 0, 1);
		      		break;
		      	case COLOR:
		      		float r = colour.getRed() / 255F;
					float g = colour.getGreen() / 255F;
					float b = colour.getBlue() / 255F;
					float a = colour.getAlpha() / 255F;
					quad.put(i, light*r, light*g, light*b, a);
		      		break;
		      	case NORMAL:
		            quad.put(i, normal.getX(), normal.getY(), normal.getZ(), 0);
				default:
					quad.put(i);
					break;
		    	  
		      }
		}
	}
	
	
	private static class PlanarQuadRenderer {
		
		private static Vector3f getOffsetFor(EnumFacing facing, double x, double y, double z, int layer) {
			Vector3f vector = new Vector3f((float)x, (float)y, (float)z);
			switch(facing) {
			case UP:
				vector.translate(0, offsetLayer(1, layer), 0);
				break;
			case DOWN:
				vector.translate(0, offsetLayer(0, -layer), 0);
				break;
			case NORTH:
				vector.translate(0, 0, offsetLayer(0, -layer));
				break;
			case SOUTH:
				vector.translate(0, 0, offsetLayer(1, layer));
				break;
			case EAST:
				vector.translate(offsetLayer(1, layer), 0, 0);
				break;
			case WEST:
				vector.translate(offsetLayer(0, -layer), 0, 0);
				break;
			}
			return vector;
		}
	
		public static Vector3f[] get3FQuadsFor(EnumFacing facing) {
			if (facing!=null) {
				switch(facing) {
				case DOWN:
					return new Vector3f[] {
							new Vector3f(16, 0, 16),
							new Vector3f(0, 0, 0)};
				case NORTH:
					return new Vector3f[] {
							new Vector3f(0, 16, 0),
							new Vector3f(16, 0, 0)};
				case SOUTH:
					return new Vector3f[] {
							new Vector3f(16, 16, 0),
							new Vector3f(0, 0, 0)};
				case EAST:
					return new Vector3f[] {
							new Vector3f(0, 16, 0),
							new Vector3f(0, 0, 16)};
				case WEST:
					
					return new Vector3f[] {
							new Vector3f(0, 16, 16),
							new Vector3f(0, 0, 0)};
				default:
					return new Vector3f[] {
							new Vector3f(16, 0, 0),
							new Vector3f(0, 0, 16)};
				}
			}
			return new Vector3f[] {
					new Vector3f(0, 0, 0),
					new Vector3f(0, 0, 0)};
		}

		private static float offsetLayer(float offset, int layer) {
			float layerOffset = 0.001f*layer;
			return offset+layerOffset;
		}
	
		private static Vector4f[] getQuadsFor(EnumFacing facing) {
			if (facing!=null) {
				switch(facing) {
				case DOWN:
					return new Vector4f[] {
							new Vector4f(1, 0, 1, 0),
							new Vector4f(1, 0, 0, 0),
							new Vector4f(0, 0, 0, 0),
							new Vector4f(0, 0, 1, 0)};
				case NORTH:
					return new Vector4f[] {
							new Vector4f(0, 1, 0, 0),
							new Vector4f(0, 0, 0, 0),
							new Vector4f(1, 0, 0, 0),
							new Vector4f(1, 1, 0, 0)};
				case SOUTH:
					return new Vector4f[] {
							new Vector4f(1, 1, 0, 0),
							new Vector4f(1, 0, 0, 0),
							new Vector4f(0, 0, 0, 0),
							new Vector4f(0, 1, 0, 0)};
				case EAST:
					return new Vector4f[] {
							new Vector4f(0, 1, 0, 0),
							new Vector4f(0, 0, 0, 0),
							new Vector4f(0, 0, 1, 0),
							new Vector4f(0, 1, 1, 0)};
				case WEST:
					
					return new Vector4f[] {
							new Vector4f(0, 1, 1, 0),
							new Vector4f(0, 0, 1, 0),
							new Vector4f(0, 0, 0, 0),
							new Vector4f(0, 1, 0, 0)};
				default:
					return new Vector4f[] {
							new Vector4f(1, 0, 0, 0),
							new Vector4f(1, 0, 1, 0),
							new Vector4f(0, 0, 1, 0),
							new Vector4f(0, 0, 0, 0)};
				}
			}
			return new Vector4f[] {
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 0, 0),
					new Vector4f(0, 0, 0, 0)};
		}
	}
}
