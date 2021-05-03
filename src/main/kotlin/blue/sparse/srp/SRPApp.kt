package blue.sparse.srp

import javafx.stage.Stage
import tornadofx.*
import kotlin.reflect.KClass

class SRPApp : App(NoPrimaryViewSpecified::class, SRPStylesheet::class) {

	init {
		reloadStylesheetsOnFocus()
	}

	override val primaryView: KClass<out UIComponent>
		get() {
			if (!RSPFiles.DATA.exists()) {
				return SetupView::class
			}

			return MainView::class
		}

	override fun start(stage: Stage) {
		if (primaryView == SetupView::class) {
			stage.width = 400.0
			stage.height = 200.0
		} else if (primaryView == MainView::class) {
			stage.width = 800.0
			stage.height = 600.0
		}

		stage.icons += resources.image("/icon.png")
		super.start(stage)
	}

//	override fun stop() {
//		exitProcess(420)
//	}
}