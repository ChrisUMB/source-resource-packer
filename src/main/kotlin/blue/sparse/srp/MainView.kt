package blue.sparse.srp

import javafx.beans.property.*
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.stage.StageStyle
import tornadofx.*
import java.lang.Integer.max
import kotlin.math.min


class MainView : View("Source Resource Packer") {

	val controller by inject<MainController>()

	override val root = vbox(alignment = Pos.CENTER) {
		this.spacing = 5.0
		primaryStage.width = 800.0
		primaryStage.height = 600.0

		val packsWidth = primaryStage.width / 1.9
		val packsHeight = primaryStage.height / 2.0

		primaryStage.width = packsWidth + 25.0
		primaryStage.height = packsHeight + 25.0

		var packListView: ListView<String>? = null

		hbox(alignment = Pos.CENTER) {
			this.spacing = 5.0
			this.id = "packListBox"

			this.maxWidth = packsWidth
			this.maxHeight = packsHeight
			vbox {
				this.spacing = 3.0

				borderpane {
					left = vbox(alignment = Pos.CENTER) { label("Pack Order") }
					right = hbox(alignment = Pos.CENTER) {
						this.spacing = 5.0
						button("Pack") {
							setOnMouseClicked {
								find<PackView>().openModal(stageStyle = StageStyle.UTILITY)
							}
						}

						button("Refresh") {
							setOnMouseClicked {
								controller.refreshPackList()
							}
						}
					}

					bottom = label("This is the order in which resource packs will be packed into the games assets.")
				}

				packListView = listview {
					cellFormat {
						graphic = borderpane {
							val list = packListView!!
							val str = it
							this.left = label(str)
							this.right = hbox(alignment = Pos.CENTER, spacing = 5.0) {

								button("\uD83E\uDC15") {
									setOnMouseClicked {
										list.selectionModel.select(str)
										controller.move(list, -1)
									}
								}

								button("\uD83E\uDC17") {
									setOnMouseClicked {
										list.selectionModel.select(str)
										controller.move(list, +1)
									}
								}
							}
						}
					}

					items.bind(controller.packList) {
						it
					}

					bindSelected(controller.selectedPackName)
				}
			}
		}

		controller.refreshPackList()
	}
}

class MainController : Controller() {

	val selectedPackName = SimpleStringProperty()
	val packList = SimpleListProperty<String>()
	val packOrder = SimpleListProperty<String>()

	fun move(view: ListView<String>, dir: Int) {
		val string = selectedPackName.get() ?: return
		val model = view.selectionModel ?: return
		val index = model.selectedIndex

		val newIndex = min(view.items.size - 1, max(index + dir, 0))
		view.items.removeAt(index)
		view.items.add(newIndex, string)
		packOrder.set(view.items)
		model.select(string)
	}

	fun refreshPackList() {
		val listFiles = SRPFiles.RESOURCE_PACKS.listFiles()
		val list = mutableListOf<String>()
		for (file in listFiles) {
			if (file.isDirectory || file.extension == "zip") {
				list.add(file.name)
			}
		}

		packList.set(FXCollections.observableArrayList(list))

	}

}