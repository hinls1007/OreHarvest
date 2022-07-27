package app.hinl.oreharvest.blockentity.chest

import app.hinl.oreharvest.blockentity.BaseTickerBlockEntity
import app.hinl.oreharvest.registry.ModBlock
import app.hinl.oreharvest.utils.BlockActionHelper
import net.minecraft.block.BlockState
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ChestOfNaturalBlockEntity(pos: BlockPos?, state: BlockState?) :
    ChestOfAttractionBlockEntity(type = ModBlock.chestOfNaturalBlockEntityType, pos = pos, state = state) {

    private val growthDelay = 20
    private var growthCounter = growthDelay

    override fun getDisplayName(): Text {
        return Text.translatable("item.oreharvest.chest_natural")
    }

    override fun tick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BaseTickerBlockEntity?) {
        world ?: return
        pos ?: return
        if (world.isClient) return
        if (growthCounter == 0) {
            BlockActionHelper.applyGrowth(world = world, pos = pos, radius = 10)
            growthCounter = growthDelay
        } else {
            growthCounter--
        }
        if (!isFull()) {
            BlockActionHelper.applyBreakCrops(world = world, pos = pos, radius = 10)
        }
        super.tick(world, pos, state, blockEntity)
    }
}