package app.hinl.oreharvest.registry

import app.hinl.oreharvest.blockentity.chest.ChestOfAttractionBlockEntity
import app.hinl.oreharvest.blockentity.chest.ChestOfNaturalBlockEntity
import app.hinl.oreharvest.utils.ServerManager
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object ModServer {

    fun register() {
        ServerPlayNetworking.registerGlobalReceiver(ServerManager.keyIsActiveAttraction) { minecraftServer: MinecraftServer,
                                                                                           serverPlayerEntity: ServerPlayerEntity,
                                                                                           serverPlayNetworkHandler: ServerPlayNetworkHandler,
                                                                                           packetByteBuf: PacketByteBuf,
                                                                                           packetSender: PacketSender ->

            val blockPos: BlockPos = packetByteBuf.readBlockPos()
            val isActiveAttraction: Boolean = packetByteBuf.readBoolean()

            minecraftServer.execute {
                val blockEntity: BlockEntity = serverPlayerEntity.world.getBlockEntity(blockPos) ?: return@execute
                val blockState: BlockState = serverPlayerEntity.world.getBlockState(blockPos) ?: return@execute
                (blockEntity as? ChestOfAttractionBlockEntity)?.isActiveAttraction = isActiveAttraction
                serverPlayerEntity.world.updateListeners(blockPos, blockState, blockState, 3)
            }
        }

        ServerPlayNetworking.registerGlobalReceiver(ServerManager.keyIsActiveGrowth) { minecraftServer: MinecraftServer,
                                                                                       serverPlayerEntity: ServerPlayerEntity,
                                                                                       serverPlayNetworkHandler: ServerPlayNetworkHandler,
                                                                                       packetByteBuf: PacketByteBuf,
                                                                                       packetSender: PacketSender ->

            val blockPos: BlockPos = packetByteBuf.readBlockPos()
            val isActiveGrowth: Boolean = packetByteBuf.readBoolean()

            minecraftServer.execute {
                val blockEntity: BlockEntity = serverPlayerEntity.world.getBlockEntity(blockPos) ?: return@execute
                (blockEntity as? ChestOfNaturalBlockEntity)?.isActiveGrowth = isActiveGrowth
            }
        }

        ServerPlayNetworking.registerGlobalReceiver(ServerManager.keyIsActiveHarvest) { minecraftServer: MinecraftServer,
                                                                                        serverPlayerEntity: ServerPlayerEntity,
                                                                                        serverPlayNetworkHandler: ServerPlayNetworkHandler,
                                                                                        packetByteBuf: PacketByteBuf,
                                                                                        packetSender: PacketSender ->

            val blockPos: BlockPos = packetByteBuf.readBlockPos()
            val isActiveHarvest: Boolean = packetByteBuf.readBoolean()

            minecraftServer.execute {
                val blockEntity: BlockEntity = serverPlayerEntity.world.getBlockEntity(blockPos) ?: return@execute
                (blockEntity as? ChestOfNaturalBlockEntity)?.isActiveHarvest = isActiveHarvest
            }
        }
    }
}