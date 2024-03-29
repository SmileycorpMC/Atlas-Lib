package net.smileycorp.atlas.api.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
		BlockPartFace face = new BlockPartFace(null, 0, spritename, new BlockFaceUV(new float[]{0, 0, 16, 16}, 0));
		quads.add(bakery.makeBakedQuad(vecs[0], vecs[1], face, sprite, facing.getOpposite(), ModelRotation.X0_Y0, null, true, true));
		return quads;
	}

	public static void drawQuad(Vec3d start, Vec3d end, ResourceLocation texture, int textureSize, Color colour, Vector3f normalStart, Vector3f normalEnd) {
		int r = colour.getRed();
		int g = colour.getGreen();
		int b = colour.getBlue();
		int a = colour.getAlpha();
		float f = 1 / 32 /10000;
		double xf = 2*(end.x-start.x)-f;
		double zf = 2*(end.z-start.z)-f;
		double dy = (end.y-start.y)/2d;
		float ndy = normalEnd.y-normalStart.y/2;
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
		buffer.pos(start.x, start.y, start.z).tex(0, 0).color(r, g, b, a).normal(normalStart.x, normalStart.y, normalStart.z).endVertex();
		buffer.pos(start.x, start.y + dy , end.z).tex(0, zf).color(r, g, b, a).normal(normalStart.x, normalStart.y + ndy , normalEnd.z).endVertex();
		buffer.pos(end.x, end.y, end.z).tex(xf, xf).color(r, g, b, a).normal(normalEnd.x, normalEnd.y, normalEnd.z).endVertex();
		buffer.pos(end.x, start.y + dy, start.z).tex(xf, 0).color(r, g, b, a).normal(normalEnd.x, normalStart.y + ndy, normalStart.z).endVertex();
		tessellator.draw();
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
							new Vector3f(0, 0, 16),
							new Vector3f(16, 0, 0)};
				case NORTH:
					return new Vector3f[] {
							new Vector3f(16, 0, 0),
							new Vector3f(0, 16, 0)};
				case SOUTH:
					return new Vector3f[] {
							new Vector3f(16, 0, 16),
							new Vector3f(0, 16, 16)};
				case EAST:
					return new Vector3f[] {
							new Vector3f(16, 0, 16),
							new Vector3f(16, 16, 0)};
				case WEST:
					return new Vector3f[] {
							new Vector3f(0, 0, 16),
							new Vector3f(0, 16, 0)};
				default:
					return new Vector3f[] {
							new Vector3f(0, 16, 16),
							new Vector3f(16, 16, 0)};
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
