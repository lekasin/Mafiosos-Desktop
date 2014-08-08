package datastruktur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import personer.*;
import personer.roller.Mafia;
import personer.roller.Politi;
import personer.roller.Bestevenn;

public class Spillerliste {

	ArrayList<Spiller> spillere;
	ArrayList<Spiller> sl = new ArrayList<>();
	Spiller s = new Spiller("Arne");

	public Spillerliste() {
		spillere = new ArrayList<>();
	}

	public String nySpiller(String n, Rolle r) {
		leggTil(new Spiller(n, r));
		return n;
	}

	public void leggTil(Spiller s){
		if(finnSpiller(s.navn()) == null)
			spillere.add(s);
	}

	public void fjern(String s){
		spillere.remove(finnSpiller(s));
	}

	public void våknOpp(){
		for(Spiller s: spillere){
			if(!s.lever() && !s.id(Rolle.ZOMBIE)) {
				s.stopp();
			}
			if(s.id(Rolle.BELIEBER)) s.rolle().lever();
		}
	}

	public void sov(){
		for(Spiller s: spillere){
			if(s.lever() || s.id(Rolle.ZOMBIE))
				s.sov();
		}
	}

	public void gjenoppliv(Spiller død) {
		sl.remove(død);
	}

	public void restart(){
		for(Spiller s: spillere)
			s.vekk();
		sl.clear();
	}

	public void slettRoller(){
		for(Spiller s: spillere)
			s.setRolle(null);
	}

	public int vinner(){
		int slemme = 0;
		int snille = 0;
		int anarki = 0;
		for(Spiller s: spillere){
			if(s.lever()){
				if(s.id(Rolle.ANARKIST)) anarki++;
				if(s.side() < Rolle.NØYTRAL) slemme++;
				else snille++;
			}
		}
		if(snille+slemme < 1)
			return 2;
		else if(snille < 1)
			return -1;
		else if(slemme < 1) {
			if(anarki > 0)
				return 3;
			else
				return 1;
		}
		else
			return 0;
	}

	public int length(){
		return spillere.size();
	}

	public Spiller finnSpiller(String n) {
		for(Spiller s: spillere)
			if(s.navn().equals(n)) return s;
		return null;
	}

	public Spiller finnSpillerSomEr(int id){
		for(Spiller s: spillere)
			if(s.id(id)) return s;
		return null;
	}

	public Rolle finnRolle(int id) {
		for(Spiller s: spillere)
			if(s.id(id)) return s.rolle();
		return null;
	}

	public Rolle randomRolle(int nedre, int øvre, int eks){
		int id = -1;
		Random random = new Random();
		while(finnSpillerSomEr(id) == null || id == eks || !finnRolle(id).fortsetter())
			id = random.nextInt((øvre - nedre) + 1) + nedre;
		return finnRolle(id);
	}
	
	public Spiller randomSpiller(Spiller eks){
		int id = -1;
		Random random = new Random();
		do
			id = random.nextInt((spillere.size()-1 - 0) + 1);
		while(spillere.get(id) == eks);

		return spillere.get(id);
	}
	
	public Spiller randomSpiller(Spiller eks, Spiller eks2){
		int id = -1;
		Random random = new Random();
		do
			id = random.nextInt((spillere.size()-1 - 0) + 1);
		while(spillere.get(id) == eks || spillere.get(id) == eks2);

		return spillere.get(id);
	}

	public ArrayList<Spiller> spillere(){
		return spillere;
	}

	public ArrayList<Spiller> døde(){
		return sl;
	}

	public ArrayList<Spiller> lik(){
		ArrayList<Spiller> lik = new ArrayList<>();
		for(Spiller s:sl)
			if(!s.skjult()) lik.add(s);
		return lik;
	}

	public ArrayList<Spiller> dødsannonse(){
		ArrayList<Spiller> nye = new ArrayList<>();

		for(Spiller s: spillere){
			if(!s.lever() && !sl.contains(s))
				nye.add(s);
		}
		for(Spiller s: nye)
			sl.add(s);
		return nye;
	}

	public void deal(){
		for(Spiller s: spillere)
			if(s.rolle() instanceof Mafia) {
				s.forsvar(s.rolle());
			}
	}


	//STRING-METODER
	public String valg(Rolle r){
		if(r.id(Rolle.JØRGEN)) return "";

		String ut = "\n\n";
		for(Spiller s: spillere)
			if(s.funker() && r.forbud() != s)
				ut += s + "\n";
		return ut;

	}

	public String jørgensListe(){
		String ut = "Jørgens notater:\n";
		for(Spiller s: spillere)
			if(!s.funker()) 
				if(!finnSpillerSomEr(Rolle.JØRGEN).skjult())
					ut += s + " var " + s.rolle() + "\n";
				else
					ut += s + " var " + randomSpiller(finnSpillerSomEr(Rolle.JØRGEN)).rolle() + "\n";
		return ut;
	}

	public String rex(Spiller spiller){
		String ut = "";
		int teller = 0;
		for(Spiller s: spillere)
			if(s.offer() == spiller && !s.id(Rolle.REX) && s.funker()) {
				ut += s.navn() + "\n";
				teller++;
			}
		
		if(finnSpillerSomEr(Rolle.REX).skjult()) {
			ut = "";
			if(teller > 0)
				while(teller > 0) {
					ut += randomSpiller(finnSpillerSomEr(Rolle.REX)) + "\n";
					teller--;
				}
			else
				ut += randomSpiller(finnSpillerSomEr(Rolle.REX)) + "\n";
			return ut;
		}
		else if(teller > 0)
			return ut;
		else
			return "Ingen";
	}

	public String mafiaNavn(){
		String ut = "Mafiaene er:\n";
		for(Spiller s: spillere)
			if(s.rolle() instanceof Mafia) ut += s + "\n";
		return ut;
	}

	public String drøm(Spiller drømmer) {
		String ut = "Drømmeren ser disse i drømmen sin:\n";
		Boolean mafia = false;
		int teller = 0;
		@SuppressWarnings("unchecked")
		ArrayList<Spiller> list = (ArrayList<Spiller>)spillere.clone();
		Collections.shuffle(list);

		if(drømmer.skjult()) {
			for(Spiller s: list)
				if(s.lever() && !s.id(Rolle.DRØMMER) && teller < 3) {
					ut += s + "\n";
					teller++;
				}
		} else {
			for(Spiller s: list)
				if(s.lever() && !s.id(Rolle.DRØMMER)) {
					if(s.side() < 0 && !mafia) {
						mafia = true;
						ut += s + "\n";
						teller++;
					}
					else if (!(s.side() < 0) && teller < 3) {
						ut += s + "\n";
						teller++;
					}
				}
		}

		if (teller == 3)
			return ut;
		else
			return "Drømmeren får ikke sove...";

	}

	public String rolleString(Rolle[] roller, int antall){
		String rolleString = spillere.size() + " Spillere\n\n" + 
				"Gjenstående roller: " + antall + "\nMafia x" + ((Mafia) roller[Rolle.MAFIA]).antall();
		if(roller[Rolle.POLITI] != null) rolleString += "\nPoliti x" + ((Politi) roller[Rolle.POLITI]).antall();
		if(roller[Rolle.BESTEVENN] != null) rolleString += "\nBestevenn x" + ((Bestevenn) roller[Rolle.BESTEVENN]).antall();

		for(Rolle r: roller){
			if(r != null && !(r instanceof Mafia || r instanceof Politi || r instanceof Bestevenn))
				rolleString += "\n" + r;
		}
		return rolleString;
	}

	public String visRoller(Rolle[] roller){
		String rolleString = "Roller: \n\nMafia x" + ((Mafia) roller[Rolle.MAFIA]).antall();
		if(roller[Rolle.POLITI] != null) rolleString += "\nPoliti x" + ((Politi) roller[Rolle.POLITI]).antall();
		if(roller[Rolle.BESTEVENN] != null) rolleString += "\nBestevenn x" + ((Bestevenn) roller[Rolle.BESTEVENN]).antall();

		for(Rolle r: roller){
			if(r != null && !(r instanceof Mafia || r instanceof Politi || r instanceof Bestevenn))
				rolleString += "\n" + r;
		}
		return rolleString;
	}

	public String toString(){
		String ut = "Spillere: " + spillere.size();
		for(Spiller s: spillere)
			ut += "\n" + s;
		return ut;
	}

	public String hvemErHva(){
		String ut = "";
		for(Spiller s: spillere)
			if(s.rolle() != null) ut += s + " er " + s.rolle() + "\n";
		return ut;
	}

}
