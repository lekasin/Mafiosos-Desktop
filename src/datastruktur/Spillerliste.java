package datastruktur;

import gui.Spill;
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
    HashMap<Spiller, Integer> stemmer = new HashMap<Spiller, Integer>();
    ArrayList<HashMap<Integer, Spiller>> pekeHistorikk = new ArrayList<>();
    ArrayList<HashMap<Spiller, Spiller>> stemmeHistorikk = new ArrayList<>();

    Spiller forsinkelse, forsinket;

    public Spillerliste() {
        spillere = new ArrayList<>();
    }

    public String nySpiller(String n, Rolle r) {
        leggTil(new Spiller(n, r));
        return n;
    }

    public void leggTil(Spiller s) {
        if (finnSpiller(s.navn()) == null)
            spillere.add(s);
    }

    public void fjern(String s) {
        spillere.remove(finnSpiller(s));
    }

    public void våknOpp() {
        if (!fanger.isEmpty())
            kidnappFanger();
        for (Spiller s : spillere) {
            if (!s.lever() && !s.id(Rolle.ZOMBIE))
                s.stopp();
            if (s.id(Rolle.BELIEBER)) s.rolle().lever();
            if (s.id(Rolle.SOFA)) s.rolle().lever();
            if (s.skalKlones())
                s.klon();
        }
    }

    public void sov() {
        for (Spiller s : spillere) {
            s.setOffer(null);
            if (s.lever() || s.id(Rolle.ZOMBIE))
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
        if (stemmeHistorikk.size() < 2)
            return null;

        HashMap stemmeDag = stemmeHistorikk.get(stemmeHistorikk.size()-2);
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

        if (stemmeHistorikk.size() < 2)
            return null;

        stemmeDag = stemmeHistorikk.get(stemmeHistorikk.size() - 2);

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
        for (Spiller s : spillere) {
            s.sov();
            s.vekk();
            s.rensAlle();
        }
        sl.clear();
    }

    public void slettRoller() {
        for (Spiller s : spillere)
            s.setRolle(null);
    }

    public int vinner() {
        int slemme = 0;
        int snille = 0;
        int anarki = 0;
        int smiths = 0;
        int fanger = 0;

        for (Spiller s : spillere) {
            if (s.lever()) {
                if (s.id(Rolle.ANARKIST)) anarki++;
                if (s.id(Rolle.SMITH)) smiths++;
                if (this.fanger.contains(s)) fanger++;
                if (s.side() < Rolle.NØYTRAL) slemme++;
                else snille++;
            }
        }
        if (snille + slemme < 1)
            return 2;
        else if (snille < 1)
            return -1;
        else if (slemme < 1) {
            if (smiths == snille)
                return 4;
            if (anarki > 0)
                return 3;
            else
                return 1;
        } else if (fanger == snille + slemme - 1 && finnSpillerSomEr(Rolle.PRINCESS).lever())
            return 5;
        else
            return 0;
    }

    public int length() {
        return spillere.size();
    }

    public Spiller finnSpiller(String n) {
        for (Spiller s : spillere)
            if (s.navn().equals(n)) return s;
        return null;
    }

    public Spiller finnSpillerSomEr(int id) {
        for (Spiller s : spillere)
            if (s.id(id)) return s;
        return null;
    }

    public Rolle finnRolle(int id) {
        for (Spiller s : spillere)
            if (s.id(id)) return s.rolle();
        return null;
    }

    public Rolle randomRolle(int nedre, int øvre, int eks) {
        int id = -1;
        Random random = new Random();
        while (finnSpillerSomEr(id) == null || id == eks || !finnRolle(id).fortsetter())
            id = random.nextInt((øvre - nedre) + 1) + nedre;
        return finnRolle(id);
    }

    public Rolle tylersRolle() {
        int id = -1;
        Random random = new Random();
        while (finnSpillerSomEr(id) == null || id == Rolle.TYLER || !finnRolle(id).fortsetter() || id == Rolle.CUPID || id == Rolle.KIRSTEN || id == Rolle.COPYCAT || id == Rolle.BERIT)
            id = random.nextInt((Rolle.MARIUS - Rolle.UNDERCOVER) + 1) + Rolle.UNDERCOVER;
        return finnRolle(id);
    }

    public Spiller randomSpiller(Spiller eks) {
        int id = -1;
        Random random = new Random();
        do
            id = random.nextInt((spillere.size() - 1 - 0) + 1);
        while (spillere.get(id) == eks);

        return spillere.get(id);
    }

    public Spiller randomSpiller(Spiller eks, Spiller eks2) {
        int id = -1;
        Random random = new Random();
        do
            id = random.nextInt((spillere.size() - 1 - 0) + 1);
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
        ArrayList<Spiller> levende = new ArrayList<Spiller>();
        for (Spiller s : spillere)
            if (s.lever())
                levende.add(s);
        return levende;
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
            stemmer.put(spiller, stemmer.get(spiller) + 1);
        else
            stemmer.put(spiller, 1);
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
                s.forsvar(s.rolle());
            }
    }

    public void forfalsk() {
        for (Spiller s : spillere)
            if (s.rolle() instanceof Mafia) {
                s.lyv(s.rolle());
            }
    }

    public boolean mafiaRolleLever(int mafia) {
        for (Spiller s : spillere) {
            if (s.getMafiarolle() == mafia)
                return s.lever();
        }
        return false;
    }

    public void fordelGjenstander(ArrayList<String> gjenstander) {
        for (String g : gjenstander) {
            int id = -1;
            Random random = new Random();
            do
                id = random.nextInt((spillere.size() - 1 - 0) + 1);
            while (spillere.get(id).gjenstand() == "");

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

    public boolean rolleFanget(int rolleId) {
        for (Spiller s : spillere)
            if (s.rolle().id(rolleId) && (!s.fange() && s.funker()))
                return false;
        return true;
    }

    public void bodyguarded(Spiller bg) {
        ((BodyGuard) bg.rolle()).setNektet(besøk(bg.offer(), bg));
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
        }
        else {
            ut += "På Youtube ser vi at " + besøk(spiller, finnSpillerSomEr(Rolle.YOUTUBER)).size() + " spillere besøkte " + spiller + " i natt!";
            if (!spiller.lever() && !spiller.id(Rolle.BESTEMOR)) {
                ut += "\nVi ser også at " + spiller + " var " + spiller.rolle() + "!";
                if (!spiller.id(Rolle.ZOMBIE) && !spiller.id(Rolle.MAFIA) && !spiller.id(Rolle.POLITI))
                    spiller.rolle().aktiver(false);
            }
        }
        return ut;
    }

    public void snylt(Spiller snylter) {
        if (!snylter.funker()) return;
        Spiller spiller = snylter.offer();

        if (spiller.rolle().blokkert()) return;

        if (!spiller.lever())       snylter.rolle().drep();
        if (spiller.beskyttet())    snylter.beskytt(spiller.beskytter());
        if (spiller.forsvart())     snylter.forsvar(spiller.forsvarer());
        if (spiller.reddet())       snylter.redd(spiller.redning());
        if (spiller.løgn())         snylter.lyv(spiller.løgner());
        if (spiller.skjult())       snylter.skjul(spiller.skjuler());
        if (spiller.kløna())        snylter.kløn(spiller.kløne());
        if (spiller.skalKlones())   snylter.klon(finnRolle(Rolle.SMITH));
        if (spiller.kidnappet())    snylter.kidnapp(finnRolle(Rolle.PRINCESS));
        if (spiller.harFlyers())    snylter.trykkOppFlyers();
        if (spiller.spiser())       snylter.inviterPåSuppe();

    }

    public void cupider(Cupid cupid) {
        Spiller mann = cupid.getMann(), kvinne = cupid.getKvinne();
        if (kvinne == null || mann == null)
            return;

        if (kvinne.rolle().blokkert()) ; //HMMMMMMM
        if (!kvinne.lever() && !(kvinne.id(Rolle.QUISLING) && kvinne.drapsmann().id(Rolle.MAFIA)))
                                mann.drep(finnRolle(Rolle.CUPID));
        if (kvinne.beskyttet()) mann.beskytt(kvinne.beskytter());
        if (kvinne.forsvart())  mann.forsvar(kvinne.forsvarer());
        if (kvinne.reddet())    mann.redd(kvinne.redning());
        if (kvinne.løgn())      mann.lyv(kvinne.løgner());
        if (kvinne.skjult())    mann.skjul(kvinne.skjuler());
        if (kvinne.kløna())     mann.kløn(kvinne.kløne());
        if (kvinne.skalKlones())mann.klon(finnRolle(Rolle.SMITH));
        if (kvinne.kidnappet()) mann.kidnapp(finnRolle(Rolle.PRINCESS));
        if (kvinne.harFlyers()) mann.trykkOppFlyers();
        if (kvinne.spiser())    mann.inviterPåSuppe();


        if (mann.rolle().blokkert()) ;//HMMMMMMM
        if (!mann.lever() && !(mann.id(Rolle.QUISLING) && mann.drapsmann().id(Rolle.MAFIA)))
                                kvinne.drep(finnRolle(Rolle.CUPID));
        if (mann.beskyttet())   kvinne.beskytt(kvinne.beskytter());
        if (mann.forsvart())    kvinne.forsvar(kvinne.forsvarer());
        if (mann.reddet())      kvinne.redd(kvinne.redning());
        if (mann.løgn())        kvinne.lyv(kvinne.løgner());
        if (mann.skjult())      kvinne.skjul(kvinne.skjuler());
        if (mann.kløna())       kvinne.kløn(kvinne.kløne());
        if (mann.skalKlones())  kvinne.klon(finnRolle(Rolle.SMITH));
        if (mann.kidnappet())   kvinne.kidnapp(finnRolle(Rolle.PRINCESS));
        if (mann.harFlyers())   kvinne.trykkOppFlyers();
        if (mann.spiser())      kvinne.inviterPåSuppe();

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

    public String visAvstemning() {
        String output = "Det er klart for avstemning!\n\nDe mistenkte er:";
        for (Spiller s : nominerte)
            output += "\n" + s.navn();
        return output;
    }

    public String jørgensListe() {
        String ut = "Jørgens notater:";

        if (finnSpillerSomEr(Rolle.JØRGEN) == null)
            return ut;

        for (Spiller s : spillere)
            if (!s.funker())
                if (!finnSpillerSomEr(Rolle.JØRGEN).skjult())
                    ut += "\n" + s + " var " + s.rolle();
                else
                    ut += "\n" + s + " var " + randomSpiller(finnSpillerSomEr(Rolle.JØRGEN)).rolle();
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

        if (finnSpillerSomEr(Rolle.REX).skjult()) {
            ut = "";
            if (teller > 0)
                while (teller > 0) {
                    ut += "\n" + randomSpiller(finnSpillerSomEr(Rolle.REX));
                    teller--;
                }
            else
                ut += "\n" + randomSpiller(finnSpillerSomEr(Rolle.REX));
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

    public String drøm(Spiller drømmer) {
        String ut = (drømmer.forsinket()) ? "Drømmeren drømte om disse forrige natt:" : "Drømmeren ser disse i drømmen sin:";
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
                "Gjenstående roller: " + antall + "\nMafia x" + ((Mafia) roller[Rolle.MAFIA]).antall();
        if (roller[Rolle.POLITI] != null) rolleString += "\nPoliti x" + ((Politi) roller[Rolle.POLITI]).antall();
        if (roller[Rolle.BESTEVENN] != null)
            rolleString += "\nBestevenn x" + ((Bestevenn) roller[Rolle.BESTEVENN]).antall();

        for (Rolle r : roller) {
            if (r != null && !(r instanceof Mafia || r instanceof Politi || r instanceof Bestevenn))
                rolleString += "\n" + r;
        }
        return rolleString;
    }

    public String visRoller(Rolle[] roller) {
        String rolleString = "Roller: \nMafia x" + ((Mafia) roller[Rolle.MAFIA]).antall();
        if (roller[Rolle.POLITI] != null) rolleString += "\nPoliti x" + ((Politi) roller[Rolle.POLITI]).antall();
        if (roller[Rolle.BESTEVENN] != null)
            rolleString += "\nBestevenn x" + ((Bestevenn) roller[Rolle.BESTEVENN]).antall();

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
            if (s.rolle() != null) ut += s + " er " + s.rolle() + "\n";
        return ut;
    }

}
