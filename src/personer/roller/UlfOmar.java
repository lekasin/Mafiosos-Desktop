package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class UlfOmar extends Rolle {

	Spiller valgt;
	String ut;
	
	public UlfOmar() {
		super("Ulf Omar");
        bilde = "ulfomar";
        oppgave = "Hvem vil Ulfie-O besøke?";
		veiledning = "Ulf Omar:\n" +
				"Ulf Omar velger hver natt en person å besøke.\n" +
				"Når Ulf Omar har valgt, trykker du på vedkommendes navn for å velge dem.\n" +
				"Ulf Omar vil da få den valgte personens rolle brukt på seg selv.";
        guide = "Ulfie-O, som er Ulf Omars kallenavn, våkner opp hver natt og vil gamble med livet sitt. " +
                "Han peker på en person, og lar dermed personens egenskaper ramme han selv. " +
                "Peker Ulfie-O på en lege, har UO vært heldig og uvitende gitt seg selv beskyttelse. " +
                "Men har han pekt på en mafia, ja, da er mest sannsynlig livet til storgambler Ulf over og ut.";
		side = BORGER;
		prioritet = ULF;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert){
			return false;
		}
		
		Rolle rolle = spiller.rolle();

		if(rolle.pri() > UNDERCOVER && rolle.pri() < ULF && !spiller.id(COPYCAT) && !spiller.id(BERIT) || rolle.pri() == SMITH || rolle.pri() == KLØNA || rolle.pri() == MARIUS || rolle.pri() == RAVN) {
			Spiller forb = rolle.forbud();
			rolle.forby(null);
		
			if(spiller.rolle().blokkert()) {
				Rolle r = spiller.rolle().hvemBlokk();
				spiller.rolle().rens(r);
				spiller.rolle().evne(this.spiller);
				spiller.blokker(r);
			}
			else
				spiller.rolle().evne(this.spiller);
		
			rolle.forby(forb);
		}

		return true;
	}
}
