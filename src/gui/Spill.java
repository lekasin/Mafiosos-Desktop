package gui;

import Utils.SkjermUtil;
import Utils.TvUtil;
import Utils.VeiledningsUtil;
import Utils.MenyUtil;
import Utils.InnstillingsUtil;
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

    public static final int ORDFØRERFASE = 0, DISKUSJONSFASE = 1, AVSTEMNINGSFASE = 2,
            TALEFASE = 3, GODKJENNINGSFASE = 4, TIEBREAKERFASE = 5, JOKERFASE = 6, RØMNINGSFASE = 7, NATTFASE = 8;
    public Vindu vindu;
    JPanel innhold;
    public Countdown timer;

    public static Spillerliste spillere;
    ArrayList<Spiller> døde;
    ArrayList<Integer> faseHistorikk = new ArrayList<>();
    LinkedList<Rolle> roller = new LinkedList<>();
    ListIterator<Rolle> i;

    Spiller sisteDød, forsvarende, ordfører, dødsdømt;
    static Rolle aktiv;
    String annonse;
    int fase, døgn, antallDøde, tid, taler;
    boolean dag, seier, rakett, tiltale, bombe, joker;
    boolean sniper = false, flukt = false, sabotage = false, forfalskning = false;
    public static Spill instans;

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
        instans = this;
    }

    public void setTid(int t) {
        tid = t;
    }

    public void natt() {
        rapporter("\nNY NATT");
        TvUtil.rensVedlegg();
        dag = false;
        nyFase(NATTFASE);
        taler = 0;
        spillere.sov();
        spillere.nullstillAvstemming();
        refresh();
        i = roller.listIterator();
        bombe = tiltale = rakett = false;
        dødsdømt = null;
        annonse = "";

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
        SkjermUtil.fargTittel(Color.black);
        innhold.add(new Knapp("Landsbyen våkner", Knapp.SUPER, e -> dag()));
        innhold.revalidate();
        innhold.repaint();
        TvUtil.lukkGuide();
    }

    public void dag() {
        dag = true;
        aktiv = null;
        rapporter("\nNY DAG");
        nyFase(DISKUSJONSFASE);

        if (sjekkOffer(Rolle.YOUTUBER) && finnSpiller(Rolle.YOUTUBER).funker())
            TvUtil.leggVed(spillere.youtube(finnOffer(Rolle.YOUTUBER),
                    finnSpiller(Rolle.YOUTUBER).skjult()));
        if (sjekkOffer(Rolle.CUPID))
            spillere.cupider((Cupid) finnRolle(Rolle.CUPID));
        if (sjekkOffer(Rolle.SNYLTER))
            spillere.snylt(finnSpiller(Rolle.SNYLTER));
        if (sjekkOffer(Rolle.BODYGUARD))
            spillere.bodyguarded(finnSpiller(Rolle.BODYGUARD));
        if (sjekkRolle(Rolle.QUISLING))
            spillere.svik((Quisling) finnRolle(Rolle.QUISLING));
        if (sjekkRolle(Rolle.RAVN) || sjekkRolle(Rolle.MARIUS))
            TvUtil.leggVed(spillere.leggVedInfo());

        forsvarende = null;
        spillere.våknOpp();
        spillere.nyStemmeHistorikk();
        spillere.nominerTalereOgFlyers();
        refresh();
        tittuler("Hvem er de mistenkte?");
        dødsannonse();
        sjekkVinner();
        if (seier) return;
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

    public void godkjenn(Spiller valgt) {
        nyFase(GODKJENNINGSFASE);
        timer.stop();
        vindu.kontroll.setVisible(false);
        innhold = vindu.innhold();

        final String info = TvUtil.getText();
        final String tittel = SkjermUtil.hentTittel();

        rapporter("");

        if (dødsdømt != null) {
            valgt = dødsdømt;
            rapporter("Carlsen har slått " + dødsdømt);
        }

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
        final Spiller henrettet = valgt;
        innhold.add(new Knapp("Godkjenn", Knapp.HEL, e -> henrett(henrettet)
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
            ordfører.ekstraStemme();
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
            tittuler("Vi har en vinner!");
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
        else if (r instanceof Carlsen)
            vindu.kontroll(new Kontroll(), fase, new Knapp("Angrep/Forsvar", Knapp.HALV, new Mafiaknapper()));
    }

    public void setTittelFarge(Rolle r) {
        if (r == null) {
            SkjermUtil.fargTittel(Color.BLACK);
            return;
        }

        if (!r.funker() && !r.nyligKlonet())
            SkjermUtil.fargTittel(Color.RED);
        else if (r.skjerm() || r.informert() || r.nyligKlonet())
            SkjermUtil.fargTittel(Color.BLUE);
        else
            SkjermUtil.fargTittel(Color.BLACK);
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
        if (aktiv != null) {
            VeiledningsUtil.setTekst(aktiv.getVeiledning());
            if (TvUtil.tv.guideErSynlig())
                TvUtil.visGuide(aktiv.getGuide());
        }
        refresh(r);

        // TvUtil.leggTil(spillere.valg(r));
    }

    public void nesteRolle() {
        if (i.hasNext()) {
            Rolle r = i.next();

            while (!r.aktiv() || r == aktiv) {
                r.autoEvne();
                if (i.hasNext())
                    r = i.next();
                else
                    break;
            }
            if (r.aktiv()) {
                r.autoEvne();
                if (r.aktiv())
                    pek(r);
                else
                    nesteRolle();
            } else
                avsluttNatt();
        } else
            avsluttNatt();
    }

    public void forrigeRolle() {
        if (i.hasPrevious()) {
            rapporter("Tilbake");

            if (aktiv != null && aktiv.id(Rolle.JOKER))
                refresh();

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
        else if (fase(JOKERFASE))
            dilemma();
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
            marius.setStemmer(2);
            spillere.stem(marius, s);
        }

        if (sjekkOffer(Rolle.PSYKOLOG) && !s.id(Rolle.PSYKOLOG) && finnOffer(Rolle.PSYKOLOG).equals(s))
            spillere.stem(finnSpiller(Rolle.PSYKOLOG), s);
    }

    public void avsluttAvstemning() {
        ArrayList<Spiller> utstemte = spillere.hentUtstemte();
        ArrayList<Spiller> talere = spillere.hentTalere(utstemte);

        if (talere.isEmpty() || rakett) {
            //Avgjørende avstemning - Sjekk ordførerstemmer
            utstemte = spillere.hentUtstemte(ordfører);

            if (utstemte.isEmpty())
                godkjenn(null);
            else if (utstemte.size() > 1) {
                if (dødsdømt != null)
                    godkjenn(dødsdømt);
                else
                    uavgjort(utstemte);
            } else {
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

        if (taler == 0 && sjekkOffer(Rolle.CARLSEN))
            dødsdømt = ((Carlsen) finnRolle(Rolle.CARLSEN)).sjekkResultat(talere.contains(finnOffer(Rolle.CARLSEN)));

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
        if (sjekkRolle(Rolle.JOKER) && finnRolle(Rolle.JOKER).aktiv()) {
            if (rakett)
                finnRolle(Rolle.ASTRONAUT).aktiver(true);
            if (bombe)
                finnRolle(Rolle.BOMBER).aktiver(true);
            if (tiltale)
                finnRolle(Rolle.AKTOR).aktiver(true);
            rakett = bombe = tiltale = false;
            joker = true;
        }
    }

    public void aktiverDagsRoller() {
        if (joker)
            anarki();
        else if (rakett)
            rakettoppskytning();
        else if (bombe)
            plantetBombe();
        else if (tiltale)
            tiltale();
    }

    public void anarki() {
        nyFase(JOKERFASE);
        timer.setText(annonse + "\n\nJokerens dilemma!!!\nHva blir landsbyen enige om?");
        timer.nyStartMin(2);
        tittuler("Jokerens dilemma!");
        vindu.deaktiverPersonKNapper(innhold);
    }

    public void dilemma() {
        joker = false;
        timer.stop();
        refresh();
        tittuler("Hvem stemmer OPP?");
        informer("Jokerens dilemma!");
        rapporter("\nJokerns dilemma:");
    }

    public void avsluttDilemma() {
        spillere.fyllDilemma();

        ArrayList<Spiller> medJokeren = new ArrayList();

        Joker joker = (Joker) finnRolle(Rolle.JOKER);
        joker.bliFerdig();
        boolean fasit = joker.fasit();

        for (Spiller s : spillere.levendeOgFri())
            if (spillere.getDilemma().get(s).equals(fasit) && !s.equals(joker.spiller()))
                medJokeren.add(s);

        rapporter("Med Jokeren: " + medJokeren.size() + "\n" +
                "Mot Jokeren: " + (spillere.levendeOgFri().size() - medJokeren.size() - 1) + "\n");

        if (medJokeren.size() == 0) {
            godkjenn(joker.spiller());
            informer("Landsbyen seiret!\nJokeren, " + joker.spiller() + ", er død!");
            rapporter("Landsbyen seiret!\nJokeren, " + joker.spiller() + ", er død!");
        } else if (medJokeren.size() == spillere.levendeOgFri().size() - 1) {
            for (Spiller s : medJokeren)
                s.henrett();
            dagensResultat();
            tittuler("Jokeren vant!");
            informer("Jokeren, " + joker.spiller() + " seiret, og vi har en vinner!");
            rapporter("Jokeren, " + joker.spiller() + " seiret, og vi har en vinner!");
        } else {
            String ut = "Landsbyen seiret!\nMen noen gikk i Jokerens felle og døde:\n";
            for (Spiller s : medJokeren) {
                ut = jokerHenrett(s, ut);
            }

            spillere.dødsannonse();
            tittuler("Landsbyen overlevde!");
            informer(ut);
            rapporter(ut);
            dagensResultat();
        }
    }

    public String jokerHenrett(Spiller s, String ut) {
        s.henrett();

        int side = s.side();
        ut += "\n" + s;
        //Løgneren bytter side
        if (s.løgn())
            side = side - (2 * side);

        //Lag henrettelsestekst
        if (s.forsvart())
            ut += " er beskyttet, og er derfor ikke død!";
        else {
            ut += (side < Rolle.NØYTRAL ? " VAR " : " var IKKE ") + "mafia!";

            //Drep belieber hvis Justin er død
            if (sjekkRolle(Rolle.BELIEBER) && finnRolle(Rolle.BELIEBER).funker()) {
                Belieber belieber = (Belieber) finnRolle(Rolle.BELIEBER);
                if (s.equals(belieber.justin())) {
                    ut = jokerHenrett(belieber.spiller(), ut);
                    belieber.spiller().snipe(finnRolle(Rolle.JOKER));
                }
            }
        }

        //Sjekk om spiller er reddet av Jesus, og drep jesus istedenfor
        if (sjekkRolle(Rolle.JESUS)) {
            Jesus jesus = (Jesus) finnRolle(Rolle.JESUS);
            if (jesus.frelst() == s) {
                ut = jokerHenrett(jesus.spiller(), ut);
                jesus.spiller().snipe(jesus);
            }
        }

        //Sjekk om den døde er illusjonistens gjemmested
        if (sjekkRolle(Rolle.ILLUSJONIST) && finnOffer(Rolle.ILLUSJONIST) == s) {
            ut = jokerHenrett(finnSpiller(Rolle.ILLUSJONIST), ut);
            finnSpiller(Rolle.ILLUSJONIST).snipe(finnRolle(Rolle.JOKER));
        }

        //Sjekk om princess er den døde, og befri i så fall fangene
        if (s.id(Rolle.PRINCESS) && spillere.harFanger()) {
            spillere.befriFanger();
            ut += "\n\nFangene er befridd!";
        }

        return ut;
    }

    public void rakettoppskytning() {
        timer.stop();

        for (Spiller s : spillere.spillere())
            if (s.lever())
                spillere.nominer(s);

        finnSpiller(Rolle.ASTRONAUT).ekstraStemme();
        startAvstemning();
        informer(annonse.substring(1) + "\n\nDet er tid for rakettoppskytning!!!");
        rapporter("\nDet er tid for rakettoppskytning!!!");
        tittuler("Hvem skal sendes opp i raketten?");
    }

    public void avsluttRakett() {
        rakett = false;
        nyFase(DISKUSJONSFASE);
        finnSpiller(Rolle.ASTRONAUT).minkStemmer();
        restartMedTimer(null, tid - 2);
        aktiverDagsRoller();
        sjekkVinner();
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

    public static Rolle hentAktiv() {
        return aktiv;
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
        setVeiledning();
        MenyUtil.visSpillMeny(vindu, fase);
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

    public boolean aktivKontroll() {
        return vindu.kontroll.isVisible();
    }

    public void sprengTrompet(Spiller s) {
        refresh();
        aktiv = s.rolle();
        tittuler("\nHvem vil Trompeten sprenge?");
        VeiledningsUtil.setTekst(finnRolle(Rolle.TROMPET).getVeiledning());
    }

    public void nominer(Spiller s, boolean leggTil) {
        if (leggTil) {
            spillere.nominer(s);
            if (s.id(Rolle.CARLSEN) && ((Carlsen) finnRolle(Rolle.CARLSEN)).erDømt())
                dødsdømt = s;
        } else
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
        VeiledningsUtil.setTekst(aktiv.getVeiledning());
        return;
    }

    public void setVeiledning() {
        int unntak = -1;

        switch (fase) {
            case DISKUSJONSFASE:
                if (sjekkOffer(Rolle.BOMBER))
                    unntak = VeiledningsUtil.FASE_BOMBE;
                else if (taler > 2)
                    unntak = VeiledningsUtil.FASE_OPPGJØR;
                break;
            case AVSTEMNINGSFASE:
                if (rakett)
                    unntak = VeiledningsUtil.FASE_RAKETT;
                else if (tiltale)
                    unntak = VeiledningsUtil.FASE_TILTALE;
                break;
            case TALEFASE:
                if (tiltale)
                    unntak = VeiledningsUtil.FASE_TILTALE;
                break;
            default:
                unntak = -1;
        }
        VeiledningsUtil.setVeiledningForFase(fase, unntak);
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
        if (!nye.isEmpty()) {
            for (Spiller s : nye) {
                if (s.equals(nye.get(0)))
                    ut += "\n\n" + s;
                else if (nye.indexOf(s) < nye.size() - 1)
                    ut += ", " + s;
                else {
                    ut += " og " + s;
                }
            }
            ut += " døde nylig, og kan gi en siste stemme.";
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
        timer.stop();
        if (finnRolle(Rolle.ARVING) != null)
            ((Arving) finnRolle(Rolle.ARVING)).arv();
        switch (spillere.vinner()) {
            case -1:
                informer("Mafiaen har vunnet!" + "\n" + annonse);
                rapporter("Mafiaen har vunnet!");
                seier = true;
                break;
            case 1:
                informer("Landsbyen har vunnet!" + "\n" + annonse);
                rapporter("Landsbyen har vunnet!");
                seier = true;
                break;
            case 2:
                informer("Alle er døde! Ingen vant!" + "\n" + annonse);
                rapporter("Alle er døde! Ingen vant!");
                seier = true;
                break;
            case 3:
                informer("Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkisten har vunnet!" + "\n" + annonse);
                rapporter("Mafiaene er døde, men Anarkisten takler ikke freden og forgifter drikkevannet til landsbyen!\nAnarkisten har vunnet!");
                seier = true;
                break;
            case 4:
                informer("Agent Smith har tatt over hele landsbyen, og har vunnet!" + "\n" + annonse);
                rapporter("Agent Smith har tatt over hele landsbyen, og har vunnet!");
                seier = true;
                break;
            case 5:
                informer("Princess98 har kidnappet hele landsbyen, og har vunnet!!" + "\n" + annonse);
                rapporter("Princess98 har kidnappet hele landsbyen, og har vunnet!");
                seier = true;
                break;
            case 6:
                informer("Jokeren har vunnet!" + "\n" + annonse);
                rapporter("Jokeren har vunnet!");
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
        if (sjekkRolle(Rolle.JESUS)) {
            Jesus jesus = (Jesus) finnRolle(Rolle.JESUS);
            if (jesus.frelst() == s || (bombet != null && s != finnSpiller(Rolle.BOMBER) && jesus.frelst() == bombet))
                jesus.spiller().snipe(jesus);
        }

        //Sjekk om den døde er illusjonistens gjemmested
        if (sjekkRolle(Rolle.ILLUSJONIST) && finnOffer(Rolle.ILLUSJONIST) == s)
            finnSpiller(Rolle.ILLUSJONIST).snipe(finnRolle(Rolle.ILLUSJONIST));

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
        vindu.visAlleKnapper(innhold, this);
    }

    public void navnEndring() {
        timer.stop();
        proklamer("Hvem skal omdøpes?");
        rapporter("Hvem skal omdøpes?");
        VeiledningsUtil.setTekst("Navnendring:\n" +
                "Trykk på spilleren du ønsker å døpe om.\n" +
                "I vinduet som kommer opp, kan du så skrive inn det nye navnet.\n" +
                "Trykk så ok for å utføre navnendringen og fortsette spillet.");
        vindu.visAlleKnapper(innhold, e -> {
            Spiller s = ((Knapp) e.getSource()).spiller();
            String navn = s.navn();
            InnstillingsUtil.endreSpillerNavn(s);
            rapporter(navn + " endret navn til " + s);
            tilbakeTilDiskusjon();
        });
    }

    public void tilbakeTilDiskusjon() {
        nyFase(DISKUSJONSFASE);
        spillere.dødsannonse();
        tittuler("Hvem er de mistenkte?");
        refresh();
        visMistenkte();
        timer.fortsett();
    }

    // //////////////////////////// TV-SKERM //////////////////////////////////

    public void informer(String tekst) {
        TvUtil.vis(tekst);
    }

    public void proklamer(String tekst) {
        SkjermUtil.tittuler(tekst);
        TvUtil.vis(tekst);
    }

    public void tittuler(String tekst) {
        SkjermUtil.tittuler(tekst);
    }

    public void rapporter(String tekst) {
        SkjermUtil.logg(tekst);
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
                    } else if (fase(JOKERFASE) && !joker) {
                        avsluttDilemma();
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
            } else if (knapp(e, "Angrep/Forsvar")) {
                Carlsen c = ((Carlsen) finnRolle(Rolle.CARLSEN));
                c.skift();
                tittuler(c.oppgave());
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
            } else if (fase(RØMNINGSFASE)) {
                rapporter(valgt + " har rømt fra landsbyen");
                valgt.henrett();
                nominer(valgt, false);
                if (valgt.equals(dødsdømt)) dødsdømt = null;
                tilbakeTilDiskusjon();
            } else if (fase(TIEBREAKERFASE))
                godkjenn(valgt);
            else if (fase(JOKERFASE)) {
                knapp(e).setEnabled(false);
                spillere.registrerDilemmaValg(valgt);
            } else {
                System.out.println("UKJENT FASE: " + fase + "(valgt på dagen)");
            }
        }

        // VELGER PÅ NATTEN
        else {
            if (fase(RØMNINGSFASE)) {
                nyFase(NATTFASE);
                rapporter(valgt + " har rømt fra landsbyen");
                valgt.snipe(null);
                pek(aktiv);
                return;
            }

            if (aktiv.funker() || (aktiv.id(Rolle.SPECIAL) && aktiv.aktiv())) {
                spillere.lagrePek(aktiv.pri(), valgt);
                aktiv.pek(valgt);
                rapporter(aktiv.rapport());
            }
            if (aktiv.fortsetter())
                nesteRolle();
            else {
                //Ikke deaktiverknapper for flertrykksroller
                refresh(aktiv(Rolle.MAFIA) || aktiv(Rolle.COPYCAT) || aktiv(Rolle.KIRSTEN) || aktiv(Rolle.CUPID) || aktiv(Rolle.PSYKOLOG) ? aktiv : null);
            }
        }
    }
}
