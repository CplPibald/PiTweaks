package us.bemrose.mc.pitweaks;

import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.init.Blocks;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class StairSlabTweak extends Tweak {

    int plateBehavior = 0;
    boolean removeRecipes = false;

    public StairSlabTweak(int pressurePlateBehavior, boolean removeOldRecipes) {
        plateBehavior = pressurePlateBehavior;
        removeRecipes = removeOldRecipes;
    }

    @Override
    public void postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent event) {
    
        System.out.println("PiStairSlab: Searching for stair/slab recipes");
        java.util.List<IRecipe> recipesToAdd = new java.util.LinkedList<IRecipe>();
        java.util.List<IRecipe> recipesToRemove = new java.util.LinkedList<IRecipe>();

        // Replace the missing vanilla pressure plate recipes with ones that take slabs.
        if (plateBehavior > 0) {
            ResourceLocation rl = new ResourceLocation("pitweaks:recipe_stone_pressure_plate");
            IRecipe r = new ShapedOreRecipe(rl, Blocks.STONE_PRESSURE_PLATE, "ss", 's', new ItemStack(Blocks.STONE_SLAB, 1, 0));
            r.setRegistryName(rl);
            recipesToAdd.add(r);
            rl = new ResourceLocation("pitweaks:recipe_wooden_pressure_plate");
            r = new ShapedOreRecipe(rl, Blocks.WOODEN_PRESSURE_PLATE, "ss", 's', "slabWood");
            r.setRegistryName(rl);
            recipesToAdd.add(r);
        }

        // Search through recipes for stairs and slabs
        java.util.Iterator<IRecipe> recipeIter = ForgeRegistries.RECIPES.iterator();
        while(recipeIter.hasNext()){
            IRecipe ir = recipeIter.next();
            
            // First check minecraft shaped recipes
            if (ir instanceof ShapedRecipes) {
                ShapedRecipes sr = (ShapedRecipes)ir;

                // Protect ourselves from other mods' malformed recipes
                if (sr.recipeItems.size() <= 0) { continue; }
                Ingredient input = sr.recipeItems.get(0);
                if (input.equals(Ingredient.EMPTY)) { continue; }
                ItemStack[] inputStacks = input.getMatchingStacks();
                if (inputStacks.length <= 0) { continue; }
                
                // Only consider recipes whose first ingredient is a block
                if (!(inputStacks[0].getItem() instanceof ItemBlock)) { continue; }
                
                // Is it a slab recipe?
                // Must be a 3x1 recipe with all three items are the same
                if (sr.recipeWidth == 3 && sr.recipeHeight == 1) {
                    if (sr.recipeItems.get(1).equals(input)
                        && sr.recipeItems.get(2).equals(input)) {
                        // all three items are the same
                        // TODO: Consider also checking for word "slab" in output
                        // TODO: Once we know we've got this right, remove existing recipe.
                        // recipeIter.remove();

                        addSlabRecipe(input, sr.getRecipeOutput(), recipesToAdd);

                    }
                }
                // Is it a stair-shaped recipe?
                if (sr.recipeWidth == 3 && sr.recipeHeight == 3) {
                    if (sr.recipeItems.get(1).equals(Ingredient.EMPTY) && sr.recipeItems.get(2).equals(Ingredient.EMPTY)
                        && sr.recipeItems.get(3).equals(input) && sr.recipeItems.get(4).equals(input) && sr.recipeItems.get(5).equals(Ingredient.EMPTY)
                        && sr.recipeItems.get(6).equals(input) && sr.recipeItems.get(7).equals(input) && sr.recipeItems.get(8).equals(input)) {
                        // It's a left-side stair recipe
                        // TODO: Consider also checking for word "stair" in output
                        // TODO: Once we know we've got this right, remove existing recipe.

                        addStairRecipe(input, sr.getRecipeOutput(), recipesToAdd);
                    }
                }
            }

            // Remove the vanilla recipes for stone and wood pressure plates, as they now conflict
            if (plateBehavior > 0) {
                if (ir.getRecipeOutput().getItem() == Item.getItemFromBlock(Blocks.WOODEN_PRESSURE_PLATE)) {
                    System.out.println("PiStairSlab: Removing vanilla recipe for wooden pressure plate");
                    // TODO: Confirm that this was actually the conflicting recipe and not another one added elsewhere.
                    recipesToRemove.add(ir);
                }
                if (ir.getRecipeOutput().getItem() == Item.getItemFromBlock(Blocks.STONE_PRESSURE_PLATE)) {
                    System.out.println("PiStairSlab: Removing vanilla recipe for stone pressure plate");
                    recipesToRemove.add(ir);
                }
            }
        }

        // Finally, actually add the recipes after we're done iterating so JVM won't get all whiny about concurrent modification
        for (IRecipe ir : recipesToAdd) {
            ForgeRegistries.RECIPES.register(ir);
        }
        if (removeRecipes) {
            for (IRecipe ir : recipesToRemove) {
                // ((net.minecraftforge.registries.IForgeRegistryModifiable<IRecipe>)(ForgeRegistries.RECIPES)).remove(ir.getRegistryName());
                ((net.minecraftforge.registries.IForgeRegistryModifiable<IRecipe>)(ForgeRegistries.RECIPES)).register(new NullRecipe(ir.getRegistryName()));
            }
        }
    }
    
    protected static void addSlabRecipe(Ingredient input, ItemStack output, java.util.List<IRecipe> recipeList) {

        // Register new recipe "XX" ==> 4x slab
        String itemName = output.getUnlocalizedName().replace(":","_");
        ItemStack slab4 = output.copy();
        slab4.setCount(4);
        String recipeName = "pitweaks:recipe_slab_" + itemName;
        IRecipe r = new ShapedRecipes(recipeName, 2, 1, NonNullList.<Ingredient>from(Ingredient.EMPTY, input, input), slab4);
        r.setRegistryName(new ResourceLocation(recipeName));
        recipeList.add(r);

        // Register shapeless 2x slab ==> material
        ItemStack slab1stack = output.copy();
        slab1stack.setCount(1);
        Ingredient slab1 = Ingredient.fromStacks(slab1stack);
        ItemStack raw = input.getMatchingStacks()[0].copy();
        raw.setCount(1);
        if (raw.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            raw.setItemDamage(0); // HACKHACK -- Need to get an actual value from this!!!
        }
        recipeName = "pitweaks:recipe_xslab_" + itemName;
        r = new ShapelessRecipes(recipeName, raw, NonNullList.<Ingredient>from(Ingredient.EMPTY, slab1, slab1));
        r.setRegistryName(new ResourceLocation(recipeName));
        recipeList.add(r);

        System.out.println("PiStairSlab: Found slab recipe for " + raw.toString() + " => " + slab1.toString());
    
    }
    
    protected static void addStairRecipe(Ingredient input, ItemStack output, java.util.List<IRecipe> recipeList) {
    
        // Register new recipe "X_/XX" ==> 4x stair
        String itemName = output.getUnlocalizedName().replace(":","_");
        ItemStack stair4 = output.copy(); stair4.setCount(4);

        String recipeName = "pitweaks:recipe_stair_" + itemName;
        IRecipe r = new ShapedRecipes(recipeName, 2, 2, NonNullList.<Ingredient>from(Ingredient.EMPTY, input, Ingredient.EMPTY, input, input), stair4);
        r.setRegistryName(new ResourceLocation(recipeName));
        recipeList.add(r);
        
        ItemStack stair1stack = output.copy();
        stair1stack.setCount(1);
        Ingredient stair1 = Ingredient.fromStacks(stair1stack);
        ItemStack raw = input.getMatchingStacks()[0].copy();
        raw.setCount(3);

        if (raw.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            raw.setItemDamage(0); // HACKHACK -- Need to get an actual value from this!!!
        }
        recipeName = "pitweaks:recipe_xstair_" + itemName;
        r = new ShapelessRecipes(recipeName + itemName, raw, NonNullList.<Ingredient>from(Ingredient.EMPTY, stair1, stair1, stair1, stair1));
        r.setRegistryName(new ResourceLocation(recipeName));
        recipeList.add(r);

        // foundStairs.add(output.getItem());
        System.out.println("PiStairSlab: Found stair recipe for " + raw.toString() + " => " + stair1.toString());
    }
}


// TODO: Add configs
//   - exclude list
//   - add list for new materials
//   - switch to yield 2 stairs instead of 4
//   - bool whether to remove 3x1 and 3x3 recipe when replacing

class NullRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public NullRecipe(ResourceLocation res) { setRegistryName(res); }

    @Override public boolean matches(net.minecraft.inventory.InventoryCrafting inv, net.minecraft.world.World worldIn) { return false; }
    @Override public ItemStack getCraftingResult(net.minecraft.inventory.InventoryCrafting inv) { return ItemStack.EMPTY; }
    @Override public boolean canFit(int width, int height) { return false; }
    @Override public ItemStack getRecipeOutput() { return ItemStack.EMPTY; }
}