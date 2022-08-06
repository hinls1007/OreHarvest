package app.hinl.oreharvest.utils

import net.minecraft.block.*
import net.minecraft.entity.EntityType
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.passive.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.server.world.ServerWorld
import net.minecraft.tag.BlockTags
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.math.absoluteValue

object BlockActionHelper {
    fun applyGrowth(world: World, pos: BlockPos, radius: Int, height: Int) {
        BlockPos.iterateOutwards(pos, radius, height, radius).forEach { blockPos ->
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block
            when {
                block is Fertilizable
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
                    if (block.isFertilizable(world, blockPos, blockState, world.isClient)) {
                        when (block) {
                            is StemBlock -> {
                                block.grow(world as? ServerWorld, world.random, blockPos, blockState)

                            }
                            is BambooBlock -> {
                                val floorBlock = world.getBlockState(blockPos.down()).block
                                if (floorBlock !is BambooBlock) {
                                    block.grow(world as? ServerWorld, world.random, blockPos, blockState)
                                }
                            }
                            else -> {
                                block.grow(world as? ServerWorld, world.random, blockPos, blockState)
                            }
                        }
                    } else {
                        if (block is StemBlock) {
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
                    }
                }
                block is SugarCaneBlock -> {
                    if (blockState.get(SugarCaneBlock.AGE) < 14) {
                        world.setBlockState(blockPos, blockState.with(SugarCaneBlock.AGE, 14), 4)
                    }
                    val newBlockState = world.getBlockState(blockPos)
                    newBlockState.randomTick(world as? ServerWorld, blockPos, world.random)
                }
                block is CactusBlock -> {
                    if (blockState.get(CactusBlock.AGE) < 14) {
                        world.setBlockState(blockPos, blockState.with(CactusBlock.AGE, 14), 4)
                    }
                    val newBlockState = world.getBlockState(blockPos)
                    newBlockState.randomTick(world as? ServerWorld, blockPos, world.random)
                }
                block is ChorusFlowerBlock -> {
                    blockState.randomTick(world as? ServerWorld, blockPos, world.random)
                }
            }
        }
    }

    fun applyBreakCrops(world: World, pos: BlockPos, radius: Int, height: Int) {
        BlockPos.iterateOutwards(pos, radius, height, radius).forEach { blockPos ->
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block
            val defaultState = block.defaultState

            when {
                block is Fertilizable
                        && block !is SaplingBlock
                        && block !is BambooSaplingBlock
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
                    if (!block.isFertilizable(world, blockPos, blockState, world.isClient)) {
                        when (block) {
                            is CropBlock -> {
                                world.breakBlock(blockPos, true)
                                world.setBlockState(blockPos, defaultState)
                            }
                            is CaveVinesBodyBlock,
                            is CaveVinesHeadBlock -> {
                                if (CaveVines.hasBerries(blockState)) {
                                    CaveVines.pickBerries(blockState, world, blockPos)
                                }
                            }
                            is CocoaBlock -> {
                                world.breakBlock(blockPos, true)
                                world.setBlockState(blockPos, defaultState)
                            }
                            is BambooBlock -> {
                                val floorBlock = world.getBlockState(blockPos.down()).block
                                if (floorBlock !is BambooBlock && floorBlock !is BambooSaplingBlock && floorBlock != Blocks.AIR) {
                                    world.breakBlock(blockPos.up(), true)
                                    world.setBlockState(blockPos, Blocks.BAMBOO_SAPLING.defaultState)
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

                }
                block is MelonBlock
                        || block is PumpkinBlock -> {
                    world.breakBlock(blockPos, true)
                }
                block is SugarCaneBlock -> {
                    val floorBlock = world.getBlockState(blockPos.down()).block
                    if (floorBlock !is SugarCaneBlock) {
                        world.breakBlock(blockPos.up(), true)
                    }
                }
                block is CactusBlock -> {
                    val floorBlock = world.getBlockState(blockPos.down()).block
                    if (floorBlock !is CactusBlock) {
                        world.breakBlock(blockPos.up(), true)
                    }
                }
                block is ChorusPlantBlock -> {
                    val floor = world.getBlockState(blockPos.down())
                    if (floor.block == Blocks.END_STONE) {
                        var foundMatureFlower = false
                        BlockPos.iterateOutwards(pos, 5, 50, 5).forEach { chorusPos ->
                            val chorusBlockState = world.getBlockState(chorusPos)
                            val chorusBlock = chorusBlockState.block
                            if (chorusBlock is ChorusFlowerBlock) {
                                if (chorusBlockState.get(ChorusFlowerBlock.AGE) >= 3) {
                                    foundMatureFlower = true
                                    val hitResult =
                                        BlockHitResult(Box(chorusPos).center, Direction.WEST, chorusPos, false)
                                    chorusBlockState.onProjectileHit(
                                        world,
                                        chorusBlockState,
                                        hitResult,
                                        EntityType.SNOWBALL.create(world)
                                    )
                                }
                            }
                        }

                        if (foundMatureFlower) {
                            world.setBlockState(blockPos, Blocks.CHORUS_FLOWER.defaultState)
                        }
                    }
                }
            }
        }
    }

    fun applyGrowPassiveEntity(world: World, player: PlayerEntity, radius: Int) {
        val passiveEntity = world.getEntitiesByClass(
            PassiveEntity::class.java,
            player.boundingBox.expand(radius.toDouble()),
            EntityPredicates.VALID_ENTITY
        )
        for (entityNearby in passiveEntity) {
            when (entityNearby) {
                is CowEntity,
                is SheepEntity,
                is PigEntity,
                is ChickenEntity,
                is BeeEntity -> {
                    if (entityNearby.isBaby) {
                        entityNearby.growUp(entityNearby.breedingAge.absoluteValue)
                    }
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