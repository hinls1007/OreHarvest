package app.hinl.oreharvest.registry

import app.hinl.oreharvest.Constants
import app.hinl.oreharvest.screenhandler.ChestScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier

object ModScreen {
    lateinit var chestScreenHandlerType: ScreenHandlerType<ChestScreenHandler>

    fun register() {
        chestScreenHandlerType = ScreenHandlerRegistry.registerSimple(
            Identifier(Constants.ModID, "chest_screen_handler")
        ) { syncId: Int, inventory: PlayerInventory ->
            ChestScreenHandler(
                syncId = syncId,
                playerInventory = inventory
            )
        }
    }
}