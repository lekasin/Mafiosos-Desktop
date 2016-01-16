package personer.roller;

import Utils.SkjermUtil;
import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Joker extends Rolle {

    boolean opp, ferdig;

	public Joker(){
		super("Joker");
        bilde = "joker";
		oppgave = "Hva vil Jokeren velge?";
		veiledning = "Jokeren:\n" +
				"Jokeren våkner én gang i løpet av spillet, og innfører litt anarki!\n" +
				"Jokeren gir enten tommel opp eller tommel ned, \n" +
				"og velger med det hva som skal være riktig valg neste dag.\n" +
				"For å registreret valget hans trykker du på den tilsvarende knappen.\n" +
				"Om Jokeren får alle til å velge det samme som han neste dag, vinner han.";
        guide = "Ved 5 gjenstående spillere, er det Jokerens tur. På natten våkner han og velger «tommel opp» eller «tommel ned». " +
                "Neste dag må de gjenværende spillerne finne ut om de skal stemme opp eller ned. " +
                "De har da to minutter til å finne ut av dette, før en anonym avstemning foretas. Her er det dog lov å stemme motsatt av det landsbyen ble enige om. " +
                "Hvis alle stemmer likt som Jokeren, vinner Jokeren og spillet er over. " +
                "Hvis alle stemmer motsatt av Jokeren, dør Jokeren og spillet fortsetter. " +
                "Hvis spillerne stemmer ulikt, taper Jokeren, men overlever, og alle de som stemmer likt som ham dør.";
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
        SkjermUtil.logg(rapport());
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

        if (!funker)
            aktiver(false);
    }

    @Override
    public void klonRolle() {
        super.klonRolle();
        ferdig = true;
    }
}
