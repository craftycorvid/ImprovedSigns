package com.ivanff.improvedSigns.datagen;

import com.ivanff.improvedSigns.loot.condition.LootConditionTypes;
import com.ivanff.improvedSigns.loot.condition.SignTextLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;

import java.util.HashSet;
import java.util.Set;

public class LootTableDatagen extends FabricBlockLootTableProvider {
    private static final Set<Item> EXPLOSION_IMMUNE = new HashSet<>();
    public LootTableDatagen(FabricDataOutput output) {
        super(output);
    }
    @Override
    public void generate() {
        addSignNBTDropTable(Blocks.OAK_SIGN);
        addSignNBTDropTable(Blocks.OAK_HANGING_SIGN);
        addSignNBTDropTable(Blocks.SPRUCE_SIGN);
        addSignNBTDropTable(Blocks.SPRUCE_HANGING_SIGN);
        addSignNBTDropTable(Blocks.BIRCH_SIGN);
        addSignNBTDropTable(Blocks.BIRCH_HANGING_SIGN);
        addSignNBTDropTable(Blocks.JUNGLE_SIGN);
        addSignNBTDropTable(Blocks.JUNGLE_HANGING_SIGN);
        addSignNBTDropTable(Blocks.ACACIA_SIGN);
        addSignNBTDropTable(Blocks.ACACIA_HANGING_SIGN);
        addSignNBTDropTable(Blocks.CHERRY_SIGN);
        addSignNBTDropTable(Blocks.CHERRY_HANGING_SIGN);
        addSignNBTDropTable(Blocks.DARK_OAK_SIGN);
        addSignNBTDropTable(Blocks.DARK_OAK_HANGING_SIGN);
        addSignNBTDropTable(Blocks.MANGROVE_SIGN);
        addSignNBTDropTable(Blocks.MANGROVE_HANGING_SIGN);
        addSignNBTDropTable(Blocks.BAMBOO_SIGN);
        addSignNBTDropTable(Blocks.BAMBOO_HANGING_SIGN);
        addSignNBTDropTable(Blocks.CRIMSON_SIGN);
        addSignNBTDropTable(Blocks.CRIMSON_HANGING_SIGN);
        addSignNBTDropTable(Blocks.WARPED_SIGN);
        addSignNBTDropTable(Blocks.WARPED_HANGING_SIGN);
    }

    public void addSignNBTDropTable(Block sign) {
        this.addDrop(sign, LootTable.builder()
            .pool(
                this.addSurvivesExplosionCondition(sign, LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1.0F))
                    .with(
                        ItemEntry.builder(sign).apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                                .withOperation("front_text", "BlockEntityTag.front_text")
                                .withOperation("back_text", "BlockEntityTag.back_text")
                                .withOperation("waxed", "BlockEntityTag.waxed")
                                .conditionally(SignTextLootCondition.builder())
                        )
                    )
                )
            )
        );
    }
}
