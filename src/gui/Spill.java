package gui;

import datastruktur.Countdown;
import datastruktur.Spillerliste;
import personer.Rolle;
import personer.Spiller;
import personer.roller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.ListIterator;

public class Spill implements ActionListener {

    public static final int DISKUSJONSFASE = 0, AVSTEMNINGSFASE = 1,
            TALEFASE = 2, GODKJENNINGSFASE = 3, TIEBREAKERFASE = 4, ORDFØRERFASE = 5, RØMNINGSFASE = 99;
    public Vindu vindu;
    JPanel innhold;
    Countdown timer;

    public static Spillerliste spillere;
    ArrayList<Spiller> døde;
    ArrayList<Integer> faseHistorikk = new ArrayList<>();
    LinkedList<Rolle> roller = new LinkedList<>();
    ListIterator<Rolle> i;

    Spiller sisteDød, forsvarende, ordfører;
    Rolle aktiv;
    String annonse;
    int fase, døgn, antallDøde, tid, taler;
    boolean dag, seier, rakett, tiltale, bombe;
    boolean sniper = false, flukt = false, sabotage = false,
            forfalskning = false;

    public Spill(Vindu v, Rolle[] r, int t) {
        vindu = v;
        spillere = v.spillere;
        innhold = v.innhold;
        tid = t;
        timer = new Countdown(vindu.getKlokke(), this);

        vindu.setSpill(this);

        for (Rolle rolle : r)
            if (rolle != null) {
                roller.add(rolle);
            }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd-hh:mm");

        rapporter("LOGG (" + format.format(cal.getTime()) + ")\n");
        rapporter(spillere.hvemErHva());

        if (spillere.mafiaRolleLever(Mafia.FLUKT))
            flukt = true;
        if (spillere.mafiaRolleLever(Mafia.SNIPER))
            sniper = true;
        if (spillere.mafiaRolleLever(Mafia.SABOTØR))
            sabotage = true;
        if (spillere.mafiaRolleLever(Mafia.FORFALSKER))
            forfalskning = true;
    }

    public void setTid(int t) {
        tid = t;
    }

    public void natt() {
        rapporter("\nNY NATT");
        TvUtil.rensVedlegg();
        dag = false;
        taler = 0;
        spillere.sov();
        spillere.nullstillAvstemming();
        refresh();
        i = roller.listIterator();
        bombe = tiltale = rakett = false;

        if (sjekkVinner()) {
            nesteRolle();
            døgn++;
            spillere.nyPekeHistorikk();
        }
    }

    public void avsluttNatt() {
        innhold = vindu.innhold();
        vindu.kontroll.setVisible(false);
        proklamer("Landsbyen Våkner");
        innhold.add(new Knapp("Landsbyen våkner", Knapp.SUPER, e -> dag()));
        innhold.revalidate();
        innhold.repaint();
    }

    public void dag() {
        dag = true;
        aktiv = null;
        rapporter("\nNY DAG");
        nyFase(DISKUSJONSFASE);

        if (sjekkOffer(Rolle.YOUTUBER) && finnSpiller(Rolle.YOUTUBER).funker())
            TvUtil.leggVed(spillere.youtube(finnOffer(Rolle.YOUTUBER),
                    finnSpiller(Rolle.YOUTUBER).skjult()));
        if (sjekkRolle(Rolle.RAVN) || sjekkRolle(Rolle.MARIUS))
            TvUtil.leggVed(spillere.leggVedInfo());
        if (sjekkOffer(Rolle.CUPID))
            spillere.cupider((Cupid) finnRolle(Rolle.CUPID));
        if (sjekkOffer(Rolle.SNYLTER))
            spillere.snylt(finnSpiller(Rolle.SNYLTER));
        if (sjekkOffer(Rolle.BODYGUARD))
            spillere.bodyguarded(finnSpiller(Rolle.BODYGUARD));
        if (sjekkRolle(Rolle.QUISLING))
            spillere.svik((Quisling) finnRolle(Rolle.QUISLING));

        forsvarende = null;
        spillere.våknOpp();
        spillere.nyStemmeHistorikk();
        spillere.nominerTalereOgFlyers();
        refresh();
        tittuler("Hvem er de mistenkte?");
        dødsannonse();
        sjekkVinner();
        timer.nyStartMin(tid);
        sjekkDagsRoller();
        TvUtil.toFront();
        if (ordfører == null)
            ordførerValg();
        else {
            aktiverDagsRoller();
            TvUtil.visOrdfører(ordfører);
        }
    }

    public void godkjenn(final Spiller valgt) {
        nyFase(GODKJENNINGSFASE);
        timer.stop();
        vindu.kontroll.setVisible(false);
        innhold = vindu.innhold();

        final String info = TvUtil.getText();
        final String tittel = vindu.overskrift.getText();

        rapporter("");

        if (valgt == null) {
            proklamer("Ingen henrettet!");
            rapporter("Ingen henrettet!");
        } else if (rakett) {
            proklamer(valgt + " sendes opp i verdensrommet!");
            rapporter(valgt + "(" + valgt.rolle() + ")" + " sendes opp i verdensrommet!");
        } else {
            proklamer(valgt + " henrettes" + (valgt.kløna() ? " av kløna!" : "!"));
            rapporter(valgt + "(" + valgt.rolle() + ")" + " henrettes" + (valgt.kløna() ? " av kløna!" : "!"));
        }

        innhold.add(new Knapp("Avbryt", Knapp.HEL, e -> {
            if (skalHaTimer()) {
                nyFase(DISKUSJONSFASE);
                restartMedTimer(null, 2);
            } else {
                if (forrigefase() == TIEBREAKERFASE)
                    uavgjort(spillere.hentUtstemte(ordfører));
                else {
                    restart(tittel, info);
                    nyFase(forrigefase());
                }
            }
        }));
        innhold.add(new Knapp("Godkjenn", Knapp.HEL, e -> henrett(valgt)
        ));
    }

    public void ordførerValg() {
        timer.stop();
        nyFase(ORDFØRERFASE);
        refresh();
        tittuler("Hvem skal være ordfører?");
        informer(annonse.substring(1) + "\n\nDet er valgdag!\nHvem skal være ordfører?");
    }

    public void velgOrdfører(Spiller valgt) {
        if (valgt != null) {
            ordfører = valgt;
            rapporter(valgt + " er ny ordfører!");
            TvUtil.visOrdfører(ordfører);
        } else {
            ordfører = new Spiller("Ingen");
            ordfører.setLiv(false);
            rapporter("Ingen ordfører valgt");
        }
        nyFase(DISKUSJONSFASE);
        tittuler("Hvem er de mistenkte?");
        timer.fortsett();
        aktiverDagsRoller();
    }

    public void dagensResultat() {
        innhold = vindu.innhold();
        vindu.kontroll.setVisible(false);

        if (sjekkRolle(Rolle.OBDUK))
            finnRolle(Rolle.OBDUK).aktiver(spillere.lik().size() > 2);
        if (sjekkRolle(Rolle.HMS))
            gjenoppliv();

        if (seier) {
            innhold.add(new Knapp("Nytt Spill", Knapp.SUPER, e -> nyttSpill()));
        } else if (rakett) {
            innhold.add(new Knapp("Fortsett", Knapp.SUPER, e -> avsluttRakett()));
        } else
            innhold.add(new Knapp("Landsbyen sovner", Knapp.SUPER, e -> natt()));

        innhold.revalidate();
        innhold.repaint();
    }

    public void refresh() {
        innhold = vindu.innhold();
        vindu.personknapper(innhold, this);
        vindu.kontroll(new Kontroll(), -1);
        vindu.oppdaterRamme(innhold);
        setTittelFarge(null);

        if (sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever()) {
            vindu.kontroll(new Kontroll(), fase, new Knapp("Halshugg!", Knapp.HALV, e -> halshugging()));
        }
    }

    public void refresh(Rolle r) {
        vindu.oppdaterKnapper(innhold, r);
        if (r == null) return;
        timer.stop();
        vindu.kontroll(new Kontroll(), -1);
        setTittelFarge(r);
        tittuler(r.oppgave());

        if (r instanceof Mafia) {
            visMafiaKnapper();
        } else if (r instanceof BodyGuard)
            vindu.kontroll(new Kontroll(), fase, new Knapp("Drep/Beskytt", Knapp.HALV, new Mafiaknapper()));
    }

    public void setTittelFarge(Rolle r) {
        if (r == null) {
            vindu.overskrift.setForeground(Color.BLACK);
            return;
        }

        if (!r.funker() && !r.nyligKlonet())
            vindu.overskrift.setForeground(Color.RED);
        else if (r.skjerm() || r.informert() || r.nyligKlonet())
            vindu.overskrift.setForeground(Color.BLUE);
        else
            vindu.overskrift.setForeground(Color.BLACK);
    }

    public void visMafiaKnapper() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(600, 60));

        Mafiaknapper mk = new Mafiaknapper();

        if (spillere.mafiaRolleLever(Mafia.SNIPER) && sniper)
            p.add(new Knapp("Snipe", Knapp.HALV, mk));
        if (spillere.mafiaRolleLever(Mafia.FLUKT) && flukt)
            p.add(new Knapp("Flykt", Knapp.HALV, mk));
        if (spillere.mafiaRolleLever(Mafia.SABOTØR) && sabotage)
            p.add(new Knapp("Saboter", Knapp.HALV, mk));
        if (spillere.mafiaRolleLever(Mafia.FORFALSKER) && forfalskning)
            p.add(new Knapp("Forfalsk", Knapp.HALV, mk));

        if (p.getComponents().length > 0)
            innhold.add(p);
    }

    public void refreshAvstemning() {
        innhold = vindu.innhold();
        vindu.stemmeKnapper(innhold, this);
        vindu.kontroll(new Kontroll(), -1);
        vindu.oppdaterRamme(innhold);
    }

    public void refresh(Spiller s, Boolean alene) {
        innhold = vindu.innhold();
        vindu.personligknapper(innhold, this, s, alene);
        vindu.kontroll(new Kontroll(), -1);
        vindu.oppdaterRamme(innhold);

        if (sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever())
            innhold.add(new Knapp("Halshugg!", Knapp.HALV, e -> halshugging()));
    }

    public void pek(Rolle r) {
        aktiv = r;
        if (aktiv != null)
            vindu.setVeiledning(aktiv.getVeiledning());
        refresh(r);

        // TvUtil.leggTil(spillere.valg(r));
    }

    public void nesteRolle() {
        if (i.hasNext()) {
            Rolle r = i.next();
            r.autoEvne();

            while (!r.aktiv() || r == aktiv) {
                if (i.hasNext())
                    r = i.next();
                else
                    break;
            }
            if (r.aktiv()) {
                pek(r);
            } else
                avsluttNatt();
        } else
            avsluttNatt();
    }

    public void forrigeRolle() {
        if (i.hasPrevious()) {
            rapporter("Tilbake");

            if (dag) {
                dag = false;
                pek(aktiv);
            } else {
                Rolle r = i.previous();
                while (!r.aktiv() || r == aktiv) {
                    if (i.hasPrevious())
                        r = i.previous();
                    else
                        break;
                }

                if (r.aktiv()) {
                    r.rens();
                    pek(r);
                }
            }
        } else {
            TvUtil.vis("Kan ikke angre");
        }
    }

    public void tidenErUte() {
        if (fase(TALEFASE)) {
            if (sjekkOffer(Rolle.AKTOR))
                startAvstemning();
            else
                nesteForsvarsTale();
        } else if (fase(DISKUSJONSFASE))
            if (spillere.nominerte().isEmpty())
                godkjenn(null);
            else
                startAvstemning();
        else if (fase(TIEBREAKERFASE))
            godkjenn(null);
        else if (fase(AVSTEMNINGSFASE))
            nesteAvstemming();
        else
            System.out.println("UKJENT FASE: " + fase + "(tidenErUte)");
    }

    public void tilbakeKnapp() {
        switch (forrigefase()) {
            case DISKUSJONSFASE:
                talt(tid - 2);
                break;
            case AVSTEMNINGSFASE:
                talt(tid - 2);
                break;
            case TIEBREAKERFASE:
                uavgjort(spillere.hentUtstemte(ordfører));
                break;
            default:
                forrigeRolle();
        }

    }

    public void startAvstemning() {
        nyFase(AVSTEMNINGSFASE);
        timer.stop();
        vindu.kontroll.setVisible(false);
        innhold = vindu.innhold();

        rapporter("\nAVSTEMNING:");
        tittuler("Avstemning!");
        TvUtil.avstemning();

        innhold.add(new Knapp("Start avstemning", Knapp.SUPER,
                e -> {
                    refreshAvstemning();
                    nesteAvstemming();
                    vindu.kontroll.setVisible(true);
                }));
    }

    public void nesteAvstemming() {
        if (forsvarende != null)
            rapporter(forsvarende.navn() + ": "
                    + spillere.antallStemmer(forsvarende));

        forsvarende = spillere.nesteNominerte(forsvarende);

        if (!forsvarende.navn().isEmpty())
            avstemming(forsvarende);
        else {
            avsluttAvstemning();
        }
    }

    public void avstemming(Spiller s) {
        tittuler("Hvem stemmer på " + s.navn() + "?");
        timer.setText("\nHvem stemmer på " + s.navn() + "?\n" + hentMistenkte());
        timer.nyStartSek(20);
        if (s.lever() && s.harFlyers()) {
            Spiller marius = new Spiller("Grafiske Marius");
            spillere.stem(marius, s);
            spillere.stem(marius, s);
        }
    }

    public void avsluttAvstemning() {
        ArrayList<Spiller> utstemte = spillere.hentUtstemte();
        ArrayList<Spiller> talere = spillere.hentTalere(utstemte);

        if (talere.isEmpty() || rakett) {
            //Avgjørende avstemning - Sjekk ordførerstemmer
            utstemte = spillere.hentUtstemte(ordfører);

            if (utstemte.isEmpty())
                godkjenn(null);
            else if (utstemte.size() > 1)
                uavgjort(utstemte);
            else {
                Spiller utstemt = utstemte.get(0);
                if (tiltale && !rakett)
                    utstemt = spillere.fikkAktorDrept();
                godkjenn(utstemt);
            }
        } else
            startForsvarsTaler(talere);

        forsvarende = null;
    }

    public void startForsvarsTaler(ArrayList<Spiller> talere) {
        Spiller først = talere.get(0);

        rapporter("");
        startForsvarstale(først);

        if (talere.size() > 1) {
            String ut = "";
            for (Spiller s : talere) {
                if (s.equals(først))
                    ut += s;
                else if (talere.indexOf(s) < talere.size() - 1)
                    ut += ", " + s;
                else
                    ut += " og " + s;
            }
            ut += " skal forsvare seg!\nFørst ut er " + først + "...";

            informer(ut);
        }
    }

    public void startForsvarstale(Spiller valgt) {
        nyFase(TALEFASE);
        timer.stop();
        vindu.kontroll.setVisible(false);
        innhold = vindu.innhold();

        proklamer(valgt + " skal forsvare seg...");
        innhold.add(new Knapp("Start tale", Knapp.SUPER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (valgt.kløna()) {
                            godkjenn(valgt);
                            return;
                        }
                        forsvarstale(valgt);
                        knapp(e).setText("Fortsett");
                        knapp(e).removeActionListener(this);
                        knapp(e).addActionListener(new Kontroll());
                    }
                }));
    }

    public void nesteForsvarsTale() {
        forsvarende = spillere.nesteTaler(forsvarende);

        if (!forsvarende.navn().isEmpty())
            startForsvarstale(forsvarende);
        else {
            avsluttForsvarsTaler();
        }
    }

    public void forsvarstale(Spiller s) {
        s.tal();
        tittuler(s.navn() + " forsvarer seg!");
        rapporter(s.navn() + " forsvarer seg!");
        timer.setText("\n" + s.navn() + " forsvarer seg!");
        timer.nyStartMin(1);
        taler++;
    }

    public void avsluttForsvarsTaler() {
        forsvarende = null;
        spillere.nullstillAvstemming();
        spillere.nominerTalereOgFlyers();
        talt(2);
    }

    public void restart(String tittel, String info) {
        tittuler(tittel);
        informer(info);
        refresh();
    }

    public void restartMedTimer(String tittel, int nyTid) {
        tittuler(tittel == null ? "Hvem er de mistenkte?" : tittel);
        spillere.nullstillAvstemming();
        spillere.nominerTalereOgFlyers();

        refresh();

        if (taler > 2) {
            oppgjøretsTime();
            annonse += "\n\nOppgjørets Time - Ingen flere forsvarstaler!";
        }

        visMistenkte();

        timer.nyStartMin(nyTid);
    }

    public void sjekkDagsRoller() {
        if (sjekkOffer(Rolle.ASTRONAUT))
            rakett = true;
        if (sjekkOffer(Rolle.BOMBER))
            bombe = true;
        if (sjekkOffer(Rolle.AKTOR))
            tiltale = true;
    }

    public void aktiverDagsRoller() {
        if (rakett)
            rakettoppskytning();
        else if (bombe)
            plantetBombe();
        else if (tiltale)
            tiltale();
    }

    public void rakettoppskytning() {
        timer.stop();

        for (Spiller s : spillere.spillere())
            if (s.lever())
                spillere.nominer(s);

        startAvstemning();
        informer(annonse.substring(1) + "\n\nDet er tid for rakettoppskytning!!!");
        rapporter("\nDet er tid for rakettoppskytning!!!");
        tittuler("Hvem skal sendes opp i raketten?");
    }

    public void avsluttRakett() {
        rakett = false;
        nyFase(DISKUSJONSFASE);
        restartMedTimer(null, tid - 2);
        aktiverDagsRoller();
    }

    public void plantetBombe() {
        timer.setText(annonse + "\n\nBomben er plantet!!!");
        timer.nyStartMin(2);
        tittuler("Hvem vil landsbyen drepe?");

        //Utsett aktor
        if (tiltale) {
            finnRolle(Rolle.AKTOR).aktiver(true);
            tiltale = false;
        }
    }

    public void tiltale() {
        Spiller tiltalt = finnOffer(Rolle.AKTOR);

        if (!tiltalt.lever()) {
            finnRolle(Rolle.AKTOR).aktiver(true);
            tiltale = false;
            return;
        }

        spillere.nominer(tiltalt);
        startForsvarstale(tiltalt);
        informer(annonse.substring(1) + "\n\n" + tiltalt + " står på TILTALEBENKEN!");
    }

    public void setVeiledning(int fase) {
        switch (fase) {
            case ORDFØRERFASE:
                vindu.setVeiledning("Valgfase:\n" +
                        "Det er på tide å velge en ordfører, som får dobbeltstemme ved alle avstemninger!\n" +
                        "Før spillet begynner for alvor, må landsbyen bestemme seg for om de ønsker en ordfører, og hvem dette i så fall skal være.\n" +
                        "Når en ordfører er valgt, trykker du på denne personens navn for å innsette vedkommende." +
                        "Om dere ikke ønsker å ha med noen ordfører, trykker du på fortsett.");
                break;
            case DISKUSJONSFASE:
                if (sjekkOffer(Rolle.BOMBER))
                    vindu.setVeiledning("Diskusjonsfasen - Bombe:\n" +
                            "Bomberen har plantet bomben, og spillerne har nå 2 minutter til å finne ut hvem de vil henrette.\n" +
                            "For å henrette en spiller, trykker du på vedkommendes navn. Det blir ingen forsvarstaler eller organisert avstemning. Landsbyen må bare bli enige\n" +
                            "Klarer landsbyen å drepe bomberen, blir bomben desarmert, og kun bomberen dør. Henretter de noen andre, dør både denne personen OG den bomben er plantet hos.\n" +
                            "Blir ingen henrettet, dør den bomben er plantet hos, og alle som besøkte vedkommende i natt.");
                else if (taler < 3)
                    vindu.setVeiledning("Diskusjonsfasen:\n" +
                            "Spillerne skal nå diskutere, og finne ut hvem som er mistenkt for å være mafia.\n" +
                            "For å mistenke en person, trykker du på personens navn. Personen blir da lagt til i mistenktlista og blir en del av den kommende avstemningen.\n" +
                            "For å fjerne en person fra mistenktlista, trykker du på personens navn igjen.\n" +
                            "Personer med navn i blått, er mistenkte, mens personer i rødt er både mistenkt, og vil dø om de får flest stemmer (som oftest fordi de har holdt forsvarstale).");
                else
                    vindu.setVeiledning("Diskusjonsfasen - Oppgjørets Time:\n" +
                            "Spillerne har nå en siste sjanse til å diskutere, og finne ut hvem som er mafia.\n" +
                            "En dag kan maks inneholde 3 forsvarstaler (Kan bli flere ved uavgjort siste avstemning). " +
                            "I oppgjørets time er det derfor ikke mulig å mistenke andre enn de som har holdt forsvarstale, " +
                            "men alle disse kan nå stemmes på.\n");
                break;
            case AVSTEMNINGSFASE:
                if (rakett)
                    vindu.setVeiledning("Avstemning - Rakettoppskytning:\n" +
                            "Astronauten har fullført raketten sin, og landsbyen skal nå anonymt stemme over hvem som skal sendes ut i rommet (og dø).\n" +
                            "Alle spillere er nå mistenkt, og må lukke øynene før avstemningen begynner. Astronauten derimot, kan se alt.\n" +
                            "Hver person vil være oppe til avstemning i 15 sekunder, og for å registrere stemmer, trykker du på navnet til de som rekker opp hånda.\n" +
                            "Hvert navn vises kun én gang, og hver person kan stemme én gang, men Astronauten har dobbeltstemme.\n" +
                            "Etter rakettoppskytningen fortsetter dagen som normalt, men med kortere tid.");
                else if (tiltale)
                    vindu.setVeiledning("Avstemning - Tiltale:\n" +
                            "Spillerne skal nå stemme for eller imot å drepe den tiltalte.\n" +
                            "For å registrere en stemme, trykker du på navnet til den som stemmer.\n" +
                            "Avstemningen varer i 15 sekunder, og alle som stemmer for, gjør dette ved å tydelig rekke opp hånda.\n" +
                            "Den tiltalte blir henrettet hvis han får minst halvparten av stemmene, " +
                            "og ellers går vi til en ny natt uten henrettelse.");
                else
                    vindu.setVeiledning("Avstemning:\n" +
                            "Spillerne skal nå stemme på personen de tror er mafia.\n" +
                            "For å registrere en stemme, trykker du på navnet til den som stemmer når den mistenktes navn vises.\n" +
                            "Hvert navn vises kun én gang, og hver person kan stemme én gang, " +
                            "ved å tydelig rekke opp hånda når den de vil stemme på vises.\n" +
                            "Alle navn i mistenktlista vil vises for avstemning i 15 sekunder.");
                break;
            case GODKJENNINGSFASE:
                vindu.setVeiledning("Godkjenning:\n" +
                        "Spillerne har nå stemt ut en mistenkt, og vedkommende er i ferd med å henrettes.\n" +
                        "Ved å trykke godkjenn, henrettes den utsemte, og det avsløres om vedkommende var på mafiaens eller borgernes side.\n" +
                        "Om noe er gått galt, og du vil avbryte henrettelsen, kan du trykke avbryt for å bli tatt tilbake til diskusjonsfasen.");
                break;
            case TALEFASE:
                if (tiltale)
                    vindu.setVeiledning("Forsvarstale - Tiltale:\n" +
                            "Aktor har kommet med en tilale og vi går derfor rett til den tiltaltes forsvarstale.\n" +
                            "Den tiltalte får ett minutt til å forsvare seg, hvor ingen andre spillere får si noe.\n" +
                            "Når tiden går ut, går vi over i en avstemning, hvor spillerne skal stemme for eller imot å henrette den tiltalte.\n" +
                            "For å avslutte forsvarstalen tidlig og gå rett til avstemningen, trykk fortsett.");
                else
                    vindu.setVeiledning("Forsvarstale:\n" +
                            "Det er nå klart for forsvarstale.\n" +
                            "Den forsvarende spilleren får ett minutt til å forsvare seg, hvor ingen andre spillere får si noe.\n" +
                            "Når tiden går ut, starter en ny diskusjonsfase, hvor spillerne kan respondere på talen, og eventuelt finne nye mistenkte.\n" +
                            "For å avslutte forsvarstalen tidlig og gå tilbake til diskusjonsfasen, trykk fortsett.");
                break;
            case RØMNINGSFASE:
                vindu.setVeiledning("Rømningsforsøk:\n" +
                        "Du kan her fjerne en spiller fra spillet ved å la vedkommende rømme.\n" +
                        "For å la en spiller rømme, trykker du på spillerens navn nå.\n" +
                        "Spilleren blir da fjernet fra spillet, og du går tilbake til diskusjonsfasen\n" +
                        "For å gå tilbake uten å foreta en rømning, trykker du på fortsett.");
                break;
            case TIEBREAKERFASE:
                vindu.setVeiledning("Uavgjort:\n" +
                        "Ved å klikke på et navn nå, vil du umiddelbart henrette vedkommende.\n" +
                        "Ved uavgjort avstemning har de siste som døde muligheten til å komme med en avgjørende stemme." +
                        "Dette forutsetter at disse ikke har vært våken på natten, eller på annen måte fått avslørt hvem som er mafia." +
                        "De som har muligheten til å avgi sin stemme kommer opp på skjermen.\n" +
                        "Hvis ingen nylig avdøde vises på skjermen, må dette løses på annen måte. En mye brukt løsning er å ringe en opplysningstelefon.\n" +
                        "For å gå til en ny natt, uten å henrette noen, trykker du fortsett.");
                break;
            default:
                vindu.setVeiledning("Veiledning");
                break;
        }
    }

    // //////////////////////////////////// SMÅ METODER
    // ///////////////////////////////////

    public boolean sjekkRolle(int id) {
        return spillere.sjekkRolle(id);
    }

    public boolean sjekkOffer(int id) {
        return spillere.sjekkOffer(id);
    }

    public Spiller finnSpiller(int id) {
        return spillere.finnSpiller(id);
    }

    public Rolle finnRolle(int id) {
        return spillere.finnRolle(id);
    }

    public Spiller finnOffer(int id) {
        return spillere.finnOffer(id);
    }

    public boolean fase(int testFase) {
        return fase == testFase;
    }

    public boolean aktiv(int rolle) {
        return aktiv != null && aktiv.id(rolle);
    }

    public int forrigefase() {
        System.out.println(faseHistorikk.get(faseHistorikk.size() - 1));
        return faseHistorikk.get(faseHistorikk.size() - 1);
    }

    public int nyFase(int nyFase) {
        int temp = fase;
        faseHistorikk.add(temp);
        fase = nyFase;
//        System.out.println("Fra " + temp + " til " + nyFase);
        setVeiledning(fase);
        return temp;
    }

    public void oppgjøretsTime() {
        for (Component c : innhold.getComponents()) {
            Spiller s = ((Knapp) c).spiller();
            if (s != null && !s.talt())
                c.setEnabled(false);
        }
    }

    public void tieBreaker(ArrayList<Spiller> utstemte) {
        for (Component c : innhold.getComponents())
            if (!utstemte.contains(((Knapp) c).spiller()))
                c.setEnabled(false);
            else
                c.setEnabled(true);
    }

    public boolean skalHaTimer() {
        return !(aktiv(Rolle.BØDDEL) || aktiv(Rolle.TROMPET)
                || aktiv(Rolle.BOMBER) || rakett || forrigefase() == TIEBREAKERFASE);
    }

    public boolean aktivTimer() {
        return timer.getAktiv();
    }

    public void sprengTrompet(Spiller s) {
        refresh();
        aktiv = s.rolle();
        tittuler("\nHvem vil Trompeten sprenge?");
        setVeiledning(Rolle.TROMPET);
    }

    public void nominer(Spiller s, boolean leggTil) {
        if (leggTil)
            spillere.nominer(s);
        else
            spillere.avnominer(s);
        visMistenkte();
    }

    public void halshugging() {
        timer.stop();
        aktiv = finnRolle(Rolle.BØDDEL);
        refresh();
        vindu.finnKnappForRolle(innhold, Rolle.BØDDEL).setEnabled(false);
        proklamer("Hvem vil bøddelen halshugge?");
        rapporter("Hvem vil bøddelen halshugge?");
        vindu.setVeiledning(aktiv.getVeiledning());
        return;
    }

    // ////////////////////////////////// KVELDEN ///////////////////////////////////////
    public void uavgjort(ArrayList<Spiller> utstemte) {
        nyFase(TIEBREAKERFASE);
        timer.stop();

        String ut = "Det er uavgjort!\n";

        for (Spiller s : utstemte) {
            if (s.equals(utstemte.get(0)))
                ut += s;
            else if (utstemte.indexOf(s) < utstemte.size() - 1)
                ut += ", " + s;
            else
                ut += " og " + s;
        }
        ut += " har like mange stemmer.\n\nHvem skal dø?";

        if (ordfører.lever())
            spillere.hentSisteStemmeFra(ordfører);

        ArrayList<Spiller> nye = spillere.nyligDøde();
        for (Spiller s : nye) {
            if (s.equals(nye.get(0)))
                ut += s;
            else if (nye.indexOf(s) < nye.size() - 1)
                ut += ", " + s;
            else {
                ut += " og " + s;
                ut += " døde nylig, og kan gi en siste stemme.";
            }
        }

        informer(ut);
        tittuler("Uavgjort! Hvem skal dø?");
        refresh();
        tieBreaker(utstemte);
    }

    public void talt(int nyTid) {
        nyFase(DISKUSJONSFASE);

        if (nyTid < 2) nyTid = 2;
        restartMedTimer("Hvem er de mistenkte?", nyTid);
    }

    public void gjenoppliv() {
        for (Spiller s : døde)
            if (s.reddet()) {
                s.vekk();
                TvUtil.leggTil("\n\n" + s + " er gjenopplivet!");
                rapporter("\n" + s + " er gjenopplivet!");
                spillere.gjenoppliv(s);
            }
    }

    public boolean sjekkVinner() {
        if (finnRolle(Rolle.ARVING) != null)
            ((Arving) finnRolle(Rolle.ARVING)).arv();
        switch (spillere.vinner()) {
            case -1:
                informer("Mafiaen har vunnet!");
                rapporter("Mafiaen har vunnet!");
                seier = true;
                break;
            case 1:
                informer("Landsbyen har vunnet!");
                rapporter("Landsbyen har vunnet!");
                seier = true;
                break;
            case 2:
                informer("Alle er døde! Ingen vant!");
                rapporter("Alle er døde! Ingen vant!");
                seier = true;
                break;
            case 3:
                informer("Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkisten har vunnet!");
                rapporter("Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkisten har vunnet!");
                seier = true;
                break;
            case 4:
                informer("Agent Smith har tatt over hele landsbyen, og har vunnet!");
                rapporter("Agent Smith har tatt over hele landsbyen, og har vunnet!");
                seier = true;
                break;
            case 5:
                informer("Princess98 har kidnappet hele landsbyen, og har vunnet!!");
                rapporter("Princess98 har kidnappet hele landsbyen, og har vunnet!");
                seier = true;
                break;

            default:
                return true;
        }
        dagensResultat();
        return false;
    }

    public void henrett(Spiller s) {
        String ut;
        Spiller bombet = null;

        //Henrett null
        if (s == null)
            if (sjekkOffer(Rolle.BOMBER)) {
                s = finnOffer(Rolle.BOMBER);
                bombet = s;
            } else {
                dagensResultat();
                return;
            }
        else if (sjekkOffer(Rolle.BOMBER))
            bombet = finnOffer(Rolle.BOMBER);

        tittuler(s.navn() + " ble henrettet!");
        ut = s.toString();
        int side = s.side();

        //Løgneren bytter side
        if (s.løgn())
            side = side - (2 * side);

        //Lag henrettelsestekst
        if (s.forsvart()
                && !((aktiv(Rolle.TROMPET) || aktiv(Rolle.BØDDEL)) && aktiv.snill())
                && !bombe) {
            ut += " er beskyttet, og er derfor ikke død!";
        } else {
            ut = rapporterSide(s, side, ut);
            if (sjekkRolle(Rolle.BELIEBER) && finnRolle(Rolle.BELIEBER).funker())
                ((Belieber) finnRolle(Rolle.BELIEBER)).beliebe();
        }

        //Sjekk Bomberen
        if (sjekkOffer(Rolle.BOMBER) && !aktiv(Rolle.TROMPET) && !rakett) {
            ut = detonerBombe(bombet, s);
        }

        //Faktisk henrett spiller
        if (!(s.id(Rolle.BOMBER) && s.forsvart()))
            s.henrett();

        //Aktor har drept. Får han reset?
        if (tiltale && s.equals(finnOffer(Rolle.AKTOR))) {
            tiltale = false;
            finnRolle(Rolle.AKTOR).aktiver(s.side() < Rolle.NØYTRAL);
        }

        //Bøddelen har drept. Er han frelst av Jesus?
        if (aktiv(Rolle.BØDDEL))
            ut = sjekkBøddelFrelse(ut);

        //Oppdater listen over nylig døde
        spillere.dødsannonse();

        //Sjekk om spiller er reddet av Jesus, og drep jesus istedenfor
        if (finnSpiller(Rolle.JESUS) != null) {
            Jesus jesus = (Jesus) finnRolle(Rolle.JESUS);

            if (jesus.frelst() == s || (bombet != null && s != finnSpiller(Rolle.BOMBER) && jesus.frelst() == bombet))
                jesus.spiller().snipe(jesus);
        }

        //Sjekk om den døde er illusjonistens gjemmested
        if (sjekkRolle(Rolle.ILLUSJONIST)
                && finnRolle(Rolle.ILLUSJONIST).offer() == s)
            finnRolle(Rolle.ILLUSJONIST).spiller().snipe(
                    finnRolle(Rolle.ILLUSJONIST));

        //Sjekk om princess er den døde, og befri i så fall fangene
        if (s.id(Rolle.PRINCESS) && spillere.harFanger()) {
            spillere.befriFanger();
            ut += "\n\nFangene er befridd!";
        }

        //Rapporter hendelsen
        informer(ut);
        rapporter(ut);

        //Gå til resultatskjermen. Evt trompetsprengning
        if (s.id(Rolle.TROMPET) && !s.forsvart())
            sprengTrompet(s);
        else
            dagensResultat();

        TvUtil.toFront();
    }

    public void nyttSpill() {
        tittuler("Vi har en vinner!");

        int svar = JOptionPane.showConfirmDialog(vindu,
                "Er du sikker på at du vil starte nytt spill?",
                "Sikker?", JOptionPane.YES_NO_OPTION);

        if (svar == JOptionPane.YES_OPTION) {
            timer.stop();
            refresh();
            spillere.restart();
            vindu.restart();
            vindu.startopp();
        }
    }

    public String rapporterSide(Spiller s, int side, String ut) {
        if ((aktiv(Rolle.TROMPET) || aktiv(Rolle.BØDDEL)) && aktiv.snill())
            s.snipe(null);

        if (s.skjult() && spillere.antallMafia() > 1)
            ut += " kan ha vært hva som helst.\nPapirene er rotet bort.";
        else if (side > Rolle.MAFIOSO)
            if (s.id(Rolle.TROMPET))
                ut += " var IKKE mafia, men var TROMPET!!!" +
                        "Hvem vil trompeten sprenge?";
            else
                ut += " var IKKE mafia!";
        else {
            ut += " var mafia!";
            if (sjekkRolle(Rolle.VARA)) {
                ut += "\n\nOg dermed trer VaraMafiaen inn i hans sted!";
                Spiller vara = finnSpiller(Rolle.VARA);
                if (vara.lever()) {
                    vara.setRolle(finnRolle(Rolle.MAFIA));
                    ((Mafia) vara.rolle()).fler();
                }
            }
        }
        return ut;
    }

    public String detonerBombe(Spiller bombet, Spiller utstemt) {
        bombe = false;
        String ut;

        // Utstemt
        if (utstemt.forsvart()) {
            ut = utstemt + " er beskyttet, og er derfor ikke død!";
        } else {
            if (utstemt.id(Rolle.BOMBER)) {
                ut = utstemt + " VAR Bomberen, og bomben er desarmert!";
                return ut;
            } else {
                if (utstemt.side() < 0)
                    ut = utstemt + " var IKKE Bomberen, men var Mafia!";
                else if (utstemt.id(Rolle.TROMPET))
                    ut = utstemt
                            + " var IKKE Bomberen, men var TROMPET!!!" +
                            "\nHvem vil trompeten sprenge?";
                else
                    ut = utstemt + " var IKKE Bomberen, og heller IKKE Mafia!";
            }
        }

        if (utstemt.equals(bombet))
            ut = "";
        else
            ut += "\n\n";

        // Bombet
        if (bombet.forsvart() && !finnRolle(Rolle.BOMBER).snill()) {
            ut += bombet
                    + " ble forsøkt sprengt,\nmen er beskyttet, og er derfor ikke død!";
        } else {
            bombet.henrett();
            if (bombet.side() < Rolle.NØYTRAL)
                ut += bombet + " ble sprengt, og VAR Mafia!";
            else
                ut += bombet + " ble sprengt, og var IKKE Mafia!";
        }

        if (bombet == utstemt)
            for (Spiller spiller : spillere.besøk(bombet, null))
                if (spiller != finnSpiller(Rolle.BOMBER)) {
                    ut += "\n\n"
                            + spiller.navn()
                            + " døde i eksplosjonen,\nog "
                            + (spiller.side() < Rolle.NØYTRAL ? "VAR Mafia!"
                            : "var IKKE Mafia!");
                    spiller.henrett();
                }
        return ut;
    }

    public String sjekkBøddelFrelse(String ut) {
        aktiv.spiller().henrett();
        if (aktiv.spiller().forsvart()
                && aktiv.spiller().forsvarer().id(Rolle.JESUS)) {
            ut += "\n\nJesus har ofret seg for bøddelen, "
                    + aktiv.spiller() + ", og "
                    + finnSpiller(Rolle.JESUS)
                    + " er derfor død i hans sted!";
            aktiv.spiller().forsvarer().funk(false);
        }
        return ut;
    }

    public void rømning() {
        nyFase(RØMNINGSFASE);
        timer.stop();
        proklamer("Hvem vil rømme??");
        rapporter("Hvem vil rømme??");
    }

    // //////////////////////////// TV-SKERM //////////////////////////////////

    public void informer(String tekst) {
        TvUtil.vis(tekst);
    }

    public void proklamer(String tekst) {
        vindu.overskrift.setText(tekst);
        TvUtil.vis(tekst);
    }

    public void tittuler(String tekst) {
        vindu.overskrift.setText(tekst);
    }

    public void rapporter(String tekst) {
        if (vindu.info.getText().length() > 1)
            vindu.info.append("\n");
        vindu.info.append(tekst);
    }

    public void dødsannonse() {
        annonse = "\nIngen døde i natt!";

        døde = spillere.dødsannonse();
        if (døde.size() > 0) {
            annonse = "";
            for (Spiller s : døde)
                annonse += "\n" + s + " er død!";
        }

        annonse += TvUtil.getVedlegg();

        if (sjekkRolle(Rolle.JESUS)
                && ((Jesus) finnRolle(Rolle.JESUS)).oppstanden()) {
            finnSpiller(Rolle.JESUS).vekk();
            refresh();
        }

        rapporter(annonse.substring(1));

        visMistenkte();
    }

    public void visMistenkte() {
        timer.setText(annonse + hentMistenkte());
    }

    public String hentMistenkte() {
        String mistenkte = "\n\nMistenkte";
        if (spillere.nominerte().size() == 5)
            mistenkte += "(Max 5):";
        else
            mistenkte += ":";

        for (Spiller s : spillere.nominerte())
            mistenkte += "\n" + s;
        return mistenkte;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////

    // KNAPPEACTION
    public Knapp knapp(ActionEvent e) {
        return (Knapp) e.getSource();
    }

    public boolean knapp(ActionEvent e, String s) {
        return knapp(e).getText() == s;
    }

    private class Kontroll implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TRYKKER TILBAKE
            if (knapp(e, "Tilbake")) {
                if (dag)
                    tilbakeKnapp();
                forrigeRolle();
            }
            // TRYKKER FORTSETT
            else if (knapp(e, "Fortsett")) {
                if (dag)
                    if (fase(RØMNINGSFASE)) {
                        nyFase(DISKUSJONSFASE);
                        tittuler("Hvem er de mistenkte?");
                        timer.fortsett();
                    } else if (fase(TALEFASE)) {
                        tidenErUte();
                    } else if (fase(AVSTEMNINGSFASE)) {
                        nesteAvstemming();
                    } else if (fase(ORDFØRERFASE)) {
                        velgOrdfører(null);
                    } else {
                        tidenErUte();
                    }
                else {
                    if (aktiv(Rolle.HEISENBERG))
                        rapporter(((Heisenberg) aktiv).getRapport());
                    if (aktiv != null && aktiv.offer() == null && aktiv.funker())
                        rapporter(aktiv.rapport());
                    nesteRolle();
                }
            }
        }
    }

    private class Mafiaknapper implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // MAFIAKNAPPER
            if (knapp(e, "Snipe")) {
                ((Mafia) finnRolle(Rolle.MAFIA)).snipe();
                sniper = false;
                knapp(e).setEnabled(false);
                proklamer("Hvem vil sniperen skyte?");
                return;
            } else if (knapp(e, "Beskytt")) {
                spillere.flykt();
                flukt = false;
                knapp(e).setEnabled(false);
                return;
            } else if (knapp(e, "Saboter")) {
                ((Mafia) aktiv).sabotage();
                sabotage = false;
                knapp(e).setEnabled(false);
                proklamer("Hvem vil sabotøren sabotere?");
                return;
            } else if (knapp(e, "Forfalsk")) {
                spillere.forfalsk();
                forfalskning = false;
                knapp(e).setEnabled(false);
                return;
            } else if (knapp(e, "Drep/Beskytt")) {
                BodyGuard bg = ((BodyGuard) finnRolle(Rolle.BODYGUARD));
                bg.skift();
                tittuler(bg.oppgave());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Spiller valgt = knapp(e).spiller();

        // VELGER PÅ DAGEN
        if (dag) {
            if (aktiv(Rolle.BØDDEL) || aktiv(Rolle.TROMPET)) {
                godkjenn(valgt);
            } else if (fase(ORDFØRERFASE)) {
                velgOrdfører(valgt);
            } else if (fase(DISKUSJONSFASE)) {
                if (sjekkOffer(Rolle.BOMBER))
                    godkjenn(valgt);
                else {
                    if (valgt.talt() || valgt.harFlyers())
                        return;
                    if (knapp(e).getForeground().equals(Color.BLUE)) {
                        nominer(valgt, false);
                        knapp(e).setForeground(Color.BLACK);
                    } else if (spillere.nominerte().size() < 5) {
                        knapp(e).setForeground(Color.BLUE);
                        nominer(valgt, true);
                    } else {
                        timer.setText(annonse + "\n\nMistenktlisten er full");
                    }
                }
            } else if (fase(AVSTEMNINGSFASE)) {
                knapp(e).setEnabled(false);
                spillere.stem(valgt, forsvarende);

                if ((rakett && valgt.equals(finnSpiller(Rolle.ASTRONAUT))))
                    spillere.stem(valgt, forsvarende);
                if (valgt == ordfører)
                    spillere.stem(valgt, forsvarende);
            } else if (dag && fase(RØMNINGSFASE)) {
                valgt.henrett();
                nyFase(DISKUSJONSFASE);
                spillere.dødsannonse();
                tittuler("Hvem er de mistenkte?");
                rapporter(valgt + " har rømt fra landsbyen");
                refresh();
                visMistenkte();
                timer.fortsett();
            } else if (fase(TIEBREAKERFASE))
                godkjenn(valgt);
            else {
                System.out.println("UKJENT FASE: " + fase + "(valgt på dagen)");
            }
        }

        // VELGER PÅ NATTEN
        else {
            if (aktiv.funker()
                    || (aktiv instanceof Specialguy && aktiv.aktiv())) {
                spillere.lagrePek(aktiv.pri(), valgt);
                aktiv.pek(valgt);
                rapporter(aktiv.rapport());
            }
            if (aktiv.fortsetter())
                nesteRolle();
            else {
                refresh(aktiv(Rolle.MAFIA) || aktiv(Rolle.COPYCAT) || aktiv(Rolle.KIRSTEN) || aktiv(Rolle.CUPID) ? aktiv : null);
            }
        }
    }
}
