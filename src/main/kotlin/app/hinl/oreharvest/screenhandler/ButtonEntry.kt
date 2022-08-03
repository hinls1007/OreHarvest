package app.hinl.oreharvest.screenhandler

import net.minecraft.text.Text

data class ButtonEntry(
    val isChecked: Boolean,
    val buttonText: Text? = null,
    val buttonToolTips: Text? = null,
    val buttonAction: (Boolean) -> Unit
)
