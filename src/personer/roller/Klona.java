package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Klona extends Rolle {

	public Klona(){
		super("Kløna");
		oppgave = "Hvem vil Kløna sikte på?";
		veiledning = "Kløna:\n" +
				"Kløna velger hver natt en person å sikte på.\n" +
				"Når kløna har valgt, trykker du på vedkommendes navn for kløne dem.\n" +
				"Den kløna sikter på, får ikke holde forsvarstale neste dag, " +
                "men dør umiddeltbart, hvis vedkommende får flest stemmer under en avstemning.";
        guide = "Kløna er et klønete medlem av landsbyen. " +
                "Han velger en person å sikte på hver natt, og kommer borti avtrekkeren på henrettelsesgeværet hvis personen skal holde forsvarstale neste dag. " +
                "Med andre ord, når personen kløna valgte skal starte forsvarstalen sin neste dag, dør plutselig personen istedenfor å kunne forsvare seg. " +
                "Dette gjelder også hvis kløna har valgt én av to personer som har like mange stemmer, og begge må forsvare seg.";
		side = BORGER;
		prioritet = KLØNA;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		if(this.spiller.lever())
			spiller.kløn(this);
		return true;
	}
}
