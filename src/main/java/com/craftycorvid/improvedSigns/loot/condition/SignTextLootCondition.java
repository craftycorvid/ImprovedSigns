package com.craftycorvid.improvedSigns.loot.condition;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import java.util.Arrays;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SignTextLootCondition implements LootItemCondition {
    public static final SignTextLootCondition INSTANCE = new SignTextLootCondition();

    public SignTextLootCondition() {}

    @Override
    public MapCodec<SignTextLootCondition> codec() {
        return LootConditionTypes.SIGN_TEXT;
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (!MOD_CONFIG.enableSignRetain)
            return false;
        SignBlockEntity signBlockEntity =
                (SignBlockEntity) lootContext.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (signBlockEntity == null)
            return false;
        if (Arrays.stream(signBlockEntity.getText(true).getMessages(false))
                .anyMatch(text -> !text.equals(CommonComponents.EMPTY)))
            return true;
        if (Arrays.stream(signBlockEntity.getText(false).getMessages(false))
                .anyMatch(text -> !text.equals(CommonComponents.EMPTY)))
            return true;
        return false;
    }

    public static LootItemCondition.Builder builder() {
        return () -> {
            return INSTANCE;
        };
    }
}
