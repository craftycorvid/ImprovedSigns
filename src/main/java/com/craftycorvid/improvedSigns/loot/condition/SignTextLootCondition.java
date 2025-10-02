package com.craftycorvid.improvedSigns.loot.condition;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenTexts;

import java.util.Arrays;

public class SignTextLootCondition implements LootCondition {
    private static final SignTextLootCondition INSTANCE = new SignTextLootCondition();

    public SignTextLootCondition() {}

    @Override
    public LootConditionType getType() {
        return LootConditionTypes.SIGN_TEXT;
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (!MOD_CONFIG.enableSignRetain)
            return false;
        SignBlockEntity signBlockEntity =
                (SignBlockEntity) lootContext.get(LootContextParameters.BLOCK_ENTITY);
        if (signBlockEntity == null)
            return false;
        if (Arrays.stream(signBlockEntity.getText(true).getMessages(false))
                .anyMatch(text -> !text.equals(ScreenTexts.EMPTY)))
            return true;
        if (Arrays.stream(signBlockEntity.getText(false).getMessages(false))
                .anyMatch(text -> !text.equals(ScreenTexts.EMPTY)))
            return true;
        return false;
    }

    public static LootCondition.Builder builder() {
        return () -> {
            return INSTANCE;
        };
    }
}
