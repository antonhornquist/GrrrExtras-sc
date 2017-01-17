GRGroup[slot] : Array {
	play { |viewKeyMap|
	}
}

// TODO: direction: ping-pong, reversed, forward, random, by index / another pattern ? or introduce GRPrand and GRPindex for the last use cases
GRPseq {
	*new { |view, repeats=1, transform, indicate=true|
		^(
			case
			{ view.isKindOf(GRMultiToggleView) } {
				this.newMultiToggleView(view, repeats, transform, indicate)
			} ?? {
				this.newView(view, repeats, transform, indicate)
			}
		)
	}

	// TODO: merge with asGRPseq methods below
	*newView { |view, repeats=1, transform, indicate=true|
		^Prout({
			repeats.do { |i|
				var origval, val;
				origval = view.value;
				val = origval ? \rest;
				indicate.if { SystemClock.sched(Server.default.latency, { view.flash(100) }) };
				(
					case
					{ transform.isKindOf(Function) } {
						transform.(val, i)
					}
					{ transform.isKindOf(Spec) } {
						if (origval.isNil) {
							val
						} {
							transform.map(view.asSpec.unmap(val))
						}
					} ?? {
						val
					}
				).yield
			};
			nil.yield
		})
	}

	// TODO: merge with asGRPseq methods below
	*newMultiToggleView { |multiToggleView, repeats=1, transform, indicate=true|
		^Prout({
			repeats.do { |i|
				multiToggleView.numToggles.do { |togglenum|
					var origval, val;
					origval = multiToggleView.toggleValue(togglenum);
					val = origval ? \rest;
					indicate.if { SystemClock.sched(Server.default.latency, { multiToggleView.flashToggle(togglenum, 100) }) };
					(
						case
						{ transform.isKindOf(Function) } {
							transform.(val, i)
						}
						{ transform.isKindOf(Spec) } {
							if (origval.isNil) {
								val
							} {
								transform.map(multiToggleView.asSpec.unmap(val))
							}
						} ?? {
							val
						}
					).yield
				};
			};
			nil.yield
		})
	}
}

+ GRView {
	asGRPseq { |repeats=1, transform, indicate=true|
		^Prout({
			repeats.do { |i|
				var origval, val;
				origval = this.value;
				val = origval ? \rest;
				indicate.if { SystemClock.sched(Server.default.latency, { this.flash(100) }) };
				(
					case
					{ transform.isKindOf(Function) } {
						transform.(val, i)
					}
					{ transform.isKindOf(Spec) } {
						if (origval.isNil) {
							val
						} {
							transform.map(this.asSpec.unmap(val))
						}
					} ?? {
						val
					}
				).yield
			};
			nil.yield
		})
	}
}

+ GRMultiButtonView {
	asGRPseq { |repeats=1, transform, indicate=true, orientation=\horizontal|
		^Prout({
			repeats.do { |i|
				case
				{ orientation == \horizontal } {
					this.numButtonsCols.do { |buttoncol|
					};
				}
				{ orientation == \vertical } {
					this.numButtonsRows.do { |buttonrow|
					};
				}
			};
			nil.yield
		})
	}

	asGRPseqs { |repeats=1, transform, indicate=true, orientation=\horizontal|
		// TODO
	}

	// TODO: asPbind and play can probably be put in GRView
	asPbind { |key=\degree, repeats=inf, transform, indicate=true|
		^Pbind(*[
			key, GRPseq(this, repeats, transform, indicate)
		])
	}

	// TODO: asPbind and play can probably be put in GRView
	play { |clock, protoEvent, quant, key=\degree, repeats=inf, transform, indicate=true|
		^this.asPbind(key, repeats, transform, indicate).play(clock, protoEvent, quant)
	}
}

+ GRMultiToggleView {
	asGRPseq { |repeats=1, transform, indicate=true|
		^Prout({
			repeats.do { |i|
				this.numToggles.do { |togglenum|
					var origval, val;
					origval = this.toggleValue(togglenum);
					val = origval ? \rest;
					indicate.if { SystemClock.sched(Server.default.latency, { this.flashToggle(togglenum, 100) }) };
					(
						case
						{ transform.isKindOf(Function) } {
							transform.(val, i)
						}
						{ transform.isKindOf(Spec) } {
							if (origval.isNil) {
								val
							} {
								transform.map(this.asSpec.unmap(val))
							}
						} ?? {
							val
						}
					).yield
				};
			};
			nil.yield
		})
	}

	// TODO: asPbind and play can probably be put in GRView
	asPbind { |key=\degree, repeats=inf, transform, indicate=true|
		^Pbind(*[
			key, GRPseq(this, repeats, transform, indicate)
		])
	}

	// TODO: asPbind and play can probably be put in GRView
	play { |clock, protoEvent, quant, key=\degree, repeats=inf, transform, indicate=true|
		^this.asPbind(key, repeats, transform, indicate).play(clock, protoEvent, quant)
	}
}

+ GRKeyboard {
	// TODO: asGRPseq to work depending on keyboard modes: if select and chord modes are introduced GRPseq may have the option to play cycled notes (the default), chord, various kinds of arpeggiation, all with indications on the GRKeyboard view
	// TODO 2: for all other views, more sequence modes may also be applicable, such as cycled selections currently available (x, y pairs for GRMultiButtonView, etc)
}
