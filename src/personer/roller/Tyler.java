package personer.roller;

import gui.Spill;
import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Tyler extends Rolle {

	Spiller original;
	Rolle rolle;

	public Tyler(){
		super("Tyler Durden");
		oppgave = "Hvem vil Tyler besøke?";
		veiledning = "Tyler Durden:\n" +
				"Tyler Durden våkner hver natt, og får vite hvilken rolle han har den natten. " +
                "Han velger så en person å bruke rollen på.\n" +
				"Når Tyler har valgt, trykker du på vedkommendes navn for å velge dem.\n" +
				"Tyler får en tilfeldig valgt rolle hver natt.";
        guide = "Tyler har Dissosiativ Identitetsforstyrrelse. " +
                "Han våkner hver natt med en ny personlighet, og får vite hvilken rolle han skal være denne natten. " +
                "Om Tyler får vite at han er tjukkas, peker han på en som skal blokkes. " +
                "Er han lege, kan han lege, ravne, kløne osv. " +
                "Ikke minst kan han drepe om han er mafia. " +
                "PS: Tyler kan ikke være trompet eller en annen rolle som brukes på dagtid, peker på flere enn én person, " +
                "eller har evner som foregår over flere netter.";
		side = BORGER;
		prioritet = TYLER;
		skjerm = true;
	}

	@Override
	public String oppgave() {
        rolle = this;
        if (nyligKlonet())
            return super.oppgave();

		if(funker) {
			rolle = Spill.spillere.tylersRolle();
			original = rolle.spiller();
			rolle.setSpiller(this.spiller);
		}
		TvUtil.vis("Tyler er nå " + rolle + "!\n");

		TvUtil.leggTil(oppgave);
		TvUtil.toFront();
		if(informert) TvUtil.leggTil(info);

		return oppgave;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;

		if(rolle.pri() < Rolle.QUISLING && rolle.pri() > Rolle.UNDERCOVER) {
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
	
	@Override
	public void sov() {
		if(rolle != null && original != null)
            rolle.setSpiller(original);
		super.sov();
	}

    @Override
    public void vekk() {
        rolle = this;
        super.vekk();
    }
}
