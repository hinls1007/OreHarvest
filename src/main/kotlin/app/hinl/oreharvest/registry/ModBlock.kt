package app.hinl.oreharvest.registry

import app.hinl.oreharvest.Constants
import app.hinl.oreharvest.OreHarvest
import app.hinl.oreharvest.block.OreCropsBlock
import app.hinl.oreharvest.block.chest.ChestOfAttractionBlock
import app.hinl.oreharvest.block.chest.ChestOfNaturalBlock
import app.hinl.oreharvest.blockentity.chest.ChestOfAttractionBlockEntity
import app.hinl.oreharvest.blockentity.chest.ChestOfNaturalBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry

object ModBlock {
    val oreCropsBlock = OreCropsBlock(
        FabricBlockSettings.of(Material.PLANT)
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)
            .emissiveLighting { state, world, pos -> true }
    )

    val chestOfAttractionBlock = ChestOfAttractionBlock(
        FabricBlockSettings.of(Material.WOOD)
            .ticksRandomly()
            .sounds(BlockSoundGroup.WOOD)
    )
    val chestOfAttractionBlockItem = BlockItem(chestOfAttractionBlock, FabricItemSettings().group(OreHarvest.itemGroup))
    lateinit var chestOfAttractionBlockEntity: BlockEntityType<ChestOfAttractionBlockEntity>


    val chestOfNaturalBlock = ChestOfNaturalBlock(
        FabricBlockSettings.of(Material.WOOD)
            .ticksRandomly()
            .sounds(BlockSoundGroup.WOOD)
    )
    val chestOfNaturalBlockItem = BlockItem(chestOfNaturalBlock, FabricItemSettings().group(OreHarvest.itemGroup))
    lateinit var chestOfNaturalBlockEntityType: BlockEntityType<ChestOfNaturalBlockEntity>

    fun register() {
        Registry.register(
            Registry.BLOCK,
            Identifier(Constants.ModID, "ore_crops_block"),
            oreCropsBlock
        )

        Registry.register(
            Registry.BLOCK,
            Identifier(Constants.ModID, "chest_attraction"),
            chestOfAttractionBlock
        )
        Registry.register(
            Registry.ITEM,
            Identifier(Constants.ModID, "chest_attraction"),
            chestOfAttractionBlockItem
        )
        chestOfAttractionBlockEntity = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Identifier(Constants.ModID, "chest_attraction_entity"),
            FabricBlockEntityTypeBuilder.create(
                { blockPos: BlockPos, blockState: BlockState ->
                    ChestOfAttractionBlockEntity(pos = blockPos, state = blockState)
                },
                chestOfAttractionBlock
            )
                .build(null)
        )

        Registry.register(
            Registry.BLOCK,
            Identifier(Constants.ModID, "chest_natural"),
            chestOfNaturalBlock
        )
        Registry.register(
            Registry.ITEM,
            Identifier(Constants.ModID, "chest_natural"),
            chestOfNaturalBlockItem
        )
        chestOfNaturalBlockEntityType = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, Identifier(Constants.ModID, "chest_natural_entity"),
            FabricBlockEntityTypeBuilder.create(::ChestOfNaturalBlockEntity, chestOfNaturalBlock)
                .build(null)
        )
    }
}