package app.hinl.oreharvest.blockentity.chest

import app.hinl.oreharvest.blockentity.BaseTickerBlockEntity
import app.hinl.oreharvest.registry.ModBlock
import app.hinl.oreharvest.registry.ModScreen
import app.hinl.oreharvest.screenhandler.ButtonEntry
import app.hinl.oreharvest.utils.BlockActionHelper
import app.hinl.oreharvest.utils.ServerManager
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.LiteralTextContent
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ChestOfNaturalBlockEntity(pos: BlockPos?, state: BlockState?) :
    ChestOfAttractionBlockEntity(type = ModBlock.chestOfNaturalBlockEntityType, pos = pos, state = state) {

    private val keyIsActiveGrowth = "isActiveGrowth"
    private val keyIsActiveBreak = "isActiveBreak"

    var isActiveGrowth = true
        set(value) {
            field = value
            markDirty()
        }
    var isActiveBreak = true
        set(value) {
            field = value
            markDirty()
        }

    private val growthDelay = 20
    private var growthCounter = growthDelay

    override fun getScreenType(): ScreenHandlerType<*> {
        return ModScreen.chestOfNaturalScreenHandlerType
    }

    override fun getDisplayName(): Text {
        return Text.translatable("block.oreharvest.chest_natural")
    }

    override fun getButtonEntries(): List<ButtonEntry> {
        val buttonEntry = mutableListOf<ButtonEntry>()
        buttonEntry.addAll(super.getButtonEntries())
        buttonEntry.add(
            ButtonEntry(
                isChecked = isActiveGrowth,
                buttonText = MutableText.of(LiteralTextContent("Growth"))
            ) {
                ServerManager.setIsGrowthActive(blockPos = pos, isActiveGrowth = it)
                isActiveGrowth = it
            }
        )
        buttonEntry.add(
            ButtonEntry(
                isChecked = isActiveBreak,
                buttonText = MutableText.of(LiteralTextContent("Break Crops"))
            ) {
                ServerManager.setIsBreakActive(blockPos = pos, isActiveBreak = it)
                isActiveBreak = it
            }
        )
        return buttonEntry
    }

    override fun tick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BaseTickerBlockEntity?) {
        world ?: return
        pos ?: return
        if (world.isClient) return
        if (isActiveGrowth && growthCounter <= 0) {
            BlockActionHelper.applyGrowth(world = world, pos = pos, radius = 10)
            growthCounter = growthDelay
        } else {
            growthCounter--
        }
        if (isActiveBreak && !isFull()) {
            BlockActionHelper.applyBreakCrops(world = world, pos = pos, radius = 10)
        }
        super.tick(world, pos, state, blockEntity)
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        isActiveGrowth = nbt?.getBoolean(keyIsActiveGrowth) ?: true
        isActiveBreak = nbt?.getBoolean(keyIsActiveBreak) ?: true
    }

    override fun writeNbt(nbt: NbtCompound?) {
        nbt?.putBoolean(keyIsActiveGrowth, isActiveGrowth)
        nbt?.putBoolean(keyIsActiveBreak, isActiveBreak)
        super.writeNbt(nbt)
    }
}