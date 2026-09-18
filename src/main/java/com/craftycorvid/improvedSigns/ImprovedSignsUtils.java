package com.craftycorvid.improvedSigns;

import java.util.Optional;
import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.entity.SignTextSlot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ImprovedSignsUtils {
    // Where versions of this mod for 26.2 and earlier kept copied/retained sign text
    private static final String LEGACY_BLOCK_ENTITY_TAG = "BlockEntityTag";

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

    // 26.3 removed SignItem: signs (and hanging signs) are block items for a SignBlock
    public static boolean isSignItem(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof SignBlock;
    }

    public static Optional<ItemStack> getSignHand(Player player) {
        ItemStack mainHandItem = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (isSignItem(mainHandItem))
            return Optional.of(mainHandItem);
        return Optional.empty();
    }

    public static boolean hasText(SignText text) {
        return text.getMessages(false).stream().anyMatch(line -> !line.getString().isEmpty());
    }

    public static boolean hasSignText(ItemStack stack) {
        SignText front = stack.get(DataComponents.SIGN_TEXT_FRONT);
        SignText back = stack.get(DataComponents.SIGN_TEXT_BACK);
        return (front != null && hasText(front)) || (back != null && hasText(back));
    }

    private static SignText stripDye(SignText text) {
        return text.withColor(DyeColor.BLACK).withGlowingText(false);
    }

    // Copies the sign's text (and waxed state) onto a sign item, the same way vanilla does when a
    // sign is pick-blocked. Vanilla applies these components again when the item is placed and
    // shows the text in the item tooltip, so no custom data or lore is needed anymore.
    public static void copySignText(SignBlockEntity source, ItemStack target) {
        migrateLegacySignData(target);
        target.remove(DataComponents.WAXED);
        target.applyComponents(source.collectComponents());

        SignText front = source.getText(SignTextSlot.FRONT);
        SignText back = source.getText(SignTextSlot.BACK);
        if (!MOD_CONFIG.retainDyeOnSignCopy) {
            front = stripDye(front);
            back = stripDye(back);
        }
        target.set(DataComponents.SIGN_TEXT_FRONT, front);
        target.set(DataComponents.SIGN_TEXT_BACK, back);
    }

    // Versions for 26.2 and earlier stored the text in custom_data.BlockEntityTag and applied it
    // through a mixin on placement, with the tooltip generated as lore. Move that text into the
    // vanilla components so those signs keep working, and drop the lore since vanilla shows the
    // components in the tooltip.
    public static void migrateLegacySignData(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null)
            return;
        CompoundTag nbt = customData.copyTag();
        Optional<CompoundTag> legacyTag = nbt.getCompound(LEGACY_BLOCK_ENTITY_TAG);
        if (legacyTag.isEmpty())
            return;
        CompoundTag legacy = legacyTag.get();

        legacy.getCompound("front_text")
                .flatMap(tag -> SignText.CODEC.parse(NbtOps.INSTANCE, tag).result())
                .ifPresent(text -> stack.set(DataComponents.SIGN_TEXT_FRONT, text));
        legacy.getCompound("back_text")
                .flatMap(tag -> SignText.CODEC.parse(NbtOps.INSTANCE, tag).result())
                .ifPresent(text -> stack.set(DataComponents.SIGN_TEXT_BACK, text));
        if (legacy.getBoolean("is_waxed").orElse(false))
            stack.set(DataComponents.WAXED, Unit.INSTANCE);

        nbt.remove(LEGACY_BLOCK_ENTITY_TAG);
        if (nbt.isEmpty())
            stack.remove(DataComponents.CUSTOM_DATA);
        else
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        stack.remove(DataComponents.LORE);
    }
}
