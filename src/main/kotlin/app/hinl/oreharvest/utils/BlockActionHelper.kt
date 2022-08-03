package app.hinl.oreharvest.utils

import net.minecraft.block.*
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

object BlockActionHelper {
    fun applyGrowth(world: World, pos: BlockPos, radius: Int) {
        BlockPos.iterateOutwards(pos, radius, 1, radius).forEach { blockPos ->
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block
            if (block is Fertilizable
                && block !is CaveVinesBodyBlock
                && block !is CaveVinesHeadBlock
                && block !is FernBlock
                && block !is GrassBlock
                && block !is MangroveLeavesBlock
                && block !is MossBlock
                && block !is NyliumBlock
                && block !is RootedDirtBlock
                && block !is TallFlowerBlock
                && block !is TwistingVinesBlock
                && block !is TwistingVinesPlantBlock
                && block !is WeepingVinesBlock
                && block !is WeepingVinesPlantBlock
            ) {
                if (block.canGrow(world, world.random, blockPos, blockState)) {
                    when (block) {
                        is StemBlock -> {
                            block.grow(world as? ServerWorld, world.random, blockPos, blockState)
                            if (blockState.get(StemBlock.AGE) >= StemBlock.MAX_AGE) {
                                val direction = Direction.Type.HORIZONTAL.random(world.random)
                                val targetBlockPos: BlockPos = blockPos.offset(direction)
                                val targetBlockState = world.getBlockState(targetBlockPos.down())
                                if (world.getBlockState(targetBlockPos).isAir && (targetBlockState.isOf(Blocks.FARMLAND) || targetBlockState.isIn(
                                        BlockTags.DIRT
                                    ))
                                ) {
                                    world.setBlockState(targetBlockPos, block.gourdBlock.defaultState)
                                    world.setBlockState(
                                        blockPos,
                                        block.gourdBlock.attachedStem.defaultState
                                            .with(
                                                HorizontalFacingBlock.FACING,
                                                direction
                                            ) as BlockState
                                    )
                                }
                            }
                        }
                        is CocoaBlock -> {
                            if (blockState.get(CocoaBlock.AGE) < CocoaBlock.MAX_AGE) {
                                block.grow(world as? ServerWorld, world.random, blockPos, blockState)
                            }
                        }
                        else -> {
                            block.grow(world as? ServerWorld, world.random, blockPos, blockState)
                        }
                    }
                }
            }
        }
    }

    fun applyBreakCrops(world: World, pos: BlockPos, radius: Int) {
        BlockPos.iterateOutwards(pos, radius, 1, radius).forEach { blockPos ->
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block
            val defaultState = block.defaultState

            when {
                block is Fertilizable
                        && block !is SaplingBlock
                        && block !is CaveVinesBodyBlock
                        && block !is CaveVinesHeadBlock
                        && block !is FernBlock
                        && block !is GrassBlock
                        && block !is MangroveLeavesBlock
                        && block !is MossBlock
                        && block !is NyliumBlock
                        && block !is RootedDirtBlock
                        && block !is TallFlowerBlock
                        && block !is TwistingVinesBlock
                        && block !is TwistingVinesPlantBlock
                        && block !is WeepingVinesBlock
                        && block !is WeepingVinesPlantBlock -> {
                    when (block) {
                        is CropBlock -> {
                            if (blockState.get(block.ageProperty) >= block.maxAge) {
                                world.breakBlock(blockPos, true)
                                world.setBlockState(blockPos, defaultState)
                            }
                        }
                        is StemBlock -> {
                            //Do nothing
                        }
                        else -> {
                            world.breakBlock(blockPos, true)
                        }
                    }
                }
                block is MelonBlock
                        || block is PumpkinBlock -> {
                    world.breakBlock(blockPos, true)
                }
            }
        }
    }

    fun applyAttractItem(world: World, player: PlayerEntity, radius: Int) {
        val entityItems = player.getWorld().getEntitiesByClass(
            ItemEntity::class.java,
            player.boundingBox.expand(radius.toDouble()),
            EntityPredicates.VALID_ENTITY
        )
        for (entityItemNearby in entityItems) {
            entityItemNearby.onPlayerCollision(player)
        }
    }
}