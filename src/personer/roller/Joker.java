package personer.roller;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Joker extends Rolle {

    boolean opp, ferdig;

	public Joker(){
		super("Joker");
		oppgave = "Jokeren skaper kaos!";
		veiledning = "Jokeren:\n" +
				"Jokeren våkner én gang i løpet av spillet, og innfører litt anarki!\n" +
				"Jokeren gir enten tommel opp eller tommel ned, \n" +
				"og velger med det hva som skal være riktig valg neste dag.\n" +
				"For å registreret valget hans trykker du på den tilsvarende knappen.\n" +
				"Om Jokeren får alle til å velge det samme som han neste dag, vinner han.";
		side = NØYTRAL;
		prioritet = JOKER;
        aktiv = false;
	}


    public void bliFerdig(){
        aktiver(false);
        ferdig = true;
    }

	@Override
	public boolean evne(Spiller spiller) {
		return true;
	}

    public void setOpp(boolean opp) {
        this.opp = opp;
    }

    public boolean fasit(){
        return opp;
    }

    @Override
    public String rapport() {
        String ut = tittel + "(" + spiller + ")";

        ut += " har valgt " + (opp ? "OPP" : "NED") + "!";
        return ut;
    }

    @Override
    public void autoEvne() {
        if (Spill.spillere.levende().size() < 6 && !ferdig && lever())
            aktiver(true);
    }
}
