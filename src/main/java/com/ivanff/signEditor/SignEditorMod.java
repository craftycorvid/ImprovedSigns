package com.ivanff.signEditor;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.DyeItem;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import org.apache.logging.log4j.Logger;

import com.ivanff.signEditor.loot.condition.SignTextLootCondition;
import com.ivanff.signEditor.mixin.SignEntityMixin;

import org.apache.logging.log4j.LogManager;

public class SignEditorMod implements ModInitializer {
    public static final String MOD_ID = "sign_editor";
    public static final String MOD_NAME = "Better Signs & Frames";

    public static final Logger LOGGER = LogManager.getLogger();

    public static LootConditionType SIGN_TEXT;

    @Override
    public void onInitialize() {
        LOGGER.info("Better Signs & Frames Initializing");

        SIGN_TEXT = Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier("sign_text"), new LootConditionType(new SignTextLootCondition.Serializer()));

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockPos pos = hitResult.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof SignBlockEntity)) return ActionResult.PASS;
            if (player.isSneaking()) {
                if (hasEmptyHand(player)) {
                    SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
                    ((SignEntityMixin) signBlock).setSignEditable(true);
                    if (signBlock.isEditable()) {
                        player.openEditSignScreen(signBlock);
                    } else {
                        player.sendMessage(new LiteralText("Sign is not editable"), false);
                    }
                }
            } else {
                BlockState state = world.getBlockState(pos);
                if (state.contains(HorizontalFacingBlock.FACING)) {
                    Direction oppositeDirection = state.get(HorizontalFacingBlock.FACING).getOpposite();
                    handlePassthrough(player, world, hand, pos, oppositeDirection);
                }
            }
            return ActionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof ItemFrameEntity) {
                if (!player.isSneaking()) {
                    BlockPos pos = entity.getBlockPos();
                    Direction oppositeDirection = entity.getHorizontalFacing().getOpposite();
                    handlePassthrough(player, world, hand, pos, oppositeDirection);
                }
            }
            return ActionResult.PASS;
        });
    }

    void handlePassthrough(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction oppositeDirection) {
        if (isHoldingDye(player)) return;
        BlockPos hangingPos = pos.add(oppositeDirection.getOffsetX(), oppositeDirection.getOffsetY(), oppositeDirection.getOffsetZ());
        BlockState hangingState = world.getBlockState(hangingPos);
        Vec3d hanginPosVec3d = new Vec3d(hangingPos.getX(), hangingPos.getY(), hangingPos.getZ());
        BlockHitResult hangingHitResult = new BlockHitResult(hanginPosVec3d, oppositeDirection, pos, false); 
        hangingState.getBlock().onUse(hangingState, world, hangingPos, player, hand, hangingHitResult);
    }

    boolean hasEmptyHand(PlayerEntity player) {
        Item mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();
        Item offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND).getItem();
        return !(mainHandItem instanceof BlockItem || mainHandItem instanceof DecorationItem || offHandItem instanceof BlockItem || offHandItem instanceof DecorationItem);
    } 

    boolean isHoldingDye(PlayerEntity player) {
        Item mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();
        Item offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND).getItem();
        boolean isSquidInk = Item.getRawId(mainHandItem) == 808 || Item.getRawId(mainHandItem) == 808;
        return mainHandItem instanceof DyeItem || offHandItem instanceof DyeItem || isSquidInk;
    }
}
