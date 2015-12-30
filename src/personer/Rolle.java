package personer;

import Utils.TvUtil;
import personer.roller.Berit;
import gui.Spill;
import personer.roller.Psykolog;

public abstract class Rolle {

	protected boolean lever = true, blokkert = false, snill = false, informert = false, aktiv = true, funker = true, fortsett = true, skjerm = false, klonet = false;
	protected String tittel, oppgave, tvOppgave, veiledning, guide;
	protected Spiller offer, spiller, forbud, forbud2;
	protected Rolle blokk, informant, forsinkelse;
	protected int prioritet = 0, side = 1;
	protected String info = "";
	public static int FAKEBORGER = -2, MAFIOSO = -1, NØYTRAL = 0, BORGER = 1,  FAKEMAFIA = 2;
	public static int
	ZOMBIE = 0,
	BELIEBER = ZOMBIE+1,
	ARVING = BELIEBER+1,
	BEDRAGER = ARVING+1,
	BESTEVENN = BEDRAGER+1,
	JESUS = BESTEVENN+1,
	INSIDER = JESUS+1,
	UNDERCOVER = INSIDER+1,
	ERIK = UNDERCOVER+1,
    TYSTER = ERIK+1,

    MAFIA = TYSTER+1,

	DIDRIK = MAFIA+1,
	INFORMANT = DIDRIK+1,
	TJUKKAS = INFORMANT+1,
	SOFA = TJUKKAS+1,
	BERIT = SOFA+1,
	NISSEN = BERIT+1,
	BODYGUARD = NISSEN+1,
	ENGEL = BODYGUARD+1,
	LEGE = ENGEL+1,
	POLITI = LEGE+1,
	PRINCESS = POLITI+1,
	HAVFRUE = PRINCESS+1,
	COPYCAT = HAVFRUE+1,
	HEISENBERG = COPYCAT+1,
	MORDER = HEISENBERG+1,
	HMS = MORDER+1,
	SNÅSA = HMS+1,
	LØGNER = SNÅSA+1,
	ULF = LØGNER+1,
	TYLER = ULF+1,
	ILLUSJONIST = TYLER+1,
	KIRSTEN = ILLUSJONIST+1,
	SNYLTER = KIRSTEN+1,
	CUPID = SNYLTER+1,
	SPECIAL = CUPID+1,

	KLØNA = SPECIAL+1,
	RAVN = KLØNA+1,
	MARIUS = RAVN+1,
	CARLSEN = MARIUS+1,
	QUISLING = CARLSEN+1,
	FILOSOF = QUISLING+1,
	HAMMER = FILOSOF+1,
	YOUTUBER = HAMMER+1,
    JØRGEN = YOUTUBER+1,
    DRØMMER = JØRGEN+1,
    BOMBER = DRØMMER+1,
	AKTOR = BOMBER+1,
	SMITH = AKTOR+1,
	SHERLOCK = SMITH+1,
	REX = SHERLOCK+1,
	OBDUK = REX+1,

	BESTEMOR = OBDUK+1,
	PSYKOLOG = BESTEMOR+1,
	ASTRONAUT = PSYKOLOG+1,
	VARA = ASTRONAUT+1,
	JENTE = VARA+1,
	BØDDEL = JENTE+1,
	TROMPET = BØDDEL+1,
	ANARKIST = TROMPET+1,
    JOKER = ANARKIST+1;

	//Konstruktører
	public Rolle()
	{
		vekk();
	}

	public Rolle(String tittel)
	{
		this.tittel = tittel;
		lever = true;
        veiledning = tittel + ":\n" +
                "Trykk på navnet til den som pekes på for å utføre rollens oppgave på vedkommende.\n" +
                "For å gå videre uten å velge noen, trykker du på fortsett.";
	}

	//Endringer
	public void drep(){
		lever = false;
	}

	public void funk(Boolean f){
		funker = f;
		if(!f) {
			if(!(id(Rolle.RAVN) || id(Rolle.MARIUS) || id(Rolle.AKTOR) || id(Rolle.PSYKOLOG)))
				offer = null;
			blokk = null;
			blokkert = false;
			informert = false;
			info = "";
		}
	}

	public void vekk(){
		lever = true;
		funker = true;
	}

	public void sov(){
		offer = null;
        tvOppgave = oppgave;
		snill = false;
		informert = false;
		informant = null;
		forsinkelse = null;
		info = "";
		if(lever()) funker = true;
		blokk = null;
		blokkert = false;
	}

	public void jul(){
		snill = true;
	}

	public void informer(Rolle r, String info){
		setInfo(info);
		informert = true;
		informant = r;
	}

	public void setInfo(String info){
		this.info += info;
	}

	public void fortsett(boolean f) {
		fortsett = f;
	}

	public void blokker(Rolle blokk){
		if(!snill) {
			if(forsinkelse != null)
				((Berit)forsinkelse).setOffer(null);
			this.blokk = blokk;
			blokkert = true;
		}
		rens();
	}

	public void rens(Rolle r){
		if(blokk == r){
			blokk = null;
			blokkert = false;
			if(offer != null)
				evne(offer());
		} else if (informant == r && !informant.id(Rolle.BELIEBER)) {
			informant = null;
			informert = false;
			info = "";
		}
	}

	public void rens(){
		forbud = null;
		if(offer != null) {
			if(id(Rolle.LEGE) && offer.id(Rolle.MAFIA)) {
				spiller.vekk();
			}
			offer.rens(this);
		}
	}

	public void klonRolle(){
		klonet = true;
	}

	public void aktiver(boolean a){
		aktiv = a;
	}

	public void setSpiller(Spiller spiller){
		this.spiller = spiller;

		switch (tittel){
		case "Tjukkas" :
		case "Belieber" :
		case "Bøddel" :
		case "Filosofen" :
		case "Informant" :
		case "Julenissen" :
		case "Snylter" :
		case "CopyCat" :
		case "Hammer" :
		case "Heisenberg" :
		case "Jesus" :
		case "Havfrue" :
		case "Ulf Omar" :
		case "Morder" :
		case "Princess98" :
		case "Agent Smith":
		case "Gærne Berit":
		case "Bodyguard":forbud2 = spiller;
						break;
		default: break;
		}
	}

	public void forby(Spiller s){
		forbud = s;
	}

	public void byttSide() {
		side = side-(side*2);
	}

	public boolean pek(Spiller spiller){
        this.spiller.setOffer(spiller);

		if(this.spiller.forsinket && blokk == this.spiller.forsinkelse)
			((Berit)this.spiller.forsinkelse).setOffer(spiller);

		if(spiller == null) return false;

		if(spiller.id(Rolle.HAVFRUE) && spiller.offer() == this.spiller) {
			if(spiller.rolle().snill())
                this.spiller.snipe(spiller.rolle());
			else
                this.spiller.drep(spiller.rolle());
		}

        if (spiller.id(Rolle.PSYKOLOG))
            ((Psykolog)spiller.rolle()).bestillTime(this.spiller);

		offer = spiller;
		evne(spiller);
		return true;
	}

	//Get-metoder
	public String tittel(){
		return tittel;
	}

	public String getVeiledning(){
		return veiledning;
	}

	public String getGuide() {
		return guide;
	}

	public boolean lever(){
		return lever;
	}

	public boolean funker(){
		return funker;
	}

	public boolean blokkert(){
		return blokkert;
	}

	public boolean informert(){
		return informert;
	}

	public boolean skjerm(){
		return skjerm;
	}

	public boolean snill() {
		return snill;
	}

    public boolean nyligKlonet(){
        return spiller != null && spiller.nyligKlonet;
    }

    public boolean rolleKlonet() {
        return klonet;
    }

	public Spiller offer(){
		return offer;
	}

	public Rolle hvemBlokk(){
		return blokk;
	}

	public int pri(){
		return prioritet;
	}

	public boolean id(int id){
		return id == prioritet;
	}

	public boolean aktiv(){
        return aktiv || nyligKlonet();
	}

	public boolean fortsetter() {
		return fortsett;
	}

	public boolean fanget() {
		return Spill.spillere.rolleFanget(prioritet);
	}

	public Spiller spiller(){
		return spiller;
	}

	public Spiller forbud(){
		return forbud;
	}

	public Spiller forbud2(){
		return forbud2;
	}

	public Rolle finnRolle(int id) {
		return Spill.spillere.finnRolle(id);
	}

	public void leggVed(String s) {
		TvUtil.leggVed(s);
	}

	//toString-metoder
	public String toString()
	{
		return tittel;
	}

	public String oppgave() {
        if (klonet || nyligKlonet() && !id(SMITH))
            kloneRapport();

		TvUtil.vis(tvOppgave);
		if(informert)
            TvUtil.leggTil(info);
		return oppgave;
	}


	public String rapport(){
		String ut = tittel + "(" + spiller.navn + ")";
		if(offer != null){
			ut += " har valgt " + offer + "(" + offer.rolle() + ")";
			if(snill) ut += ", med hjelp fra nissen";
			if(blokkert) ut += ", men ble blokkert av " + blokk;
		}
		else
			ut += " valgte ingen";
		return ut;
	}

    public void kloneRapport(){
        if(nyligKlonet()) {
            informer(spiller.smith, pri() < Rolle.SMITH ? "\n\n" + spiller + " er nå en Smith!" :
                    "\n\n" + spiller + " er i ferd med å bli en Smith! (Kan bruke rollen sin en siste gang)");
        } else if (klonet){
            info = "";
            informer(spiller.smith, "\n\n" + spiller + " er nå en Smith!");
        }
        spiller.setNyligKlonet(false);
        TvUtil.toFront();
    }

	public boolean flere(){
		return false;
	}

    public int antall() {
        return 1;
    }


	//Evner/Arv
	public abstract boolean evne(Spiller spiller);

    public void autoEvne(){}
}
