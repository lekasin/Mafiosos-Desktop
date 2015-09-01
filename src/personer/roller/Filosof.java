package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Filosof extends Rolle {
	
	Spiller valgt;
	String ut = "";
	
	public Filosof(){
		super("Filosofen");
		oppgave = "Hvem vil Filosofen studere?";
		veiledning = "Filosofen:\n" +
				"Filosofen våkner første natt, og velger en person han vil filosofere rundt.\n" +
				"Når filosofen har valgt, trykker du på personens navn, og vedkommendes rolle kommer da opp på skjermen.\n" +
				"Filosofen kommer da ikke til å våkne på nytt, før den valgte personen dør. " +
                "Når det skjer, kan filosofen velge en ny person.\n";

		side = BORGER;
		prioritet = FILOSOF;
		fortsett = false;
	}
	
	public Spiller getValgt(){
		return valgt;
	}
	
	
	@Override
	public String oppgave() {
		// TODO Auto-generated method stub
		System.out.println("Aktiv oppgave: " + aktiv);

		return super.oppgave();
	}
	@Override
	public boolean evne(Spiller spiller) {
		tv.toFront();
		
		if(this.spiller.skjult())
			ut += spiller + " har rollen " + tv.spillere().randomSpiller(this.spiller, spiller).rolle();
		else
			ut += spiller + " har rollen " + spiller.rolle();

		ut += (forsinkelse != null) ? ".\n\n" : ".";
		
		if(blokkert) {
			if(blokk != forsinkelse) ut = "Filosofen ble blokkert forrige natt!\n\n";
			tv.vis("Filosofen er blokkert"); 
			return false;
		}

		valgt = spiller;
		aktiver(false);
		tv.vis(ut);
		return true;
	}
	
	@Override
	public void jul() {
		// TODO Auto-generated method stub
		aktiver(true);
		super.jul();
	}

	public void sov(){
		if(forsinkelse == null) ut = "";
		if((valgt == null || !valgt.lever() ) && !spiller.rolle().id(SMITH)) 
			aktiver(true);
		super.sov();

	}
	
}
