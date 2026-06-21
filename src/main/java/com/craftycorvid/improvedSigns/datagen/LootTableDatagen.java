package com.craftycorvid.improvedSigns.datagen;

import java.util.concurrent.CompletableFuture;
import com.craftycorvid.improvedSigns.loot.condition.SignTextLootCondition;
import com.mojang.serialization.JavaOps;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext.BlockEntityTarget;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyCustomDataFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class LootTableDatagen extends FabricBlockLootSubProvider {
    // CopyCustomDataFunction.copyData only has public overloads for EntityTarget and NbtProvider;
    // 26.2 exposes no factory for a block-entity NbtProvider, so parse the BlockEntityTarget's
    // serialized name ("block_entity") through the public inline codec to get one.
    private static final NbtProvider BLOCK_ENTITY = ContextNbtProvider.INLINE_CODEC
            .parse(JavaOps.INSTANCE, BlockEntityTarget.BLOCK_ENTITY.getSerializedName()).result()
            .orElseThrow();

    public LootTableDatagen(FabricPackOutput output,
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

    // copyData(NbtProvider) is deprecated in 26.2, but it is the only public overload that can copy
    // from a block entity (copyData(EntityTarget) cannot), so the deprecation is accepted here.
    @SuppressWarnings("deprecation")
    public void addSignNBTDropTable(Block sign) {
        this.add(sign,
                LootTable.lootTable().withPool(this.applyExplosionCondition(sign,
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(
                                LootItem.lootTableItem(sign)
                                        .apply(CopyCustomDataFunction.copyData(BLOCK_ENTITY)
                                                .copy("front_text", "BlockEntityTag.front_text")
                                                .copy("back_text", "BlockEntityTag.back_text")
                                                .copy("is_waxed", "BlockEntityTag.is_waxed")
                                                .when(SignTextLootCondition.builder()))))));
    }
}
