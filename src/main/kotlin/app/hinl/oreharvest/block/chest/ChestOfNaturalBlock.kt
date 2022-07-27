package app.hinl.oreharvest.block.chest

import app.hinl.oreharvest.blockentity.chest.ChestOfNaturalBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class ChestOfNaturalBlock(settings: Settings?) : ChestOfAttractionBlock(settings) {
    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return ChestOfNaturalBlockEntity(pos = pos, state = state)
    }
}