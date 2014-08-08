package personer;

import gui.TV;

public abstract class Rolle {

	protected boolean lever = true, blokkert = false, snill = false, informert = false, aktiv = true, funker = true, fortsett = true;
	protected String tittel;
	protected Spiller offer, spiller, forbud, forbud2;
	protected Rolle blokk, informant;
	protected TV tv;
	protected int prioritet = 0, side = 1;
	protected String oppgave, info = "";
	public static int FAKEBORGER = -2, MAFIOSO = -1, NØYTRAL = 0, BORGER = 1,  FAKEMAFIA = 2;
	public static int 
	ZOMBIE = 0,
	BELIEBER = ZOMBIE+1,
	ARVING = BELIEBER+1, 
	BEDRAGER = ARVING+1,
	BESTEVENN = BEDRAGER+1,
	JESUS = BESTEVENN+1,
	
	MAFIA = JESUS+1,

	DIDRIK = MAFIA+1,
	INFORMANT = DIDRIK+1,
	TJUKKAS = INFORMANT+1,
	NISSEN = TJUKKAS+1,
	ENGEL = NISSEN+1,
	LEGE = ENGEL+1,
	POLITI = LEGE+1,
	HAVFRUE = POLITI+1,
	COPYCAT = HAVFRUE+1,
	HEISENBERG = COPYCAT+1,
	MORDER = HEISENBERG+1,
	HMS = MORDER+1, 
	SNÅSA = HMS+1,	
	LØGNER = SNÅSA+1,
	KLØNA = LØGNER+1,
	ULF = KLØNA+1,
	SNYLTER = ULF+1,
	SPECIAL = SNYLTER+1,
	ILLUSJONIST = SPECIAL+1,
	TYLER = ILLUSJONIST+1,
	KIRSTEN = TYLER+1,
	CUPID = KIRSTEN+1,
	
	RAVN = CUPID+1,
	MARIUS = RAVN+1,
	QUISLING = MARIUS+1,
	FILOSOF = QUISLING+1,
	HAMMER = FILOSOF+1,
	BOMBER = HAMMER+1,
	AKTOR = BOMBER+1,
	SHERLOCK = AKTOR+1,
	REX = SHERLOCK+1,
	OBDUK = REX+1,

	JØRGEN = OBDUK+1,
	DRØMMER = JØRGEN+1,
	ASTRONAUT = DRØMMER+1,
	
	VARA = ASTRONAUT+1,
	UNDERCOVER = VARA+1,
	INSIDER = UNDERCOVER+1,
	JENTE = INSIDER+1,
	BØDDEL = JENTE+1,
	TROMPET = BØDDEL+1,
	ANARKIST = TROMPET+1;
	
	//Konstruktører
	public Rolle()
	{
		vekk();
		setTV(tv);
	}

	public Rolle(String tittel)
	{
		this.tittel = tittel;
		lever = true;
	}

	//Endringer
	public void drep(){
		lever = false;
	}

	public void funk(Boolean f){
		funker = f;
		if(!f) {
			if(!(id(Rolle.RAVN) || id(Rolle.MARIUS) || id(Rolle.AKTOR)))
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
		snill = false;
		informert = false;
		informant = null;
		info = "";
		if(lever()) funker = true;
		if(blokkert && blokk.id(RAVN)) return;
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
		this.info = info;
	}

	public void fortsett(boolean f) {
		fortsett = f;
	}
	
	public void blokker(Rolle blokk){
		if(!snill) {
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
		} else if (informant == r) {
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
		case "Morder" : forbud2 = spiller; 
						break;
		default: break;
		}
	}

	public void setTV(TV tv){
		this.tv = tv;
	}

	public void forby(Spiller s){
		forbud = s;
	}
	
	public void byttSide() {
		side = side-(side*2);
	}

	public boolean pek(Spiller spiller){
		if(spiller == null) return false;
		if(spiller.id(Rolle.HAVFRUE) && spiller.offer() == this.spiller) {
			if(spiller.rolle().snill()) this.spiller.snipe(spiller.rolle());
			else this.spiller.snipe(spiller.rolle());
		}
		offer = spiller;
		evne(spiller);
		return true;
	}

	//Get-metoder
	public String tittel(){
		return tittel;
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
	
	public boolean snill() {
		return snill;
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
		return aktiv;
	}

	public boolean fortsetter() {
		return fortsett;
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
		return tv.spillere().finnRolle(id);
	}
	
	public void leggVed(String s) {
		tv.leggVed(s);
	}
	
	//toString-metoder

	public String toString()
	{
		return tittel;
	}
	
	public String oppgave() {
		tv.vis(oppgave);
		if(informert) tv.leggtil(info);
		return oppgave;
	}


	public String rapport(){
		String ut = tittel + "(" + spiller.navn + ")";
		if(offer != null) ut += " har valgt " + offer + "(" + offer.rolle() + ")";
		if(snill) ut += ", med hjelp fra nissen";
		if(blokkert) ut += ", men ble blokkert av " + blokk + ".";
		return ut;
	}

	//Evner/Arv
	public abstract boolean evne(Spiller spiller);
}
