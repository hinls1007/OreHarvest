package app.hinl.oreharvest.registry

import app.hinl.oreharvest.Constants
import app.hinl.oreharvest.block.OreCropsBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.CropBlock
import net.minecraft.block.Material
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
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

    fun register() {
        Registry.register<Block, CropBlock>(
            Registry.BLOCK,
            Identifier(Constants.ModID, "ore_crops_block"),
            oreCropsBlock
        )
    }
}