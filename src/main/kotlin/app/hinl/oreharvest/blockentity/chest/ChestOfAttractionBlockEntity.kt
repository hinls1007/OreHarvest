package app.hinl.oreharvest.blockentity.chest

import app.hinl.oreharvest.blockentity.BaseTickerBlockEntity
import app.hinl.oreharvest.registry.ModBlock
import app.hinl.oreharvest.registry.ModScreen
import app.hinl.oreharvest.screenhandler.ButtonEntry
import app.hinl.oreharvest.utils.ServerManager
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.Packet
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import java.util.*

open class ChestOfAttractionBlockEntity(
    type: BlockEntityType<*>? = ModBlock.chestOfAttractionBlockEntity,
    pos: BlockPos?,
    state: BlockState?
) : BaseChestEntity(type, pos, state) {

    val uuid = UUID.randomUUID()

    var isActiveAttraction = true
        set(value) {
            field = value
            markDirty()
        }

    private val keyIsActiveAttraction = "isActiveAttraction"
    private val pickupDelay = 20


    override fun getScreenType(): ScreenHandlerType<*> {
        return ModScreen.chestOfAttractionScreenHandlerType
    }

    override fun getDisplayName(): Text {
        return TranslatableText("block.oreharvest.chest_attraction")
    }

    override fun getButtonEntries(): List<ButtonEntry> {
        val buttonEntries = mutableListOf<ButtonEntry>()
        buttonEntries.add(
            ButtonEntry(
                isChecked = isActiveAttraction,
                buttonText = LiteralText("A"),
                buttonToolTips = TranslatableText("gui.tooltip.activate_attraction")
            ) {
                ServerManager.setIsAttractionActive(blockPos = pos, isActiveAttraction = it)
                isActiveAttraction = it
            }
        )
        return buttonEntries
    }

    override fun tick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BaseTickerBlockEntity?) {
        world ?: return
        if (world.isClient) return
        if (!isActiveAttraction) return
        val radius = 10
        val entityItems = world.getEntitiesByClass(
            ItemEntity::class.java,
            Box(pos).expand(radius.toDouble()),
            EntityPredicates.VALID_ENTITY
        )

        entityItems.forEach { entity ->
            if (entity.age >= pickupDelay && (entity.owner == null || entity.owner == uuid) && entity.thrower != uuid) {
                entity.velocity = Box(pos).center.subtract(entity.pos).normalize()
            }
        }
    }

    fun addStack(entity: ItemEntity) {
        val remainingStack = addStackInternal(entity.stack)
        if (remainingStack.isEmpty) {
            entity.remove(Entity.RemovalReason.DISCARDED)
        } else {
            entity.thrower = uuid
            entity.stack = remainingStack
            dropItem(entity)
        }
    }

    private fun dropItem(itemEntity: ItemEntity) {
        val random = Random()
        val f = random.nextFloat() * 0.5f
        val g = random.nextFloat() * 6f
        itemEntity.setVelocity(
            (-MathHelper.sin(g) * f).toDouble(),
            0.2,
            (MathHelper.cos(g) * f).toDouble()
        )
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        isActiveAttraction = nbt?.getBoolean(keyIsActiveAttraction) ?: true
    }

    override fun writeNbt(nbt: NbtCompound?) {
        nbt?.putBoolean(keyIsActiveAttraction, isActiveAttraction)
        super.writeNbt(nbt)
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        val nbtCompound = super.toInitialChunkDataNbt()
        writeNbt(nbtCompound)
        return nbtCompound
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener>? {
        return BlockEntityUpdateS2CPacket.create(this)
    }
}