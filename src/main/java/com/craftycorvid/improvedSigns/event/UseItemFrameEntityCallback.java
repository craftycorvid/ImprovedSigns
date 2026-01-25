package com.craftycorvid.improvedSigns.event;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

public class UseItemFrameEntityCallback {
    public static InteractionResult onUseItemFrameEntityCallback(Player player, Level world,
            InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (!(world instanceof net.minecraft.server.level.ServerLevel))
            return InteractionResult.PASS;
        if (!(entity instanceof ItemFrame frameEntity))
            return InteractionResult.PASS;

        if (!hand.equals(InteractionHand.MAIN_HAND))
            return InteractionResult.FAIL;

        if (MOD_CONFIG.enableInvisibleFrames && player.isShiftKeyDown()) {
            Item item;
            switch (MOD_CONFIG.invisibleFrameItem) {
                case GLASS_PANE:
                    item = Items.GLASS_PANE;
                    break;
                default:
                case AMETHYST_SHARD:
                    item = Items.AMETHYST_SHARD;
                    break;
            }

            Optional<ItemStack> itemOption = ImprovedSignsUtils.getItemHand(player, item);
            if (itemOption.isPresent()) {
                if (entity.isInvisible()) {
                    return InteractionResult.FAIL;
                }
                if (frameEntity.getItem().is(Items.AIR)) {
                    return InteractionResult.PASS;
                }
                itemOption.get().consume(1, player);
                entity.setInvisible(true);
                return InteractionResult.SUCCESS;
            }
        }

        if (MOD_CONFIG.enableFramePassthrough && !player.isShiftKeyDown()) {
            BlockPos pos = entity.blockPosition();
            Direction oppositeDirection = entity.getDirection().getOpposite();
            return ImprovedSignsUtils.handlePassthrough(player, world, pos, oppositeDirection);
        }

        return InteractionResult.PASS;
    }
}
