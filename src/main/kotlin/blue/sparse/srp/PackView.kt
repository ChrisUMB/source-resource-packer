package blue.sparse.srp

import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import tornadofx.*
import java.io.File

class PackView : Fragment("Packing...") {

	val controller by inject<PackController>()

	private lateinit var assetLabel: Label
	private lateinit var progress: ProgressBar

	private val extra = SimpleStringProperty("This may take a few minutes.")

	override val root = vbox(alignment = Pos.CENTER) {
		minWidth = 400.0
		minHeight = 200.0

		val mainView = find<MainView>()
		val list = mainView.controller.packOrder.get()

		runAsync {
			controller.packDefault()

			for (s in list) {
				if (mainView.controller.packExempt.contains(s)) {
					continue
				}

				controller.pack(s)
			}
		}.ui {
			controller.step.value = "Finished."
			controller.assetName.value = ""
			extra.value = "You can close this window."
			controller.packingProgress.value = 1.0
		}

		label(controller.step) {
			style {
				fontSize = 24.px
			}
		}

		label(extra) {
			style {
				paddingBottom = 20
			}
		}

		vbox(alignment = Pos.CENTER) {
			this.maxWidth = 325.0

			hbox(alignment = Pos.CENTER_LEFT) {
				assetLabel = label(controller.assetName) {
					style {
						paddingBottom = 2
					}
				}
			}

			progress = progressbar(controller.packingProgress) {
				maxWidth = 325.0
			}
		}
	}
}

class PackController : Controller() {

	val step = SimpleStringProperty("Starting packer...")

	val packingProgress = SimpleDoubleProperty(0.0)
	val assetName = SimpleStringProperty("")

	fun packDefault() {
		Platform.runLater {
			step.value = "Packing default textures..."
		}

		RSPPacker.packDefaultResources { name, progress ->
			Platform.runLater {
				packingProgress.value = progress
				assetName.value = name
			}
		}
	}

	fun pack(name: String) {
		Platform.runLater {
			step.value = "Packing \"${name}\"..."
		}

		val f = File(RSPFiles.RESOURCE_PACKS, name)
		println("Packing $name")
		RSPPacker.pack(f) { name, progress ->
			Platform.runLater {
				packingProgress.value = progress
				assetName.value = "Asset \"$name\""
			}
		}
	}
}