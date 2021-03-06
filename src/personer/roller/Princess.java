package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Princess extends Rolle {

    String fanger;
    boolean infiltrert = false;

    public Princess() {
        super("Princess98");
        bilde = "princess";
        oppgave = "Hvem vil Jonatan kidnappe?";
        veiledning = "Princess98:\n" +
                "Jonatan velger hver natt en person å kidnappe.\n" +
                "Når Jonatan har valgt, trykker du på vedkommendes navn for å kidnappe dem.\n" +
                "Neste dag vises det på skjermen hvem som er kidnappet. " +
                "De kidnappe kan hverken prate, stemme eller bruke rollen sin.\n" +
                "Når Jonatan blir drept eller arrestert av politiet, blir fangene frigitt.";
        guide = "Princess98 velger hver natt en person å kidnappe. Vedkommende blir da satt i fangekjelleren, og er «midlertidig død». " +
                "Dvs at danger ikke kan snakke eller stemme på dagen, men kan heller ikke pekes på av andre roller, og dermed heller ikke drepes." +
                "De kan ikke selv bruke sin rolle, men er fortsatt med i spillet, og må holde øyenene lukket om natten for å ikke se hva som foregår. " +
                "Hvis Princess 98 dør, blir smithet eller blir arrestert av politiet, slippes alle fangene løs, og kan spille videre på vanlig måte. " +
                "Om Princess98 fortsatt lever kan han på ny begynne å kidnappe folk neste natt. " +
                "Princess98 er på sitt eget lag, og vinner spillet hvis han klarer å kidnappe hele landsbyen.";
        side = NØYTRAL;
        prioritet = PRINCESS;
    }

    public boolean befridd() {
        if (!spiller.lever() || (blokkert() && blokk.id(POLITI) && !infiltrert) || (offer != null && offer.rolle() != blokk && infiltrert) || nyligKlonet())
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
