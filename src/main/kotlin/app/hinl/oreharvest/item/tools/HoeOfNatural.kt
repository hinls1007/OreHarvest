package app.hinl.oreharvest.item.tools

import app.hinl.oreharvest.utils.BlockActionHelper
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HoeOfNatural(settings: Settings?) : Item(settings) {

    private var rangeRadius = 10
    private var rangeHeight = 10

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        tooltip?.apply {
            add(TranslatableText("item.tooltip.tool_hoe_natural.1").formatted(Formatting.DARK_GREEN))
            add(TranslatableText("item.tooltip.tool_hoe_natural.2").formatted(Formatting.DARK_BLUE))
            add(TranslatableText("item.tooltip.tool_hoe_natural.3").formatted(Formatting.DARK_BLUE))
        }
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity || world?.isClient != false) {
            return
        }
        val equippedMain = entity.mainHandStack
        if (stack == equippedMain) {
            if (entity.age % 20 == 0) {
                BlockActionHelper.applyGrowth(
                    world = world,
                    pos = BlockPos(entity.pos),
                    radius = rangeRadius,
                    height = rangeHeight
                )
            }
        }
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        user ?: return TypedActionResult.pass(null)
        val itemStack: ItemStack = user.getStackInHand(hand)
        world ?: return TypedActionResult.pass(itemStack)
        BlockActionHelper.applyBreakCrops(
            world = world, pos = BlockPos(user.pos),
            radius = rangeRadius,
            height = rangeHeight
        )
        return TypedActionResult.success(itemStack, true)
    }
}