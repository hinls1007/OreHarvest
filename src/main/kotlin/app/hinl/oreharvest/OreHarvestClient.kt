package app.hinl.oreharvest

import app.hinl.oreharvest.registry.ModBlock
import app.hinl.oreharvest.registry.ModScreen
import app.hinl.oreharvest.screenhandler.ChestScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.client.render.RenderLayer

class OreHarvestClient : ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlock.oreCropsBlock)
        ScreenRegistry.register(ModScreen.chestScreenHandlerType, ::ChestScreen)
    }
}