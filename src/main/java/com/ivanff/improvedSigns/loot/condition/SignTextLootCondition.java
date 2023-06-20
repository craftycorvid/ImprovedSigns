package com.ivanff.improvedSigns.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.ivanff.improvedSigns.config.ModConfig;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.util.JsonSerializer;

import java.util.Arrays;

public class SignTextLootCondition implements LootCondition {
    private static final SignTextLootCondition INSTANCE = new SignTextLootCondition();

    public SignTextLootCondition() {
    }

    @Override
    public LootConditionType getType() {
        return LootConditionTypes.SIGN_TEXT;
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (!ModConfig.get().enableSignRetain) return false;
        SignBlockEntity signBlockEntity = (SignBlockEntity) lootContext.get(LootContextParameters.BLOCK_ENTITY);
        if (signBlockEntity == null) return false;
        if (Arrays.stream(signBlockEntity.getText(true).getMessages(false)).anyMatch(text -> !text.equals(ScreenTexts.EMPTY))) return true;
        if (Arrays.stream(signBlockEntity.getText(false).getMessages(false)).anyMatch(text -> !text.equals(ScreenTexts.EMPTY))) return true;
        return false;
    }

    public static LootCondition.Builder builder() {
        return () -> {
            return INSTANCE;
        };
    }

    public static class Serializer implements JsonSerializer<SignTextLootCondition> {
        public void toJson(JsonObject jsonObject, SignTextLootCondition signTextLootCondition, JsonSerializationContext jsonSerializationContext) {
        }

        public SignTextLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return SignTextLootCondition.INSTANCE;
        }
    }
}
