package net.smileycorp.atlas.api.util.recipe;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.item.ItemStack;

public class WeightedOutputs {

	private final List<Entry<ItemStack, Integer>> entries;
	private final int deafaultTries;
	
	public WeightedOutputs(int deafultTries, Map<ItemStack, Integer> entries) {
		this(deafultTries, new ArrayList<Entry<ItemStack, Integer>>(entries.entrySet()));
	}

	public WeightedOutputs(int deafaultTries, List<Entry<ItemStack, Integer>> entries) {
		this.deafaultTries = deafaultTries;
		this.entries = entries;
		System.out.println(deafaultTries);
	}

	public int getAmount() {
		return deafaultTries;
	}

	public List<Entry<ItemStack, Integer>> getTable() {
		return new ArrayList<Entry<ItemStack, Integer>>(entries);
	}

	public List<ItemStack> getResults(Random rand) {
		return getResults(rand, 1);
	}

	private List<ItemStack> getResults(Random rand, int tries) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		List<Entry<ItemStack, Integer>> mappedEntries = new ArrayList<Entry<ItemStack, Integer>>();
		int max = 0;
		for(Entry<ItemStack, Integer> entry : entries) {
			mappedEntries.add(new SimpleEntry<ItemStack, Integer>(entry.getKey(), max));
			max+=entry.getValue();
		}
		Collections.reverse(mappedEntries);
		for(int i=0; i<(tries*deafaultTries);i++){
			int result = rand.nextInt(max);
			for(Entry<ItemStack, Integer> entry : mappedEntries) {
				if (result>=entry.getValue()) {
					stacks.add(entry.getKey().copy());
					break;
				}
			}
		}
		System.out.println(stacks);
		return stacks;
	}
}
