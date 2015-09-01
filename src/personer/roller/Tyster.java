package personer.roller;

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
    public String oppgave() {
        if (tv.spillere().hentSisteStemme(spiller) != null)
            evne(spiller);
        return super.oppgave();
    }

    @Override
    public boolean evne(Spiller spiller) {
        if (blokkert)
            return false;

        finnRolle(Rolle.MAFIA).informer(this, "\n\n" + tv.spillere().tyster(spiller, spiller.skjult()));

        return true;
    }
}
