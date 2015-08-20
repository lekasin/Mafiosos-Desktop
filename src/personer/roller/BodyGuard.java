package personer.roller;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import personer.Rolle;
import personer.Spiller;

public class BodyGuard extends Rolle {

	ArrayList<Spiller> nektet = new ArrayList<Spiller>();
	boolean angrep = false;
	Spiller vip = null;
	
	public BodyGuard(){
		super("Bodyguard");
		oppgave = "Hvem vil Bodyguarden beskytte?";
		side = BORGER;
		prioritet = BODYGUARD;
	}
	
	public void skift() {
		angrep = !angrep;
		if(angrep)
			oppgave = "Hvem vil Bodyguarden drepe?";
		else
			oppgave = "Hvem vil Bodyguarden beskytte?";
	}
	
	public Spiller getVip(){
		return vip;
	}
	
	public void setNektet(ArrayList<Spiller> n) {
		nektet = n;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
//		forby(spiller);
		if(blokkert)
			return false;
		
		if(angrep && nektet.contains(spiller)) {
			spiller.drep(this);
			vip = null;
		}
		else {
			spiller.beskytt(this);
			vip = spiller;
		}
		return true;
	}
	
	public String rapport(){
		String ut = tittel + "(" + spiller + ")";
		if(offer != null) ut += (angrep ? " har drept " : " har beskyttet ") + offer + "(" + offer.rolle() + ")";
		if(snill) ut += ", med hjelp fra nissen";
		if(blokkert) ut += ", men ble blokkert av " + blokk + ".";
		if(angrep) skift();
		return ut;
	}

}
