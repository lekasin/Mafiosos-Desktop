package personer.roller;

import Utils.TvUtil;
import personer.Rolle;
import personer.Spiller;

public class Quisling extends Rolle {

    boolean konvertert = false;

    public Quisling() {
        super("Quisling");
        oppgave = "Hvordan går det med Quisling?";
        veiledning = "Quisling:\n" +
                "Quisling våkner hver natt og får vite om han er drept eller ikke.\n" +
                "Hvis Quisling ikke er drept, legger han seg til å sove igjen. " +
                "Hvis han er drept, konverterer han til mafiaens side.\n" +
                "Quisling velger aldri noen på natten, så for å gå videre trykker du på fortsett.";
        guide = "If you can’t beat’em, join em! " +
                "Hvis mafiaen prøver å drepe Quisling på natten, overlever han og konverterer over til mafiaen sin side." +
                "Han blir da fullt og helt mafia, og våkner fra da av sammen med dem. " +
                "Frem til da er han  fullt og helt borger, og heier på landsbyen. " +
                "Han våkner hver natt, og får vite om han er drept eller ikke.";
        side = BORGER;
        prioritet = QUISLING;
        skjerm = true;
    }

    public void konverter() {
        konvertert = true;
    }

    public boolean konvertert() {
        return konvertert;
    }

    @Override
    public String oppgave() {
        if (spiller.rolle() == this) {
            TvUtil.visQuislingBeskjed(lever, spiller);
        } else {
            if (funker) funk(false);
            TvUtil.vis("Quisling har allerede konvertert.");
        }
        if (spiller.nyligKlonet())
            return super.oppgave();

        if (informert) TvUtil.leggTil(info);
        TvUtil.toFront();
        return oppgave;
    }

    @Override
    public boolean evne(Spiller spiller) {
        return true;
    }
}
