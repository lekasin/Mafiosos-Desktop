package personer.roller;

import gui.Spill;
import personer.Rolle;
import personer.Spiller;

public class Tyster extends Rolle {

    public Tyster() {
        super("Tyster");
        bilde = "tyster";
        oppgave = "Tysteren våkner";
        veiledning = "Tyster:\n" +
                "Hvis personen våknet før informanten på natten, får de ingen info.";
        guide = "Tysteren er på mafiaen sitt lag, og fremstår som mafia ved henrettelse, men våkner aldri sammen med de andre mafiaene. " +
                "Tysteren gjør derimot jobben sin på egenhånd, når ingen venter det. " +
                "Ved hver avstemning sjekker nemlig tysteren bakgrunnen til alle som er ved stemmeboden samtidig med han, og finner ut hvilke roller som er representert. " +
                "Denne infoen videreformidler han så til mafiaen ved solnedgang. " +
                "Med andre ord får mafiaen hver natt vite hvilke roller som stemte det samme som tysteren ved siste avstemning dagen før.";
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
