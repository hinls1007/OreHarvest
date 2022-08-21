package app.hinl.oreharvest.item.rings

import app.hinl.oreharvest.utils.BlockActionHelper
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class RingOfAttraction(settings: Settings?) : Item(settings) {
    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        tooltip?.apply {
            add(Text.translatable("item.tooltip.ring_growth").formatted(Formatting.DARK_GREEN))
        }
    }

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity) return
        world ?: return
        BlockActionHelper.applyAttractItem(world = world, player = entity, 20)
    }
}