
package personer;

import gui.Spill;
import personer.roller.Bestemor;
import personer.roller.Mafia;
import personer.roller.Smith;

public class Spiller {

	String navn, gjenstand;
	Rolle rolle;
	Spiller offer;
	Rolle beskytter, snåsa, redning, løgner, skjuler, kløne, drapsmann, smith, forsinkelse, supperpartner, grafiker, postmann, forsvarer, dynker;
	int stemmer = 1;
	String mafiarolle = "";
	boolean lever = true, funker = true, død = false, beskyttet = false, snåset = false, minelagt = false, forsinket = false,
			reddet = false, skjult = false, løgn = false, kløna = false, nyligKlonet = false, skalKlones = false, fange = false, kidnappet = false,
			talt = false, spiser = false, flyers = false, post = false, forsvart = false, dynket = false;

	public Spiller(String navn) {
		this.navn = navn;
		this.lever = true;
	}

	public Spiller(String navn, Rolle rolle) {
		this.navn = navn;
		this.rolle = rolle;
		this.lever = true;
	}

	public boolean pek(Spiller spiller){
		return rolle.pek(spiller);
	}

	public void drep(Rolle r){
		drapsmann = r;
		if(beskyttet){
			if(beskytter.id(Rolle.JESUS) && !id(Rolle.JESUS)){
				beskytter.spiller().snipe(r);
				beskyttet = false;
				beskytter = null;
			}
			return;
		}

		lever = false;
		rolle.drep();
	}

	public void snipe(Rolle r){
		lever = false;
		død = true;
		rolle.drep();
	}

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setLiv(boolean lever) {
		this.lever = lever;
	}

    public void setStemmer(int stemmer){
        this.stemmer = stemmer;
    }

    public int getStemmer() {
        return stemmer;
    }

    public void ekstraStemme(){
        stemmer++;
    }

    public void minkStemmer(){
        stemmer--;
    }

    public void fjernStemmer(){
        stemmer = 0;
    }

	public void setOffer(Spiller spiller) {
		offer = spiller;
	}

	public void tal(){
		talt = true;
	}
	
	public void henrett(){
		if(snåset){
			return;
		}

		lever = false;
		stopp();
		rolle.drep();
	}

	public void sov(){
		talt = false;
        snåset = false;
        snåsa = null;
        forsvart = false;
        forsvarer = null;
		beskyttet = false;
		beskytter = null;
		reddet = false;
		redning = null;
		if (!id(Rolle.TYSTER)) skjult = false;
		skjuler = null;
		løgn = false;
		løgner = null;
		kløna = false;
		kløne = null;
		forsinket = false;
		forsinkelse = null;
		kidnappet = false;
		drapsmann = null;
        spiser = false;
        supperpartner = null;
        flyers = false;
        grafiker = null;
        post = false;
        postmann = null;
        dynket = false;
        dynker = null;
		rolle.sov();
        if (skalKlones())
            klon();
    }

    public void våknOpp() {
        if (!lever() && !id(Rolle.ZOMBIE))
            stopp();
        if (id(Rolle.BELIEBER)) rolle().lever();
        if (id(Rolle.SOFA)) rolle().lever();
        minelagt = false;
    }

	public void rens(Rolle r){
        if (r.id(Rolle.MAFIA)) {
            minelagt = false;
        }
		if(beskytter == r){
			beskytter = null;
			beskyttet = false;
		}
        if(snåsa == r){
            snåsa = null;
            snåset = false;
        }
        if(forsvarer == r){
            forsvarer = null;
            forsvart = false;
        }
		if(redning == r) {
			redning = null;
			reddet = false;
		}
		if(skjuler == r) {
			skjuler = null;
			skjult = false;
		}
		if(løgner == r) {
			løgner = null;
			løgn = false;
		}
		if(kløne == r) {
			kløne = null;
			kløna = false;
		}
		if(forsinkelse == r) {
			forsinkelse = null;
			forsinket = false;
		}
        if (supperpartner == r){
            supperpartner = null;
            spiser = false;
        }
        if (grafiker == r){
            grafiker = null;
            flyers = false;
        }
        if (postmann == r) {
            postmann = null;
            post = false;
        }
        if (dynker == r) {
            dynker = null;
            dynket = false;
        }
        if(drapsmann == r) {
			drapsmann = null;
		}
		if(r.id(Rolle.MAFIA) && !r.snill() && !død && !(r.id(Rolle.MAFIA) && id(Rolle.POLITI) && r.blokk == rolle)){
			vekk();
		}
		rolle.rens(r);
	}

	public void rensAlle(){
		beskytter = null;
		beskyttet = false;
		snåsa = null;
		snåset = false;
        forsvarer = null;
        forsvart = false;
		redning = null;
		reddet = false;
		skjuler = null;
		skjult = false;
		løgner = null;
		løgn = false;
		kløne = null;
		kløna = false;
		forsinkelse = null;
		forsinket = false;
        spiser = false;
        supperpartner = null;
        flyers = false;
        grafiker = null;
        post = false;
        postmann = null;
        minelagt = false;
	}

	public void stopp(){
		lever = false;
		funker = false;
		setNyligKlonet(false);
		rolle.funk(false);
	}

	public void vekk(){
		lever = true;
		funker = true;
		rolle.vekk();
	}

	public void beskytt(Rolle r){
		if(id(Rolle.BESTEMOR)) return;
		beskytter = r;
		beskyttet = true;
		if(!lever && !død) 
			vekk();
	}

    public void inviterPåSuppe(Rolle r){
        supperpartner = r;
		spiser = true;
    }

    public void giPost(Rolle r) {
        postmann = r;
        post = true;
    }

    public void trykkOppFlyers(Rolle r){
        grafiker = r;
        flyers = true;
    }

    public void snås(Rolle r){
		if(id(Rolle.BESTEMOR)) return;
		snåsa = r;
		snåset = true;
	}

    public void forsvar(Rolle r){
        forsvarer = r;
        forsvart = true;
        snås(r);
    }

	public void redd(Rolle r) {
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		redning = r;
		reddet = true;
	}

	public void lyv(Rolle r){
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		løgn = true;
		løgner = r;
	}

	public void skjul(Rolle r){
		if(id(Rolle.BESTEMOR)  || id(Rolle.ILLUSJONIST)) return;
		skjuler = r;
		skjult = true;
	}

	public void kløn(Rolle r) {
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		kløne = r;
		kløna = true;
	}

    public void dynk(Rolle r) {
        dynker = r;
        dynket = true;
    }

    public void forsink(Rolle r) {
		if(id(Rolle.BESTEMOR) || id(Rolle.ILLUSJONIST)) return;
		blokker(r);
		forsinkelse = r;
		rolle.forsinkelse = r;
		forsinket = true;
        if (offer != null)
            Spill.spillere.leggInnDelay(offer, rolle);
	}

    public void minelegg() {
        if (id(Rolle.MAFIA))
            minelagt = true;
    }

    public void setNyligKlonet(boolean klonet){
        nyligKlonet = klonet;
    }

    public void setSkalKlones(boolean klones){
        skalKlones = klones;
    }

    public boolean skalKlones(){
        return skalKlones;
    }

	public void klon(Rolle r) {
        if(id(Rolle.MAFIA) || (beskyttet() && !(id(Rolle.ILLUSJONIST) && rolle().offer().id(Rolle.SMITH))))
            return;

		smith = r;
        setSkalKlones(true);
        setNyligKlonet(true);
	}

    public void avbrytKloning(){
        smith = null;
        setSkalKlones(false);
        setNyligKlonet(false);
    }

	public void klon() {
        setSkalKlones(false);
		if(id(Rolle.SMITH) || ((beskyttet && beskytter.pri() < Rolle.SMITH) && !(id(Rolle.ILLUSJONIST) && rolle.offer.id(Rolle.SMITH)) || (this.rolle.id(Rolle.BESTEMOR) && ((Bestemor)this.rolle).flereBesøk())))
			return;
		rolle.drep();
		rolle.funk(false);
		((Smith)finnRolle(Rolle.SMITH)).klon(this);
    }
	
	public void kidnapp(Rolle r) {
		kidnappet = true;
        Spill.spillere.kidnappSpiller(this);
	}
	
	public void fang() {
		fange = true;
	}
	
	public void befri() {
		fange = false;
	}

	public void blokker(Rolle blokk){
		rolle.blokker(blokk);
	}

	public void setRolle(Rolle rolle){
		this.rolle = rolle;
		if(rolle != null)
			rolle.setSpiller(this);
	}

    public void setRolle(Mafia mafia, String spesialist) {
        setRolle(mafia);
        setMafiaRolle(spesialist);
        if (!spesialist.isEmpty())
            mafia.setSpesialist(spesialist, this);
    }

	public void setGjenstand(String gjenstand){
		this.gjenstand = gjenstand;
	}

	//Get-metoder
	public String navn(){
		return navn;
	}

	public String tittel(){
		return rolle.tittel();
	}

	public boolean id(int i){
		return rolle.id(i);
	}

	public boolean lever(){
		return lever;
	}

	public boolean funker(){
		return funker;
	}
	
	public boolean talt(){
		return talt;
	}

	public boolean beskyttet(){
		return beskyttet;
	}

	public boolean snåset(){
		if(snåsa != null && snåsa.id(Rolle.JESUS) && !snåsa.lever()) snåset = false;
		return snåset;
	}

    public boolean forsvart(){
        return forsvart;
    }

	public boolean reddet() {
		return reddet;
	}

    public boolean spiser() {
        return spiser;
    }

    public boolean harFlyers() {
        return flyers;
    }

    public boolean erDynket() {
        return dynket;
    }

    public boolean harPost() {
        return post;
    }

    public boolean død() {
		return død;
	}
	
	public boolean løgn() {
		return løgn;
	}

	public boolean skjult() {
		return skjult;
	}

	public boolean kløna() {
		return kløna;
	}

    public boolean nyligKlonet(){
        return nyligKlonet;
    }

    public boolean klonet(){
        return rolle().rolleKlonet();
    }

	public boolean fange() {
		return fange;
	}

	public boolean kidnappet() {
		return kidnappet;
	}

	public boolean forsinket() {
		return forsinket;
	}

    public boolean minelagt(){
        return minelagt;
    }

	public Spiller offer(){
		return offer;
	}

	public Rolle rolle(){
		return rolle;
	}

	public Rolle beskytter() {
		return beskytter;
	}

	public Rolle snåsa() {
		return snåsa;
	}

    public Rolle forsvarer() {
        return forsvarer;
    }

	public Rolle redning() {
		return redning;
	}

	public Rolle skjuler() {
		return skjuler;
	}

	public Rolle løgner() {
		return løgner;
	}

	public Rolle kløne() {
		return kløne;
	}

	public Rolle forsinkelse() {
		return forsinkelse;
	}

	public Rolle drapsmann() {
		return drapsmann;
	}
	
	public Rolle smith() {
		return smith;
	}
	
	public int side(){
		return rolle.side;
	}

	public String gjenstand() {
		return gjenstand;
	}

	public String getMafiarolle() {
		return mafiarolle;
	}

    public void setMafiaRolle(String spesialist) {
        mafiarolle = spesialist;
    }

    public Rolle finnRolle(int id) {
		return rolle.finnRolle(id);
	}

	public String toString(){
		return navn;
	}

	public String rapport(){
		String ut = navn;
		if(offer() != null) ut += " har valgt " + offer();
		else ut += " har ikke valgt noen";
		if(rolle.blokkert) ut += ", men ble blokkert av " + rolle.blokk;
		return ut;
	}

    public void nullstill() {
        lever = true;
        funker = true;
        død = false;
        beskyttet = false;
        snåset = false;
        minelagt = false;
        forsinket = false;
        reddet = false;
        skjult = false;
        løgn = false;
        kløna = false;
        nyligKlonet = false;
        skalKlones = false;
        fange = false;
        kidnappet = false;
        talt = false;
        spiser = false;
        flyers = false;
        post = false;
        forsvart = false;
        dynket = false;

        snåsa = null;
        forsvarer = null;
        beskytter = null;
        redning = null;
        skjuler = null;
        løgner = null;
        kløne = null;
        forsinkelse = null;
        drapsmann = null;
        supperpartner = null;
        grafiker = null;
        postmann = null;
        dynker = null;
        smith = null;

        rolle = null;
        offer = null;
        gjenstand = null;
        mafiarolle = "";
        stemmer = 1;
    }
}
