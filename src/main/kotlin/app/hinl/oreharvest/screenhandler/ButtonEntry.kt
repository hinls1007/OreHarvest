package app.hinl.oreharvest.screenhandler

import net.minecraft.text.Text

data class ButtonEntry(val isChecked: Boolean, val buttonText: Text, val buttonAction: (Boolean) -> Unit)
