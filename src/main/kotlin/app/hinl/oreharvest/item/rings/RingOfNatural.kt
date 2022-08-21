package app.hinl.oreharvest.item.rings

import app.hinl.oreharvest.utils.BlockActionHelper
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class RingOfNatural(settings: Settings?) : Item(settings) {

    private var rangeRadius = 10
    private var rangeHeight = 10

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        tooltip?.apply {
            add(Text.translatable("item.tooltip.ring_natural.1").formatted(Formatting.DARK_GREEN))
            add(Text.translatable("item.tooltip.ring_natural.2").formatted(Formatting.DARK_BLUE))
        }
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity || world?.isClient != false) {
            return
        }

        val equippedMain = entity.mainHandStack
        val equippedOffHand = entity.offHandStack
        if (stack == equippedMain || stack == equippedOffHand) {
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
}