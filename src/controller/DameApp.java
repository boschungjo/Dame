package controller;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import view.*;

/**
 * 
 * Klasse, welche das GUI erstellt und die Logik enthält
 * 
 * @author boschungjo
 * @author radonjicl
 *
 */

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

	/**
	 * Main-Methode
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Start-Methode
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		try {
			erstelleStartseite();
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mehtode, welche die Startseite zusammensetzt
	 */
	private void erstelleStartseite() {

		// Bild
		final Image titleScreen = new Image("checkers1.png", 800, 500, false, false);
		final ImageView icon = new ImageView();
		icon.setImage(titleScreen);

		// Titel
		primaryStage.setTitle("D A M E S P I E L");
		primaryStage.getIcons().add(titleScreen);

		// Textfelder
		TextField spieler1 = new TextField("Spieler 1");
		spieler1.setMinSize(100, 25);
		spieler1.setTranslateX(325);
		spieler1.setTranslateY(550);

		TextField spieler2 = new TextField("Spieler 2");
		spieler1.setMinSize(100, 25);
		spieler2.setTranslateX(325);
		spieler2.setTranslateY(600);

		// Start Button wird erstellt
		Button startBtn = new Button("Spiel starten");
		startBtn.setTranslateX(200);
		startBtn.setTranslateY(700);
		startBtn.setMinSize(100, 50);

		// Button Event, welcher für den Wechsel auf das Spielfeldfenster sorgt
		EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				spielerWeiss.setName(spieler1.getText());
				spielerRot.setName(spieler2.getText());
				erstelleSpielfeld();
			}
		};
		startBtn.setOnAction(eventHandler);

		// Regel Button wird erstellt
		Button regelButton = new Button("Spielregeln");
		regelButton.setTranslateX(500);
		regelButton.setTranslateY(700);
		regelButton.setMinSize(100, 50);

		// Button Event, welcher für den Wechsel auf die Regelseite sorgt
		EventHandler<ActionEvent> regelEventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				erstelleRegelfenster();
			}
		};
		regelButton.setOnAction(regelEventHandler);

		// Pane erstellen und die einzelnen Elemente hinzufügen
		Pane root = new Pane();
		root.getChildren().addAll(icon, startBtn, regelButton, spieler1, spieler2);

		// Scene erstellen und der Stage zuweisen
		Scene startScene = new Scene(root, 800, 800);
		primaryStage.setScene(startScene);

	}

	/**
	 * Methode, welche das Spielfeld zusammensetzt
	 */
	private void erstelleSpielfeld() {

		// Info-Label erstellen, welches jeweils schreibt, welcher Spieler am Zug ist
		// (weiss beginnt)
		infoLabel = new Label();
		infoLabel.setMinWidth(200);
		infoLabel.setTranslateX(300);
		infoLabel.setTranslateY(850);
		infoLabel.setText(spielerWeiss.getName() + " ( " + spielerWeiss.getFarbe() + " )" + " ist an der Reihe");

		// Steine auf den richtigen Feldern in den richtigen Farben erstellen
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

		// Pane erstellen und die einzelnen Elemente hinzufügen
		Pane root = new Pane();
		root.setPrefSize(BREITE * FELDGROESSE, HOEHE * FELDGROESSE);
		root.getChildren().addAll(feldGruppe, steinGruppe, infoLabel);

		// Scene erstellen und der Stage zuweisen
		Scene playScene = new Scene(root, 800, 900);
		primaryStage.setScene(playScene);
	}

	/**
	 * Methode, welche das Regelfenster erstellt
	 */
	private void erstelleRegelfenster() {

		// Zu jeder Regel wird ein Label erstellt
		Label titel = new Label("Spielregeln");
		Label regel1 = new Label("1. Man darf die Steine nur auf den dunklen Feldern fahren.");
		Label regel2 = new Label("2. Man darf mit einem normalen Spielstein nur nach vorne fahren.");
		Label regel3 = new Label(
				"3. Man darf mit einem normalen Spielstein nur ein Feld diagonal fahren, solange man keinen gegnerischen Stein überspringen kann.");
		Label regel4 = new Label(
				"4. Wenn man einen gegenerischen Stein überspringen kann (muss auf dem Feld direkt dahinter aufsetzen), dann darf man zwei Felder nach vorne fahren. Der gegnerische Stein wird dabei entfernt.");
		Label regel5 = new Label(
				"5. Wenn man auf der gegnerischen Grundlinie ankommt, dann wird der Stein vom normalen Spielstein zur Dame befördert.");
		Label regel6 = new Label(
				"6. Eine Dame kann diagonal so viele Felder nach vorne oder hinten fahren, wie sie will.");
		Label regel7 = new Label(
				"7. Eine Dame kann gegnerische Steine ebenfalls entfernen indem sie sie überspringt, muss jedoch auch direkt dahinter aufsetzen.");
		Label regel8 = new Label("8. Nach jedem Zug ist der andere Spieler an der Reihe.");
		Label regel9 = new Label(
				"9. Egal ob Dame oder normaler Spielstein: Niemals ist es erlaubt einen eigenen Stein zu überspringen.");
		Label regel10 = new Label(
				"10. Das Spiel ist zu Ende, wenn ein Spieler keine Spielsteine mehr hat oder nirgendwo mehr fahren kann. Derjenige Spieler hat verloren.");

		// Die Labels werden etwas nach rechts verschoben
		titel.setTranslateX(500);
		regel1.setTranslateX(50);
		regel2.setTranslateX(50);
		regel3.setTranslateX(50);
		regel4.setTranslateX(50);
		regel5.setTranslateX(50);
		regel6.setTranslateX(50);
		regel7.setTranslateX(50);
		regel8.setTranslateX(50);
		regel9.setTranslateX(50);
		regel10.setTranslateX(50);

		// Die Labels werden untereinander mit etwas Abstand dazwischen angeordnet
		titel.setTranslateY(50);
		regel1.setTranslateY(150);
		regel2.setTranslateY(200);
		regel3.setTranslateY(250);
		regel4.setTranslateY(300);
		regel5.setTranslateY(350);
		regel6.setTranslateY(400);
		regel7.setTranslateY(450);
		regel8.setTranslateY(500);
		regel9.setTranslateY(550);
		regel10.setTranslateY(600);

		// Button wird erstellt, mit welchem man züruck auf die Startseite kommt
		Button retourButton = new Button("zurück zur Startseite");
		retourButton.setTranslateX(500);
		retourButton.setTranslateY(700);
		retourButton.setMinSize(100, 50);

		// Button Event, welcher für den Wechsel auf die Startseite sorgt
		EventHandler<ActionEvent> retourEventHandler = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				erstelleStartseite();
			}
		};

		retourButton.setOnAction(retourEventHandler);

		// Pane erstellen und die einzelnen Elemente hinzufügen
		Pane root = new Pane();
		root.setPrefSize(800, 800);
		root.getChildren().addAll(feldGruppe, steinGruppe, titel, regel1, regel2, regel3, regel4, regel5, regel6,
				regel7, regel8, regel9, regel10, retourButton);

		// Scene erstellen und der Stage zuweisen
		Scene ruleScene = new Scene(root, 1200, 800);
		primaryStage.setScene(ruleScene);
	}

	/**
	 * Methode, welche die Information aktualisiert, welcher Spieler an der Reihe
	 * ist
	 */
	private void aktualisiereLabel() {
		if (spielerRot.getAnDerReihe()) {
			infoLabel.setText(spielerRot.getName() + " ( " + spielerRot.getFarbe() + " )" + " ist an der Reihe.");
		} else {
			infoLabel.setText(spielerWeiss.getName() + " ( " + spielerWeiss.getFarbe() + " )" + " ist an der Reihe");
		}
	}

	/**
	 * 
	 * Methode, welche die Double-Werte der Pixel zu Integers macht
	 * 
	 * @param pixel
	 * @return int
	 */
	private int zuBrett(double pixel) {
		return (int) (pixel + FELDGROESSE / 2) / FELDGROESSE;
	}

	/**
	 * 
	 * Methode, welche einen Stein macht und ausserdem die verschiedenen
	 * Bewegungstypen ( aus Methode versucheBewegung() ) unterscheidet...
	 * 
	 * @param farbe - Farbe des Steines welcher gemacht werden soll
	 * @param x
	 * @param y
	 * @return Stein
	 */
	private Stein macheStein(SteinFarbe farbe, int x, int y) {
		// Stein erzeugen
		Stein stein = new Stein(farbe, x, y);

		// Unterscheidung der Bewegungstypen (normal, fressen, verboten) zum Zeitpunkt,
		// in dem man die Maus loslässt
		stein.setOnMouseReleased(event -> {
			int neuX = zuBrett(stein.getLayoutX());
			int neuY = zuBrett(stein.getLayoutY());

			BewegungsErgebnis ergebnis;

			// Stein kann nicht aus dem Spielfeld gezogen werden
			if (neuX < 0 || neuY < 0 || neuX >= BREITE || neuY >= HOEHE) {
				ergebnis = new BewegungsErgebnis(BewegungTyp.VERBOTEN);
			} else {
				ergebnis = versucheBewegung(stein, neuX, neuY);
			}

			int x0 = zuBrett(stein.getAltX());
			int y0 = zuBrett(stein.getAltY());

			switch (ergebnis.getTyp()) {
			// illegaleBewegung() wird aufgerufen, weil die versuchte Bewegung nicht erlaubt
			// ist
			case VERBOTEN:
				stein.illegaleBewegung();
				break;

			// der Stein wird am neuen Ort gesetzt und am alten entfernt
			case NORMAL:
				stein.bewege(neuX, neuY);
				brett[x0][y0].setStein(null);
				brett[neuX][neuY].setStein(stein);
				break;

			case FRESSEN:
				// der Stein wird am neuen Ort gesetzt und am alten entfernt
				stein.bewege(neuX, neuY);
				brett[x0][y0].setStein(null);
				brett[neuX][neuY].setStein(stein);

				// der gegnerische Stein wird entfernt, weil er übersprungen wurde
				Stein andererStein = ergebnis.getStein();
				brett[zuBrett(andererStein.getAltX())][zuBrett(andererStein.getAltY())].setStein(null);
				steinGruppe.getChildren().remove(andererStein);
				break;
			}
		});
		return stein;
	}

	/**
	 * 
	 * Methode, die prüft, ob eine Bewegung möglich ist und auf welche Art
	 * 
	 * @param stein - der Stein, der bewegt wird
	 * @param neuX  - die x-Koordinate des Feldes auf das der Stein versucht wird zu
	 *              bewegen
	 * @param neuY  - die y-Koordinate des Feldes auf das der Stein versucht wird zu
	 *              bewegen
	 * @return BewegungsErgebnis - Das Ergebnis des Versuches mit entsprechenden
	 *         Bewegungstyp (normal, fressen oder verboten)
	 */
	private BewegungsErgebnis versucheBewegung(Stein stein, int neuX, int neuY) {
		Spieler spieler;
		Spieler otherSpieler;

		// es wird gespeichert, welcher Spieler an der Reihe ist
		if (spielerRot.getAnDerReihe()) {
			spieler = spielerRot;
			otherSpieler = spielerWeiss;
		} else {
			spieler = spielerWeiss;
			otherSpieler = spielerRot;
		}

		// nur der Spieler, der an der Reihe ist, kann eine Bewegung machen
		if (stein.getFarbe() == spieler.getFarbe()) {

			// die alten Koordinaten des Steines werden gespeichert
			int x0 = zuBrett(stein.getAltX());
			int y0 = zuBrett(stein.getAltY());

			// Züge auf ein besetztes Feld oder auf ein helles Feld verhindern
			if (brett[neuX][neuY].hatStein() || (neuX + neuY) % 2 == 0) {
				return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
			}

			// man darf nur 1 Feld fahren und nur in die erlaubte Richtung (ausser Dame)
			if ((Math.abs(neuX - x0) == 1 && neuY - y0 == stein.getFarbe().richtung)
					|| (stein.getType() == SteinTyp.DAME && ((neuY - neuX == y0 - x0) || (neuX + neuY == x0 + y0)))) {

				// wenn der Stein auf die gegnerische Grundlinie gelangt und noch keine Dame
				// ist, wird er zu dieser befördert
				if (((neuY == 0 && stein.getFarbe() == SteinFarbe.WEISS)
						|| (neuY == 7 && stein.getFarbe() == SteinFarbe.ROT)) && stein.getType() != SteinTyp.DAME) {
					stein.werdeDame();
				}

				// wenn der Stein bereits eine Dame ist, muss man die verschiedenen
				// Diagonalbewegungen unterscheiden
				else if (stein.getType() == SteinTyp.DAME) {

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

								// aktualisieren, welcher Spieler an der Reihe ist
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

								// aktualisieren, welcher Spieler an der Reihe ist
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

								// aktualisieren, welcher Spieler an der Reihe ist
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

								// aktualisieren, welcher Spieler an der Reihe ist
								spieler.setAnDerReihe(false);
								otherSpieler.setAnDerReihe(true);
								aktualisiereLabel();
								return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x][y0].getStein());
							}
						}
					}

				}

				// aktualisieren, welcher Spieler an der Reihe ist
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

					// aktualisieren, welcher Spieler an der Reihe ist
					spieler.setAnDerReihe(false);
					otherSpieler.setAnDerReihe(true);
					aktualisiereLabel();
					return new BewegungsErgebnis(BewegungTyp.FRESSEN, brett[x1][y1].getStein());
				}

			}
		}

		// wenn der Spieler nicht an der Reihe ist, ist jegliche Bewegung verboten
		return new BewegungsErgebnis(BewegungTyp.VERBOTEN);
	}

}
