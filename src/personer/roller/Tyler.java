package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Tyler extends Rolle {
	
	Spiller original;
	Rolle rolle;

	public Tyler(){
		super("Tyler Durden");
		oppgave = "Hvem vil Tyler besøke?";
		side = BORGER;
		prioritet = TYLER;
	}

	@Override
	public String oppgave() {
		rolle = null;
		rolle = tv.spillere().randomRolle(Rolle.JESUS, Rolle.QUISLING, Rolle.TYLER);
		original = rolle.spiller();
		rolle.setSpiller(this.spiller);

		tv.vis("Tyler er nå " + rolle + "!\n");
		tv.leggtil(oppgave);
		tv.toFront();
		if(informert) tv.leggtil(info);
		return oppgave;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		if(rolle.pri() < Rolle.QUISLING && rolle.pri() > Rolle.MAFIA) {
			Spiller forb = rolle.forbud();
			rolle.forby(null);
			
			if(rolle.blokkert()) {
				Rolle r = rolle.hvemBlokk();
				rolle.rens(r);
				rolle.evne(spiller);
				rolle.blokker(r);
			} else
				rolle.evne(spiller);
			
			rolle.forby(forb);
		}
		rolle.setSpiller(original);
		return true;
	}
	
	@Override
	public String rapport(){
		String ut = tittel + "(" + spiller.navn() + ")";
		if(offer != null) ut += " har valgt " + offer + "(" + offer.rolle() + ")";
		ut += ", og var " + rolle;
		if(blokkert) ut += ", men ble blokkert av " + blokk + ".";

		return ut;
	}

}
