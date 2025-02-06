package com.craftycorvid.improvedSigns.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public class RecipeDatagen extends FabricRecipeProvider {
    public RecipeDatagen(FabricDataOutput dataOutput,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup,
            RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
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

            public void generateClearSignRecipe(RecipeExporter exporter, ItemConvertible sign) {
                createShapeless(RecipeCategory.DECORATIONS, sign).input(sign)
                        .criterion("has_sign", InventoryChangedCriterion.Conditions.items(sign))
                        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE,
                                getRecipeIdentifier(Registries.ITEM.getId(sign.asItem()))));
            }
        };

    }

    @Override
    public String getName() {
        return "";
    }
}
