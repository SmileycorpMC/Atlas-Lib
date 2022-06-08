package net.smileycorp.atlas.api.metals;

import net.minecraft.resources.ResourceLocation;

public class Metal {

	private Metal(Builder builder) {

	}

	private static class Builder {

		private ResourceLocation name;
		private int moltenColour;
		private boolean isAlloy = false;


		public Builder(ResourceLocation name, int colour) {
			int oreColour = moltenColour = colour;
		}


	}

}
