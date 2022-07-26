package app.hinl.oreharvest.screenhandler

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ChestScreen(handler: ChestScreenHandler, inventory: PlayerInventory?, title: Text?) :
    HandledScreen<ChestScreenHandler>(handler, inventory, title), ScreenHandlerProvider<ChestScreenHandler> {

    private val texture = Identifier("textures/gui/container/generic_54.png")
    private val rows: Int

    init {
        rows = handler.rows
        backgroundHeight = 114 + this.rows * 18
        playerInventoryTitleY = this.backgroundHeight - 94
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
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
}