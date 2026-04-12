package com.craftycorvid.improvedSigns.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class LootConditionTypes {
    public static final MapCodec<SignTextLootCondition> SIGN_TEXT =
            MapCodec.unit(SignTextLootCondition.INSTANCE);

    public static void register() {
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE,
                Identifier.fromNamespaceAndPath("minecraft", "sign_text"), SIGN_TEXT);
    }
}
