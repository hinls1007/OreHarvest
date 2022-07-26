package app.hinl.oreharvest.blockentity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

abstract class BaseTickerBlockEntity(type: BlockEntityType<*>?, pos: BlockPos?, state: BlockState?) : BlockEntity(
    type,
    pos,
    state
), BlockEntityTicker<BaseTickerBlockEntity>