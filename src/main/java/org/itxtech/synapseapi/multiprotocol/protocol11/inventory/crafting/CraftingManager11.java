package org.itxtech.synapseapi.multiprotocol.protocol11.inventory.crafting;

import cn.nukkit.block.BlockAir;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import org.itxtech.synapseapi.SynapseAPI;
import org.itxtech.synapseapi.multiprotocol.protocol11.protocol.CraftingDataPacket;

import java.util.*;

/**
 * @author CreeperFace
 */
public class CraftingManager11 {

    public final Map<UUID, Recipe> recipes = new HashMap<>();

    public final Map<String, FurnaceRecipe> furnaceRecipes = new HashMap<>();

    protected final Map<String, Map<String, Recipe>> recipeLookup = new HashMap<>();

    private static int RECIPE_COUNT = 0;

    public static CraftingDataPacket packet = null;

    public CraftingManager11(SynapseAPI api) {
        //api.saveResource("recipes11.json", true);

        List<Map> recipes = new Config(api.getDataFolder() + "/recipes11.json", Config.JSON).getMapList("recipes");
        MainLogger.getLogger().info("Loading recipes...");
        for (Map<String, Object> recipe : recipes) { //TODO: implement this better
            switch (toInt(recipe.get("type"))) {
                case 0:
                    // TODO: handle multiple result items
                    Map<String, Object> first = ((List<Map>) recipe.get("output")).get(0);
                    ShapelessRecipe result = new ShapelessRecipe(Item.fromJson(first));
                    for (Map<String, Object> ingredient : ((List<Map>) recipe.get("input"))) {
                        result.addIngredient(Item.fromJson(ingredient)); //ingredient count should be always 1 for now
                    }
                    this.registerRecipe(result);
                    break;
                case 1:
                    // TODO: handle multiple result items
                    first = ((List<Map>) recipe.get("output")).get(0);

                    ShapedRecipe11 shapedRecipe = new ShapedRecipe11(Item.fromJson(first), toInt(recipe.get("height")), toInt(recipe.get("width")));
                    Object[][] shape = Utils.splitArray(((List) recipe.get("input")).stream().toArray(), toInt(recipe.get("width")));
                    for (int y = 0; y < shape.length; y++) {
                        Object[] row = shape[y];
                        for (int x = 0; x < row.length; x++) {
                            Object data = row[x];

                            if (data instanceof Map) {
                                Map<String, Object> ingredient = (Map) data;
                                shapedRecipe.addIngredient(x, y, Item.get(toInt(ingredient.get("id")), toInt(ingredient.get("damage")), /*Utils.toInt(ingredient.get("count"))*/ 1, ingredient.get("nbt").toString().getBytes()));
                            } else {
                                shapedRecipe.addIngredient(x, y, new ItemBlock(new BlockAir()));
                            }
                        }
                    }
                    this.registerRecipe(shapedRecipe);
                    break;
                case 2:
                case 3:
                    Map<String, Object> resultMap = (Map) recipe.get("output");
                    Item resultItem = Item.get(toInt(resultMap.get("id")), toInt(resultMap.get("damage")), toInt(resultMap.get("count")), ((String) resultMap.get("nbt")).getBytes());
                    this.registerRecipe(new FurnaceRecipe(resultItem, Item.get(toInt(recipe.get("inputId")), recipe.containsKey("inputDamage") ? toInt(recipe.get("inputDamage")) : -1, 1)));
                    break;
                default:
                    break;
            }
        }

        this.rebuildPacket();

        MainLogger.getLogger().info("Loaded " + this.recipes.size() + " recipes.");
    }

    public void rebuildPacket() {
        CraftingDataPacket pk = new CraftingDataPacket();
        pk.cleanRecipes = true;

        for (Recipe recipe : this.getRecipes().values()) {
            if (recipe instanceof ShapedRecipe) {
                pk.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }

        for (FurnaceRecipe recipe : this.getFurnaceRecipes().values()) {
            pk.addFurnaceRecipe(recipe);
        }

        pk.encode();
        pk.isEncoded = true;

        packet = pk;
    }

    public final Comparator<Item> comparator = (i1, i2) -> {
        if (i1.getId() > i2.getId()) {
            return 1;
        } else if (i1.getId() < i2.getId()) {
            return -1;
        } else if (i1.getDamage() > i2.getDamage()) {
            return 1;
        } else if (i1.getDamage() < i2.getDamage()) {
            return -1;
        } else if (i1.getCount() > i2.getCount()) {
            return 1;
        } else if (i1.getCount() < i2.getCount()) {
            return -1;
        } else {
            return 0;
        }
    };

    public Recipe getRecipe(UUID id) {
        return this.recipes.containsKey(id) ? this.recipes.get(id) : null;
    }

    public Map<UUID, Recipe> getRecipes() {
        return recipes;
    }

    public Map<String, FurnaceRecipe> getFurnaceRecipes() {
        return furnaceRecipes;
    }

    public FurnaceRecipe matchFurnaceRecipe(Item input) {
        if (this.furnaceRecipes.containsKey(input.getId() + ":" + input.getDamage())) {
            return this.furnaceRecipes.get(input.getId() + ":" + input.getDamage());
        } else if (this.furnaceRecipes.containsKey(input.getId() + ":?")) {
            return this.furnaceRecipes.get(input.getId() + ":?");
        }

        return null;
    }

    public void registerShapedRecipe(ShapedRecipe recipe) {
        Item result = recipe.getResult();
        this.recipes.put(recipe.getId(), recipe);
        Map<Integer, Map<Integer, Item>> ingredients = recipe.getIngredientMap();
        String hash = "";
        for (Map<Integer, Item> v : ingredients.values()) {
            for (Item item : v.values()) {
                if (item != null && item.getId() != Item.AIR) {
                    hash += item.getId() + ":" + (!item.hasMeta() ? "?" : item.getDamage()) + "x" + item.getCount() + ",";
                }
            }

            hash += ";";
        }

        String index = result.getId() + ":" + (result.hasMeta() ? result.getDamage() : "");
        if (!this.recipeLookup.containsKey(index)) {
            this.recipeLookup.put(index, new HashMap<>());
        }

        this.recipeLookup.get(index).put(hash, recipe);
    }

    public void registerShapelessRecipe(ShapelessRecipe recipe) {
        Item result = recipe.getResult();
        this.recipes.put(recipe.getId(), recipe);
        String hash = "";
        List<Item> ingredients = recipe.getIngredientList();
        ingredients.sort(this.comparator);
        for (Item item : ingredients) {
            hash += item.getId() + ":" + (!item.hasMeta() ? "?" : item.getDamage()) + "x" + item.getCount() + ",";
        }

        if (!this.recipeLookup.containsKey(result.getId() + ":" + result.getDamage())) {
            this.recipeLookup.put(result.getId() + ":" + result.getDamage(), new HashMap<>());
        }
        this.recipeLookup.get(result.getId() + ":" + result.getDamage()).put(hash, recipe);
    }

    public void registerFurnaceRecipe(FurnaceRecipe recipe) {
        Item input = recipe.getInput();
        this.furnaceRecipes.put(input.getId() + ":" + (!input.hasMeta() ? "?" : input.getDamage()), recipe);
    }

    public boolean matchRecipe(ShapelessRecipe recipe) {
        String idx = recipe.getResult().getId() + ":" + recipe.getResult().getDamage();
        if (!this.recipeLookup.containsKey(idx)) {
            return false;
        }

        String hash = "";
        List<Item> ingredients = recipe.getIngredientList();
        ingredients.sort(this.comparator);
        for (Item item : ingredients) {
            hash += item.getId() + ":" + (!item.hasMeta() ? "?" : item.getDamage()) + "x" + item.getCount() + ",";
        }

        if (this.recipeLookup.get(idx).containsKey(hash)) {
            return true;
        }

        Recipe hasRecipe = null;

        for (Recipe r : this.recipeLookup.get(idx).values()) {
            if (r instanceof ShapelessRecipe) {
                if (((ShapelessRecipe) r).getIngredientCount() != ingredients.size()) {
                    continue;
                }
                List<Item> checkInput = ((ShapelessRecipe) r).getIngredientList();
                for (Item item : ingredients) {
                    int amount = item.getCount();
                    for (Item checkItem : checkInput) {
                        if (checkItem.equals(item, checkItem.hasMeta())) {
                            int remove = Math.min(checkItem.getCount(), amount);
                            checkItem.setCount(checkItem.getCount() - amount);
                            if (checkItem.getCount() == 0) {
                                checkInput.remove(checkItem);
                            }
                            amount -= remove;
                            if (amount == 0) {
                                break;
                            }
                        }
                    }
                }

                if (checkInput.isEmpty()) {
                    hasRecipe = r;
                    break;
                }
            }
        }
        return hasRecipe != null;
    }

    public void registerRecipe(Recipe recipe) {
        if (recipe instanceof CraftingRecipe) {
            ((CraftingRecipe) recipe).setId(Utils.dataToUUID(String.valueOf(++RECIPE_COUNT), String.valueOf(recipe.getResult().getId()), String.valueOf(recipe.getResult().getDamage()), String.valueOf(recipe.getResult().getCount()), Arrays.toString(recipe.getResult().getCompoundTag())));
        }

        if (recipe instanceof ShapedRecipe) {
            this.registerShapedRecipe((ShapedRecipe) recipe);
        } else if (recipe instanceof ShapelessRecipe) {
            this.registerShapelessRecipe((ShapelessRecipe) recipe);
        } else if (recipe instanceof FurnaceRecipe) {
            this.registerFurnaceRecipe((FurnaceRecipe) recipe);
        }
    }

    public Recipe[] getRecipesByResult(Item result) {
        return recipeLookup.get(result.getId() + ":" + result.getDamage()).values().stream().toArray(Recipe[]::new);
    }

    public static int toInt(Object number) {
        return (int) Math.round((double) number);
    }

    public static class Entry {
        final int resultItemId;
        final int resultMeta;
        final int ingredientItemId;
        final int ingredientMeta;
        final String recipeShape;
        final int resultAmount;

        public Entry(int resultItemId, int resultMeta, int ingredientItemId, int ingredientMeta, String recipeShape, int resultAmount) {
            this.resultItemId = resultItemId;
            this.resultMeta = resultMeta;
            this.ingredientItemId = ingredientItemId;
            this.ingredientMeta = ingredientMeta;
            this.recipeShape = recipeShape;
            this.resultAmount = resultAmount;
        }
    }
}