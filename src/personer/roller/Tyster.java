package personer.roller;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Tyster extends Rolle {

    public Tyster() {
        super("Tyster");
        oppgave = "Tysteren våkner";
        veiledning = "Tyster:\n" +
                "Hvis personen våknet før informanten på natten, får de ingen info.";
        side = MAFIOSO;
        prioritet = TYSTER;
        aktiver(false);
    }

    @Override
    public void autoEvne() {
        if (funker && Spill.spillere.hentSisteStemmeFra(spiller) != null)
            evne(spiller);
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert)
            return false;

        finnRolle(Rolle.MAFIA).informer(this, "\n\n" + Spill.spillere.tyster(spiller, spiller.skjult()));

        if (Spill.spillere.sjekkRolle(DIDRIK))
            spiller.rens(finnRolle(DIDRIK));
        return true;
    }
}
