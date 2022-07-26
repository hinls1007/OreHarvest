package app.hinl.oreharvest.blockentity.chest

import app.hinl.oreharvest.blockentity.BaseTickerBlockEntity
import app.hinl.oreharvest.registry.ModBlock
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import java.util.*

open class ChestOfAttractionBlockEntity(
    type: BlockEntityType<*>? = ModBlock.chestOfAttractionBlockEntity,
    pos: BlockPos?,
    state: BlockState?
) :
    BaseChestEntity(type, pos, state) {

    val uuid = UUID.randomUUID()

    private val pickupDelay = 20

    override fun tick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BaseTickerBlockEntity?) {
        world ?: return
        if (world.isClient) return
        val radius = 10
        val entityItems = world.getEntitiesByClass(
            ItemEntity::class.java,
            Box(pos).expand(radius.toDouble()),
            EntityPredicates.VALID_ENTITY
        )

        entityItems.forEach { entity ->
            if (entity.age >= pickupDelay && (entity.owner == null || entity.owner == uuid) && entity.thrower != uuid) {
                entity.velocity = Box(pos).center.subtract(entity.pos).normalize()
            }
        }
    }

    fun addStack(entity: ItemEntity) {
        val remainingStack = addStackInternal(entity.stack)
        if (remainingStack.isEmpty) {
            entity.remove(Entity.RemovalReason.DISCARDED)
        } else {
            entity.thrower = uuid
            entity.stack = remainingStack
            dropItem(entity)
        }
    }

    private fun dropItem(itemEntity: ItemEntity) {
        val random = Random.create()
        val f = random.nextFloat() * 0.5f
        val g = random.nextFloat() * 6f
        itemEntity.setVelocity(
            (-MathHelper.sin(g) * f).toDouble(),
            0.2,
            (MathHelper.cos(g) * f).toDouble()
        )
    }
}