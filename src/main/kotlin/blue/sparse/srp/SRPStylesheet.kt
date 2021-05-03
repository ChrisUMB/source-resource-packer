package blue.sparse.srp

import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.px

class SRPStylesheet : Stylesheet() {

	init {
		root {
			backgroundColor += Color.rgb(40, 44, 52)
		}

		text {
//			this.fontSize = 24.px
			this.fill = Color.rgb(171, 178, 191)
		}

		menu {
		}

		menuBar {
			this.selectionBarText = Color.rgb(255, 0, 0)
			this.backgroundColor += Color.rgb(33, 37, 43)
			this.textFill = Color.rgb(171, 178, 191)
		}

		menuButton {
			and(hover) {
				this.backgroundColor += Color.rgb(33, 37, 43).darker()
			}

			and(focused) {
				this.backgroundColor += Color.rgb(33, 37, 43).darker()
			}

			and(showing) {
				this.backgroundColor += Color.rgb(33, 37, 43).darker()
			}
		}

		menuItem {
			and(focused) {
				this.backgroundColor += Color.rgb(33, 37, 43).darker()
			}
		}

		contextMenu {
			this.backgroundColor += Color.rgb(33, 37, 43)
		}

		progressBar {
			this.accentColor = Color.color(0.0, 0.33, 1.0)
			this.borderRadius += box(0.px)
			this.maxHeight = 24.px
			this.padding = box(0.px)
			this.labelPadding = box(0.px)
			this.backgroundInsets += box(0.px)
			this.backgroundRadius += box(0.px)
		}

		button {
			this.backgroundColor += Color.rgb(33, 37, 43).brighter()
//			this.borderColor += box(Color.rgb(33, 37, 43))
			this.textFill = Color.rgb(171, 178, 191)
		}

		listView {
			odd {
				this.backgroundColor += Color.rgb(33, 37, 43)
			}

			even {
				this.backgroundColor += Color.rgb(33, 37, 43).darker()
			}

			"*" {
//				and(selected) {
//					this.backgroundColor += Color.rgb(33, 37, 43).brighter()
//				}
			}

			this.backgroundColor += Color.rgb(33, 37, 43)
			this.borderColor += box(Color.rgb(33, 37, 43).darker().darker())
		}

		"#packListBox" {
			this.backgroundColor += Color.rgb(33, 37, 43).desaturate()
			this.borderColor += box(Color.rgb(33, 37, 43).darker())
			this.borderWidth += box(2.px)
			this.padding = box(2.px)
		}

	}

}