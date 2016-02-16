package Utils;

import datastruktur.Mafiosos;
import gui.Oppstart;
import gui.Spill;
import personer.Rolle;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Created by lars-erikkasin on 24.09.15.
 */
public class MenyUtil {
    private static JMenu spillMeny, tvSpillMeny;

    public static JMenuBar lagMenyer() {
        JMenuBar m = new JMenuBar();
        m.add(innstillingsMeny());
        m.add(skjermMeny());
        m.add(guideMeny());
        return m;
    }

    public static JMenu skjermMeny() {
        JMenu skjerm = new JMenu("Display");
        skjerm.setMnemonic(KeyEvent.VK_S);
        skjerm.getAccessibleContext().setAccessibleDescription(
                "Innstillenger for TV-display");

        JMenuItem fullskjerm = new JMenuItem("Fullskjerm");
        fullskjerm.addActionListener(e -> TvUtil.visSkjulRamme());
        skjerm.add(fullskjerm);

        JMenu tekstMeny = new JMenu("Tekststørrelse");
        tekstMeny.setMnemonic(KeyEvent.VK_T);
        tekstMeny.getAccessibleContext().setAccessibleDescription(
                "Endre tekststørrelsen på displayet");
        skjerm.add(tekstMeny);

        JMenuItem tekstItem = new JMenuItem("Hoveddisplay");
        tekstItem.addActionListener(e -> {
            String input = JOptionPane
                    .showInputDialog("Skriv inn skriftstørrelse (Standard: 30)");
            if (input != null && input.matches("\\d{1,2}")) {
                int størrelse = Integer.parseInt(input);
                TvUtil.setTvFont(størrelse);
            }
        });
        tekstMeny.add(tekstItem);

        tekstItem = new JMenuItem("Rolleliste");
        tekstItem.addActionListener(e -> {
            String input = JOptionPane
                    .showInputDialog("Skriv inn skriftstørrelse (Standard: 30)");
            if (input != null && input.matches("\\d{1,2}")) {
                int størrelse = Integer.parseInt(input);
                TvUtil.setRollelisteFont(størrelse);
            }
        });
        tekstMeny.add(tekstItem);

        tekstItem = new JMenuItem("Guide");
        tekstItem.addActionListener(e -> {
            String input = JOptionPane
                    .showInputDialog("Skriv inn skriftstørrelse (Standard: 25)");
            if (input != null && input.matches("\\d{1,2}")) {
                int størrelse = Integer.parseInt(input);
                TvUtil.setGuideFont(størrelse);
            }
        });
        tekstMeny.add(tekstItem);

        JMenu fargeMeny = new JMenu("Farger");
        fargeMeny.setMnemonic(KeyEvent.VK_T);
        fargeMeny.getAccessibleContext().setAccessibleDescription(
                "Endre fargene på displayet");
        skjerm.add(fargeMeny);

        JMenuItem fargeitem = new JMenuItem("Hoveddisplay");
        fargeitem.addActionListener(e -> TvUtil.setTvFarge(false));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Hoveddisplaytekst");
        fargeitem.addActionListener(e -> TvUtil.setTvFarge(true));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Rolleliste");
        fargeitem.addActionListener(e -> TvUtil.setRollelisteFarge(false));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Rollelistetekst");
        fargeitem.addActionListener(e -> TvUtil.setRollelisteFarge(true));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("Guide");
        fargeitem.addActionListener(e -> TvUtil.setGuideFarge(false));
        fargeMeny.add(fargeitem);

        fargeitem = new JMenuItem("GuideTekst");
        fargeitem.addActionListener(e -> TvUtil.setGuideFarge(true));
        fargeMeny.add(fargeitem);

        return skjerm;
    }

    public static JMenu guideMeny() {
        JMenu guide = new JMenu("Guide");
        guide.setMnemonic(KeyEvent.VK_G);
        guide.getAccessibleContext().setAccessibleDescription(
                "Rollerforklaringer og øvrig veiledning");

        JMenuItem lukk = new JMenuItem("Lukk");
        lukk.addActionListener(e -> TvUtil.lukkGuide());
        guide.add(lukk);

        JMenu rolleMeny = new JMenu("Roller A-J");
        rolleMeny.getAccessibleContext().setAccessibleDescription("Rolleforklaringer");
        guide.add(rolleMeny);
        JMenu rolleMeny2 = new JMenu("Roller K-Å");
        rolleMeny2.getAccessibleContext().setAccessibleDescription("Rolleforklaringer");
        guide.add(rolleMeny2);

        JMenuItem rolleItem;
        for (Rolle rolle : Mafiosos.roller()) {
            rolleItem = new JMenuItem(rolle.toString());
            rolleItem.addActionListener(e -> TvUtil.visGuide(rolle.getGuide()));
            if (Mafiosos.roller().indexOf(rolle) <= 30)
                rolleMeny.add(rolleItem);
            else
                rolleMeny2.add(rolleItem);
        }

        return guide;
    }

    public static JMenu innstillingsMeny() {
        JMenu innstillinger = new JMenu("Innstillinger");
        innstillinger.setMnemonic(KeyEvent.VK_I);
        innstillinger.getAccessibleContext().setAccessibleDescription(
                "Diverse innstillinger");

        JMenuItem veiledning = new JMenuItem("Vis/Skjul fortellerveiledning");
        veiledning.addActionListener(e -> VeiledningsUtil.visSkjulVeiledning());
        innstillinger.add(veiledning);

        JMenuItem dagtid = new JMenuItem("Sett dagtid");
        dagtid.addActionListener(e -> InnstillingsUtil.setDagtid());
        innstillinger.add(dagtid);

        JMenuItem stemmetid = new JMenuItem("Sett stemmetid");
        stemmetid.addActionListener(e -> InnstillingsUtil.setStemmetid());
        innstillinger.add(stemmetid);

        JMenuItem fortellerinfo = new JMenuItem("Vis fortellerinfo");
        fortellerinfo.addActionListener(e -> InnstillingsUtil.promptFortellerInfo());
        innstillinger.add(fortellerinfo);

        return innstillinger;
    }

    public static JMenu dagsMeny(int fase, JMenu spill) {
        if (spill == null) {
            spill = new JMenu("Spill");
            spill.setMnemonic(KeyEvent.VK_S);
            spill.getAccessibleContext().setAccessibleDescription(
                    "Funksjoner i spillet");
        } else
            spill.removeAll();

        if (fase == Oppstart.VELGROLLER) {
            JMenuItem nySpiller = new JMenuItem("Legg til spiller");
            nySpiller.addActionListener(e -> InnstillingsUtil.promptLeggTilSpiller());
            spill.add(nySpiller);
        } else if (fase < 10) {
            JMenuItem navnEndring = new JMenuItem("Endre spillernavn");
            navnEndring.addActionListener(e -> Spill.instans.navnEndring());
            spill.add(navnEndring);

            if (fase == Spill.DISKUSJONSFASE || fase == Spill.NATTFASE) {
                JMenuItem røm = new JMenuItem("Fjern spiller");
                røm.addActionListener(e -> Spill.instans.rømning());
                spill.add(røm);
            }
        } else
            return null;

        return spill;
    }


    public static void visSpillMeny(JFrame vindu, int fase) {
        JMenu spill, tvSpill;
        spill = dagsMeny(fase, spillMeny);
        tvSpill = dagsMeny(fase, tvSpillMeny);

        if (spill != null) {
            vindu.getJMenuBar().add(spill);
            TvUtil.tv.getJMenuBar().add(tvSpill);
            spillMeny = spill;
            tvSpillMeny = tvSpill;
        } else
            skjulSpillMeny(vindu);
    }

    public static void skjulSpillMeny(JFrame vindu) {
        if (Arrays.asList(vindu.getJMenuBar().getComponents()).contains(spillMeny)) {
            vindu.getJMenuBar().remove(spillMeny);
            TvUtil.tv.getJMenuBar().remove(tvSpillMeny);
            spillMeny = null;
            tvSpillMeny = null;
        }
    }
}
