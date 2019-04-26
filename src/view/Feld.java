package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import application.DameApp;

public class Feld extends Rectangle {

	private Stein stein;
	
	//Konstruktor
	public Feld(boolean leicht, int x, int y) {
		setWidth(DameApp.FELDGROESSE);
		setHeight(DameApp.FELDGROESSE);

		relocate(x * DameApp.FELDGROESSE, y * DameApp.FELDGROESSE);
		setFill(leicht ? Color.valueOf("#F5DEB3") : Color.valueOf("#994C00"));
	}

	public boolean hatStein() {
		return stein != null;
	}

	

	public Stein getStein() {
		return stein;
	}

	public void setStein(Stein stein) {
		this.stein = stein;
	}

}
