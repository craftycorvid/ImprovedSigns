package com.craftycorvid.improvedSigns.datagen;

import java.util.concurrent.CompletableFuture;
import com.craftycorvid.improvedSigns.loot.condition.SignTextLootCondition;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class LootTableDatagen extends FabricBlockLootSubProvider {
    public LootTableDatagen(FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate() {
        addSignDropTable(Blocks.OAK_SIGN);
        addSignDropTable(Blocks.OAK_HANGING_SIGN);
        addSignDropTable(Blocks.SPRUCE_SIGN);
        addSignDropTable(Blocks.SPRUCE_HANGING_SIGN);
        addSignDropTable(Blocks.BIRCH_SIGN);
        addSignDropTable(Blocks.BIRCH_HANGING_SIGN);
        addSignDropTable(Blocks.JUNGLE_SIGN);
        addSignDropTable(Blocks.JUNGLE_HANGING_SIGN);
        addSignDropTable(Blocks.ACACIA_SIGN);
        addSignDropTable(Blocks.ACACIA_HANGING_SIGN);
        addSignDropTable(Blocks.DARK_OAK_SIGN);
        addSignDropTable(Blocks.DARK_OAK_HANGING_SIGN);
        addSignDropTable(Blocks.MANGROVE_SIGN);
        addSignDropTable(Blocks.MANGROVE_HANGING_SIGN);
        addSignDropTable(Blocks.CHERRY_SIGN);
        addSignDropTable(Blocks.CHERRY_HANGING_SIGN);
        addSignDropTable(Blocks.PALE_OAK_SIGN);
        addSignDropTable(Blocks.PALE_OAK_HANGING_SIGN);
        addSignDropTable(Blocks.BAMBOO_SIGN);
        addSignDropTable(Blocks.BAMBOO_HANGING_SIGN);
        addSignDropTable(Blocks.CRIMSON_SIGN);
        addSignDropTable(Blocks.CRIMSON_HANGING_SIGN);
        addSignDropTable(Blocks.WARPED_SIGN);
        addSignDropTable(Blocks.WARPED_HANGING_SIGN);
    }

    // Since 26.3 sign text and the waxed state are item components, so the drop can copy them
    // straight from the block entity, the same way shulker boxes keep their contents.
    public void addSignDropTable(Block sign) {
        LootItemFunction copySignText = CopyComponentsFunction
                .copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                .include(DataComponents.SIGN_TEXT_FRONT)
                .include(DataComponents.SIGN_TEXT_BACK)
                .include(DataComponents.WAXED)
                .when(Holder.direct(SignTextLootCondition.INSTANCE))
                .build();
        this.add(sign,
                LootTable.lootTable().withPool(this.applyExplosionCondition(sign,
                        LootPool.lootPool().add(LootItem.lootTableItem(sign)
                                .apply(Holder.direct(copySignText))))));
    }
}
