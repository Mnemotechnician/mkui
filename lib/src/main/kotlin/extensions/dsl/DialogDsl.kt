package com.github.mnemotechnician.mkui.extensions.dsl

import arc.scene.ui.Dialog
import arc.scene.ui.Dialog.DialogStyle
import arc.scene.ui.layout.Table
import mindustry.ui.Styles
import mindustry.ui.dialogs.BaseDialog

/**
 * Creates a dialog using [constructor] and passes it to the [configurator] lambda for it to configure the dialog.
 *
 * The created dialog can then be shown using the [Dialog.show] function.
 *
 * @param constructor creates an instance of [Dialog] using the providen title and style, by default it creates an [arc.scene.ui.Dialog].
 * @param configurator configures the created dialog, adding ui elements, etc. Its receiver is the dialog's container.
 * @param addCloseButton whether a "close" button should be added to [Dialog.buttons].
 * @param closeOnBack whether the dialog should close when <esc> or <back> is pressed.
 */
inline fun createDialog(
	title: String = "",
	style: DialogStyle = Styles.defaultDialog,
	addCloseButton: Boolean = false,
	closeOnBack: Boolean = true,
	constructor: (title: String, style: DialogStyle) -> Dialog = { t, s -> Dialog(t, s) },
	configurator: Table.(Dialog) -> Unit
): Dialog {
	return constructor(title, style).also {
		if (addCloseButton) it.addCloseButton()
		if (closeOnBack) it.closeOnBack()
		configurator(it.cont, it)
	}
}

/**
 * Similar to [createDialog], but creates a BaseDialog.
 * @see createDialog
 */
inline fun createBaseDialog(
	title: String = "",
	style: DialogStyle = Styles.defaultDialog,
	addCloseButton: Boolean = false,
	closeOnBack: Boolean = true,
	crossinline configurator: Table.(BaseDialog) -> Unit
) = createDialog(
	title, style, addCloseButton, closeOnBack, { t, s -> BaseDialog(t, s) }, {
		(it as BaseDialog).let { configurator(it.cont, it) }
	}
) as BaseDialog
