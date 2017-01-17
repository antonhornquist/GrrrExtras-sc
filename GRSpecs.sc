+ GRButton {
	asSpec { ^[0, 1, \lin, 1, 0].asSpec }
}

+ GRToggle {
	asSpec { ^[0, this.maximumValue, \lin, 1, 0].asSpec }
}

+ GRMultiButtonView {
	asSpec { ^buttons.first.asSpec }
}

+ GRMultiToggleView {
	asSpec { ^toggles.first.asSpec }
}
