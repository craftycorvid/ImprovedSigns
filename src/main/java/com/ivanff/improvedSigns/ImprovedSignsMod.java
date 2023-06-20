package com.ivanff.improvedSigns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ivanff.improvedSigns.compat.FlanCompat;
import com.ivanff.improvedSigns.config.ModConfig;
import com.ivanff.improvedSigns.event.UseItemFrameEntityCallback;
import com.ivanff.improvedSigns.event.UseSignBlockCallback;
import com.ivanff.improvedSigns.loot.condition.LootConditionTypes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class ImprovedSignsMod implements ModInitializer {
    public static final String MOD_ID = "improved-signs";
    public static final String MOD_NAME = "Improved Signs";

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Improved Signs Initializing");
        ModConfig.init();
        LootConditionTypes.register();
         FlanCompat.register();

        UseBlockCallback.EVENT.register(UseSignBlockCallback::onUseSignBlockCallback);

        UseEntityCallback.EVENT.register(UseItemFrameEntityCallback::onUseItemFrameEntityCallback);
    }
}
