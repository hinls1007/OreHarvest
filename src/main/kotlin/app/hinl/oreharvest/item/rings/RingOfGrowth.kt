package app.hinl.oreharvest.item.rings

import app.hinl.oreharvest.utils.BlockActionHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class RingOfGrowth(settings: Settings?) : Item(settings) {

    private var rangeRadius = 10

    override fun inventoryTick(stack: ItemStack?, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        if (entity !is PlayerEntity || world?.isClient != false) {
            return
        }

        val equippedMain = entity.mainHandStack
        val equippedOffHand = entity.offHandStack
        if (stack == equippedMain || stack == equippedOffHand) {
            if (entity.age % 20 == 0) {
                BlockActionHelper.applyGrowPassiveEntity(
                    world = world,
                    player = entity,
                    radius = rangeRadius,
                )
            }
        }
    }
}