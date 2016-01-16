package personer.roller;

import Utils.SkjermUtil;
import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Hammer extends Rolle {

	int søk = 0;
	Spiller valgt;
	String ut;
	
	public Hammer() {
		super("Hammer");
        bilde = "hammer";
		oppgave = "Hvem vil Hammer undersøke?";
		veiledning = "Hammer:\n" +
				"Hammer undersøker personer for å finne ut hvilken rolle de har, men han får ikke svar før etter to netter med undersøkelser.\n" +
				"For å undersøke en person, trykker du på navnet til personen Hammer velger. Resultatet av undersøkelsen kommer da opp på skjermen.\n" +
				"Første natt Hammer undersøker en person, kommer det opp at han ikke har gjort nok research. " +
                "For å få et resultat, må hammer undersøke samme person to netter på rad. Da vises vedkommendes rolle på skjermen.\n" +
				"Personen Hammer undersøker for øyeblikket vil ha navn i grønt, neste natt.";
        guide = "Hammer er Telens eminente redaktør og gravene journalist. Han peker hver natt ut en mistenkt han ønsker å undersøke nærmere. " +
                "Neste natt kan han velge å konkludere rundt personen han har valgt (ved å peke på samme person igjen), og dermed få vite om denne personen er mafia eller ikke. " +
                "Eventuelt kan han velge en ny person, men da må han igjen vente til neste natt med å konkludere. " +
                "Altså får hammer først info etter å ha pekt på samme person to netter på rad.";
		side = BORGER;
		prioritet = HAMMER;
		fortsett = false;
	}
	
	public Spiller valgt(){
		return valgt;
	}
	
	@Override
	public boolean evne(Spiller spiller) {
		TvUtil.toFront();
		if(blokkert){
			TvUtil.vis("Hammer ble blokkert");
			return false;
		}
		
		if(søk == 0 || valgt != spiller) {
			valgt = spiller;
            ut = "Ikke nok research";
			TvUtil.vis(ut);
			søk = 1;
		}
		else {
			int resultat = spiller.side();
			if(spiller.løgn()) resultat = resultat-(2*resultat);
			ut = spiller + " er ";
			if(resultat >= 0) ut += "ikke ";
			ut += "mafia!";
			TvUtil.vis(ut);
			søk = 0;
			valgt = null;
		}

		return true;
	}

    @Override
    public String rapport() {
        if (ut.isEmpty())
            return super.rapport();
        return super.rapport() + "\nInfo: " + ut;
    }
}
