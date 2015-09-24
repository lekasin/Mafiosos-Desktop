package Utils;

import datastruktur.Mafiosos;
import personer.Rolle;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by lars-erikkasin on 24.09.15.
 */
public class MenyUtil {
    public static JMenuBar getMeny() {
        return lagMenyer();
    }

    public static JMenuBar lagMenyer() {
        JMenuBar m = new JMenuBar();
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

        JMenu rolleMeny = new JMenu("Roller A-N");
        rolleMeny.getAccessibleContext().setAccessibleDescription("Rolleforklaringer");
        guide.add(rolleMeny);
        JMenu rolleMeny2 = new JMenu("Roller O-Å");
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
}
