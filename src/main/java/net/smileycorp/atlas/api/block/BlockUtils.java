package net.smileycorp.atlas.api.block;

import java.util.Random;

public class BlockUtils {
	
	public static int getFortune(int fortune, Random rand) {
		return getFortune(fortune, 1, rand);
	}
	
	public static int getFortune(int fortune, int base, Random rand) {
		int drops = (Math.max(0, rand.nextInt(fortune + 2) - 1) + 1 + fortune)*base;
		return drops;
	}

	
}
