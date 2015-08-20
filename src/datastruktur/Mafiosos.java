package datastruktur;

import gui.Vindu;

public class Mafiosos {
	
	public static void main(String[] args){
		Spillerliste spillere = new Spillerliste();
		Vindu v = new Vindu("Mafiosos", spillere);
		v.startopp();
	}

}
