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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

public class Spill implements ActionListener {

    public static final int DISKUSJONSFASE = 0, AVSTEMNINGSFASE = 1,
            TALEFASE = 2, GODKJENNINGSFASE = 3, TIEBREAKERFASE = 4, RØMNINGSFASE = 99;
    public Vindu vindu;
    TV tv;
    JPanel innhold;
    Countdown timer;

    Spillerliste spillere;
    ArrayList<Spiller> døde;
    ArrayList<Integer> faseHistorikk = new ArrayList<>();
    LinkedList<Rolle> roller = new LinkedList<>();
    ListIterator<Rolle> i;

    Spiller sisteDød, forsvarende;
    Rolle aktiv;
    String annonse;
    Knapp halshugg, snipe;
    int fase, døgn, antallDøde, tid, taler;
    boolean dag, seier, rakett;
    boolean sniper = false, flukt = false, sabotage = false,
            forfalskning = false;

    public Spill(Vindu v, Rolle[] r, int t) {
        vindu = v;
        spillere = v.spillere;
        innhold = v.innhold;
        tv = v.tv;
        tid = t;
        timer = new Countdown(vindu.getKlokke(), this);

        vindu.setSpill(this);

        for (Rolle rolle : r)
            if (rolle != null) {
                roller.add(rolle);
                rolle.setTV(tv);
                if (sjekkRolle(Rolle.BØDDEL))
                    halshugg = new Knapp("Halshugg!", Knapp.HALV, new Special());
            }
        rapporter("Rapport:");

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
        tv.nyttVedlegg();
        dag = false;
        taler = 0;
        spillere.sov();
        spillere.nullstillAvstemming();
        refresh();
        vindu.kontroll(new Kontroll(), -1);
        i = roller.listIterator();

        vindu.setVeiledning("Natt:\n" +
                "Vekk rollene etter tur, og klikk på navnet til personen de peker på.");

        if (sjekkVinner()) {
            nesteRolle();
            døgn++;
            spillere.nyPekeHistorikk();
            spillere.nyStemmeHistorikk();
        }
    }

    public void dag() {
        dag = true;
        nyFase(DISKUSJONSFASE);
        rapporter("\nNY DAG");

        if (sjekkRolle(Rolle.YOUTUBER) && finnSpiller(Rolle.YOUTUBER).funker())
            tv.leggVed(spillere.youtube(finnSpiller(Rolle.YOUTUBER).offer(),
                    finnSpiller(Rolle.YOUTUBER).skjult()));
        if (sjekkRolle(Rolle.CUPID))
            spillere.cupider((Cupid) finnRolle(Rolle.CUPID));
        if (sjekkRolle(Rolle.SNYLTER))
            spillere.snylt(finnSpiller(Rolle.SNYLTER));
        if (sjekkRolle(Rolle.BODYGUARD))
            spillere.bodyguarded(finnSpiller(Rolle.BODYGUARD));
        if (sjekkRolle(Rolle.QUISLING))
            spillere.svik((Quisling) finnRolle(Rolle.QUISLING));
        if (sjekkOffer(Rolle.MARIUS) && finnRolle(Rolle.MARIUS).offer().lever())
            spillere.nominer(finnRolle(Rolle.MARIUS).offer());

        forsvarende = null;
        spillere.våknOpp();
        refresh();
        tittuler("Hvem er de mistenkte?");
        dødsannonse();
        sjekkVinner();
        timer.nyStartMin(tid);
        dagsRoller();
        tv.toFront();
        visMistenkte();
    }

    public void godkjenn(final Spiller valgt) {
        nyFase(GODKJENNINGSFASE);
        timer.stop();
        vindu.kontroll.setVisible(false);
        innhold = vindu.innhold();

        final String info = tv.getTvText();
        final String tittel = vindu.overskrift.getText();

        if (valgt == null)
            proklamer("Ingen henrettet!");
        else {
            proklamer(valgt + " henrettes!");
            rapporter(valgt + " henrettes!");
        }

        innhold.add(new Knapp("Avbryt", Knapp.HEL, e -> {
            if (skalHaTimer()) {
                nyFase(DISKUSJONSFASE);
                restartMedTimer(null, 2);
            } else {
                if (forrigefase() == TIEBREAKERFASE)
                    uavgjort(spillere.hentUtstemte());
                else {
                    restart(tittel, info);
                    nyFase(forrigefase());
                }
            }
        }));
        innhold.add(new Knapp("Godkjenn", Knapp.HEL, e ->
                henrett(valgt)
        ));
    }

    public void dagensResultat() {
        innhold = vindu.innhold();
        vindu.kontroll.setVisible(false);

        if (sjekkRolle(Rolle.OBDUK))
            finnRolle(Rolle.OBDUK).aktiver(spillere.lik().size() > 2);
        if (sjekkRolle(Rolle.HMS))
            gjenoppliv();
        if (seier) {
            innhold.add(new Knapp("Nytt Spill", Knapp.SUPER, new Special()));
            tittuler("Vi har en vinner!");
        } else if (rakett) {
            innhold.add(new Knapp("Fortsett", Knapp.SUPER, new Special()));
        } else
            innhold.add(new Knapp("Landsbyen sovner", Knapp.SUPER,
                    new Special()));

        innhold.revalidate();
        innhold.repaint();
    }

    public void refresh(Rolle r) {
        timer.stop();
        vindu.oppdaterKnapper(innhold, this, r);

        if (r instanceof Mafia) {
            JPanel p = new JPanel();
            p.setPreferredSize(Vindu.kontrollSize);
            Special mk = new Special();
            if (spillere.mafiaRolleLever(Mafia.SNIPER) && sniper)
                p.add(new Knapp("Snipe", Knapp.HALV, mk));
            if (spillere.mafiaRolleLever(Mafia.FLUKT) && flukt)
                p.add(new Knapp("Flykt", Knapp.HALV, mk));
            if (spillere.mafiaRolleLever(Mafia.SABOTØR) && sabotage)
                p.add(new Knapp("Saboter", Knapp.HALV, mk));
            if (spillere.mafiaRolleLever(Mafia.FORFALSKER) && forfalskning)
                p.add(new Knapp("Forfalsk", Knapp.HALV, mk));
            innhold.add(p);
            tittuler(r.oppgave());
        } else if (r instanceof BodyGuard) {
            JPanel p = new JPanel();
            p.setPreferredSize(Vindu.kontrollSize);
            Special mk = new Special();
            p.add(new Knapp("Drep/Beskytt", Knapp.HALV, mk));
            innhold.add(p);
            tittuler(r.oppgave());
        }
    }

    public void refresh() {
        innhold = vindu.innhold();
        vindu.personknapper(innhold, this);
        vindu.kontroll(new Kontroll(), -1);
        vindu.oppdaterRamme(innhold);

        if (sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever())
            innhold.add(halshugg);
    }

    public void refreshAvstemning() {
        innhold = vindu.innhold();
        vindu.stemmeKnapper(innhold, this);
        vindu.kontroll(new Kontroll(), -1);
        vindu.oppdaterRamme(innhold);

        if (sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever())
            innhold.add(halshugg);
    }

    public void refresh(Spiller s, Boolean alene) {
        innhold = vindu.innhold();
        vindu.personligknapper(innhold, this, s, alene);
        vindu.kontroll(new Kontroll(), -1);
        vindu.oppdaterRamme(innhold);

        if (sjekkRolle(Rolle.BØDDEL) && dag && finnRolle(Rolle.BØDDEL).lever())
            innhold.add(halshugg);
    }

    public void pek(Rolle r) {
        aktiv = r;
        refresh(r);
        tittuler(r.oppgave());
        if (r.skjerm() || r.informert())
            vindu.overskrift.setForeground(Color.BLUE);
        if (!r.funker() && !r.spiller().klonet())
            vindu.overskrift.setForeground(Color.RED);

        // tv.leggtil(spillere.valg(r));
    }

    public void nesteRolle() {
        if (i.hasNext()) {
            Rolle r = i.next();
            if (r.spiller().klonet()) {
                pek(r);
                return;
            }

            while (!r.aktiv() || r == aktiv) {
                if (r.id(Rolle.JESUS) || r.id(Rolle.BESTEMOR))
                    r.oppgave();
                if (i.hasNext())
                    r = i.next();
                else
                    break;
            }
            if (r.aktiv()) {
                pek(r);
            } else
                dag();
        } else
            dag();
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
            tv.vis("Kan ikke angre");
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
        else
            System.out.println("UKJENT FASE: " + fase);
    }

    public void tilbakeKnapp() {
        switch (forrigefase()) {
            case DISKUSJONSFASE:
                talt(tid-2);
                break;
            case AVSTEMNINGSFASE:
                talt(tid-2);
                break;
            case TIEBREAKERFASE:
                uavgjort(spillere.hentUtstemte());
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

        rapporter("AVSTEMNING:");
        tittuler("Avstemning!");
        tv.avstemning();

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
        tittuler("Hvem stemmer på " + s.navn());
        timer.setText("Hvem stemmer på " + s.navn());
        timer.nyStartSek(20);
        if (sjekkRolle(Rolle.MARIUS)
                && s.equals(finnRolle(Rolle.MARIUS).offer()) && s.lever()) {
            Spiller marius = new Spiller("Grafiske Marius");
            spillere.stem(marius, s);
            spillere.stem(marius, s);
        }
    }

    public void avsluttAvstemning() {
        rapporter("");
        ArrayList<Spiller> utstemte = spillere.hentUtstemte();
        ArrayList<Spiller> talere = spillere.hentTalere(utstemte);

        if (talere.isEmpty() || rakett) {
            if (utstemte.isEmpty())
                godkjenn(null);
            else if (utstemte.size() > 1)
                uavgjort(utstemte);
            else {
                Spiller utstemt = utstemte.get(0);
                if (sjekkOffer(Rolle.AKTOR) && finnOffer(Rolle.AKTOR).equals(utstemt))
                    utstemt = spillere.fikkAktorDrept();
                godkjenn(utstemt);
            }
        } else
            startForsvarsTaler(talere);

        forsvarende = null;
    }


    public void startForsvarsTaler(ArrayList<Spiller> talere) {
        nyFase(TALEFASE);

        Spiller først = talere.get(0);

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
        timer.stop();
        vindu.kontroll.setVisible(false);
        innhold = vindu.innhold();

        proklamer(valgt + " skal forsvare seg...");
        innhold.add(new Knapp("Start tale", Knapp.SUPER,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
        nyFase(TALEFASE);
        s.tal();
        tittuler(s.navn() + " forsvarer seg!");
        rapporter(s.navn() + " forsvarer seg!");
        timer.setText(s.navn() + " forsvarer seg!");
        timer.nyStartSek(10);
        if (++taler == 3) {
            oppgjøretsTime();
            annonse += "\nIngen flere forsvarstaler!";
        }
    }

    public void avsluttForsvarsTaler() {
        forsvarende = null;
        spillere.nullstillAvstemming();
        spillere.nominerTalere();
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
        spillere.nominerTalere();
        visMistenkte();
        refresh();
        timer.nyStartMin(nyTid);
    }

    public void dagsRoller() {
        if (sjekkOffer(Rolle.AKTOR)) {
            spillere.nominer(finnOffer(Rolle.AKTOR));
            startForsvarstale(finnOffer(Rolle.AKTOR));
            informer(annonse);
        }

        if (sjekkOffer(Rolle.ASTRONAUT))
            rakettoppskytning();

        if (sjekkOffer(Rolle.BOMBER)) {
            timer.nyStartMin(2);
        }
    }

    public void setVeiledning(int fase) {
        switch (fase) {
            case DISKUSJONSFASE:
                vindu.setVeiledning("Diskusjonsfasen:\n" +
                        "Spillerne skal nå diskutere, og finne ut hvem som er mistenkt for å være mafia.\n" +
                        "For å mistenke en person, trykker du på personens navn. Personen blir da lagt til i mistenktlista og blir en del av den kommende avstemningen.\n" +
                        "For å fjerne en person fra mistenktlista, trykker du på personens navn igjen.\n" +
                        "Personer med navn i blått, er mistenkte, mens personer i rødt er både mistenkt, og vil dø om de får flest stemmer (som oftest fordi de har holdt forsvarstale).");
                break;
            case AVSTEMNINGSFASE:
                vindu.setVeiledning("Avstemning:\n" +
                        "Spillerne skal nå stemme på personen de tror er mafia.\n" +
                        "For å registrere en stemme, trykker du på navnet til den som stemmer når den mistenktes navn vises." +
                        "Hver person kan stemme én gang.\n" +
                        "Alle navn i mistenktlista vil vises for avstemning i 20 sekunder. " +
                        "Hvert navn vises kun én gang, og de som vil stemme på vedkommende må rekke opp hånda innen disse 20 sekundene.");
                break;
            case GODKJENNINGSFASE:
                vindu.setVeiledning("Godkjenning:\n" +
                        "Spillerne har nå stemt ut en mistenkt, og vedkommende er i ferd med å henrettes.\n" +
                        "Ved å trykke godkjenn, henrettes den utsemte, og det avsløres om vedkommende var på mafiaens eller borgernes side.\n" +
                        "Om noe er gått galt, og du vil avbryte henrettelsen, kan du trykke avbryt for å bli tatt tilbake til diskusjonsfasen.");
                break;
            case TALEFASE:
                vindu.setVeiledning("Forsvarstale:\n" +
                        "Det er nå klart for forsvarstale. " +
                        "Den forsvarende spilleren får ett minutt til å forsvare seg, hvor ingen andre spillere får si noe.\n" +
                        "Når tiden går ut, starter en ny diskusjonsfase, hvor spillerne kan respondere på talen, og eventuelt finne nye mistenkte.\n" +
                        "For å avslutte forsvarstalen tidlig, og gå tilbake til diskusjonsfasen, trykk fortsett.");
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

    public Rolle finnRolle(int id) {
        return spillere.finnRolle(id);
    }

    public Spiller finnSpiller(int id) {
        return spillere.finnSpillerSomEr(id);
    }

    public Spiller finnOffer(int id) {
        return spillere.finnRolle(id).offer();
    }

    public Boolean sjekkRolle(int id) {
        return finnRolle(id) != null;
    }

    public Boolean sjekkOffer(int id) {
        return sjekkRolle(id) && finnOffer(id) != null;
    }

    public boolean fase(int testFase) {
        return fase == testFase;
    }

    public int forrigefase() {
        System.out.println(faseHistorikk.get(faseHistorikk.size() - 1));
        return faseHistorikk.get(faseHistorikk.size() - 1);
    }

    public int nyFase(int nyFase) {
        int temp = fase;
        faseHistorikk.add(temp);
        fase = nyFase;
        System.out.println("Fra " + temp + " til " + nyFase);
        if (fase(DISKUSJONSFASE))
            visMistenkte();
        setVeiledning(fase);
        return temp;
    }

    public void oppgjøretsTime() {
        for (Component c : innhold.getComponents())
            if (((Knapp) c).spiller().talt())
                c.setEnabled(false);
    }

    public void tieBreaker(ArrayList<Spiller> utstemte) {
        for (Component c : innhold.getComponents())
            if (!utstemte.contains(((Knapp) c).spiller()))
                c.setEnabled(false);
            else
                c.setEnabled(true);
    }

    public boolean skalHaTimer() {
        return !(aktiv.id(Rolle.BØDDEL) || aktiv.id(Rolle.TROMPET)
                || aktiv.id(Rolle.BOMBER) || rakett || forrigefase() == TIEBREAKERFASE);
    }

    public boolean aktivTimer() {
        return timer.getAktiv();
    }

    public void sprengTrompet(Spiller s) {
        refresh();
        aktiv = s.rolle();
        tittuler("Hvem vil Trompeten sprenge?");
    }

    public void nominer(Spiller s, boolean leggTil) {
        if (leggTil)
            spillere.nominer(s);
        else
            spillere.avnominer(s);
        visMistenkte();
    }

    public void rakettoppskytning() {
        rakett = true;
        timer.stop();

        for (Spiller s : spillere.spillere())
            if (s.lever())
                spillere.nominer(s);

        startAvstemning();
        informer(annonse + "\nDet er tid for rakettoppskytning!!!");
        rapporter("\nDet er tid for rakettoppskytning!!!");
        tittuler("Hvem skal sendes opp i raketten?");
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
        if (nyTid < 2) nyTid = 2;
        restartMedTimer("Hvem er de mistenkte?", nyTid);
        nyFase(DISKUSJONSFASE);
    }

    public void gjenoppliv() {
        for (Spiller s : døde)
            if (s.reddet()) {
                s.vekk();
                tv.leggtil("\n\n" + s + " er gjenopplivet!");
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

        if (s == null)
            if (sjekkOffer(Rolle.BOMBER)) {
                s = finnOffer(Rolle.BOMBER);
                bombet = s;
            } else {
                dagensResultat();
                return;
            }

        tittuler(s.navn() + " ble henrettet!");
        ut = s.toString();
        int side = s.side();
        Spiller jesus = finnSpiller(Rolle.JESUS);

        if (s.løgn())
            side = side - (2 * side);

        if (s.forsvart() && !(aktiv.id(Rolle.TROMPET) && aktiv.snill())
                && !(aktiv.id(Rolle.BØDDEL) && aktiv.snill())
                && !s.id(Rolle.BOMBER)) {
            ut += " er beskyttet, og er derfor ikke død!";
        } else {
            if ((aktiv.id(Rolle.TROMPET) || aktiv.id(Rolle.BØDDEL))
                    && aktiv.snill())
                s.snipe(null);

            if (s.skjult() && spillere.antallMafia() > 1)
                ut += " kan ha vært hva som helst.\nPapirene er rotet bort.";
            else if (side > Rolle.MAFIOSO)
                ut += " var IKKE mafia!";
            else {
                ut += " var mafia!";
                if (sjekkRolle(Rolle.VARA)) {
                    ut += "\n\nOg dermed trer VaraMafiaen inn i hans sted!";
                    Spiller vara = finnSpiller(Rolle.VARA);
                    if (vara.lever()) {
                        vara.setRolle(s.rolle());
                        ((Mafia) vara.rolle()).fler();
                    }
                }
            }

            if (sjekkRolle(Rolle.BELIEBER)
                    && finnRolle(Rolle.BELIEBER).funker())
                ((Belieber) finnRolle(Rolle.BELIEBER)).beliebe();
        }

        if (sjekkOffer(Rolle.BOMBER) && !aktiv.id(Rolle.TROMPET)) {
            ut = detonerBombe(bombet, s, ut);
        }

        if (!(s.id(Rolle.BOMBER) && s.forsvart()))
            s.henrett();

        spillere.dødsannonse();

        if (jesus != null
                && (((Jesus) jesus.rolle()).frelst() == s || (bombet != null
                && s != finnSpiller(Rolle.BOMBER) && ((Jesus) jesus
                .rolle()).frelst() == bombet)))
            jesus.snipe(jesus.rolle());

        if (sjekkRolle(Rolle.ILLUSJONIST)
                && finnRolle(Rolle.ILLUSJONIST).offer() == s)
            finnRolle(Rolle.ILLUSJONIST).spiller().snipe(
                    finnRolle(Rolle.ILLUSJONIST));

        if (s.id(Rolle.PRINCESS) && spillere.harFanger()) {
            spillere.befriFanger();
            ut += "\n\nFangene er befridd!";
        }

        informer(ut);
        rapporter("Landsbyen har drept " + s + "(" + s.rolle() + ")");
        rapporter(ut);

        if (s.id(Rolle.TROMPET))
            sprengTrompet(s);
        else
            dagensResultat();

        tv.toFront();
    }

    public String detonerBombe(Spiller bombet, Spiller utstemt, String ut) {
        bombet = finnOffer(Rolle.BOMBER);

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
                            + " var IKKE Bomberen, men var TROMPET!!!\nHvem vil trompeten sprenge?";
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

    public void rømning() {
        nyFase(RØMNINGSFASE);
        timer.stop();
        proklamer("Hvem vil rømme??");
        rapporter("Hvem vil rømme??");
    }

    // //////////////////////////// TV-SKERM //////////////////////////////////

    public void informer(String tekst) {
        tv.vis(tekst);
    }

    public void proklamer(String tekst) {
        vindu.overskrift.setText(tekst);
        vindu.overskrift.setForeground(Color.BLACK);
        tv.vis(tekst);
    }

    public void tittuler(String tekst) {
        vindu.overskrift.setText(tekst);
        vindu.overskrift.setForeground(Color.BLACK);
    }

    public void rapporter(String tekst) {
        if (vindu.info.getText().length() > 1)
            vindu.info.append("\n");
        vindu.info.append(tekst);
    }

    public void dødsannonse() {
        annonse = "Ingen døde i natt!\n";

        døde = spillere.dødsannonse();
        if (døde.size() > 0) {
            annonse = "";
            for (Spiller s : døde)
                annonse += s + " er død!\n";
        }

        annonse += tv.vedlegg();

        if (sjekkRolle(Rolle.JESUS)
                && ((Jesus) finnRolle(Rolle.JESUS)).oppstanden()) {
            finnSpiller(Rolle.JESUS).vekk();
            refresh();
        }

        rapporter(annonse);
        timer.setText(annonse);
    }

    public void visMistenkte() {
        String mistenkte = "\nMistenkte";
        if (spillere.nominerte().size() == 5)
            mistenkte += "(Max 5):";
        else
            mistenkte += ":";

        for (Spiller s : spillere.nominerte())
            mistenkte += "\n" + s;

        timer.setText(annonse + mistenkte);
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
                    } else {
                        tidenErUte();
                    }
                else {
                    if (aktiv.id(Rolle.HEISENBERG))
                        rapporter(((Heisenberg) aktiv).getRapport());
                    if (aktiv.offer() == null && aktiv.funker())
                        rapporter(aktiv.rapport());
                    nesteRolle();
                }
            }
        }
    }

    private class Special implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // HALSHUGG
            if (knapp(e, "Halshugg!")) {
                timer.stop();
                aktiv = finnRolle(Rolle.BØDDEL);
                godkjenn(finnSpiller(Rolle.BØDDEL));
                refresh();
                proklamer("Hvem vil bøddelen halshugge?");
                rapporter("Hvem vil bøddelen halshugge?");

                if (aktiv.spiller().forsvart()
                        && aktiv.spiller().forsvarer().id(Rolle.JESUS)) {
                    tv.leggtil("\n\nJesus har ofret seg for bøddelen, "
                            + aktiv.spiller() + ", og "
                            + finnSpiller(Rolle.JESUS)
                            + " er derfor død i hans sted!");
                    aktiv.spiller().forsvarer().funk(false);
                }

                return;
            }

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

            // LANDSBYEN SOVNER
            else {
                if (seier) {
                    int svar = JOptionPane.showConfirmDialog(vindu,
                            "Er du sikker på at du vil starte nytt spill?",
                            "Sikker?", JOptionPane.YES_NO_OPTION);

                    if (svar == JOptionPane.YES_OPTION) {
                        timer.stop();
                        spillere.restart();
                        vindu.restart();
                        vindu.startopp();
                    }
                } else if (rakett) {
                    restartMedTimer(null, tid - 2);
                    rakett = false;
                    nyFase(DISKUSJONSFASE);
                } else
                    natt();
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Spiller valgt = knapp(e).spiller();

        // VELGER PÅ DAGEN
        if (dag) {
            if (fase(DISKUSJONSFASE)) {
                if (aktiv.id(Rolle.BØDDEL) || aktiv.id(Rolle.TROMPET)
                        || sjekkOffer(Rolle.BOMBER))
                    godkjenn(valgt);
                else {
                    if (knapp(e).getForeground().equals(Color.RED))
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
                if (rakett && valgt.equals(finnSpiller(Rolle.ASTRONAUT)))
                    spillere.stem(valgt, forsvarende);
            } else if (dag && fase(RØMNINGSFASE)) {
                valgt.henrett();
                nyFase(DISKUSJONSFASE);
                spillere.dødsannonse();
                tittuler("Hvem er de mistenkte?");
                rapporter(valgt + " har rømt fra landsbyen");
                refresh();
                timer.fortsett();
            } else if (fase(TIEBREAKERFASE))
                godkjenn(valgt);
            else {
                System.out.println("UKJENT FASE: " + fase);
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
                refresh(aktiv.id(Rolle.MAFIA) || aktiv.id(Rolle.COPYCAT)
                        || aktiv.id(Rolle.KIRSTEN) || aktiv.id(Rolle.CUPID) ? aktiv
                        : null);
            }
        }
    }
}
