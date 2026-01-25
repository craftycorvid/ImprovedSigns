package com.craftycorvid.improvedSigns.datagen;

import java.util.concurrent.CompletableFuture;
import com.craftycorvid.improvedSigns.loot.condition.SignTextLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableDatagen extends FabricBlockLootTableProvider {
    public LootTableDatagen(FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
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
        addSignNBTDropTable(Blocks.DARK_OAK_SIGN);
        addSignNBTDropTable(Blocks.DARK_OAK_HANGING_SIGN);
        addSignNBTDropTable(Blocks.MANGROVE_SIGN);
        addSignNBTDropTable(Blocks.MANGROVE_HANGING_SIGN);
        addSignNBTDropTable(Blocks.CHERRY_SIGN);
        addSignNBTDropTable(Blocks.CHERRY_HANGING_SIGN);
        addSignNBTDropTable(Blocks.PALE_OAK_SIGN);
        addSignNBTDropTable(Blocks.PALE_OAK_HANGING_SIGN);
        addSignNBTDropTable(Blocks.BAMBOO_SIGN);
        addSignNBTDropTable(Blocks.BAMBOO_HANGING_SIGN);
        addSignNBTDropTable(Blocks.CRIMSON_SIGN);
        addSignNBTDropTable(Blocks.CRIMSON_HANGING_SIGN);
        addSignNBTDropTable(Blocks.WARPED_SIGN);
        addSignNBTDropTable(Blocks.WARPED_HANGING_SIGN);
    }

    public void addSignNBTDropTable(Block sign) {
        this.add(sign, LootTable.lootTable().withPool(this.applyExplosionCondition(sign,
                LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(
                        // TODO: EntityTarget needs to be "block_entity" not "THIS", need to figure
                        // out a way to make that happen.
                        LootItem.lootTableItem(sign)
                                .apply(CopyCustomDataFunction.copyData(EntityTarget.TARGET_ENTITY)
                                        .copy("front_text", "BlockEntityTag.front_text")
                                        .copy("back_text", "BlockEntityTag.back_text")
                                        .copy("is_waxed", "BlockEntityTag.is_waxed")
                                        .when(SignTextLootCondition.builder()))))));
    }
}
