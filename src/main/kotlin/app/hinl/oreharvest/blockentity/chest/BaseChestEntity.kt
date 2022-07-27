package app.hinl.oreharvest.blockentity.chest

import app.hinl.oreharvest.blockentity.BaseTickerBlockEntity
import app.hinl.oreharvest.screenhandler.ChestScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos


abstract class BaseChestEntity(type: BlockEntityType<*>?, pos: BlockPos?, state: BlockState?) :
    BaseTickerBlockEntity(type, pos, state), NamedScreenHandlerFactory, Inventory {

    private val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(6 * 9, ItemStack.EMPTY)

    override fun createMenu(syncId: Int, inv: PlayerInventory?, player: PlayerEntity?): ScreenHandler {
        return ChestScreenHandler(syncId = syncId, playerInventory = inv, this)
    }

    override fun clear() {
        inventory.clear()
    }

    override fun size(): Int {
        return inventory.size
    }

    override fun isEmpty(): Boolean {
        inventory.forEach { itemStack ->
            if (!itemStack.isEmpty) {
                return false
            }
        }
        return true
    }

    protected fun isFull(itemStack: ItemStack? = null): Boolean {
        inventory.forEach { inventoryItemStack ->
            if (inventoryItemStack.isEmpty
                || itemStack?.let {
                    canStackAddMore(inventoryItemStack, itemStack)
                } == true
            ) {
                return false
            }
        }
        return true
    }

    fun addStackInternal(itemStack: ItemStack): ItemStack {
        if (isFull(itemStack = itemStack)) return itemStack

        while (!isFull(itemStack = itemStack) && itemStack.count != 0) {
            val slotWithStackIndex = getSlotWithStack(itemStack = itemStack)
            if (slotWithStackIndex != -1) {
                val remainingCount = itemStack.maxCount - inventory[slotWithStackIndex].count
                if (remainingCount >= itemStack.count) {
                    inventory[slotWithStackIndex].count = inventory[slotWithStackIndex].count + itemStack.count
                    itemStack.count = 0
                } else {
                    inventory[slotWithStackIndex].count = itemStack.maxCount
                    itemStack.split(remainingCount)
                }
            } else {
                val emptySlotIndex = getEmptySlotIndex()
                if (emptySlotIndex != -1) {
                    setStack(emptySlotIndex, itemStack.copy())
                    itemStack.count = 0
                }
            }
        }
        markDirty()
        return itemStack
    }

    private fun getSlotWithStack(itemStack: ItemStack): Int {
        inventory.forEachIndexed { index, inventoryItemStack ->
            if (!inventoryItemStack.isEmpty
                && ItemStack.canCombine(inventoryItemStack, itemStack)
                && canStackAddMore(inventoryItemStack, itemStack)
            ) {
                return index
            }
        }
        return -1
    }

    private fun getEmptySlotIndex(): Int {
        return inventory.indexOfFirst { itemStack: ItemStack -> itemStack.isEmpty }
    }

    private fun canStackAddMore(existingStack: ItemStack, stack: ItemStack): Boolean {
        return !existingStack.isEmpty && ItemStack.canCombine(
            existingStack,
            stack
        ) && existingStack.isStackable && existingStack.count < existingStack.maxCount && existingStack.count < this.maxCountPerStack
    }

    override fun getStack(slot: Int): ItemStack {
        return inventory[slot]
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        val result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty) {
            markDirty();
        }
        return result
    }

    override fun removeStack(slot: Int): ItemStack {
        return Inventories.removeStack(inventory, slot)
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        inventory[slot] = stack;
        if (stack.count > stack.maxCount) {
            stack.count = stack.maxCount;
        }
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean {
        return true
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        Inventories.readNbt(nbt, inventory)
    }

    override fun writeNbt(nbt: NbtCompound?) {
        Inventories.writeNbt(nbt, inventory)
        super.writeNbt(nbt)
    }
}