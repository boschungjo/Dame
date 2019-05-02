package view;

import controller.DameApp;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * die Klasse, welche ein Feld und seine Eigenschaften beschreibt
 * 
 * @author boschungjo
 * @author radonjicl
 */
public class Feld extends Rectangle {

	private Stein stein;

	// Konstruktor
	public Feld(boolean leicht, int x, int y) {
		setWidth(DameApp.FELDGROESSE);
		setHeight(DameApp.FELDGROESSE);

		relocate(x * DameApp.FELDGROESSE, y * DameApp.FELDGROESSE);
		setFill(leicht ? Color.valueOf("#F5DEB3") : Color.valueOf("#994C00"));
	}

	/**
	 * Methode, die überprüft, ob ein Feld ein Stein enthält
	 */
	public boolean hatStein() {
		return stein != null;
	}

	// Getter
	public Stein getStein() {
		return stein;
	}

	// Setter
	public void setStein(Stein stein) {
		this.stein = stein;
	}

}
