package app.hinl.oreharvest.screenhandler

import app.hinl.oreharvest.Constants
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider
import net.minecraft.client.gui.widget.CheckboxWidget
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ChestScreen(handler: ChestScreenHandler, inventory: PlayerInventory?, title: Text?) :
    HandledScreen<ChestScreenHandler>(handler, inventory, title), ScreenHandlerProvider<ChestScreenHandler> {

    private val texture = Identifier(Constants.ModID, "textures/screen/chest_base.png")
    private val rows: Int
    private val buttonEntries = handler.buttonEntries
    private val buttonList = mutableListOf<CheckboxWidget>()

    init {
        rows = handler.rows
        backgroundHeight = 114 + this.rows * 18
        backgroundWidth = 215
        playerInventoryTitleY = this.backgroundHeight - 94
    }

    override fun init() {
        val initX = (width - backgroundWidth) / 2
        val initY = (height - backgroundHeight) / 2

        buttonEntries.forEachIndexed { index, buttonEntry ->
            val buttonY = initY + 17 + (index * 18 * 2)
            val button = OptionCheckbox(
                x = initX + 180,
                y = buttonY,
                width = 12,
                height = 12,
                message = buttonEntry.buttonText,
                checked = buttonEntry.isChecked,
                clickListener = buttonEntry.buttonAction
            )
            buttonList.add(button)
            addSelectableChild(button)
        }

        super.init()
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
        buttonList.forEach {
            it.render(matrices, mouseX, mouseY, delta)
        }
        drawMouseoverTooltip(matrices, mouseX, mouseY)
    }

    override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, texture)
        val i = (width - backgroundWidth) / 2
        val j = (height - backgroundHeight) / 2
        this.drawTexture(matrices, i, j, 0, 0, backgroundWidth, rows * 18 + 17)
        this.drawTexture(matrices, i, j + rows * 18 + 17, 0, 126, backgroundWidth, 96)
    }

    override fun drawForeground(matrices: MatrixStack?, mouseX: Int, mouseY: Int) {
        super.drawForeground(matrices, mouseX, mouseY)
    }

    class OptionCheckbox(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        message: Text?,
        checked: Boolean,
        showMessage: Boolean = false,
        private val clickListener: (Boolean) -> Unit
    ) : CheckboxWidget(x, y, width, height, message, checked, showMessage) {
        override fun onClick(mouseX: Double, mouseY: Double) {
            super.onClick(mouseX, mouseY)
            clickListener.invoke(isChecked)
        }
    }
}