package com.craftycorvid.improvedSigns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ImprovedSignsUtils {
    public static InteractionResult handlePassthrough(Player player, Level world, BlockPos pos,
            Direction oppositeDirection) {
        BlockPos hangingPos = pos.offset(oppositeDirection.getStepX(), oppositeDirection.getStepY(),
                oppositeDirection.getStepZ());
        BlockState hangingState = world.getBlockState(hangingPos);
        Vec3 hanginPosVec3d = new Vec3(hangingPos.getX(), hangingPos.getY(), hangingPos.getZ());
        BlockHitResult hangingHitResult =
                new BlockHitResult(hanginPosVec3d, oppositeDirection, hangingPos, false);
        return hangingState.useWithoutItem(world, player, hangingHitResult);
    }

    public static Optional<ItemStack> getItemHand(Player player, Item item) {
        ItemStack mainHandItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (mainHandItem.is(item))
            return Optional.of(mainHandItem);
        return Optional.empty();
    }

    public static Optional<ItemStack> getSignHand(Player player) {
        ItemStack mainHandItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (mainHandItem.getItem() instanceof SignItem)
            return Optional.of(mainHandItem);
        return Optional.empty();
    }

    private static Optional<List<MutableComponent>> parseSignCustomData(CompoundTag nbtCompound,
            String key) {
        return SignText.DIRECT_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompoundOrEmpty(key))
                .result().map(signText -> Arrays.stream(signText.getMessages(false)).map(text -> {
                    int color = signText.getColor().equals(DyeColor.BLACK)
                            ? TextColor.DARK_PURPLE.getValue()
                            : signText.getColor().getTextColor();
                    return text.copy().setStyle(Style.EMPTY.withItalic(signText.hasGlowingText())
                            .withColor(color).withShadowColor(TextColor.WHITE.getValue()));
                }).toList());

    }

    public static void appendSignTooltip(ItemStack stack) {
        if (!MOD_CONFIG.serverSideSignTextPreview)
            return;

        stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()
                .getCompound("BlockEntityTag").ifPresent(nbtCompound -> {
                    Optional<List<MutableComponent>> front =
                            parseSignCustomData(nbtCompound, "front_text");
                    Optional<List<MutableComponent>> back =
                            parseSignCustomData(nbtCompound, "back_text");

                    List<Component> textList = new ArrayList<>();
                    front.ifPresent(texts -> {
                        textList.add(Component.nullToEmpty("Front:").copy()
                                .setStyle(Style.EMPTY.withItalic(false)));
                        textList.addAll(texts);
                    });
                    back.ifPresent(texts -> {
                        textList.add(Component.nullToEmpty("Back:").copy()
                                .setStyle(Style.EMPTY.withItalic(false)));
                        textList.addAll(texts);
                    });
                    textList.removeIf(text -> text.getString().isEmpty());

                    stack.applyComponents(DataComponentMap.builder()
                            .set(DataComponents.LORE, new ItemLore(textList)).build());
                });
    }
}
