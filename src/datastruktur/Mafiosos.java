package datastruktur;

import gui.Vindu;
import personer.Rolle;
import personer.roller.*;

import java.util.ArrayList;

public class Mafiosos {

	private static ArrayList<Rolle> rolleListe = new ArrayList<>();

	public static void main(String[] args){
        Mafiosos.opprettRolleliste();
        Spillerliste spillere = new Spillerliste();
		Vindu v = new Vindu("Mafiosos", spillere);
		v.startopp();
	}

	public static ArrayList<Rolle> roller(){
        return rolleListe;
	}

	private static void opprettRolleliste(){
        Mafia mafia = new Mafia();
        Politi politi = new Politi();

        rolleListe.add(mafia);

        rolleListe.add(new Smith());
        rolleListe.add(new Aktor());
        rolleListe.add(new Anarkist());
        rolleListe.add(new Arving());
        rolleListe.add(new Astronaut());

        rolleListe.add(new Erik());
        rolleListe.add(new Bedrager());
        rolleListe.add(new Belieber());
        rolleListe.add(new Bestemor());
        rolleListe.add(new Bestevenn());
        rolleListe.add(new BodyGuard());
        rolleListe.add(new Bomber());
        rolleListe.add(new Boddel());

        rolleListe.add(new CopyCat());
        rolleListe.add(new Cupid());

        rolleListe.add(new Didrik());
        rolleListe.add(new Drommer());

        rolleListe.add(new Filosof());

        rolleListe.add(new Berit());

        rolleListe.add(new Hammer());
        rolleListe.add(new Havfrue());
        rolleListe.add(new Heisenberg());
        rolleListe.add(new HMSansvarlig());

        rolleListe.add(new Illusjonist());
        rolleListe.add(new Informant());
        rolleListe.add(new Insider(politi));

        rolleListe.add(new Jesus());
        rolleListe.add(new Joker());
        rolleListe.add(new Julenissen());
        rolleListe.add(new Jorgen());

        rolleListe.add(new Kirsten());
        rolleListe.add(new Klona());

        rolleListe.add(new Lege());
        rolleListe.add(new Jente());
        rolleListe.add(new Logner());

        rolleListe.add(new Carlsen());
        rolleListe.add(new Marius());
        rolleListe.add(new Morder());

        rolleListe.add(new Obduksjonist());

        rolleListe.add(new Politi());
        rolleListe.add(new Princess());
        rolleListe.add(new Psykolog());

        rolleListe.add(new Quisling());

        rolleListe.add(new Ravn());
        rolleListe.add(new Rex());

        rolleListe.add(new Sherlock());
        rolleListe.add(new Skytsengel());
        rolleListe.add(new Snylter());
        rolleListe.add(new Snasamann());
        rolleListe.add(new Sofasurfer());
        rolleListe.add(new Specialguy());

        rolleListe.add(new Tjukkas());
        rolleListe.add(new Trompet());
        rolleListe.add(new Tyler());
        rolleListe.add(new Tyster());

        rolleListe.add(new UlfOmar());
        rolleListe.add(new Undercover(mafia));

        rolleListe.add(new Vara());

        rolleListe.add(new Youtuber());

        rolleListe.add(new Zombie());
    }

}
