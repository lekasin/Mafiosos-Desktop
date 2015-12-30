package personer.roller;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Politi extends Rolle {

	int antall = 1, levende = 1;
	
	public Politi(){
		super("Politi");
		oppgave = "Hvem vil Politiet arrestere?";
		veiledning = "Politi:\n" +
				"Politiet velger hver natt en person å arrestere.\n" +
				"Når politiet har valgt, trykker du på vedkommendes navn for å arrestere dem.\n" +
                "Hvis den arrestert har en drapsrolle, får ikke vedkommende drept noen denne natten.";
        guide = "Politiets jobb er å finne mafiaen og stoppe dem fra å drepe borgerne. " +
                "Politiet kan være alene, eller beste av flere personer, og peker hver natt ut en person de tror er mafia. " +
                "Denne personen blir da satt i varetekt, og kan ikke drepe noen den natten. " +
                "Politiet fungerer altså kun på drapsroller (Mafia, Morder, Heisenberg, osv), men har ett unntak: " +
                "Hvis den arresterte peker på politiet samme natt som han arresteres, dør politiet likevel, ettersom både han og den mistenkte oppholder seg i fengselet.";
		side = BORGER;
		prioritet = POLITI;
	}

    @Override
	public int antall(){
		return antall;
	}
	
	public void fler(){
		antall++;
		levende++;
	}
	
	public void fjern(){
		antall--;
		levende--;
	}
	
	public boolean flere(){
		return levende > 1;
	}
	
	@Override
	public void drep(){
		if(!flere())
			lever = false;
		levende--;
	}
	
	@Override
	public boolean pek(Spiller spiller) {
		for(Spiller s: Spill.spillere.spillere())
			if(s.id(POLITI) && s.funker()){
				s.setOffer(spiller);
				if(!this.spiller.lever())
					setSpiller(s);
			}
		
		return super.pek(spiller);
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		if(spiller.side() < 1) {
			if(!spiller.rolle().blokkert()) spiller.blokker(this);
		}

		return true;
	}
}
