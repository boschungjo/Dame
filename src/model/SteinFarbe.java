package model;

/**
 * 
 * Die Farben der beiden Spieler. Eine SteinFarbe enthält eine bestimmte
 * Richtung entlang der y-Achse.
 * 
 * @author boschungjo
 * @author
 */
public enum SteinFarbe {
	ROT(1), WEISS(-1);

	public final int richtung;

	// Konstruktor
	SteinFarbe(int richtung) {
		this.richtung = richtung;
	}
}
