package view;

import static controller.DameApp.FELDGROESSE;

import controller.DameApp;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;

public class Stein extends StackPane {
	private SteinFarbe farbe;
	private SteinTyp typ;

	private double mausX, mausY;
	private double altX, altY;

	// Konstruktor
	public Stein(SteinFarbe farbe, int x, int y) {
		this.farbe = farbe;
		this.typ = SteinTyp.NORMAL;

		bewege(x, y);

		// SteinHintergrund erzeugen
		Ellipse bg = new Ellipse(FELDGROESSE * 0.3125, FELDGROESSE * 0.26);
		bg.setFill(Color.BLACK);

		// Umrandung des SteinHintergrunds
		bg.setStroke(Color.BLACK);
		bg.setStrokeWidth(FELDGROESSE * 0.03);

		// Steinhintergrund zentrieren
		bg.setTranslateX((FELDGROESSE - FELDGROESSE * 0.3125 * 2) / 2);
		bg.setTranslateY((FELDGROESSE - FELDGROESSE * 0.26 * 2) / 2 + FELDGROESSE * 0.07);

		// Stein erzeugen
		Ellipse ellipse = new Ellipse(FELDGROESSE * 0.3125, FELDGROESSE * 0.26);
		ellipse.setFill(farbe == SteinFarbe.ROT ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

		// Umrandung der Steine
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

	public void werdeDame() {

		this.typ = SteinTyp.DAME;
//		System.out.println(this.getType());
//		System.out.println("Dame");
		
		// oberer Stein erzeugen
		Ellipse ellipse = new Ellipse(FELDGROESSE * 0.3125, FELDGROESSE * 0.26);

		if (this.getFarbe() == SteinFarbe.WEISS) {
			ellipse.setFill(Color.valueOf("#fff9f4"));
		} else {
			ellipse.setFill(Color.valueOf("#c40003"));
		}

		// Umrandung des oberen Steins
		ellipse.setStroke(Color.BLACK);
		ellipse.setStrokeWidth(FELDGROESSE * 0.03);

		// Stein verschieben
		ellipse.setTranslateX(10);
		ellipse.setTranslateY(20);

		this.getChildren().add(ellipse);
	}

	// Getter
	public SteinTyp getType() {
		return typ;
	}

	public SteinFarbe getFarbe() {
		return farbe;
	}

	public double getAltX() {
		return altX;
	}

	public double getAltY() {
		return altY;
	}
}
