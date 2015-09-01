package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Smith extends Rolle {

	Spiller klone = null;
	int antall = 1;

	public Smith(){
		super("Agent Smith");
		oppgave = "Hvem vil Smith klone?";
		veiledning = "Agent Smith:\n" +
				"Smith velger hver natt en person å klone til en Smith.\n" +
				"Når Smith har valgt, trykker du på vedkommendes navn for å klone dem.\n" +
				"Neste gang vedkommende våkner, vises det på skjermen at de er klonet, " +
                "og fra nå av er en Smith.\n" +
				"Nye Smither skal fra da av våkne når Smith våkner. " +
                "Så lenge én Smith lever, kan Smith fortsette å klone.";
		side = NØYTRAL;
		prioritet = SMITH;
	}

	public void klon(Spiller spiller) {
		spiller.rolle().klonRolle();
		spiller.setRolle(this);
		antall++;
	}
	
	@Override
	public boolean pek(Spiller spiller) {
		for(Spiller s: tv.spillere().spillere())
			if(s.id(SMITH) && s.funker()){
				s.setOffer(spiller);
				if(!this.spiller.lever() && spiller.lever())
					setSpiller(s);
			}
		
		return super.pek(spiller);
	}

	@Override
	public void drep() {
		if(--antall == 0 && klone == null)
			super.drep();
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(!(spiller.id(Rolle.MAFIA) || blokkert || (spiller.beskyttet()) && !(spiller.id(Rolle.ILLUSJONIST) && spiller.rolle().offer().id(Rolle.SMITH)))) {
			spiller.klon(this);
			klone = spiller;
		}
		return true;
	}
}
