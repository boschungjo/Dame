package controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

	private Spieler spielerRot = new Spieler(SteinFarbe.ROT, false);
	private Spieler spielerWeiss = new Spieler(SteinFarbe.WEISS, true);

	private Stage primaryStage;
	private Label infoLabel;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		try {
			erstelleStartseite();
			// Scene scene = new Scene(erstelleStartseite());
			// primaryStage.setTitle("DameApp");
			// primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		erstelleStartseite();
	}

	private void erstelleStartseite() {

		final Image titleScreen = new Image("checkers1.png", 800, 500, false, false);

		final ImageView icon = new ImageView();
		icon.setImage(titleScreen);

		primaryStage.setTitle("D A M E S P I E L");
		primaryStage.getIcons().add(titleScreen);

		Pane root = new Pane();

		TextField spieler1 = new TextField("Spieler 1");
		spieler1.setMinSize(100, 25);
		spieler1.setTranslateX(325);
		spieler1.setTranslateY(550);

		TextField spieler2 = new TextField("Spieler 2");
		spieler1.setMinSize(100, 25);
		spieler2.setTranslateX(325);
		spieler2.setTranslateY(600);

		Button startBtn = new Button("Spiel starten");
		startBtn.setTranslateX(350);
		startBtn.setTranslateY(700);
		startBtn.setMinSize(100, 50);
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				spielerWeiss.setName(spieler1.getText());
				spielerRot.setName(spieler2.getText());
				erstelleSpielfeld();
			}
		};

		startBtn.setOnAction(eventHandler);

		root.getChildren().addAll(icon, startBtn, spieler1, spieler2);

		Scene startScene = new Scene(root, 800, 800);
		primaryStage.setScene(startScene);

	}

	private void aktualisiereLabel() {
		if (spielerRot.getAnDerReihe()) {
			infoLabel.setText(spielerRot.getName() + " ( " + spielerRot.getFarbe() + " )" + " ist an der Reihe.");
		} else {
			infoLabel.setText(spielerWeiss.getName() + " ( " + spielerWeiss.getFarbe() + " )" + " ist an der Reihe");
		}
	}

	// GUI erstellen
	private void erstelleSpielfeld() {
		Pane root = new Pane();
		infoLabel = new Label();

		infoLabel.setMinWidth(200);
		infoLabel.setTranslateX(300);
		infoLabel.setTranslateY(850);

		infoLabel.setText(spielerWeiss.getName() + " ( " + spielerWeiss.getFarbe() + " )" + " ist an der Reihe");

		root.setPrefSize(BREITE * FELDGROESSE, HOEHE * FELDGROESSE);
		root.getChildren().addAll(feldGruppe, steinGruppe, infoLabel);

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
		primaryStage.setScene(new Scene(root, 800, 900));
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
		Spieler spieler;
		Spieler otherSpieler;

		if (spielerRot.getAnDerReihe()) {
			spieler = spielerRot;
			otherSpieler = spielerWeiss;
		} else {
			spieler = spielerWeiss;
			otherSpieler = spielerRot;
		}
		if (stein.getFarbe() == spieler.getFarbe()) {

			int x0 = zuBrett(stein.getAltX());
			int y0 = zuBrett(stein.getAltY());

			
			for (Node stNode : steinGruppe.getChildren()) {
				//Abfrage ob irgend ein Stein gefressen werden kann. Wenn ja, alle andere Züge blockieren!!!
			}
			
						
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
								if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()
										|| brett[x + 1][y0 + 1].hatStein() || (neuX - x > 1)) {
									return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
								}

								spieler.setAnDerReihe(false);
								otherSpieler.setAnDerReihe(true);
								aktualisiereLabel();
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
								if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()
										|| brett[x + 1][y0 - 1].hatStein() || (neuX - x > 1)) {
									return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
								}
								spieler.setAnDerReihe(false);
								otherSpieler.setAnDerReihe(true);
								aktualisiereLabel();
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
								if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()
										|| brett[x - 1][y0 + 1].hatStein() || Math.abs(neuX - x) > 1) {
									return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
								}
								spieler.setAnDerReihe(false);
								otherSpieler.setAnDerReihe(true);
								aktualisiereLabel();
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
								if (brett[x][y0].getStein().getFarbe() == stein.getFarbe()
										|| brett[x - 1][y0 - 1].hatStein() || Math.abs(neuX - x) > 1) {
									return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
								}
								spieler.setAnDerReihe(false);
								otherSpieler.setAnDerReihe(true);
								aktualisiereLabel();
								return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x][y0].getStein());
							}
						}
					}

				}
				spieler.setAnDerReihe(false);
				otherSpieler.setAnDerReihe(true);
				aktualisiereLabel();
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
					spieler.setAnDerReihe(false);
					otherSpieler.setAnDerReihe(true);
					aktualisiereLabel();
					return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x1][y1].getStein());
				}

			}
		}

		return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
	}

}
