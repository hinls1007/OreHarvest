package app.hinl.oreharvest.block

import app.hinl.oreharvest.registry.ModItems
import net.minecraft.block.*
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class OreCropsBlock(settings: Settings?) : CropBlock(settings) {

    private val AGE_TO_SHAPE = arrayOf(
        createCuboidShape(4.0, 0.0, 4.0, 12.0, 5.0, 12.0),
        createCuboidShape(4.0, 0.0, 4.0, 12.0, 7.0, 12.0),
        createCuboidShape(3.0, 0.0, 3.0, 13.0, 9.0, 13.0),
        createCuboidShape(2.0, 0.0, 2.0, 14.0, 12.0, 14.0),
        createCuboidShape(1.0, 0.0, 1.0, 15.0, 15.0, 15.0),
        createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0),
        createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0),
        createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
    )

    override fun canPlantOnTop(floor: BlockState?, world: BlockView?, pos: BlockPos?): Boolean {
        return floor?.block is OreBlock || floor?.block is RedstoneOreBlock
    }

    override fun canGrow(world: World?, random: Random?, pos: BlockPos?, state: BlockState?): Boolean {
        return super.canGrow(world, random, pos, state)
    }

    //FIXME: The onStacksDropped method deprecated will move logic to other function later
    @Suppress("DEPRECATION")
    override fun onStacksDropped(
        state: BlockState?,
        world: ServerWorld?,
        pos: BlockPos?,
        stack: ItemStack?,
        dropExperience: Boolean
    ) {
        val currentAge = state?.get(AGE) ?: 0
        if (currentAge == maxAge) {
            val lowerBlockState = world?.getBlockState(pos?.mutableCopy()?.down())
            val dropCount = (world?.random?.nextInt(4) ?: 0) + 1
            Block.dropStack(world, pos, ItemStack(lowerBlockState?.block, dropCount))
        } else {
            Block.dropStack(world, pos, ItemStack(ModItems.oreCropsSeed))
        }
        super.onStacksDropped(state, world, pos, stack, dropExperience)
    }

    override fun getSeedsItem(): ItemConvertible {
        return ModItems.oreCropsSeed
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return AGE_TO_SHAPE[state?.get(ageProperty) ?: 0]
    }
}