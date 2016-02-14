package gui;

import Utils.*;
import datastruktur.Spillerliste;
import personer.FlerSpillerRolle;
import personer.Rolle;
import personer.Spiller;
import personer.roller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Oppstart implements ActionListener {

    JPanel innhold;
    JTextField navnefelt;
    JLabel tekst, spesialistTekst = new JLabel();
    Vindu vindu;
    private static Spillerliste spillere;

    public static Rolle[] roller;
    ArrayList<String> gjenstander;
    Knapp fortsett;

    public int antallspillere = 0;
    int fase = 100, mafiaer = -1, politi = -1, venner = -1, backup, tid = 6;
    private static int indeks = 0, personIndeks = -1;
    boolean fordelerSpesialister, fjerning;

    public static final int VELGSPILLERE = 100, VELGROLLER = 101, VELGSPESIALISTER = 102, VELGGJENSTANDER = -1, HVEMERHVA = 103, STARTSPILL = 104;
    public static final int TITTEL = 50, UNDERTITTEL = 30;

    private Mafia mafia;

    public Oppstart(Vindu vindu) {
        this.vindu = vindu;
        spillere = vindu.spillere;
        this.innhold = vindu.innhold;

        vindu.setOppstart(this);
        vindu.kontroll(new KontrollLytter(), -1);

        indeks = 0;
        roller = null;
        velgSpillere();
        InnstillingsUtil.setOppstart(this);
    }

    public Spillerliste getSpillere() {
        return spillere;
    }

    public void nyfase(int f) {
        vindu.getFortsett().setEnabled(true);
        switch (f) {
            case VELGSPILLERE:
                velgSpillere();
                break;
            case VELGROLLER:
                velgRoller();
                break;
            case VELGSPESIALISTER:
                velgSpesialister();
                break;
            case VELGGJENSTANDER:
                velgGjenstander();
                break;
            case HVEMERHVA:
                hvemErHva();
                break;
            case STARTSPILL:
                startSpill();
                break;
        }
        MenyUtil.visSpillMeny(vindu, f);
    }

    private void velgSpillere() {
        fase = VELGSPILLERE;
        vindu.kontroll.setVisible(false);
        vindu.visLogg(false);
        VeiledningsUtil.setTekst("Spillerregistrering:\n" +
                "Skriv inn navnet på nye spillere og trykk enter, eller klikk på registrer for å registrere dem.\n" +
                "For å fjerne en spiller, trykker du på spillerens navn i listen og trykker fjern.\n" +
                "Trykk fortsett for å gå til neste steg.");
        navnefelt = new JTextField();
        navnefelt.setPreferredSize(Knapp.HEL);
        navnefelt.addActionListener(this);
        navnefelt.requestFocusInWindow();

        SkjermUtil.tittuler("Hvem skal spille?");

        Knapp registrer = new Knapp("Registrer", Knapp.HEL, this);
        fortsett = vindu.getFortsett();
        fortsett.setPreferredSize(Knapp.HEL);
        fortsett.setVisible(true);
        Knapp fjern = new Knapp("Fjern", Knapp.HEL, e -> fjernSpillere());

        informer(spillere.toString());
        leggTilSpillere(spillere);

        innhold = vindu.innhold();
        innhold.add(navnefelt);
        innhold.add(registrer);
        innhold.add(fjern);
        innhold.add(fortsett);
        innhold.add(new Knapp("Fjern alle", Knapp.HEL, e -> {
            spillere.spillere().clear();
            vindu.tømListe();
            vindu.velgIngen();
            informer(spillere.toString());
        }));
        innhold.add(new Knapp("Legg til alle", Knapp.HEL, e -> leggTilAlle()));
    }

    public void velgRoller() {
        fase = VELGROLLER;
        vindu.kontroll.setVisible(true);
        VeiledningsUtil.setTekst("Rollevalg:\n" +
                "Velg hvilke roller som skal være med i spill ved å klikke på rollens navn.\n" +
                "Når du velger en rolle, vil knappen deaktiveres, med mindre rollen kan ha flere spillere.\n" +
                "For å fjerne en rolle trykker du på fjern, og velger rollen som skal fjernes.\n" +
                "For å nullstille rollene, trykker du på tilbake.\n" +
                "Når nok roller er valgt, blir du automatisk tatt videre til neste steg.");

        innhold = vindu.innhold();
        SkjermUtil.tittuler("Hvilke roller skal være med?");
        vindu.kontroll(new KontrollLytter(), VELGROLLER);
        vindu.visLogg(true);

        opprettRolleKnapper();

        if (roller == null) {
            antallspillere = spillere.length();
            roller = new Rolle[100];
            roller[Rolle.MAFIA] = mafia;
            informer(spillere.rolleString(roller, --antallspillere));
        } else {
            informer(spillere.rolleString(roller, antallspillere));
            oppdaterKnapper();
        }

        if (antallspillere > 0)
            vindu.getFortsett().setEnabled(false);
    }

    public void velgSpesialister() {
        VeiledningsUtil.setTekst("Spesialistvalg:\n" +
                "Her kan du velge om mafiaen skal noen spesialister på sitt lag, som får en ekstra engangsevne." +
                "For å legge til en spesialist, trykker knappen med spesialistens navn. " +
                "Om du ikke vil ha noen spesialister, eller har valgt alle du vil ha, trykker du på fortsett.\n" +
                "For å nullstille spesialistene, trykker du tilbake én gang.\n" +
                "Trykker du på tilbake igjen, går du tilbake til rollevalg.");
        fase = VELGSPESIALISTER;
        innhold = vindu.innhold();
        mafia.fjernAlleSpesialister();

        SkjermUtil.tittuler("Hvilke spesialister skal mafiaen ha?");

        innhold.add(new Knapp(Mafia.SNIPER, Knapp.KVART, this));
        innhold.add(new Knapp(Mafia.SJÅFØR, Knapp.KVART, this));
        innhold.add(new Knapp(Mafia.SABOTØR, Knapp.KVART, this));
        innhold.add(new Knapp(Mafia.FORFALSKER, Knapp.KVART, this));
        innhold.add(new Knapp(Mafia.LOMMETYV, Knapp.KVART, this));

        vindu.kontroll(new KontrollLytter(), VELGSPESIALISTER);
    }

    public void velgGjenstander() {
        fase = VELGGJENSTANDER;
        gjenstander = new ArrayList<>();
        innhold = vindu.innhold();
        SkjermUtil.tittuler("Hvilke gjenstander skal være med?");

        innhold.add(new Knapp("Pistol", Knapp.KVART, this));
        innhold.add(new Knapp("Skuddsikker vest", Knapp.KVART, this));
        innhold.add(new Knapp("Benåding", Knapp.KVART, this));
        innhold.add(new Knapp("Trippelstemme", Knapp.KVART, this));
        innhold.add(new Knapp("Felle", Knapp.KVART, this));

        innhold.add(vindu.getTilbake());
        innhold.add(fortsett);

        fortsett.setPreferredSize(Knapp.HALV);

        informer("Gjenstander(" + spillere.spillere().size() + " spillere):");
    }

    public void hvemErHva() {
        fase = HVEMERHVA;
        VeiledningsUtil.setTekst("Rollefordeling:\n" +
                "Her skal rollene fordeles på spillerne ved å vekke rollen som vises til høyre, " +
                "og trykke på navnene til personene som våkner.\n" +
                "Husk at noen roller har flere spillere, og at alle mafiaspesialister er mafia.\n" +
                "Husk også at roller som undercover og insider må våkne sammen med henholdsvis mafia og politi.");

        informer("Følgende roller er med:");
        visRoller(spillere.visRoller(roller));
        SkjermUtil.fullLogg(spillere.visRoller(roller));

        innhold = vindu.innhold();
        SkjermUtil.tittuler("Hvem er hva?");
        vindu.kontroll(new KontrollLytter(), HVEMERHVA);
        fortsett = vindu.getFortsett();
        fortsett.setVisible(false);

        tekst = new JLabel();
        tekst.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
        tekst.setHorizontalAlignment(SwingConstants.CENTER);
        tekst.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel p = new JPanel(new BorderLayout());
        p.setVisible(true);
        p.add(tekst, BorderLayout.NORTH);
        p.add(innhold, BorderLayout.CENTER);

        vindu.personknapper(innhold, this);
        tommePersonKnapper();
        vindu.oppdaterRamme(p);

        indeks = 0;
        nesteRolle();
    }

    public void startSpill() {
        fase = STARTSPILL;
        innhold = vindu.innhold();
        vindu.kontroll.remove(1);
        fortsett.setVisible(true);
        fortsett.setText("Start spill!");
        fortsett.setPreferredSize(Knapp.SUPER);
        innhold.add(fortsett);
        SkjermUtil.fullLogg("");
        SkjermUtil.tittuler("Klar til å begynne spillet?");
    }

    private void leggTilSpiller(Spiller spiller){
        if (spillere.finnSpillerMedNavn(spiller.navn()) != null)
            return;
        spillere.leggTil(spiller);
        antallspillere++;
        vindu.leggTilListe(spiller);
        vindu.setListetittel("Spillere: " + spillere.spillere().size());
        informer(spillere.toString());
    }

    private void leggTilSpillere(Spillerliste spillere){
        vindu.setListetittel("Spillere: " + spillere.spillere().size());
        for (Spiller spiller : spillere.spillere()) {
            vindu.leggTilListe(spiller);
        }
    }

    private void fjernSpillere(){
        List<Spiller> valgte = vindu.hentValgteSpillere();
        if (valgte.isEmpty()) valgte = new ArrayList<>();
        Spiller navngitt = spillere.finnSpillerMedNavn(navnefelt.getText());
        if (navngitt != null)
            valgte.add(navngitt);

        for (Spiller spiller : valgte) {
            vindu.fjernFraListe(spiller);
            spillere.fjern(spiller.navn());
            antallspillere--;
        }
        informer(spillere.toString());
        navnefelt.setText("");
        navnefelt.requestFocusInWindow();
        vindu.setListetittel("Spillere: " + spillere.spillere().size());
        vindu.velgIngen();
    }

    private void tommePersonKnapper(){
        spillere.tømTommeRoller();
        for (int i = antallspillere; i < 0; i++) {
            Knapp k = new Knapp("Ingen spiller", Knapp.HALV, this);
            k.setForeground(Color.RED);
            innhold.add(k);
        }
    }

    private void fordelSpesialister() {
        fordelerSpesialister = true;
        String spesialist = mafia.hentLedigSpesialist();
        TvUtil.visMafiaRolleBilde(mafia, spesialist);
        if (spesialist.isEmpty())
            return;
        spesialistTekst = new JLabel();
        spesialistTekst.setPreferredSize(new Dimension(600, 135));
        spesialistTekst.setFont(new Font("Arial", Font.BOLD, Oppstart.UNDERTITTEL));
        spesialistTekst.setHorizontalAlignment(JLabel.CENTER);
        spesialistTekst.setText("Hvem er " + spesialist + "?");
        innhold.add(spesialistTekst);

    }

    public void stopSpesialistFordeling() {
        fordelerSpesialister = false;
        innhold.remove(spesialistTekst);
        innhold.revalidate();
        innhold.repaint();
    }

    public void autoFordelRoller() {
        mafia.nullstillSpesialister();
        spillere.fordelRoller(roller);
        innhold = vindu.innhold();
        fortsett.setVisible(true);

        SkjermUtil.tittuler("Hvem er hva?");

        tekst = new JLabel();
        tekst.setFont(new Font("Arial", Font.BOLD, Oppstart.TITTEL));
        tekst.setHorizontalAlignment(SwingConstants.CENTER);
        tekst.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        innhold.add(tekst);
        vindu.oppdaterRamme(innhold);

        personIndeks = -1;
        nestePerson();
    }

    public void nesteRolle() {
        while (indeks < roller.length - 1 && roller[indeks] == null) indeks++;

        if (indeks == roller.length - 1 && roller[roller.length - 1] == null && fase == HVEMERHVA) {
            nyfase(++fase);
            TvUtil.skjulBilde();
            TvUtil.lukkGuide();
        } else if (indeks < roller.length) {
            tekst.setText(roller[indeks].tittel() + " våkner");
            innhold.revalidate();
            if (TvUtil.tv.guideErSynlig())
                TvUtil.visGuide(roller[indeks].getGuide());

            if (indeks == Rolle.MAFIA)
                fordelSpesialister();
            else
                TvUtil.visRolleBilde(roller[indeks]);
        } else {
            nyfase(++fase);
            TvUtil.skjulBilde();
        }
    }

    public void nestePerson() {
        if (++personIndeks == spillere.spillere().size()) {
            nyfase(++fase);
            TvUtil.skjulBilde();
            TvUtil.lukkGuide();
            return;
        }
        Spiller spiller = spillere.spillere().get(personIndeks);
        tekst.setText(spiller.navn() + " våkner");
        SkjermUtil.fullLogg(spiller.navn() + " er " + spiller.rolle() + (spiller.getMafiarolle().isEmpty() ? "" : "(" + spiller.getMafiarolle() + ")"));
        if (spiller.rolle() instanceof Mafia)
            TvUtil.visMafiaRolleBilde((Mafia) spiller.rolle(), spiller.getMafiarolle());
        else
            TvUtil.visRolleBilde(spiller.rolle());
        if (TvUtil.tv.guideErSynlig())
            TvUtil.visGuide(spiller.rolle().getGuide());
    }

    public static Rolle hentRolle() {
        try {
            if (personIndeks >= 0 && personIndeks < spillere.spillere().size())
                return spillere.spillere().get(personIndeks).rolle();
            return roller[indeks];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setAntall(int a) {
        antallspillere = a;
    }

    public void setTid(int t) {
        tid = t;
    }

    public void informer(String informasjon) {
        TvUtil.vis(informasjon);
        SkjermUtil.fullLogg(informasjon);
    }

    private void visRoller(String rollerListe) {
        TvUtil.setRoller(rollerListe);
        TvUtil.visSideInfo(rollerListe);
    }

    public void inverserKnapper() {
        fjerning = !fjerning;
        oppdaterKnapper();
    }

    public void oppdaterKnapper() {
        for (Component k : innhold.getComponents()) {
            if (k instanceof Knapp) {
                Rolle r = ((Knapp) k).rolle();
                if (fjerning) {
                    k.setEnabled(harRolle(r.pri()));
                    if (r.id(Rolle.MAFIA) && !r.flere())
                        k.setEnabled(false);
                } else if (antallspillere < -2)
                    k.setEnabled(false);
                else if (r instanceof FlerSpillerRolle)
                    k.setEnabled(true);
                else {
                    k.setEnabled(!harRolle(r.pri()));
                }
            }
        }
    }

    private boolean harRolle(int id) {
        if (roller != null)
            return roller[id] != null;
        else
            return false;
    }

    private void tilbakeVelgRoller(){
        roller = null;
        if (antallspillere < (spillere.length() - 1))
            nyfase(fase);
        else
            nyfase(--fase);
    }

    private void tilbakeVelgSpesialister() {
        if (mafia.fjernAlleSpesialister()) {
            nyfase(fase);
        } else
            nyfase(--fase);
    }

    private void tilbakeVelgGjenstander() {
        if (!gjenstander.isEmpty()) {
            gjenstander.clear();
            nyfase(fase);
        } else
            nyfase(--fase);
    }

    private void tilbakeHvemErHva() {
        //Nullstill indeks for fordeling av roller
        int i = indeks;
        politi = -1;
        mafiaer = -1;
        venner = -1;
        for (indeks = 0; roller[indeks] == null; indeks++) ;

        if (mafia.nullstillSpesialister() || i != indeks || personIndeks >= 0) {
            nyfase(HVEMERHVA);
        } else {
            nyfase(--fase);
            TvUtil.skjulBilde();
        }

        personIndeks = -1;
        fortsett.setText("Fortsett");
        fortsett.setPreferredSize(Knapp.HEL);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(navnefelt)) {
            registrerSpiller();
            return;
        }

        Knapp k = (Knapp) e.getSource();
        if (fase == VELGSPILLERE)
            registrerSpiller();
        else if (fase == VELGROLLER && !fjerning)
            rolleValgt(k);
        else if (fase == VELGROLLER)
            rolleFjernet(k);
        else if (fase == VELGSPESIALISTER)
            spesialistValgt(k);
        else if (fase == VELGGJENSTANDER)
            gjenstandValgt(k);
        else if (fase == HVEMERHVA)
            rolleSatt(k);
    }

    //TRYKKEMETODER
    private void registrerSpiller(){
        if (spillere.finnSpillerMedNavn(navnefelt.getText()) == null) {
            Spiller spiller = new Spiller(navnefelt.getText().isEmpty() ? "Anonym" : navnefelt.getText());
            leggTilSpiller(spiller);
        } else {
            informer("Finnes allerede");
        }

        navnefelt.setText("");
        navnefelt.requestFocusInWindow();
        vindu.velgIngen();
    }

    private void rolleValgt(Knapp k) {
        Rolle rolle = k.rolle();

        if (roller[rolle.pri()] == null) {
            roller[rolle.pri()] = rolle;
            if (!(rolle instanceof FlerSpillerRolle))
                k.setEnabled(false);
        } else if (rolle instanceof FlerSpillerRolle)
            ((FlerSpillerRolle) rolle).fler();

        informer(spillere.rolleString(roller, --antallspillere));
        sjekkRolleAntall();
    }

    private void rolleFjernet(Knapp k){
        Rolle rolle = k.rolle();
        if (rolle != null) {
            if (rolle.flere()) {
                ((FlerSpillerRolle) rolle).fjern();
            } else
                roller[rolle.pri()] = null;
        }

        informer(spillere.rolleString(roller, ++antallspillere));
        inverserKnapper();

        if (antallspillere > 0)
            vindu.getFortsett().setEnabled(false);
        else
            vindu.getFortsett().setEnabled(true);
        sjekkRolleAntall();
    }

    private void sjekkRolleAntall(){
        if (antallspillere > 0)
            vindu.getFortsett().setEnabled(false);
        else if (antallspillere == 0) {
            Object[] options = {"Fortsett", "Velg flere"};
            int svar = JOptionPane.showOptionDialog(vindu,
                    "Du har nå nok roller for alle spillerne.\nVil du fortsette eller legge til ekstra roller?",
                    "Nok Roller", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ImgUtil.getAppIcon(), options, options[0]);

            if (svar == JOptionPane.YES_OPTION)
                nyfase(++fase);
        }
        else if (antallspillere > -3) {
            vindu.getFortsett().setEnabled(true);
        } else
            nyfase(++fase);
    }

    private void spesialistValgt(Knapp k){
        String tekst = k.getText();
        k.setEnabled(false);
        if (mafia.leggTilSpesialist(tekst))
            vindu.getFortsett().doClick();

    }

    private void gjenstandValgt(Knapp k){
        gjenstander.add(k.getText());
        TvUtil.leggTil("\n" + k.getText());

        SkjermUtil.fullLogg(TvUtil.getText());

        if (gjenstander.size() == spillere.spillere().size()) {
            spillere.fordelGjenstander(gjenstander);
            nyfase(++fase);
        }
    }

    private void rolleSatt(Knapp k){
        if (k.spiller() == null) {
            roller[indeks].tom();
            spillere.hentTommeRoller().add(roller[indeks]);
        } else if (fordelerSpesialister) {
            k.spiller().setRolle(mafia, mafia.hentLedigSpesialist());
            stopSpesialistFordeling();
        } else
            k.spiller().setRolle(roller[indeks]);

        k.setEnabled(false);

        if (!(roller[indeks] instanceof FlerSpillerRolle) || ((FlerSpillerRolle) roller[indeks]).registrer())
            indeks++;

        nesteRolle();
    }

    private class KontrollLytter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //////////////////////FORTSETT//////////////////////
            if (e.getSource() == vindu.getFortsett()) {
                if (fase == STARTSPILL) {
                    Spill spill = new Spill(vindu, roller, tid);
                    indeks = -1;
                    spill.natt();
                } else if (fase == HVEMERHVA)
                    nestePerson();
                else if (spillere.length() < 5)
                    informer("Ikke nok spillere!");
                else
                    nyfase(++fase);
            }
            //////////////////////TILBAKE//////////////////////
            else if (e.getSource() == vindu.getTilbake()) {
                if (fase == VELGROLLER)
                    tilbakeVelgRoller();
                else if (fase == VELGGJENSTANDER)
                    tilbakeVelgGjenstander();
                else if (fase == VELGSPESIALISTER)
                    tilbakeVelgSpesialister();
                else if (fase == HVEMERHVA || fase == STARTSPILL)
                    tilbakeHvemErHva();
            }
        }
    }

    private void leggTilAlle(){
        leggTilSpiller(new Spiller("Sondre"));
        leggTilSpiller(new Spiller("Lars-Erik"));
        leggTilSpiller(new Spiller("Ørnulf"));
        leggTilSpiller(new Spiller("Daniel"));
        leggTilSpiller(new Spiller("Adrian"));
        //				spillere.leggTil(new Spiller("Kjetil"));
        //				spillere.leggTil(new Spiller("Jens Emil"));
        //				spillere.leggTil(new Spiller("Ole-Halvor"));
        //				spillere.leggTil(new Spiller("Randi"));
        //				spillere.leggTil(new Spiller("Bård Anders"));
        //				spillere.leggTil(new Spiller("Simon"));
        //				spillere.leggTil(new Spiller("Ole Martin"));
        //				spillere.leggTil(new Spiller("Emil"));
        //				spillere.leggTil(new Spiller("Ingrid"));
        //				spillere.leggTil(new Spiller("Anders"));
        //				spillere.leggTil(new Spiller("Andreas"));

				/* Trondheim
                spillere.leggTil(new Spiller("Sinte Simen"));
				spillere.leggTil(new Spiller("Ida Nigga Reite"));
				spillere.leggTil(new Spiller("Swigurd"));
				spillere.leggTil(new Spiller("Morten"));
				spillere.leggTil(new Spiller("Shiv"));
				spillere.leggTil(new Spiller("Stian"));
				spillere.leggTil(new Spiller("Jboy"));
				spillere.leggTil(new Spiller("DJ Sindre"));
				*/

        informer(spillere.toString());
    }

    private void opprettRolleKnapper() {
        mafia = new Mafia();
        Politi politi = new Politi();
        Bestevenn venn = new Bestevenn();
        innhold.add(new Knapp("Mafia", mafia, Knapp.KVART, this));

        innhold.add(new Knapp("Agent Smith", new Smith(), Knapp.KVART, this));
        innhold.add(new Knapp("Aktor", new Aktor(), Knapp.KVART, this));
        innhold.add(new Knapp("Anarkist", new Anarkist(), Knapp.KVART, this));
        innhold.add(new Knapp("Arving", new Arving(), Knapp.KVART, this));
        innhold.add(new Knapp("Astronaut", new Astronaut(), Knapp.KVART, this));

        innhold.add(new Knapp("Barnslige Erik", new Erik(), Knapp.KVART, this));
        innhold.add(new Knapp("Bedrager", new Bedrager(), Knapp.KVART, this));
        innhold.add(new Knapp("Belieber", new Belieber(), Knapp.KVART, this));
        innhold.add(new Knapp("Bestemor", new Bestemor(), Knapp.KVART, this));
        innhold.add(new Knapp("Bestevenn", venn, Knapp.KVART, this));
        innhold.add(new Knapp("Bodyguard", new BodyGuard(), Knapp.KVART, this));
        innhold.add(new Knapp("Bomber", new Bomber(), Knapp.KVART, this));
        innhold.add(new Knapp("Bøddelen", new Boddel(), Knapp.KVART, this));

        innhold.add(new Knapp("CopyCat", new CopyCat(), Knapp.KVART, this));
        innhold.add(new Knapp("Cupid", new Cupid(), Knapp.KVART, this));

        innhold.add(new Knapp("Distré Didrik", new Didrik(), Knapp.KVART, this));
        innhold.add(new Knapp("Drømmer", new Drommer(), Knapp.KVART, this));

        innhold.add(new Knapp("Filosof", new Filosof(), Knapp.KVART, this));
        innhold.add(new Knapp("Forsvarer", new Forsvarer(), Knapp.KVART, this));

        innhold.add(new Knapp("Gærne Berit", new Berit(), Knapp.KVART, this));

        innhold.add(new Knapp("Hammer", new Hammer(), Knapp.KVART, this));
        innhold.add(new Knapp("Havfrue", new Havfrue(), Knapp.KVART, this));
        innhold.add(new Knapp("Heisenberg", new Heisenberg(), Knapp.KVART, this));
        innhold.add(new Knapp("HMS-Ansvarlig", new HMSansvarlig(), Knapp.KVART, this));

        innhold.add(new Knapp("Illusjonist", new Illusjonist(), Knapp.KVART, this));
        innhold.add(new Knapp("Informant", new Informant(), Knapp.KVART, this));
        innhold.add(new Knapp("Insider", new Insider(politi), Knapp.KVART, this));

        innhold.add(new Knapp("Jesus", new Jesus(), Knapp.KVART, this));
        innhold.add(new Knapp("Joker", new Joker(), Knapp.KVART, this));
        innhold.add(new Knapp("Julenissen", new Julenissen(), Knapp.KVART, this));
        innhold.add(new Knapp("Jørgen", new Jorgen(), Knapp.KVART, this));

        innhold.add(new Knapp("Kirsten Giftekniv", new Kirsten(), Knapp.KVART, this));
        innhold.add(new Knapp("Kløna", new Klona(), Knapp.KVART, this));

        innhold.add(new Knapp("Lege", new Lege(), Knapp.KVART, this));
        innhold.add(new Knapp("Liten jente", new Jente(), Knapp.KVART, this));
        innhold.add(new Knapp("Løgner", new Logner(), Knapp.KVART, this));

        innhold.add(new Knapp("Magnus Carlsen", new Carlsen(), Knapp.KVART, this));
        innhold.add(new Knapp("Marius", new Marius(), Knapp.KVART, this));
        innhold.add(new Knapp("Morder", new Morder(), Knapp.KVART, this));

        innhold.add(new Knapp("Obduksjonist", new Obduksjonist(), Knapp.KVART, this));

        innhold.add(new Knapp("Politi", politi, Knapp.KVART, this));
        innhold.add(new Knapp("Postmann", new Postmann(), Knapp.KVART, this));
        innhold.add(new Knapp("Princess98", new Princess(), Knapp.KVART, this));
        innhold.add(new Knapp("Psykolog", new Psykolog(), Knapp.KVART, this));
        innhold.add(new Knapp("Pyroman", new Pyroman(), Knapp.KVART, this));

        innhold.add(new Knapp("Quisling", new Quisling(), Knapp.KVART, this));

        innhold.add(new Knapp("Ravn", new Ravn(), Knapp.KVART, this));
        innhold.add(new Knapp("Rex", new Rex(), Knapp.KVART, this));

        innhold.add(new Knapp("Sherlock", new Sherlock(), Knapp.KVART, this));
        innhold.add(new Knapp("Skytsengel", new Skytsengel(), Knapp.KVART, this));
        innhold.add(new Knapp("Snylter", new Snylter(), Knapp.KVART, this));
        innhold.add(new Knapp("Snåsamannen", new Snasamann(), Knapp.KVART, this));
        innhold.add(new Knapp("Sofasurfer", new Sofasurfer(), Knapp.KVART, this));
        innhold.add(new Knapp("Special Guy", new Specialguy(), Knapp.KVART, this));

        innhold.add(new Knapp("Tjukkas", new Tjukkas(), Knapp.KVART, this));
        innhold.add(new Knapp("Trompet", new Trompet(), Knapp.KVART, this));
        innhold.add(new Knapp("Tyler Durden", new Tyler(), Knapp.KVART, this));
        innhold.add(new Knapp("Tyster", new Tyster(), Knapp.KVART, this));

        innhold.add(new Knapp("Ulf Omar", new UlfOmar(), Knapp.KVART, this));
        innhold.add(new Knapp("Undercover", new Undercover(mafia), Knapp.KVART, this));

        innhold.add(new Knapp("VaraMafia", new Vara(), Knapp.KVART, this));

        innhold.add(new Knapp("Youtuber", new Youtuber(), Knapp.KVART, this));

        innhold.add(new Knapp("Zombie", new Zombie(), Knapp.KVART, this));
    }
}
