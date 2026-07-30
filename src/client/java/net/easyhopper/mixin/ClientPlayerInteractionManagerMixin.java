package net.easyhopper.mixin;

import net.easyhopper.EasyHopperConfig;
import net.easyhopper.EasyHopperMod;
import net.minecraft.block.*;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Unique
    private static boolean easyhopper_fakeSneaking = false;

    public static boolean isFakeSneaking() {
        return easyhopper_fakeSneaking;
    }

    @Inject(method = "interactBlock", at = @At("HEAD"))
    private void onInteractBlockHead(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult,
                                     CallbackInfoReturnable<ActionResult> cir) {
        easyhopper_fakeSneaking = false;

        if (!EasyHopperMod.isEnabled()) return;

        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty() || !EasyHopperConfig.isAllowedItem(stack.getItem())) return;

        World world = player.getWorld();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (isInteractiveBlock(state)) {
            easyhopper_fakeSneaking = true;
        }
    }

    @Inject(method = "interactBlock", at = @At("RETURN"))
    private void onInteractBlockReturn(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult,
                                       CallbackInfoReturnable<ActionResult> cir) {
        easyhopper_fakeSneaking = false;
    }

    @Unique
    private boolean isInteractiveBlock(BlockState state) {
        Block block = state.getBlock();
        return block instanceof AbstractChestBlock
            || block instanceof HopperBlock
            || block instanceof AbstractFurnaceBlock
            || block instanceof BarrelBlock
            || block instanceof ShulkerBoxBlock
            || block instanceof DispenserBlock
            || block instanceof BrewingStandBlock
            || block instanceof EnchantingTableBlock
            || block instanceof AnvilBlock
            || block instanceof CraftingTableBlock
            || block instanceof CartographyTableBlock
            || block instanceof LoomBlock
            || block instanceof StonecutterBlock
            || block instanceof GrindstoneBlock
            || block instanceof LecternBlock
            || block instanceof BeaconBlock
            || block instanceof EnderChestBlock
            || block instanceof NoteBlock
            || block instanceof JukeboxBlock
            || block instanceof DaylightDetectorBlock
            || block instanceof TargetBlock
            || block instanceof LeverBlock
            || block instanceof ButtonBlock
            || block instanceof AbstractPressurePlateBlock
            || block instanceof DoorBlock
            || block instanceof TrapdoorBlock
            || block instanceof FenceGateBlock
            || block instanceof BedBlock
            || block instanceof CakeBlock
            || block instanceof RespawnAnchorBlock
            || block instanceof BellBlock
            || block instanceof CampfireBlock
            || block instanceof ComposterBlock
            || block instanceof DecoratedPotBlock
            || block instanceof CrafterBlock;
    }
}
