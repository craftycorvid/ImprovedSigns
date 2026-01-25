package com.craftycorvid.improvedSigns.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class RecipeDatagen extends FabricRecipeProvider {
    public RecipeDatagen(FabricDataOutput dataOutput,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup,
            RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                generateClearSignRecipe(output, Items.OAK_SIGN);
                generateClearSignRecipe(output, Items.OAK_HANGING_SIGN);
                generateClearSignRecipe(output, Items.SPRUCE_SIGN);
                generateClearSignRecipe(output, Items.SPRUCE_HANGING_SIGN);
                generateClearSignRecipe(output, Items.BIRCH_SIGN);
                generateClearSignRecipe(output, Items.BIRCH_HANGING_SIGN);
                generateClearSignRecipe(output, Items.JUNGLE_SIGN);
                generateClearSignRecipe(output, Items.JUNGLE_HANGING_SIGN);
                generateClearSignRecipe(output, Items.ACACIA_SIGN);
                generateClearSignRecipe(output, Items.ACACIA_HANGING_SIGN);
                generateClearSignRecipe(output, Items.DARK_OAK_SIGN);
                generateClearSignRecipe(output, Items.DARK_OAK_HANGING_SIGN);
                generateClearSignRecipe(output, Items.MANGROVE_SIGN);
                generateClearSignRecipe(output, Items.MANGROVE_HANGING_SIGN);
                generateClearSignRecipe(output, Items.CHERRY_SIGN);
                generateClearSignRecipe(output, Items.CHERRY_HANGING_SIGN);
                generateClearSignRecipe(output, Items.PALE_OAK_SIGN);
                generateClearSignRecipe(output, Items.PALE_OAK_HANGING_SIGN);
                generateClearSignRecipe(output, Items.BAMBOO_SIGN);
                generateClearSignRecipe(output, Items.BAMBOO_HANGING_SIGN);
                generateClearSignRecipe(output, Items.CRIMSON_SIGN);
                generateClearSignRecipe(output, Items.CRIMSON_HANGING_SIGN);
                generateClearSignRecipe(output, Items.WARPED_SIGN);
                generateClearSignRecipe(output, Items.WARPED_HANGING_SIGN);
            }

            public void generateClearSignRecipe(RecipeOutput exporter, ItemLike sign) {
                shapeless(RecipeCategory.DECORATIONS, sign).requires(sign)
                        .unlockedBy("has_sign",
                                InventoryChangeTrigger.TriggerInstance.hasItems(sign))
                        .save(exporter, ResourceKey.create(Registries.RECIPE,
                                getRecipeIdentifier(BuiltInRegistries.ITEM.getKey(sign.asItem()))));
            }
        };

    }

    @Override
    public String getName() {
        return "";
    }
}
