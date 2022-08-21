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

    private var rangeRadius = 10
    private var rangeHeight = 10

    private val keyIsActiveGrowth = "isActiveGrowth"
    private val keyIsActiveBreak = "isActiveBreak"

    var isActiveGrowth = true
        set(value) {
            field = value
            markDirty()
        }
    var isActiveHarvest = true
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
                buttonText = MutableText.of(LiteralTextContent("G")),
                buttonToolTips = Text.translatable("gui.tooltip.activate_growth")
            ) {
                ServerManager.setIsGrowthActive(blockPos = pos, isActiveGrowth = it)
                isActiveGrowth = it
            }
        )
        buttonEntry.add(
            ButtonEntry(
                isChecked = isActiveHarvest,
                buttonText = MutableText.of(LiteralTextContent("H")),
                buttonToolTips = Text.translatable("gui.tooltip.activate_harvest")
            ) {
                ServerManager.setIsHarvestActive(blockPos = pos, isActiveHarvest = it)
                isActiveHarvest = it
            }
        )
        return buttonEntry
    }

    override fun tick(world: World?, pos: BlockPos?, state: BlockState?, blockEntity: BaseTickerBlockEntity?) {
        world ?: return
        pos ?: return
        if (world.isClient) return
        if (isActiveGrowth && growthCounter <= 0) {
            BlockActionHelper.applyGrowth(
                world = world, pos = pos,
                radius = rangeRadius,
                height = rangeHeight)
            growthCounter = growthDelay
        } else {
            growthCounter--
        }
        if (isActiveHarvest && !isFull()) {
            BlockActionHelper.applyBreakCrops(
                world = world, pos = pos,
                radius = rangeRadius,
                height = rangeHeight)
        }
        super.tick(world, pos, state, blockEntity)
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        isActiveGrowth = nbt?.getBoolean(keyIsActiveGrowth) ?: true
        isActiveHarvest = nbt?.getBoolean(keyIsActiveBreak) ?: true
    }

    override fun writeNbt(nbt: NbtCompound?) {
        nbt?.putBoolean(keyIsActiveGrowth, isActiveGrowth)
        nbt?.putBoolean(keyIsActiveBreak, isActiveHarvest)
        super.writeNbt(nbt)
    }
}