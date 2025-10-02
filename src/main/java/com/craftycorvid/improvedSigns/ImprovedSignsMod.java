package com.craftycorvid.improvedSigns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftycorvid.improvedSigns.config.ModConfig;
import com.craftycorvid.improvedSigns.event.UseItemFrameEntityCallback;
import com.craftycorvid.improvedSigns.event.UseSignBlockCallback;
import com.craftycorvid.improvedSigns.loot.condition.LootConditionTypes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class ImprovedSignsMod implements ModInitializer {
    public static final String MOD_ID = "improved-signs";
    public static final String MOD_NAME = "Improved Signs";
    public static ModConfig MOD_CONFIG;

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Improved Signs Initializing");
        MOD_CONFIG = ModConfig.loadConfig();
        LootConditionTypes.register();

        UseBlockCallback.EVENT.register(UseSignBlockCallback::onUseSignBlockCallback);

        UseEntityCallback.EVENT.register(UseItemFrameEntityCallback::onUseItemFrameEntityCallback);
    }
}
