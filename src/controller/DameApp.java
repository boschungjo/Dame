package controller;

import java.util.Iterator;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import view.*;

public class DameApp extends Application {
	public static final int FELDGROESSE = 100;
	public static final int BREITE = 8;
	public static final int HOEHE = 8;

	private Feld[][] brett = new Feld[BREITE][HOEHE];

	private Group feldGruppe = new Group();
	private Group steinGruppe = new Group();

	public static void main(String[] args) {
		launch(args);
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

	// GUI erstellen
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

	// Methode, die prüft, ob eine Bewegung möglich ist
	private BewegungsErgebnis versucheBewegung(Stein stein, int neuX, int neuY) {
		
		int x0 = zuBrett(stein.getAltX());
		int y0 = zuBrett(stein.getAltY());
		
		
		// Züge auf ein besetztes Feld oder auf ein helles Feld verhindern
		if (brett[neuX][neuY].hatStein() || (neuX + neuY) % 2 == 0) {
			return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
		}
		
		// man darf nur 1 Feld fahren und nur in die erlaubte Richtung (ausser Dame)
		if ((Math.abs(neuX - x0) == 1 && neuY - y0 == stein.getFarbe().richtung)
				|| (stein.getType() == SteinTyp.DAME && ((neuY - neuX == y0 - x0) || (neuX + neuY == x0 + y0)))) {
			if (((neuY == 0 && stein.getFarbe() == SteinFarbe.WEISS)
					|| (neuY == 7 && stein.getFarbe() == SteinFarbe.ROT)) && stein.getType() != SteinTyp.DAME) {
				stein.werdeDame();
			}

			else if (stein.getType() == SteinTyp.DAME) {

				System.out.println("gut");

				// Dame-Bewegung nach rechts unten
				if (neuX > x0 && neuY > y0) {
					System.out.println("rechts unten");
					for (int x = x0 + 1; x < neuX; x++) {
						y0++;
						if (brett[x][y0].hatStein()) {
							if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()) {
								return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
							}
							return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x][y0].getStein());
						}

					}
				}

				// Dame-Bewegung nach rechts oben
				else if (neuX > x0 && neuY < y0) {
					System.out.println("rechts oben");
					for (int x = x0 + 1; x < neuX; x++) {
						y0--;
						if (brett[x][y0].hatStein()) {
							if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()) {
								return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
							}
							return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x][y0].getStein());
						}

					}
				}

				// Dame-Bewegung nach links unten
				else if (neuX < x0 && neuY > y0) {
					System.out.println("links unten");
					for (int x = x0 - 1; x > neuX; x--) {
						y0++;
						if (brett[x][y0].hatStein()) {
							if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()) {
								return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
							}
							return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x][y0].getStein());
						}
					}
				}

				// Dame-Bewegung nach links oben
				else if (neuX < x0 && neuY < y0) {
					System.out.println("links oben");
					for (int x = x0 - 1; x > neuX; x--) {
						y0--;
						if (brett[x][y0].hatStein()) {
							if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()) {
								return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
							}
							return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x][y0].getStein());
						}
					}
				}

			}
			return new BewegungsErgebnis(BewegungTyp.NORMAL);
		}

		// man darf nicht auf ein Feld auf welchem schon ein Stein ist und man darf
		// nicht auf die weissen Felder
		if (brett[neuX][neuY].hatStein() || (neuX + neuY) % 2 == 0) {
			return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
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

}
