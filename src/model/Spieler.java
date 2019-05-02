package model;

/**
 * 
 * die Klasse welche einen Spieler und seine Eigenschaften beschreibt
 * 
 * @author boschungjo
 * @author radonjicl
 */
public class Spieler {
	private boolean anDerReihe;
	private SteinFarbe farbe;
	private String name;

	// Konstruktor
	public Spieler(SteinFarbe farbe, boolean anDerReihe) {
		this.farbe = farbe;
		this.anDerReihe = anDerReihe;
	}

	// Getter und Setter
	public boolean getAnDerReihe() {
		return anDerReihe;
	}

	public void setAnDerReihe(boolean anDerReihe) {
		this.anDerReihe = anDerReihe;
	}

	public SteinFarbe getFarbe() {
		return farbe;
	}

	public void setFarbe(SteinFarbe farbe) {
		this.farbe = farbe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
