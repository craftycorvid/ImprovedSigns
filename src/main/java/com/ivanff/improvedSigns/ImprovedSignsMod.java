package com.ivanff.improvedSigns;

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
import net.minecraft.item.*;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
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

import com.ivanff.improvedSigns.compat.FlanCompat;
import com.ivanff.improvedSigns.config.ModConfig;
import com.ivanff.improvedSigns.loot.condition.SignTextLootCondition;
import com.ivanff.improvedSigns.mixin.SignEntityMixin;

import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Field;
import java.util.Optional;

public class ImprovedSignsMod implements ModInitializer {
    public static final String MOD_ID = "improvedsigns";
    public static final String MOD_NAME = "Improved Signs";

    public static final Logger LOGGER = LogManager.getLogger();

    public static LootConditionType SIGN_TEXT;

    @Override
    public void onInitialize() {
        LOGGER.info("Improved Signs Initializing");
        ModConfig.init();
        FlanCompat.register();
        SIGN_TEXT = Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier("sign_text"), new LootConditionType(new SignTextLootCondition.Serializer()));

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;
            BlockPos pos = hitResult.getBlockPos();
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (!(blockEntity instanceof SignBlockEntity)) return ActionResult.PASS;
            if (player.isSneaking()) {
                if (hasEmptyHand(player)) {
                    if (!(ModConfig.get().enableSignEdit) || FlanCompat.checkEdit(world, player, pos) == ActionResult.FAIL) return ActionResult.PASS;
                    SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
                    ((SignEntityMixin) signBlock).setSignEditable(true);
                    if (signBlock.isEditable()) {
                        player.openEditSignScreen(signBlock);
                    } else {
                        player.sendMessage(Text.literal("Sign is not editable"), false);
                    }
                }
            } else {
                Optional<ItemStack> signOption = getSignHand(player);
                if (ModConfig.get().enableSignCopy && signOption.isPresent()) {
                    ItemStack sign = signOption.get();
                    NbtCompound nbt = sign.getOrCreateNbt();
                    NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
                    SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
                    for(int i = 0; i < 4; i++) {
                        Text text = signBlock.getTextOnRow(i, false);
                        String string = Text.Serializer.toJson(text);
                        blockEntityTag.putString(String.format("Text%d", i + 1), string);
                    }
                    nbt.put("BlockEntityTag", blockEntityTag);
                    sign.setNbt(nbt);
                } else {
                    BlockState state = world.getBlockState(pos);
                    if (state.contains(HorizontalFacingBlock.FACING)) {
                        Direction oppositeDirection = state.get(HorizontalFacingBlock.FACING).getOpposite();
                        handlePassthrough(player, world, hand, pos, oppositeDirection);
                    }
                }
            }
            return ActionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof ItemFrameEntity) {
                Class items = Items.class;
                Item item;
                try {
                    Field itemField = items.getField(ModConfig.get().invisibleFrameItem);
                    item = (Item) itemField.get(null);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    item = Items.AMETHYST_SHARD;
                }
                Optional<ItemStack> itemOption = geItemHand(player, item);
                if (ModConfig.get().enableInvisibleFrames && itemOption.isPresent()) {
                    ItemStack itemStack = itemOption.get();
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                    entity.setInvisible(true);
                } else if (!player.isSneaking()) {
                    BlockPos pos = entity.getBlockPos();
                    Direction oppositeDirection = entity.getHorizontalFacing().getOpposite();
                    handlePassthrough(player, world, hand, pos, oppositeDirection);
                }
            }
            return ActionResult.PASS;
        });
    }

    void handlePassthrough(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction oppositeDirection) {
        if (!ModConfig.get().enableSignPassthrough) return;
        if (isHoldingDye(player)) return;
        BlockPos hangingPos = pos.add(oppositeDirection.getOffsetX(), oppositeDirection.getOffsetY(), oppositeDirection.getOffsetZ());
        if (FlanCompat.checkPassthrough(world, player, hangingPos) == ActionResult.FAIL) return;
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

    Optional<ItemStack> geItemHand(PlayerEntity player, Item item) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND);
        if (mainHandItem.isOf(item)) return Optional.of(mainHandItem);
        if (offHandItem.isOf(item)) return Optional.of(offHandItem);
        return Optional.empty();
    }

    Optional<ItemStack> getSignHand(PlayerEntity player) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND);
        if (mainHandItem.getItem() instanceof SignItem) return Optional.of(mainHandItem);
        if (offHandItem.getItem() instanceof SignItem) return Optional.of(offHandItem);
        return Optional.empty();
    }

    boolean isHoldingDye(PlayerEntity player) {
        Item mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();
        Item offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND).getItem();
        boolean isSquidInk = Item.getRawId(mainHandItem) == 808 || Item.getRawId(mainHandItem) == 808;
        return mainHandItem instanceof DyeItem || offHandItem instanceof DyeItem || isSquidInk;
    }
}
