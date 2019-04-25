package view;

public enum SteinTyp {
	ROT(1), WEISS(-1), DAMEROT(0), DAMEWEISS(0);
	
	public final int richtung;
	
	SteinTyp(int richtung){
		this.richtung = richtung;
	}

}
