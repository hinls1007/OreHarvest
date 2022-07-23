package app.hinl.oreharvest.item.tools

import app.hinl.oreharvest.utils.BlockActionHelper
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class HoeOfNature(settings: Settings?) : Item(settings) {
    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        tooltip?.apply {
            add(Text.translatable("item.tooltip.tool_hoe_nature.1").formatted(Formatting.DARK_GREEN))
            add(Text.translatable("item.tooltip.tool_hoe_nature.2").formatted(Formatting.DARK_BLUE))
            add(Text.translatable("item.tooltip.tool_hoe_nature.3").formatted(Formatting.DARK_BLUE))
        }
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity || world?.isClient != false) {
            return
        }
        val equippedMain = entity.mainHandStack
        if (stack == equippedMain) {
            BlockActionHelper.applyGrowth(
                world = world,
                player = entity,
                10,
                20
            )
        }
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user ?: return TypedActionResult.pass(null)
        val itemStack: ItemStack = user.getStackInHand(hand)
        world ?: return TypedActionResult.pass(itemStack)
        BlockActionHelper.applyBreakCrops(world = world, player = user, 10)
        return TypedActionResult.success(itemStack, true)
    }
}