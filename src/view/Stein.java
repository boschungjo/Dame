package view;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;

import static application.DameApp.FELDGROESSE;

public class Stein extends StackPane {
	private SteinTyp typ;

	private double mausX, mausY;
	private double altX, altY;

	// Konstruktor
	public Stein(SteinTyp typ, int x, int y) {
		this.typ = typ;
		
		bewege(x, y);

		// SteinHintergrund erzeugen
		Ellipse bg = new Ellipse(FELDGROESSE * 0.3125, FELDGROESSE * 0.26);
		bg.setFill(Color.BLACK);

		// Schatten der Steine
		bg.setStroke(Color.BLACK);
		bg.setStrokeWidth(FELDGROESSE * 0.03);

		// Steinhintergrund zentrieren
		bg.setTranslateX((FELDGROESSE - FELDGROESSE * 0.3125 * 2) / 2);
		bg.setTranslateY((FELDGROESSE - FELDGROESSE * 0.26 * 2) / 2 + FELDGROESSE * 0.07);

		// Stein erzeugen
		Ellipse ellipse = new Ellipse(FELDGROESSE * 0.3125, FELDGROESSE * 0.26);
		ellipse.setFill(typ == SteinTyp.ROT ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

		// Schatten der Steine
		ellipse.setStroke(Color.BLACK);
		ellipse.setStrokeWidth(FELDGROESSE * 0.03);

		// Stein zentrieren
		ellipse.setTranslateX((FELDGROESSE - FELDGROESSE * 0.3125 * 2) / 2);
		ellipse.setTranslateY((FELDGROESSE - FELDGROESSE * 0.26 * 2) / 2);

		getChildren().addAll(bg, ellipse);

		setOnMousePressed(event -> {
			mausX = event.getSceneX();
			mausY = event.getSceneY();
		});

		setOnMouseDragged(event -> {
			relocate(event.getSceneX() - mausX + altX, event.getSceneY() - mausY + altY);
		});
	}

	public void bewege(int x, int y) {
		altX = x * FELDGROESSE;
		altY = y * FELDGROESSE;
		relocate(altX, altY);
	}

	public void illegaleBewegung() {
		relocate(altX, altY);
	}

	public SteinTyp getType() {
		return typ;
	}

	public double getAltX() {
		return altX;
	}

	public double getAltY() {
		return altY;
	}
}
