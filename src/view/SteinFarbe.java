package view;

public enum SteinFarbe {
	ROT(1), WEISS(-1);

	public final int richtung;

	SteinFarbe(int richtung){
		this.richtung = richtung;
	}
}
