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
        bilde = "bodyguard";
		oppgave = "Hvem vil Bodyguarden beskytte?";
		veiledning = "Bodyguard:\n" +
				"Bodyguarden velger hver natt en person å beskytte eller drepe, men kan kun drepe etter å ha beskyttet noen først.\n" +
				"For å la bodyguarden beskytte noen, trykker du på navnet til den bodyguarden velger.\n" +
                "For å bytte mellom drap og beskyttelse, trykker du på Drep/Beskytt.\n" +
                "Hvis bodyguarden vil drepe, peker han med pistolhånd, og du peker på den han velger.";
        guide = "Bodyguarden er hovedsakelig en beskyttende rolle. Han kan velge en person hver natt som han beskytter, men kan ikke velge seg selv. " +
                "Men bodyguarden er også en badass som kan drepe folk, men han trenger litt info for å få det til. " +
                "Bodyguarden kan derfor kun drepe personer han har møtt på før. " +
                "Dvs personer som besøkte den siste personen han beskyttet den natten han beskyttet dem. " +
                "Han vet ikke hvem han kan drepe og ikke, og får heller noen tilbakemelding på om han lykkes eller ikke når han prøver, " +
                "utover at denne personen muligens våkner opp død.";
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
