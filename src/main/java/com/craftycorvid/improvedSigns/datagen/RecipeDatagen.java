package com.craftycorvid.improvedSigns.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class RecipeDatagen extends FabricRecipeProvider {
    public RecipeDatagen(FabricDataOutput dataOutput,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        generateClearSignRecipe(exporter, Items.OAK_SIGN);
        generateClearSignRecipe(exporter, Items.OAK_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.SPRUCE_SIGN);
        generateClearSignRecipe(exporter, Items.SPRUCE_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.BIRCH_SIGN);
        generateClearSignRecipe(exporter, Items.BIRCH_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.JUNGLE_SIGN);
        generateClearSignRecipe(exporter, Items.JUNGLE_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.ACACIA_SIGN);
        generateClearSignRecipe(exporter, Items.ACACIA_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.DARK_OAK_SIGN);
        generateClearSignRecipe(exporter, Items.DARK_OAK_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.MANGROVE_SIGN);
        generateClearSignRecipe(exporter, Items.MANGROVE_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.CHERRY_SIGN);
        generateClearSignRecipe(exporter, Items.CHERRY_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.BAMBOO_SIGN);
        generateClearSignRecipe(exporter, Items.BAMBOO_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.CRIMSON_SIGN);
        generateClearSignRecipe(exporter, Items.CRIMSON_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.WARPED_SIGN);
        generateClearSignRecipe(exporter, Items.WARPED_HANGING_SIGN);
    }

    public void generateClearSignRecipe(RecipeOutput exporter, ItemLike sign) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, sign).requires(sign)
                .unlockedBy("has_sign", InventoryChangeTrigger.TriggerInstance.hasItems(sign))
                .save(exporter, getRecipeIdentifier(BuiltInRegistries.ITEM.getKey(sign.asItem())));
    }

    @Override
    public String getName() {
        return "";
    }
}
