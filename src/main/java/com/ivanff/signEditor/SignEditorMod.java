package com.ivanff.signEditor;

import net.fabricmc.api.ModInitializer;
import net.minecraft.text.LiteralText;

import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;

public class SignEditorMod implements ModInitializer {
    public static final String MOD_ID = "sign_editor";
    public static final String MOD_NAME = "Sign Editor";

    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Sign Editor Initializing");

        SignEditorCallback.EVENT.register((player, signBlock) -> {
            if (player.isSneaking()) {
                signBlock.setEditable(true);
                if (signBlock.isEditable()) {
                    signBlock.setEditor(player);
                    player.openEditSignScreen(signBlock);
                } else {
                    player.sendMessage(new LiteralText("Sign is not editable"));
                }
            }
        });
    }
}