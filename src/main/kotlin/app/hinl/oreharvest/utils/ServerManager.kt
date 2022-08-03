package app.hinl.oreharvest.utils

import app.hinl.oreharvest.Constants
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object ServerManager {

    val keyIsActiveAttraction = Identifier(Constants.ModID, "active_attraction")
    val keyIsActiveGrowth = Identifier(Constants.ModID, "active_growth")
    val keyIsActiveHarvest = Identifier(Constants.ModID, "active_harvest")

    fun setIsAttractionActive(blockPos: BlockPos, isActiveAttraction: Boolean) {
        sendPackageToServer(keyIsActiveAttraction) { buffer ->
            buffer.writeBlockPos(blockPos)
            buffer.writeBoolean(isActiveAttraction)
        }
    }

    fun setIsGrowthActive(blockPos: BlockPos, isActiveGrowth: Boolean) {
        sendPackageToServer(keyIsActiveGrowth) { buffer ->
            buffer.writeBlockPos(blockPos)
            buffer.writeBoolean(isActiveGrowth)
        }
    }

    fun setIsHarvestActive(blockPos: BlockPos, isActiveHarvest: Boolean) {
        sendPackageToServer(keyIsActiveHarvest) { buffer ->
            buffer.writeBlockPos(blockPos)
            buffer.writeBoolean(isActiveHarvest)
        }
    }

    fun sendPackageToServer(identifier: Identifier, actions: (PacketByteBuf) -> Unit) {
        val buffer = PacketByteBufs.create()
        actions.invoke(buffer)
        ClientPlayNetworking.send(identifier, buffer)
    }
}