package personer.roller;

import Utils.SkjermUtil;
import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Heisenberg extends Rolle {
	
	String giftRapport = "";
    Spiller forgiftet;

	public Heisenberg(){
		super("Heisenberg");
        bilde = "heisenberg";
		oppgave = "Hvem vil Heisenberg forgifte?";
		veiledning = "Heisenberg:\n" +
				"Heisenberg kan hver natt velge å drepe eller ikke drepe.\n" +
				"Hvis han ikke ønsker å forgifte noen, trykker du på fortsett, for å gå videre.\n" +
                "Hvis Heisenberg vil forgifte noen, trykker du på navnet til den han velger.\n" +
                "Personen vil da dø av giften neste natt.";
        guide = "Heisenberg er en borger som våkner hver natt og kan velge en person som skal forgiftes. " +
                "En forgiftet person lever dagen derpå, men dør deretter den påfølgende natten. " +
                "Heisenberg ønsker å drepe mafiaen og vinne spillet sammen med landsbyen, " +
                "og bør derfor være sikker på at det er en mafia han forgifter. " +
                "Hvis han ikke er sikker, kan han om natten velge å ikke forgifte noen.";
		side = BORGER;
		prioritet = HEISENBERG;
	}

	@Override
	public boolean evne(Spiller spiller) {
		forby(spiller);
		if(blokkert)
			return false;
		
		if(!spiller.beskyttet() || snill) {
            Spill.spillere.leggInnDelay(spiller, this);
            forgiftet = spiller;
        }
		return true;
	}

    @Override
    public void delay(Spiller offer) {
        System.out.println("Heisenberg delay: " + this + " velger " + offer + "(" + forgiftet + ")");
        if (offer != forgiftet) {
            super.delay(offer);
            return;
        }
        giftRapport = "";
        if(offer != null){
            if(snill)
                offer.snipe(this);
            else
                offer.drep(this);
            giftRapport = "Heisenbergs gift svekker " + offer + "(" + offer.rolle() + ")";
        }
        SkjermUtil.logg(giftRapport);
    }
}
