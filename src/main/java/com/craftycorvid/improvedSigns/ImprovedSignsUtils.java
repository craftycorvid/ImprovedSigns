package com.craftycorvid.improvedSigns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignText;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ImprovedSignsUtils {
    public static ActionResult handlePassthrough(PlayerEntity player, World world, BlockPos pos,
            Direction oppositeDirection) {
        BlockPos hangingPos = pos.add(oppositeDirection.getOffsetX(),
                oppositeDirection.getOffsetY(), oppositeDirection.getOffsetZ());
        BlockState hangingState = world.getBlockState(hangingPos);
        Vec3d hanginPosVec3d = new Vec3d(hangingPos.getX(), hangingPos.getY(), hangingPos.getZ());
        BlockHitResult hangingHitResult =
                new BlockHitResult(hanginPosVec3d, oppositeDirection, hangingPos, false);
        return hangingState.onUse(world, player, hangingHitResult);
    }

    public static Optional<ItemStack> getItemHand(PlayerEntity player, Item item) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (mainHandItem.isOf(item))
            return Optional.of(mainHandItem);
        return Optional.empty();
    }

    public static Optional<ItemStack> getSignHand(PlayerEntity player) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (mainHandItem.getItem() instanceof SignItem)
            return Optional.of(mainHandItem);
        return Optional.empty();
    }

    private static Optional<List<MutableText>> parseSignCustomData(NbtCompound nbtCompound,
            String key) {
        return SignText.CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompoundOrEmpty(key)).result()
                .map(signText -> Arrays.stream(signText.getMessages(false)).map(text -> {
                    int color = signText.getColor().equals(DyeColor.BLACK)
                            ? Formatting.DARK_PURPLE.getColorValue()
                            : signText.getColor().getSignColor();
                    return text.copy().setStyle(Style.EMPTY.withItalic(signText.isGlowing())
                            .withColor(color).withShadowColor(Formatting.WHITE.getColorValue()));
                }).toList());

    }

    public static void appendSignTooltip(ItemStack stack) {
        if (!MOD_CONFIG.serverSideSignTextPreview)
            return;

        stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt()
                .getCompound("BlockEntityTag").ifPresent(nbtCompound -> {
                    Optional<List<MutableText>> front =
                            parseSignCustomData(nbtCompound, "front_text");
                    Optional<List<MutableText>> back =
                            parseSignCustomData(nbtCompound, "back_text");

                    List<Text> textList = new ArrayList<>();
                    front.ifPresent(texts -> {
                        textList.add(
                                Text.of("Front:").copy().setStyle(Style.EMPTY.withItalic(false)));
                        textList.addAll(texts);
                    });
                    back.ifPresent(texts -> {
                        textList.add(
                                Text.of("Back:").copy().setStyle(Style.EMPTY.withItalic(false)));
                        textList.addAll(texts);
                    });
                    textList.removeIf(text -> text.getString().isEmpty());

                    stack.applyComponentsFrom(ComponentMap.builder()
                            .add(DataComponentTypes.LORE, new LoreComponent(textList)).build());
                });
    }
}
