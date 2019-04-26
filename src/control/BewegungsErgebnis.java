package control;

import view.Stein;

public class BewegungsErgebnis {

	private BewegungTyp typ;
	private Stein stein;

	public BewegungsErgebnis(BewegungTyp typ, Stein stein) {
		this.typ = typ;
		this.stein = stein;
	}

	public BewegungsErgebnis(BewegungTyp typ) {
		this(typ, null);
	}

	public BewegungTyp getTyp() {
		return typ;
	}

	public Stein getStein() {
		return stein;
	}

}
