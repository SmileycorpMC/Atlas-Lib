package net.smileycorp.atlas.api.client;

import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class RenderingUtils {
	
	public static BakedModel replaceRegisteredModel(Map<ResourceLocation, BakedModel> map, ResourceLocation name, Function<BakedModel, BakedModel> function) {
		BakedModel model = function.apply(map.get(name));
		map.put(name, model);
		return model;
	}
	
	/*public static void renderCubeQuad(BufferBuilder buffer, double x, double y, double z, int layer, Color colour, TextureAtlasSprite texture, World world, int luminance, BlockPos pos) {
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
	
	public static void renderFont(String text, Vec3d fontvector, int color) {
		Minecraft mc = Minecraft.getMinecraft();
		FontRenderer fontrenderer = mc.fontRenderer;
		//fontrenderer.drawString(text, x, y, color);
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
		}*/
	}
