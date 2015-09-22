package Utils;

import datastruktur.Mafiosos;
import personer.Rolle;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by lars-erikkasin on 17.09.15.
 */
public class VeiledningsUtil {
    private static JTextArea info;
    public static final int ORDFØRERFASE = 0, DISKUSJONSFASE = 1, AVSTEMNINGSFASE = 2,
            TALEFASE = 3, GODKJENNINGSFASE = 4, TIEBREAKERFASE = 5, JOKERFASE = 6, RØMNINGSFASE = 99;


    public static void init(JTextArea info) {
        VeiledningsUtil.info = info;
    }

    public static void setTekst(String tekst) {
        info.setText(tekst);
        if (tekst.isEmpty())
            info.setVisible(false);
    }

    public static void visVeiledning(boolean vis) {
        info.setVisible(vis);
    }

    public static void visSkjulVeiledning() {
        visVeiledning(!info.isVisible());
    }

    public static int FASE_BOMBE = 0, FASE_TILTALE = 1, FASE_RAKETT = 2, FASE_OPPGJØR = 3;

    public static void setVeiledningForFase(int fase, int unntak) {
        switch (fase) {
            case ORDFØRERFASE:
                setTekst("Valgfase:\n" +
                        "Det er på tide å velge en ordfører, som får dobbeltstemme ved alle avstemninger!\n" +
                        "Før spillet begynner for alvor, må landsbyen bestemme seg for om de ønsker en ordfører, og hvem dette i så fall skal være.\n" +
                        "Når en ordfører er valgt, trykker du på denne personens navn for å innsette vedkommende." +
                        "Om dere ikke ønsker å ha med noen ordfører, trykker du på fortsett.");
                break;
            case DISKUSJONSFASE:
                if (unntak == FASE_BOMBE)
                    setTekst("Diskusjonsfasen - Bombe:\n" +
                            "Bomberen har plantet bomben, og spillerne har nå 2 minutter til å finne ut hvem de vil henrette.\n" +
                            "For å henrette en spiller, trykker du på vedkommendes navn. Det blir ingen forsvarstaler eller organisert avstemning. Landsbyen må bare bli enige\n" +
                            "Klarer landsbyen å drepe bomberen, blir bomben desarmert, og kun bomberen dør. Henretter de noen andre, dør både denne personen OG den bomben er plantet hos.\n" +
                            "Blir ingen henrettet, dør den bomben er plantet hos, og alle som besøkte vedkommende i natt.");
                else if (unntak == FASE_OPPGJØR)
                    setTekst("Diskusjonsfasen - Oppgjørets Time:\n" +
                            "Spillerne har nå en siste sjanse til å diskutere, og finne ut hvem som er mafia.\n" +
                            "En dag kan maks inneholde 3 forsvarstaler (Kan bli flere ved uavgjort siste avstemning). " +
                            "I oppgjørets time er det derfor ikke mulig å mistenke andre enn de som har holdt forsvarstale, " +
                            "men alle disse kan nå stemmes på.\n");
                else
                    setTekst("Diskusjonsfasen:\n" +
                            "Spillerne skal nå diskutere, og finne ut hvem som er mistenkt for å være mafia.\n" +
                            "For å mistenke en person, trykker du på personens navn. Personen blir da lagt til i mistenktlista og blir en del av den kommende avstemningen.\n" +
                            "For å fjerne en person fra mistenktlista, trykker du på personens navn igjen.\n" +
                            "Personer med navn i blått, er mistenkte, mens personer i rødt er både mistenkt, og vil dø om de får flest stemmer (som oftest fordi de har holdt forsvarstale).");
                break;
            case AVSTEMNINGSFASE:
                if (unntak == FASE_RAKETT)
                    setTekst("Avstemning - Rakettoppskytning:\n" +
                            "Astronauten har fullført raketten sin, og landsbyen skal nå anonymt stemme over hvem som skal sendes ut i rommet (og dø).\n" +
                            "Alle spillere er nå mistenkt, og må lukke øynene før avstemningen begynner. Astronauten derimot, kan se alt.\n" +
                            "Hver person vil være oppe til avstemning i 15 sekunder, og for å registrere stemmer, trykker du på navnet til de som rekker opp hånda.\n" +
                            "Hvert navn vises kun én gang, og hver person kan stemme én gang, men Astronauten har dobbeltstemme.\n" +
                            "Etter rakettoppskytningen fortsetter dagen som normalt, men med kortere tid.");
                else if (unntak == FASE_TILTALE)
                    setTekst("Avstemning - Tiltale:\n" +
                            "Spillerne skal nå stemme for eller imot å drepe den tiltalte.\n" +
                            "For å registrere en stemme, trykker du på navnet til den som stemmer.\n" +
                            "Avstemningen varer i 15 sekunder, og alle som stemmer for, gjør dette ved å tydelig rekke opp hånda.\n" +
                            "Den tiltalte blir henrettet hvis han får minst halvparten av stemmene, " +
                            "og ellers går vi til en ny natt uten henrettelse.");
                else
                    setTekst("Avstemning:\n" +
                            "Spillerne skal nå stemme på personen de tror er mafia.\n" +
                            "For å registrere en stemme, trykker du på navnet til den som stemmer når den mistenktes navn vises.\n" +
                            "Hvert navn vises kun én gang, og hver person kan stemme én gang, " +
                            "ved å tydelig rekke opp hånda når den de vil stemme på vises.\n" +
                            "Alle navn i mistenktlista vil vises for avstemning i 15 sekunder.");
                break;
            case GODKJENNINGSFASE:
                setTekst("Godkjenning:\n" +
                        "Spillerne har nå stemt ut en mistenkt, og vedkommende er i ferd med å henrettes.\n" +
                        "Ved å trykke godkjenn, henrettes den utsemte, og det avsløres om vedkommende var på mafiaens eller borgernes side.\n" +
                        "Om noe er gått galt, og du vil avbryte henrettelsen, kan du trykke avbryt for å bli tatt tilbake til diskusjonsfasen.");
                break;
            case TALEFASE:
                if (unntak == FASE_TILTALE)
                    setTekst("Forsvarstale - Tiltale:\n" +
                            "Aktor har kommet med en tilale og vi går derfor rett til den tiltaltes forsvarstale.\n" +
                            "Den tiltalte får ett minutt til å forsvare seg, hvor ingen andre spillere får si noe.\n" +
                            "Når tiden går ut, går vi over i en avstemning, hvor spillerne skal stemme for eller imot å henrette den tiltalte.\n" +
                            "For å avslutte forsvarstalen tidlig og gå rett til avstemningen, trykk fortsett.");
                else
                    setTekst("Forsvarstale:\n" +
                            "Det er nå klart for forsvarstale.\n" +
                            "Den forsvarende spilleren får ett minutt til å forsvare seg, hvor ingen andre spillere får si noe.\n" +
                            "Når tiden går ut, starter en ny diskusjonsfase, hvor spillerne kan respondere på talen, og eventuelt finne nye mistenkte.\n" +
                            "For å avslutte forsvarstalen tidlig og gå tilbake til diskusjonsfasen, trykk fortsett.");
                break;
            case RØMNINGSFASE:
                setTekst("Rømningsforsøk:\n" +
                        "Du kan her fjerne en spiller fra spillet ved å la vedkommende rømme.\n" +
                        "For å la en spiller rømme, trykker du på spillerens navn nå.\n" +
                        "Spilleren blir da fjernet fra spillet, og du går tilbake til diskusjonsfasen\n" +
                        "For å gå tilbake uten å foreta en rømning, trykker du på fortsett.");
                break;
            case TIEBREAKERFASE:
                setTekst("Uavgjort:\n" +
                        "Ved å klikke på et navn nå, vil du umiddelbart henrette vedkommende.\n" +
                        "Ved uavgjort avstemning har de siste som døde muligheten til å komme med en avgjørende stemme." +
                        "Dette forutsetter at disse ikke har vært våken på natten, eller på annen måte fått avslørt hvem som er mafia." +
                        "De som har muligheten til å avgi sin stemme kommer opp på skjermen.\n" +
                        "Hvis ingen nylig avdøde vises på skjermen, må dette løses på annen måte. En mye brukt løsning er å ringe en opplysningstelefon.\n" +
                        "For å gå til en ny natt, uten å henrette noen, trykker du fortsett.");
                break;
            case JOKERFASE:
                setTekst("Jokerens dilemma:\n" +
                        "Jokeren har nå kommet med sitt dilemma, og landsbyen har to minutter til å komme frem til en avgjørelse.\n" +
                        "De må sammen bestemme seg for å stemme opp eller ned for å prøve å unngå Jokerens vrede. Det er likevel lov til å stemme imot det landsbyen blir enige om.\n" +
                        "Når landsbyen har bestemt seg, eller tiden går ut, må alle lukke øynene for en anonym avstemning, hvor alle skal velge enten tommel opp eller ned.\n" +
                        "For å gå rett til avstemningen, trykker du på fortsett. Når avstemningen er i gang, trykker du på alle som stemmer OPP (appen ordner resten).\n" +
                        "Når avstemningen er ferdig, trykker du fortsett igjen for å se resultatet.");
                break;

            default:
                setTekst("Veiledning");
                break;
        }
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

        JMenuItem rolleItem;
        for (Rolle rolle : Mafiosos.roller()) {
            if (Mafiosos.roller().indexOf(rolle) <= 30) {
                rolleItem = new JMenuItem(rolle.toString());
                rolleItem.addActionListener(e -> TvUtil.visGuideFor(rolle.pri()));
                rolleMeny.add(rolleItem);
            }
        }

        JMenu rolleMeny2 = new JMenu("Roller O-Å");
        rolleMeny2.getAccessibleContext().setAccessibleDescription("Rolleforklaringer");
        guide.add(rolleMeny2);

        for (Rolle rolle : Mafiosos.roller()) {
            if (Mafiosos.roller().indexOf(rolle) > 30) {
                rolleItem = new JMenuItem(rolle.toString());
                rolleItem.addActionListener(e -> TvUtil.visGuideFor(rolle.pri()));
                rolleMeny2.add(rolleItem);
            }
        }

        return guide;
    }

    public static String hentRolleGuide(int id) {
        return "Rolleveiledning!";
    }
}
