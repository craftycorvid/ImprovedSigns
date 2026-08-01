package com.craftycorvid.improvedSigns.event;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_ID;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ItemFrameReachCallback {
    private static final Identifier FRAME_REACH_MODIFIER =
            Identifier.fromNamespaceAndPath(MOD_ID, "frame_reach");
    // Padding vanilla adds to the reach it accepts from clients, to be forgiving about lag
    private static final double VANILLA_REACH_PADDING = 3.0;

    // Item frames are entities, so they are only usable at entity reach (3) while the chest behind
    // them is usable at block reach (4.5). Sync the two so frame passthrough reaches just as far.
    // The attribute is synced to vanilla clients, which is what makes them target the frame.
    public static void extendEntityReach(ServerPlayer player) {
        AttributeInstance entityReach = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        AttributeInstance blockReach = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
        if (entityReach == null || blockReach == null)
            return;

        double bonus = blockReach.getBaseValue() - entityReach.getBaseValue();
        if (bonus > 0) {
            entityReach.addOrUpdateTransientModifier(new AttributeModifier(FRAME_REACH_MODIFIER,
                    bonus, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    // The extended reach is only meant for item frames, so hand back the bonus for every other
    // entity: reject uses and attacks vanilla would have turned down without it.
    public static InteractionResult limitReachToItemFrames(Player player, Level world,
            InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (entity instanceof ItemFrame)
            return InteractionResult.PASS;

        AttributeInstance entityReach = player.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        AttributeModifier bonus =
                entityReach == null ? null : entityReach.getModifier(FRAME_REACH_MODIFIER);
        if (bonus != null && !player.isWithinEntityInteractionRange(entity,
                VANILLA_REACH_PADDING - bonus.amount()))
            return InteractionResult.FAIL;

        return InteractionResult.PASS;
    }
}
