package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Astronaut extends Rolle {

	String ut = "";
	
	public Astronaut(){
		super("Astronaut");
		oppgave = "Er Astronauten ferdig med raketten?";
		side = BORGER;
		prioritet = ASTRONAUT;
	}
		
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert && !snill) {
			offer = null;
			return false;	
		}
		
		if(offer != null) {
			aktiver(false);
		}
		return true;
	}
	
	@Override
	public String rapport() {
		String ut = tittel + "(" + spiller.navn() + ")";
		if(offer != null){
			ut += " har gjort ferdig raketten!";
			if(snill) ut += ", med hjelp fra nissen";
			if(blokkert) ut += ", men ble blokkert av " + blokk + ".";
		}
		else
			ut += " har ikke fullført raketten.";		
		return ut;	
	}
}
