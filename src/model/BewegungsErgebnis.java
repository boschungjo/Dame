package model;

import view.Stein;

/**
 * 
 * Die Klasse, welche das Erstellen von BewegungsErgebnissen aus versuchten
 * Bewegungen ermöglicht. Ein Bewegungsergebnis enthält einen bestimmten
 * Bewegungstyp.
 * 
 * @author boschungjo
 * @author radonjicl
 */

public class BewegungsErgebnis {

	private BewegungTyp typ;
	private Stein stein;

	// Konstruktor
	public BewegungsErgebnis(BewegungTyp typ, Stein stein) {
		this.typ = typ;
		this.stein = stein;
	}

	// Konstruktor
	public BewegungsErgebnis(BewegungTyp typ) {
		this(typ, null);
	}

	// Getter
	public BewegungTyp getTyp() {
		return typ;
	}

	public Stein getStein() {
		return stein;
	}

}
