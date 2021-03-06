package datastruktur;

import gui.Oppstart;
import personer.Rolle;
import personer.Spiller;
import personer.roller.*;

import java.util.*;

public class Spillerliste {
    ArrayList<Spiller> spillere;
    ArrayList<Spiller> sl = new ArrayList<>();
    ArrayList<Spiller> nye = new ArrayList<>();
    ArrayList<Spiller> fanger = new ArrayList<>();
    ArrayList<Spiller> nominerte = new ArrayList<>();
    HashMap<Spiller, Rolle> delays = new HashMap<>();
    HashMap<Spiller, Integer> stemmer = new HashMap<>();
    HashMap<Spiller, Boolean> dilemma = new HashMap<>();
    ArrayList<HashMap<Integer, Spiller>> pekeHistorikk = new ArrayList<>();
    ArrayList<HashMap<Spiller, Spiller>> stemmeHistorikk = new ArrayList<>();

    List<Rolle> tommeRoller = new ArrayList<>();

    public Spillerliste() {
        spillere = new ArrayList<>();
    }

    public String nySpiller(String n, Rolle r) {
        leggTil(new Spiller(n, r));
        return n;
    }

    public void leggTil(Spiller s) {
        if (finnSpillerMedNavn(s.navn()) == null)
            spillere.add(s);
    }

    public void fjern(String s) {
        spillere.remove(finnSpillerMedNavn(s));
    }

    public void fordelRoller(Rolle[] roller) {
        int låste = 0;
        List<Rolle> aktiveRoller = new ArrayList<>();
        for (Rolle rolle : hentAktiveRoller(roller, false)) {
            if (rolle.erLåst()) {
                aktiveRoller.add(0, rolle);
                låste++;
            } else {
                aktiveRoller.add(rolle);
            }
        }

        Collections.shuffle(aktiveRoller.subList(låste, aktiveRoller.size()));
        Collections.shuffle(aktiveRoller.subList(0, spillere.size()));

        for (int i = 0; i < aktiveRoller.size(); i++) {
            Rolle rolle = aktiveRoller.get(i);
            if (i >= spillere.size()) {
                rolle.tom();
                tommeRoller.add(rolle);
            } else if (rolle instanceof Mafia) {
                Mafia mafia = ((Mafia) rolle);
                String spesialist = mafia.hentRandomLedigSpesialist();
                spillere.get(i).setRolle(mafia, spesialist);
            } else
                spillere.get(i).setRolle(rolle);
        }
    }

    //Låse utvalg av flerspillerroller
//    if (rolle.erLåst() && !aktiveRoller.contains(rolle)) {
//        if (rolle instanceof FlerSpillerRolle) {
//            for (int i = 0; i < rolle.antall(); i++) {
//                if (i < ((FlerSpillerRolle) rolle).hentAntallLåste()) {
//                    aktiveRoller.add(0, rolle);
//                    låste++;
//                } else
//                    aktiveRoller.add(rolle);
//            }
//        } else {
//            aktiveRoller.add(0, rolle);
//            låste++;
//        }
//    } else {
//        aktiveRoller.add(rolle);
//    }

    public List<Rolle> hentAktiveRoller(Rolle[] roller, boolean unik) {
        List<Rolle> aktiveRoller = new ArrayList();
        for (Rolle rolle : roller) {
            if (rolle != null) {
                if (unik)
                    aktiveRoller.add(rolle);
                else
                    for (int i = 0; i < rolle.antall(); i++)
                        aktiveRoller.add(rolle);
            }
        }
        return aktiveRoller;
    }

    public boolean alleErLåst(Rolle[] roller) {
        int låste = 0;
        for (Rolle rolle : hentAktiveRoller(roller, false)) {
            if (rolle.erLåst())
                låste++;
        }
        return låste >= spillere.size();
    }

    public List<Rolle> hentTommeRoller() {
        return tommeRoller;
    }

    public void tømTommeRoller() {
        for (Rolle rolle : tommeRoller) {
            rolle.reset();
        }
        tommeRoller.clear();
    }

    public void våknOpp() {
        if (!fanger.isEmpty())
            kidnappFanger();
        for (Spiller s : spillere) {
            s.våknOpp();
        }
    }

    public void sov() {
        for (Spiller s : spillere) {
            s.setOffer(null);
            s.sov();
        }
    }

    public void nyPekeHistorikk() {
        pekeHistorikk.add(new HashMap<Integer, Spiller>());
    }

    public HashMap<Integer, Spiller> gjeldendePek() {
        if (pekeHistorikk.isEmpty())
            nyPekeHistorikk();
        return pekeHistorikk.get(pekeHistorikk.size() - 1);
    }

    public void lagrePek(Integer r, Spiller s) {
        gjeldendePek().put(r, s);
    }

    public Spiller angrePek(Integer r) {
        return gjeldendePek().remove(r);
    }

    public Spiller hentSistePek(Integer r) {
        return gjeldendePek().get(r);
    }

    public Spiller hentPekFraNatt(Integer r, int natt) {
        return pekeHistorikk.get(natt + 1).get(r);
    }

    public void nyStemmeHistorikk() {
        stemmeHistorikk.add(new HashMap<>());
    }

    public HashMap<Spiller, Spiller> gjeldendeStemmer() {
        if (stemmeHistorikk.isEmpty())
            nyStemmeHistorikk();
        return stemmeHistorikk.get(stemmeHistorikk.size() - 1);
    }

    public void lagreStemme(Spiller k, Spiller v) {
        gjeldendeStemmer().put(k, v);
    }

    public Spiller angreStemme(Spiller k) {
        return gjeldendeStemmer().remove(k);
    }

    public Spiller hentSisteStemmeFra(Spiller k) {
        if (stemmeHistorikk.isEmpty())
            return null;

        HashMap stemmeDag = stemmeHistorikk.get(stemmeHistorikk.size() - 1);
        if (stemmeDag != null)
            return (Spiller) stemmeDag.get(k);
        else
            return null;
    }

    public Spiller hentStemmeFraDag(Spiller k, int dag) {
        return stemmeHistorikk.get(dag + 1).get(k);
    }

    public ArrayList<Spiller> hentStemmerForSpiller(Spiller k) {
        ArrayList<Spiller> stemmer = new ArrayList<>();
        for (HashMap<Spiller, Spiller> stemme : stemmeHistorikk)
            stemmer.add(stemme.get(k));
        return stemmer;
    }

    public ArrayList<Rolle> hentStemmerPå(Spiller mistenkt) {
        ArrayList<Rolle> stemmer = new ArrayList<>();
        HashMap<Spiller, Spiller> stemmeDag;

        if (stemmeHistorikk.isEmpty())
            return null;

        stemmeDag = stemmeHistorikk.get(stemmeHistorikk.size() - 1);

        if (stemmeDag == null)
            return stemmer;

        for (Map.Entry<Spiller, Spiller> stemme : stemmeDag.entrySet()) {
            if (stemme.getValue() != null && stemme.getValue() == mistenkt &&
                    !stemme.getKey().id(Rolle.TYSTER) && !stemmer.contains(stemme.getKey().rolle()))
                stemmer.add(stemme.getKey().rolle());
        }
        return stemmer;
    }

    public void gjenoppliv(Spiller død) {
        sl.remove(død);
    }

    public void restart() {
        for (Spiller s : spillere)
            s.nullstill();
        sl.clear();
        stemmeHistorikk.clear();
        pekeHistorikk.clear();
        stemmer.clear();
        fanger.clear();
        dilemma.clear();
        delays.clear();
        tommeRoller.clear();
    }

    public void slettRoller() {
        for (Spiller s : spillere)
            s.setRolle(null);
    }

    public int length() {
        return spillere.size();
    }

    public Spiller finnSpillerMedNavn(String n) {
        for (Spiller s : spillere)
            if (s.navn().equals(n)) return s;
        return null;
    }

    public Spiller finnSpiller(int id) {
        for (Spiller s : spillere)
            if (s.id(id)) return s;
        return null;
    }

    public Rolle finnRolle(int id) {
        for (Spiller s : spillere)
            if (s.id(id)) return s.rolle();
        return null;
    }

    public Spiller finnOffer(int id) {
        return finnRolle(id).offer();
    }

    public Boolean sjekkRolle(int id) {
        return finnRolle(id) != null;
    }

    public Boolean sjekkOffer(int id) {
        return sjekkRolle(id) && finnOffer(id) != null;
    }

    public Rolle randomRolle(int nedre, int øvre, int eks) {
        int id = -1;
        Random random = new Random();
        while (finnSpiller(id) == null || id == eks || !finnRolle(id).fortsetter())
            id = random.nextInt((øvre - nedre) + 1) + nedre;
        return finnRolle(id);
    }

    public Rolle tylersRolle() {
        int id = -1, index = 0;
        List<Rolle> roller = hentAktiveRoller(Oppstart.roller, true);
        Collections.shuffle(roller);

        while (finnSpiller(id) == null || rolleKanIkkeKopieres(id))
            id = roller.get(index++).pri();

        return finnRolle(id);
    }

    private boolean rolleKanIkkeKopieres(int id){
        return !finnRolle(id).fortsetter() || id == Rolle.TYLER || id <= Rolle.UNDERCOVER || id > Rolle.MARIUS
                || id == Rolle.TYSTER || id == Rolle.POSTMANN || id == Rolle.CUPID || id == Rolle.KIRSTEN || id == Rolle.COPYCAT
                || id == Rolle.BERIT || id == Rolle.SPECIAL || (id == Rolle.PRINCESS && !finnRolle(Rolle.PRINCESS).funker());
    }

    public Spiller randomSpiller(Spiller eks) {
        int id;
        Random random = new Random();
        do
            id = random.nextInt((spillere.size() - 1) + 1);
        while (spillere.get(id) == eks);

        return spillere.get(id);
    }

    public Spiller randomSpiller(Spiller eks, Spiller eks2) {
        int id;
        Random random = new Random();
        do
            id = random.nextInt((spillere.size() - 1) + 1);
        while (spillere.get(id) == eks || spillere.get(id) == eks2);

        return spillere.get(id);
    }

    public ArrayList<Spiller> besøk(Spiller vert, Spiller ekskludert) {
        ArrayList<Spiller> besøk = new ArrayList<>();
        for (Spiller s : spillere)
            if (s.offer() == vert && s != ekskludert && !s.id(Rolle.ASTRONAUT))
                besøk.add(s);
        return besøk;
    }

    public ArrayList<Spiller> spillere() {
        return spillere;
    }

    public ArrayList<Spiller> døde() {
        return sl;
    }

    public ArrayList<Spiller> levende() {
        ArrayList<Spiller> levende = new ArrayList<>();
        for (Spiller s : spillere)
            if (s.lever())
                levende.add(s);
        return levende;
    }

    public ArrayList<Spiller> levendeOgFri() {
        ArrayList<Spiller> levendeOgFri = levende();
        for (Spiller s : levende())
            if (s.fange())
                levendeOgFri.remove(s);
        return levendeOgFri;
    }

    public ArrayList<Spiller> nyligDøde() {
        return nye;
    }

    public ArrayList<Spiller> lik() {
        ArrayList<Spiller> lik = new ArrayList<>();
        for (Spiller s : sl)
            if (!s.skjult()) lik.add(s);
        return lik;
    }

    public ArrayList<Spiller> dødsannonse() {
        nye = new ArrayList<>();

        for (Spiller s : spillere) {
            if (!s.lever() && !sl.contains(s))
                nye.add(s);
        }
        for (Spiller s : nye)
            sl.add(s);
        return nye;
    }

    public ArrayList<Spiller> nominerte() {
        return nominerte;
    }

    public void nominer(Spiller s) {
        if (!nominerte.contains(s))
            nominerte.add(s);
    }

    public void avnominer(Spiller s) {
        if (nominerte.contains(s))
            nominerte.remove(s);
    }

    public Spiller nesteNominerte(Spiller nåværende) {
        int index = nominerte.indexOf(nåværende) + 1;

        if (nåværende == null) {
            if (nominerte.isEmpty())
                return new Spiller("");
            return nominerte.get(0);
        } else if (index < nominerte.size())
            return nominerte.get(index);
        else
            return new Spiller("");
    }

    public void nominerTalereOgFlyers() {
        for (Spiller spiller : levende()) {
            if (spiller.talt() || spiller.harFlyers())
                nominer(spiller);
        }
    }

    public void stem(Spiller stemmende, Spiller spiller) {
        lagreStemme(stemmende, spiller);
        if (stemmer.containsKey(spiller))
            stemmer.put(spiller, stemmer.get(spiller) + stemmende.getStemmer());
        else
            stemmer.put(spiller, stemmende.getStemmer());
    }

    public int antallStemmer(Spiller spiller) {
        if (stemmer.containsKey(spiller))
            return stemmer.get(spiller);
        else
            return 0;
    }

    public ArrayList<Spiller> hentUtstemte() {
        ArrayList<Spiller> utstemte = new ArrayList<>();

        if (stemmer.isEmpty())
            return nominerte();

        int max = Collections.max(stemmer.values());

        for (Spiller s : nominerte) {
            if (stemmer.containsKey(s) && stemmer.get(s).equals(max)) {
                utstemte.add(s);
            }
        }

        return utstemte;
    }

    public ArrayList<Spiller> hentUtstemte(Spiller ordfører) {
        ArrayList<Spiller> utstemte = hentUtstemte();

        //Fjern ordførers stemme ved uavgjort
        if (utstemte.size() > 1 && ordfører.lever() && utstemte.contains(hentSisteStemmeFra(ordfører)))
            utstemte.remove(hentSisteStemmeFra(ordfører));

        return utstemte;
    }

    public ArrayList<Spiller> hentTalere(ArrayList<Spiller> utstemte) {
        ArrayList<Spiller> talekø = new ArrayList<>();

        for (Spiller s : utstemte)
            if (!s.talt())
                talekø.add(s);

        return talekø;
    }

    public Spiller nesteTaler(Spiller s) {
        ArrayList<Spiller> talekø = hentTalere(hentUtstemte());

        int index = talekø.indexOf(s) + 1;

        if (s == null) {
            if (talekø.isEmpty())
                return new Spiller("");
            return talekø.get(0);
        } else if (index < talekø.size())
            return talekø.get(index);
        else
            return new Spiller("");
    }

    public Spiller fikkAktorDrept() {
        int flertall = levende().size() / 2;

        Spiller s = nominerte.get(0);

        if (stemmer.containsKey(s) && stemmer.get(s) > flertall)
            return s;
        else
            return null;
    }

    public void nullstillAvstemming() {
        nominerte.clear();
        stemmer.clear();
    }

    public void flykt() {
        for (Spiller s : spillere)
            if (s.rolle() instanceof Mafia) {
                s.snås(s.rolle());
            }
        ((Mafia) finnRolle(Rolle.MAFIA)).fjernSpesialist(Mafia.SJÅFØR);
    }

    public void forfalsk() {
        for (Spiller s : spillere)
            if (s.rolle() instanceof Mafia) {
                s.lyv(s.rolle());
            }
        ((Mafia) finnRolle(Rolle.MAFIA)).fjernSpesialist(Mafia.FORFALSKER);
    }

    public boolean spesialistLever(String mafia) {
        return ((Mafia) finnRolle(Rolle.MAFIA)).spesialistLever(mafia);
    }

    public void fordelGjenstander(ArrayList<String> gjenstander) {
        for (String g : gjenstander) {
            int id;
            Random random = new Random();
            do
                id = random.nextInt((spillere.size() - 1) + 1);
            while (spillere.get(id).gjenstand().equals(""));

            spillere.get(id).setGjenstand(g);
        }
    }

    public void kidnappSpiller(Spiller s) {
        if (!fanger.contains(s))
            fanger.add(s);
    }

    public void befriSpiller(Spiller s) {
        fanger.remove(s);
    }

    public void kidnappFanger() {
        Princess jon = ((Princess) finnRolle(Rolle.PRINCESS));
        if (jon == null) {
            befriFanger();
            finnRolle(Rolle.MAFIA).leggVed("\n\nFangene er befridd!");
            return;
        }

        if (jon.befridd() && jon.offer() != null)
            fanger.remove(jon.offer());

        if (jon.befridd() && harFanger()) {
            befriFanger();
            jon.leggVed("\n\nFangene er befridd!");
            return;
        }
        Iterator<Spiller> i = fanger.iterator();
        while (i.hasNext()) {
            Spiller s = i.next();

            if (!s.lever() || s.id(Rolle.PRINCESS))
                i.remove();
            else if (s.kidnappet()) {
                s.fang();
                s.rolle().funk(false);
                s.rolle().leggVed("\n\n" + s + " er kidnappet!");
            }
        }
    }

    public void befriFanger() {
        for (Spiller s : fanger) {
            s.rolle().funk(true);
            s.befri();
        }
        fanger.clear();
    }

    public boolean harFanger() {
        return !fanger.isEmpty();
    }

    public List<Spiller> hentFanger() {
        return fanger;
    }

    public boolean rolleFanget(int rolleId) {
        for (Spiller s : spillere)
            if (s.rolle().id(rolleId) && (!s.fange() && s.funker()))
                return false;
        return true;
    }

    public void bodyguarded(Spiller bg) {
        ((BodyGuard) bg.rolle()).setNektet(besøk(bg.offer(), bg));
    }

    public void registrerDilemmaValg(Spiller s) {
        dilemma.put(s, true);
    }

    public void fyllDilemma() {
        for (Spiller s : levendeOgFri())
            if (!dilemma.containsKey(s))
                dilemma.put(s, false);
        dilemma.remove(finnRolle(Rolle.JOKER));
    }

    public HashMap<Spiller, Boolean> getDilemma() {
        return dilemma;
    }

    //MORGENMETODER

    public String youtube(Spiller spiller, boolean skjult) {
        String ut = "\n\n";
        if (spiller == null || finnRolle(Rolle.YOUTUBER).blokkert())
            return "";

        if (skjult) {
            ut += "På Youtube ser vi at " + new Random().nextInt(5) + " spillere besøkte " + spiller + " i natt!";
            if (!spiller.lever())
                ut += "\nVi ser også at " + spiller + " var " + randomRolle(-1, 100, -1) + "!";
        } else {
            ut += "På Youtube ser vi at " + besøk(spiller, finnSpiller(Rolle.YOUTUBER)).size() + " spillere besøkte " + spiller + " i natt!";
            if (!spiller.lever() && !spiller.id(Rolle.BESTEMOR)) {
                ut += "\nVi ser også at " + spiller + " var " + spiller.rolle() + "!";
                if (!spiller.reddet() && !spiller.id(Rolle.ZOMBIE) && !spiller.id(Rolle.MAFIA) && !spiller.id(Rolle.POLITI))
                    spiller.rolle().aktiver(false);
            }
        }
        return ut;
    }

    public String leggVedInfo() {
        String ut = "";
        for (Spiller s : levende()) {
            if (s.spiser())
                ut += "\n\n" + s + " spiser Friduns fiskesuppe på Den Blå Fisk.";
            if (s.harFlyers())
                ut += "\n\nFlyerne påstår at " + s + " er mafia!";
            if (s.forsvart())
                ut += "\n\nForsvareren har tatt på seg " + s + " sin sak.";
            if (s.erDynket() && s.lever())
                ut += "\n\nDet lukter bensin av " + s + "...";
        }

        return ut;
    }

    public void snylt(Spiller snylter) {
        if (!snylter.funker()) return;
        Spiller spiller = snylter.offer();
        Rolle r = snylter.rolle();

        if (spiller.rolle().blokkert()) return;

        if (!spiller.lever()) snylter.rolle().drep();
        if (spiller.beskyttet()) snylter.beskytt(r);
        if (spiller.snåset()) snylter.snås(r);
        if (spiller.reddet()) snylter.redd(r);
        if (spiller.løgn()) snylter.lyv(r);
        if (spiller.skjult()) snylter.skjul(r);
        if (spiller.kløna()) snylter.kløn(r);
        if (spiller.skalKlones()) snylter.klon(finnRolle(Rolle.SMITH));
        if (spiller.kidnappet()) snylter.kidnapp(finnRolle(Rolle.PRINCESS));
        if (spiller.harFlyers()) snylter.trykkOppFlyers(r);
        if (spiller.spiser()) snylter.inviterPåSuppe(r);
        if (spiller.erDynket()) snylter.dynk(r);
        if (spiller.forsvart()) snylter.forsvar(r);
    }

    public void cupider(Cupid cupid) {
        Spiller mann = cupid.getMann(), kvinne = cupid.getKvinne();
        if (kvinne == null || mann == null)
            return;

        if (kvinne.rolle().blokkert()) ; //HMMMMMMM
        if (!kvinne.lever() && !(kvinne.id(Rolle.QUISLING) && kvinne.drapsmann().id(Rolle.MAFIA)))
            mann.drep(finnRolle(Rolle.CUPID));
        if (kvinne.beskyttet()) mann.beskytt(kvinne.beskytter());
        if (kvinne.snåset()) mann.snås(kvinne.snåsa());
        if (kvinne.reddet()) mann.redd(kvinne.redning());
        if (kvinne.løgn()) mann.lyv(kvinne.løgner());
        if (kvinne.skjult()) mann.skjul(kvinne.skjuler());
        if (kvinne.kløna()) mann.kløn(kvinne.kløne());
        if (kvinne.skalKlones()) mann.klon(finnRolle(Rolle.SMITH));
        if (kvinne.kidnappet()) mann.kidnapp(finnRolle(Rolle.PRINCESS));
        if (kvinne.harFlyers()) mann.trykkOppFlyers(cupid);
        if (kvinne.spiser()) mann.inviterPåSuppe(cupid);
        if (kvinne.erDynket()) mann.dynk(cupid);
        if (kvinne.forsvart()) mann.forsvar(cupid);


        if (mann.rolle().blokkert()) ;//HMMMMMMM
        if (!mann.lever() && !(mann.id(Rolle.QUISLING) && mann.drapsmann().id(Rolle.MAFIA)))
            kvinne.drep(finnRolle(Rolle.CUPID));
        if (mann.beskyttet()) kvinne.beskytt(kvinne.beskytter());
        if (mann.snåset()) kvinne.snås(kvinne.snåsa());
        if (mann.reddet()) kvinne.redd(kvinne.redning());
        if (mann.løgn()) kvinne.lyv(kvinne.løgner());
        if (mann.skjult()) kvinne.skjul(kvinne.skjuler());
        if (mann.kløna()) kvinne.kløn(kvinne.kløne());
        if (mann.skalKlones()) kvinne.klon(finnRolle(Rolle.SMITH));
        if (mann.kidnappet()) kvinne.kidnapp(finnRolle(Rolle.PRINCESS));
        if (mann.harFlyers()) kvinne.trykkOppFlyers(cupid);
        if (mann.spiser()) kvinne.inviterPåSuppe(cupid);
        if (mann.erDynket()) kvinne.dynk(cupid);
        if (mann.forsvart()) kvinne.forsvar(cupid);

        cupid.nullstill();
    }

    public void svik(Quisling quisling) {
        if (!quisling.konvertert()) return;

        Spiller s = quisling.spiller();
        s.setRolle(finnRolle(Rolle.MAFIA));
        s.vekk();
        ((Mafia) s.rolle()).fler();
    }

    //STRING-METODER
    public String valg(Rolle r) {
        if (r.id(Rolle.JØRGEN)) return "";

        String ut = "\n\n";
        for (Spiller s : spillere)
            if (s.funker() && r.forbud() != s)
                ut += s + "\n";
        return ut;

    }

    public String visAvstemming() {
        String output = "Det er klart for avstemming!\n\nDe mistenkte er:";
        for (Spiller s : nominerte)
            output += "\n" + s.navn();
        return output;
    }

    public String jørgensListe() {
        String ut = "Jørgens notater:";

        if (finnSpiller(Rolle.JØRGEN) == null)
            return ut;

        for (Spiller s : spillere)
            if (!s.funker())
                if (!finnSpiller(Rolle.JØRGEN).skjult())
                    ut += "\n" + s + " var " + s.rolle();
                else
                    ut += "\n" + s + " var " + randomSpiller(finnSpiller(Rolle.JØRGEN)).rolle();
        return ut;
    }

    public String rex(Spiller spiller) {
        String ut = "";
        int teller = 0;
        for (Spiller s : spillere)
            if (s.offer() == spiller && !s.id(Rolle.REX) && s.funker()) {
                ut += "\n" + s.navn();
                teller++;
            }

        if (finnSpiller(Rolle.REX).skjult()) {
            ut = "";
            if (teller > 0)
                while (teller > 0) {
                    ut += "\n" + randomSpiller(finnSpiller(Rolle.REX));
                    teller--;
                }
            else
                ut += "\n" + randomSpiller(finnSpiller(Rolle.REX));
            return ut;
        } else if (teller > 0)
            return ut;
        else
            return "\n" + "Ingen";
    }

    public String tyster(Spiller tyster, boolean skjult) {
        String ut = "Tysteren stemte med: ";

        if (!skjult)
            for (Rolle rolle : hentStemmerPå(hentSisteStemmeFra(tyster)))
                ut += "\n" + rolle;
        else
            for (Rolle rolle : hentStemmerPå(hentSisteStemmeFra(tyster)))
                ut += "\n" + randomRolle(0, Rolle.ANARKIST, Rolle.TYSTER);

        return ut;
    }

    public String mafiaNavn() {
        String ut = "Mafiaene er:";
        for (Spiller s : spillere)
            if (s.rolle() instanceof Mafia) ut += "\n" + s;
        return ut;
    }

    public int antallMafia() {
        int antall = 0;
        for (Spiller s : spillere)
            if (s.side() < Rolle.NØYTRAL) antall++;
        return antall;
    }

    public void utførDelayFunksjoner() {
        for (Spiller offer : delays.keySet()) {
            delays.get(offer).delay(offer);
            System.out.println("Delay: " + delays.get(offer) + " utføres på " + offer);
        }
        nullstillDelays();
    }

    public void leggInnDelay(Spiller offer, Rolle rolle) {
        delays.put(offer, rolle);
    }

    public void fjernDelay(Spiller offer, Rolle rolle) {
        delays.remove(offer, rolle);
    }

    public void nullstillDelays() {
        delays.clear();
    }

    public String drøm(Spiller drømmer) {
        String ut = drømmer.forsinket() ? "Drømmeren ser disse forrige natt:" : "Drømmeren drømte om disse i drømmen sin:";
        Boolean mafia = false;
        int teller = 0;

        @SuppressWarnings("unchecked")
        ArrayList<Spiller> list = (ArrayList<Spiller>) spillere.clone();
        Collections.shuffle(list);

        if (drømmer.skjult()) {
            for (Spiller s : list)
                if (s.lever() && !s.id(Rolle.DRØMMER) && teller < 3) {
                    ut += "\n" + s;
                    teller++;
                }
        } else {
            for (Spiller s : list)
                if (s.lever() && !s.id(Rolle.DRØMMER)) {
                    if (s.side() < 0 && !mafia) {
                        mafia = true;
                        ut += "\n" + s;
                    } else if (s.side() >= 0 && teller < 2) {
                        teller++;
                        ut += "\n" + s;
                    }
                }
        }

        if (teller == 2 && mafia)
            return ut;
        else
            return "Drømmeren får ikke sove...";

    }

    public String rolleString(Rolle[] roller, int antall) {
        String rolleString = spillere.size() + " Spillere\n\n" +
                (antall < 0 ? "Ekstra roller: " + (antall * -1) : "Gjenstående roller: " + antall) +
                "\n----------------\n" +
                "Mafia x" + roller[Rolle.MAFIA].antall();

        if (roller[Rolle.POLITI] != null) rolleString += "\nPoliti x" + roller[Rolle.POLITI].antall();
        if (roller[Rolle.BESTEVENN] != null)
            rolleString += "\nBestevenn x" + roller[Rolle.BESTEVENN].antall();

        for (Rolle r : roller) {
            if (r != null && !(r instanceof Mafia || r instanceof Politi || r instanceof Bestevenn))
                rolleString += "\n" + r;
        }
        return rolleString;
    }

    public String visRoller(Rolle[] roller) {
        String rolleString = "Roller: \nMafia x" + roller[Rolle.MAFIA].antall();
        if (roller[Rolle.POLITI] != null) rolleString += "\nPoliti x" + roller[Rolle.POLITI].antall();
        if (roller[Rolle.BESTEVENN] != null)
            rolleString += "\nBestevenn x" + roller[Rolle.BESTEVENN].antall();

        for (Rolle r : roller) {
            if (r != null && !(r instanceof Mafia || r instanceof Politi || r instanceof Bestevenn))
                rolleString += "\n" + r;
        }
        return rolleString;
    }

    public String toString() {
        String ut = "Spillere: " + spillere.size();
        for (Spiller s : spillere)
            ut += "\n" + s;
        return ut;
    }

    public String hvemErHva() {
        String ut = "";
        for (Spiller s : spillere)
            if (s.rolle() != null)
                ut += s + " er " + s.rolle() + (s.getMafiarolle().isEmpty() ? "\n" : "(" + s.getMafiarolle() + ")\n");
        for (Rolle r : tommeRoller)
            ut += "Ingen er " + r + "\n";
        return ut;
    }
}
