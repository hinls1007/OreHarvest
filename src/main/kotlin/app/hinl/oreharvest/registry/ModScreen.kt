package app.hinl.oreharvest.registry

import app.hinl.oreharvest.Constants
import app.hinl.oreharvest.screenhandler.ChestScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ModScreen {
    lateinit var chestOfAttractionScreenHandlerType: ScreenHandlerType<ChestScreenHandler>
    lateinit var chestOfNaturalScreenHandlerType: ScreenHandlerType<ChestScreenHandler>

    fun register() {
        chestOfAttractionScreenHandlerType = Registry.register(
            Registry.SCREEN_HANDLER,
            Identifier(Constants.ModID, "chest_attraction_screen_handler"),
            ExtendedScreenHandlerType { syncId: Int, inventory: PlayerInventory, packetByteBuf: PacketByteBuf ->
                val blockEntity: BlockEntity? = inventory.player.world.getBlockEntity(packetByteBuf.readBlockPos())
                (blockEntity as? ExtendedScreenHandlerFactory)?.createMenu(
                    syncId,
                    inventory,
                    inventory.player
                ) as ChestScreenHandler
            }
        )

        chestOfNaturalScreenHandlerType = Registry.register(
            Registry.SCREEN_HANDLER,
            Identifier(Constants.ModID, "chest_natural_screen_handler"),
            ExtendedScreenHandlerType { syncId: Int, inventory: PlayerInventory, packetByteBuf: PacketByteBuf ->
                val blockEntity: BlockEntity? = inventory.player.world.getBlockEntity(packetByteBuf.readBlockPos())
                (blockEntity as? ExtendedScreenHandlerFactory)?.createMenu(
                    syncId,
                    inventory,
                    inventory.player
                ) as ChestScreenHandler
            }
        )
    }
}