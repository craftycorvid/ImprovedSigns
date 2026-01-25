package com.craftycorvid.improvedSigns.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootConditionTypes {
    public static final LootItemConditionType SIGN_TEXT =
            new LootItemConditionType(MapCodec.unit(SignTextLootCondition.builder().build()));

    public static void register() {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Identifier.parse("sign_text"),
                SIGN_TEXT);
    }
}
