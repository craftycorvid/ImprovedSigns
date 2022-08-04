package com.ivanff.improvedSigns.loot.condition;

import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LootConditionTypes
{
    public static final LootConditionType SIGN_TEXT = new LootConditionType(new SignTextLootCondition.Serializer());

    public static void register()
    {
        Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier("sign_text"), SIGN_TEXT);
    }
}
