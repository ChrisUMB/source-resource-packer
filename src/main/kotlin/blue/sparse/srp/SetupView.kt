package blue.sparse.srp

import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import tornadofx.*

class SetupView : View("Source Resource Packer") {

	private val controller: SetupController by inject()

	private lateinit var assetLabel: Label
	private lateinit var progress: ProgressBar

	override val root = vbox(alignment = Pos.CENTER) {
		label("Extracting default resources...") {
			style {
				fontSize = 24.px
			}
		}

		label("This might take a few minutes, but it only happens once.") {
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

			progress = progressbar(controller.extractionProgress) {
				maxWidth = 325.0
			}
		}

		runAsync {
			controller.extractResources()
		}.ui {
//			MainView().openWindow()
//			close()
			replaceWith(MainView::class)
		}
	}
}

class SetupController : Controller() {

	val extractionProgress = SimpleDoubleProperty(0.0)
	val assetName = SimpleStringProperty("")

	fun extractResources() {
		SRP.extractDefaultResources { name, progress ->
			Platform.runLater {
				extractionProgress.value = progress
				assetName.value = "Asset \"$name\""
			}
		}

		SRPFiles.makeDirectories()
	}
}

fun interface ResourceProgress {
	fun apply(asset: String, progress: Double)
}