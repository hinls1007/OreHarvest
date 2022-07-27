package app.hinl.oreharvest

import app.hinl.oreharvest.registry.ModBlock
import app.hinl.oreharvest.registry.ModItems
import app.hinl.oreharvest.registry.ModScreen
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.Blocks
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object OreHarvest : ModInitializer {

    val itemGroup: ItemGroup = FabricItemGroupBuilder.build(
        Identifier(Constants.ModID, "general")
    ) {
        ItemStack(Blocks.DIAMOND_ORE)
    }

    override fun onInitialize() {
        ModBlock.register()
        ModItems.register()
        ModScreen.register()
    }
}