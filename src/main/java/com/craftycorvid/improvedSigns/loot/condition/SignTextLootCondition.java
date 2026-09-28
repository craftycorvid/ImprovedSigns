package com.craftycorvid.improvedSigns.loot.condition;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import java.util.Set;
import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SignTextLootCondition implements LootItemCondition {
    public static final SignTextLootCondition INSTANCE = new SignTextLootCondition();

    public SignTextLootCondition() {}

    @Override
    public MapCodec<SignTextLootCondition> codec() {
        return LootConditionTypes.SIGN_TEXT;
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.BLOCK_ENTITY);
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (!MOD_CONFIG.enableSignRetain)
            return false;
        BlockEntity blockEntity = lootContext.getOptional(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof SignBlockEntity signBlockEntity))
            return false;
        return ImprovedSignsUtils.hasText(signBlockEntity.getText(SignTextSlot.FRONT))
                || ImprovedSignsUtils.hasText(signBlockEntity.getText(SignTextSlot.BACK));
    }

    public static LootItemCondition.Builder builder() {
        return () -> {
            return INSTANCE;
        };
    }
}
