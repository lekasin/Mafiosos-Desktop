package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Princess extends Rolle {

    String fanger;
    boolean infiltrert = false;

    public Princess() {
        super("Princess98");
        oppgave = "Hvem vil Jonatan kidnappe?";
        veiledning = "Princess98:\n" +
                "Jonatan velger hver natt en person å kidnappe.\n" +
                "Når Jonatan har valgt, trykker du på vedkommendes navn for å kidnappe dem.\n" +
                "Neste dag vises det på skjermen hvem som er kidnappet. " +
                "De kidnappe kan hverken prate, stemme eller bruke rollen sin.\n" +
                "Når Jonatan blir drept eller arrestert av politiet, blir fangene frigitt.";
        side = NØYTRAL;
        prioritet = PRINCESS;
    }

    public boolean befridd() {
        if (!spiller.lever() || (blokkert() && blokk.id(POLITI) && !infiltrert) || (offer != null && offer.rolle() != blokk && infiltrert))
            return true;
        return false;
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert)
            if (blokk.id(POLITI) && offer.id(POLITI))
                infiltrert = true;
            else
                return false;

        if (!spiller.id(PRINCESS))
            spiller.kidnapp(this);
        return true;
    }
}
