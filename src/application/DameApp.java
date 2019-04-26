package application;

import control.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import view.*;

public class DameApp extends Application {
	public static final int FELDGROESSE = 100;
	public static final int BREITE = 8;
	public static final int HOEHE = 8;

	private Feld[][] brett = new Feld[BREITE][HOEHE];

	private Group feldGruppe = new Group();
	private Group steinGruppe = new Group();

	private int zuBrett(double pixel) {
		return (int) (pixel + FELDGROESSE / 2) / FELDGROESSE;
	}

	private Stein macheStein(SteinFarbe farbe, int x, int y) {
		Stein stein = new Stein(farbe, x, y);
		stein.setOnMouseReleased(event -> {
			int neuX = zuBrett(stein.getLayoutX());
			int neuY = zuBrett(stein.getLayoutY());

			BewegungsErgebnis ergebnis;

			if (neuX < 0 || neuY < 0 || neuX >= BREITE || neuY >= HOEHE) {
				ergebnis = new BewegungsErgebnis(BewegungTyp.VERBOTEN);
			} else {
				ergebnis = versucheBewegung(stein, neuX, neuY);
			}

			int x0 = zuBrett(stein.getAltX());
			int y0 = zuBrett(stein.getAltY());

			switch (ergebnis.getTyp()) {
			case VERBOTEN:
				stein.illegaleBewegung();
				break;
			case NORMAL:
				stein.bewege(neuX, neuY);
				brett[x0][y0].setStein(null);
				brett[neuX][neuY].setStein(stein);
				break;
			case FRESSEN:
				stein.bewege(neuX, neuY);
				brett[x0][y0].setStein(null);
				brett[neuX][neuY].setStein(stein);

				Stein andererStein = ergebnis.getStein();
				brett[zuBrett(andererStein.getAltX())][zuBrett(andererStein.getAltY())].setStein(null);
				steinGruppe.getChildren().remove(andererStein);
				break;
			}
		});

		return stein;
	}

	private Parent erstelleFenster() {
		Pane root = new Pane();
		root.setPrefSize(BREITE * FELDGROESSE, HOEHE * FELDGROESSE);
		root.getChildren().addAll(feldGruppe, steinGruppe);

		for (int y = 0; y < HOEHE; y++) {
			for (int x = 0; x < BREITE; x++) {
				Feld feld = new Feld((x + y) % 2 == 0, x, y);
				brett[x][y] = feld;

				feldGruppe.getChildren().add(feld);

				Stein stein = null;

				if (y <= 2 && (x + y) % 2 != 0) {
					stein = macheStein(SteinFarbe.ROT, x, y);
				}

				if (y >= 5 && (x + y) % 2 != 0) {
					stein = macheStein(SteinFarbe.WEISS, x, y);
				}

				if (stein != null) {
					feld.setStein(stein);
					steinGruppe.getChildren().add(stein);
				}

			}
		}

		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Scene scene = new Scene(erstelleFenster());
			primaryStage.setTitle("DameApp");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BewegungsErgebnis versucheBewegung(Stein stein, int neuX, int neuY) {

		// man darf nicht auf ein Feld auf welchem schon ein Stein ist und man darf
		// nicht auf die weissen Felder
		if (brett[neuX][neuY].hatStein() || (neuX + neuY) % 2 == 0) {
			return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
		}

		int x0 = zuBrett(stein.getAltX());
		int y0 = zuBrett(stein.getAltY());

		// man darf nur 1 Feld fahren und nur in die erlaubte Richtung (ausser Dame)
		if ((Math.abs(neuX - x0) == 1 && neuY - y0 == stein.getFarbe().richtung) || (stein.getType() == SteinTyp.DAME && ((neuY -neuX == y0 - x0) || (neuX + neuY == x0 + y0) ))) {
			
//			int summe = neuX + neuY;
//			int dif = neuY - neuX;
//			if (stein.getType() == SteinTyp.DAME) {
//				for (int x = 0; x < brett.length; x++) {
//				for (int y = 0; y < brett.length; y++) {
//					if (brett[x][y].hatStein() && brett[x][y].getStein().getFarbe() != stein.getFarbe() && (x + y == summe || y - x == dif)) {
//						return new BewegungsErgebnis(BewegungTyp.FRESSEN,brett[x][y].getStein());
//					}
//				}
//			}
//			}
			
			
			if (stein.getType() == SteinTyp.DAME) {
				if (brett[neuX+1][neuY+1].hatStein()) {
					return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[neuX+1][neuY+1].getStein());
				}
				else if (brett[neuX-1][neuY-1].hatStein()) {
					return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[neuX-1][neuY-1].getStein());
				}
			}
			
			
			
			// wenn man auf der Grundlinie des Gegners ankommt, wird der eigene Stein zur
			// Dame befördert
			if ((neuY == 0 && stein.getFarbe() == SteinFarbe.WEISS)
					|| (neuY == 7 && stein.getFarbe() == SteinFarbe.ROT)) {
				stein.werdeDame();
			}
			return new BewegungsErgebnis(BewegungTyp.NORMAL);
		}

		// wenn man zwei Felder nach vorne fahren will
		else if (Math.abs(neuX - x0) == 2 && neuY - y0 == stein.getFarbe().richtung * 2) {

			int x1 = x0 + (neuX - x0) / 2;
			int y1 = y0 + (neuY - y0) / 2;

			// zwei Felder nach vorne sind nur erlaubt, wenn man einen Gegner fressen kann
			if (brett[x1][y1].hatStein() && brett[x1][y1].getStein().getFarbe() != stein.getFarbe()) {

				// wenn man auf der Grundlinie des Gegners ankommt, wird der eigene Stein zur
				// Dame befördert
				if ((neuY == 0 && stein.getFarbe() == SteinFarbe.WEISS)
						|| (neuY == 7 && stein.getFarbe() == SteinFarbe.ROT)) {
					stein.werdeDame();
				}
				return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x1][y1].getStein());
			}
			
		}

		return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
