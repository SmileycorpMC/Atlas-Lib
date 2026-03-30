package net.smileycorp.atlas.api.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;
import java.util.Map;

public abstract class RecipeTool extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private final ResourceLocation group;
    private final ItemStack output;
    private final NonNullList<Ingredient> ingredients;

    public RecipeTool(ResourceLocation group, ItemStack output, NonNullList<Ingredient> ingredients) {
        this.group = group;
        this.output = output.copy();
        this.ingredients = ingredients;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return output.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public String getGroup() {
        return group == null ? "" : group.toString();
    }

    protected boolean matches(Ingredient ingredient, ItemStack stack) {
        if (stack.isItemStackDamageable()) {
            stack = stack.copy();
            stack.setItemDamage(0);
        }
        boolean matches = ingredient.apply(stack);
        System.out.println(matches + ", " + stack + ", " + ingredient);
        return matches;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public static class Shapeless extends RecipeTool {

        public Shapeless(ResourceLocation group, ItemStack output, NonNullList<Ingredient> ingredients) {
            super(group, output, ingredients);
        }

        @Override
        public boolean matches(InventoryCrafting inv, World world) {
            List<Ingredient> ingredients = Lists.newArrayList(getIngredients());
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack.isEmpty()) continue;
                if (!hasIngredient(ingredients, stack)) return false;
                if (ingredients.isEmpty()) return true;
            }
            return false;
        }

        private boolean hasIngredient(List<Ingredient> ingredients, ItemStack stack) {
            for (int j = 0; j < ingredients.size(); j++) if (matches(ingredients.get(j), stack)) {
                ingredients.remove(j);
                return true;
            }
            return false;
        }

        @Override
        public boolean canFit(int width, int height) {
            return width * height >= getIngredients().size();
        }

        public static RecipeTool.Shapeless deserialize(JsonContext ctx, JsonObject json) {
            ResourceLocation group = json.has("group") ? new ResourceLocation(json.get("group").getAsString()) : null;
            ItemStack output = CraftingHelper.getItemStack(json.get("result").getAsJsonObject(), ctx);
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (JsonElement element : json.get("ingredients").getAsJsonArray()) ingredients.add(CraftingHelper.getIngredient(element, ctx));
            if (ingredients.isEmpty()) throw new JsonParseException("No ingredients for tool usage recipe");
            return new RecipeTool.Shapeless(group, output, ingredients);
        }

    }

    public static class Shaped extends RecipeTool implements IShapedRecipe {

        private final int width, height;
        private final boolean mirrored;

        public Shaped(ResourceLocation group, ItemStack output, NonNullList<Ingredient> ingredients, int width, int height, boolean mirrored) {
            super(group, output, ingredients);
            this.width = width;
            this.height = height;
            this.mirrored = mirrored;
        }

        @Override
        public boolean matches(InventoryCrafting inv, World world) {
            for (int x = 0; x <= inv.getWidth() - width; x++) for (int y = 0; y <= inv.getHeight() - height; ++y) {
                if (matches(inv, x, y, false)) return true;
                if (mirrored && matches(inv, x, y, true)) return true;
            }
            return false;
        }

        protected boolean matches(InventoryCrafting inv, int startX, int startY, boolean mirror) {
            for (int x = 0; x < inv.getWidth(); x++) for (int y = 0; y < inv.getHeight(); y++) {
                int subX = x - startX;
                int subY = y - startY;
                if (!matches(subX >= 0 && subY >= 0 && subX < width && subY < height ? getIngredients().get(mirror ? width - subX - 1 + subY * width
                        : subX + subY * width) : Ingredient.EMPTY, inv.getStackInRowAndColumn(x, y))) return false;
            }
            return true;
        }

        @Override
        public int getRecipeWidth() {
            return width;
        }

        @Override
        public int getRecipeHeight() {
            return height;
        }

        @Override
        public boolean canFit(int width, int height) {
            return width >= this.width && height >= this.height;
        }

        public static RecipeTool.Shaped deserialize(JsonContext ctx, JsonObject json) {
            ResourceLocation group = json.has("group") ? new ResourceLocation(json.get("group").getAsString()) : null;
            ItemStack output = CraftingHelper.getItemStack(json.get("result").getAsJsonObject(), ctx);
            Map<Character, Ingredient> keys = Maps.newHashMap();
            for (Map.Entry<String, JsonElement> entry : json.get("key").getAsJsonObject().entrySet()) {
                if (entry.getKey().length() != 1)
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                if (" ".equals(entry.getKey()))
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                keys.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), ctx));
            }
            keys.put(' ', Ingredient.EMPTY);
            NonNullList<Ingredient> ingredients = NonNullList.create();
            JsonArray pattern = json.getAsJsonArray("pattern");
            if (pattern.size() == 0) throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            int height = pattern.size();
            int width = -1;
            for (JsonElement element : pattern) {
                String str = element.getAsString();
                int len = str.length();
                if (width == -1) width = len;
                else if (width != len) throw new JsonSyntaxException("Invalid pattern: each row must  be the same width");
                for (int i = 0; i < len; i++) {
                    char key = str.charAt(i);
                    Ingredient ingredient = keys.get(key);
                    if (ingredient == null) throw new JsonSyntaxException("Pattern references symbol '" + key + "' but it's not defined in the key");
                    ingredients.add(ingredient);
                }
            }
            boolean mirrored = json.has("mirrored") ? json.get("mirrored").getAsBoolean() : true;
            return new RecipeTool.Shaped(group, output, ingredients, width, height, mirrored);
        }

    }

    public static class Factory implements IRecipeFactory {

        @Override
        public IRecipe parse(JsonContext ctx, JsonObject json) {
            IRecipe recipe = json.get("type").getAsString().contains("shaped") ? Shaped.deserialize(ctx, json) : Shapeless.deserialize(ctx, json);
            System.out.println(recipe + ", " + json);
            return recipe;
        }

    }

}
