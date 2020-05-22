package com.ivanff.signEditor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import org.apache.logging.log4j.Logger;

import com.ivanff.signEditor.mixin.SignEntityMixin;

import org.apache.logging.log4j.LogManager;

public class SignEditorMod implements ModInitializer {
    public static final String MOD_ID = "sign_editor";
    public static final String MOD_NAME = "Sign Editor";

    public static Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("Sign Editor Initializing");

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity.getType() == BlockEntityType.SIGN) {
                SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
                if (player.isSneaking()) {
                    ((SignEntityMixin) signBlock).setSignEditable(true);
                    if (signBlock.isEditable()) {
                        signBlock.setEditor(player);
                        player.openEditSignScreen(signBlock);
                    } else {
                        player.sendMessage(new LiteralText("Sign is not editable"));
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}