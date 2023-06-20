package com.ivanff.improvedSigns.datagen;

import com.ivanff.improvedSigns.ImprovedSignsMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class RecipeDatagen extends FabricRecipeProvider {
    public RecipeDatagen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
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
        generateClearSignRecipe(exporter, Items.CHERRY_SIGN);
        generateClearSignRecipe(exporter, Items.CHERRY_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.DARK_OAK_SIGN);
        generateClearSignRecipe(exporter, Items.DARK_OAK_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.MANGROVE_SIGN);
        generateClearSignRecipe(exporter, Items.MANGROVE_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.BAMBOO_SIGN);
        generateClearSignRecipe(exporter, Items.BAMBOO_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.CRIMSON_SIGN);
        generateClearSignRecipe(exporter, Items.CRIMSON_HANGING_SIGN);
        generateClearSignRecipe(exporter, Items.WARPED_SIGN);
        generateClearSignRecipe(exporter, Items.WARPED_HANGING_SIGN);
    }

    public void generateClearSignRecipe(Consumer<RecipeJsonProvider> exporter, Item sign) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, sign).input(sign).criterion("has_sign", InventoryChangedCriterion.Conditions.items(sign)).offerTo(exporter, new Identifier(ImprovedSignsMod.MOD_ID, "reset_" + Registries.ITEM.getId(sign).getPath()));
    }
}
