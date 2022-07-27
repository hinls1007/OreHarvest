package app.hinl.oreharvest.block.chest

import app.hinl.oreharvest.blockentity.BaseTickerBlockEntity
import app.hinl.oreharvest.blockentity.chest.ChestOfAttractionBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class ChestOfAttractionBlock(settings: Settings?) : BaseChestBlock(settings) {

    override fun <T : BlockEntity?> getTicker(
        world: World?,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T> {
        return BlockEntityTicker { world1: World?, pos: BlockPos?, state1: BlockState?, blockEntity: T ->
            (blockEntity as? BaseTickerBlockEntity)?.tick(
                world1,
                pos,
                state1,
                blockEntity
            )
        }
    }

    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return ChestOfAttractionBlockEntity(pos = pos, state = state)
    }

    @Suppress("DEPRECATION")
    override fun onEntityCollision(state: BlockState?, world: World?, pos: BlockPos?, entity: Entity?) {
        world ?: return
        if (world.isClient) return
        if (entity is ItemEntity) {
            val blockEntity = (world.getBlockEntity(pos) as? ChestOfAttractionBlockEntity) ?: return
            if (entity.thrower != blockEntity.uuid) {
                blockEntity.addStack(entity = entity)
            }
        }
    }
}