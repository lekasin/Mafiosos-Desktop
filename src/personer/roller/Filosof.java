package personer.roller;

import gui.Spill;
import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Filosof extends Rolle {
	
	Spiller valgt;
	String ut = "";
	
	public Filosof(){
		super("Filosofen");
        bilde = "filosof";
		oppgave = "Hvem vil Filosofen studere?";
		veiledning = "Filosofen:\n" +
				"Filosofen våkner første natt, og velger en person han vil filosofere rundt.\n" +
				"Når filosofen har valgt, trykker du på personens navn, og vedkommendes rolle kommer da opp på skjermen.\n" +
				"Filosofen kommer da ikke til å våkne på nytt, før den valgte personen dør. " +
                "Når det skjer, kan filosofen velge en ny person.\n";
        guide = "Filosofen undrer seg over stort og smått, og er god på analysere oppførsel. " +
                "Han våkner en natt, og peker på en person han vil fundere over. " +
                "Han finner da ut hvilken rolle denne personen har, og kan bruke dette til å tolke personens videre oppførsel. " +
                "Filosofen er imidlertid trofast i sin studie, og velger ikke en ny person før den første eventuelt dør.";
		side = BORGER;
		prioritet = FILOSOF;
		fortsett = false;
	}

    @Override
    public void autoEvne() {
        if (Spill.NATT > 1 && !funker)
            aktiver(false);
    }

    public Spiller getValgt(){
		return valgt;
	}

	@Override
	public boolean evne(Spiller spiller) {
		TvUtil.toFront();
		
		if(this.spiller.skjult())
			ut += spiller + " har rollen " + Spill.spillere.randomSpiller(this.spiller, spiller).rolle();
		else
			ut += spiller + " har rollen " + spiller.rolle();

		ut += (forsinkelse != null) ? ".\n\n" : ".";
		
		if(blokkert) {
			if(blokk != forsinkelse) ut = "Filosofen ble blokkert forrige natt!\n\n";
			TvUtil.vis("Filosofen er blokkert");
			return false;
		}

		valgt = spiller;
		aktiver(false);
		TvUtil.vis(ut);
		return true;
	}
	
	@Override
	public void jul() {
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
