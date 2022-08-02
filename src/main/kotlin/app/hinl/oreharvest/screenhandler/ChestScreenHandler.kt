package app.hinl.oreharvest.screenhandler

import app.hinl.oreharvest.blockentity.chest.BaseChestEntity
import app.hinl.oreharvest.utils.SyncPair
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot

class ChestScreenHandler(
    type: ScreenHandlerType<*>?,
    syncId: Int,
    playerInventory: PlayerInventory?,
    val blockEntity: BaseChestEntity,
    val buttonEntries: List<ButtonEntry>,
    val rows: Int = 6
) : ScreenHandler(type, syncId) {

    init {
        val expectedSize = rows * 9
        checkSize(blockEntity, expectedSize)
        blockEntity.onOpen(playerInventory?.player)
        val i: Int = (this.rows - 4) * 18
        for (j in 0 until rows) {
            for (k in 0 until 9) {
                this.addSlot(Slot(blockEntity, k + j * 9, 8 + k * 18, 18 + j * 18))
            }
        }
        for (j in 0 until 3) {
            for (k in 0 until 9) {
                addSlot(Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i))
            }
        }
        for (j in 0 until 9) {
            this.addSlot(Slot(playerInventory, j, 8 + j * 18, 161 + i))
        }
    }

    private val syncListener = mutableMapOf<String, SyncPair>()

    override fun transferSlot(player: PlayerEntity?, index: Int): ItemStack {
        var itemStack = ItemStack.EMPTY
        val slot = slots[index]
        if (slot != null && slot.hasStack()) {
            val itemStack2 = slot.stack
            itemStack = itemStack2.copy()
            if (index < rows * 9) {
                if (!insertItem(itemStack2, rows * 9, slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(itemStack2, 0, rows * 9, false)) {
                return ItemStack.EMPTY
            }
            if (itemStack2.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }
        return itemStack
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return blockEntity.canPlayerUse(player)
    }
}