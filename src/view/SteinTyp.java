package view;

public enum SteinTyp {
	ROT(1), WEISS(-1);
	
	final int richtung;
	
	SteinTyp(int richtung){
		this.richtung = richtung;
	}

}
