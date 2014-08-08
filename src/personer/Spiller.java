
package personer;

public class Spiller {

	String navn;
	Rolle rolle;
	Rolle beskytter, forsvarer, redning, løgner, skjuler, kløne, drapsmann;
	boolean lever = true, funker = true, død = false, beskyttet = false, forsvart = false, reddet = false, skjult = false, løgn = false, kløna = false;

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

	public void henrett(){
		if(forsvart){
			if(forsvarer.id(Rolle.JESUS) && !id(Rolle.JESUS)){
				forsvarer.spiller().snipe(null);
				forsvart = false;
				forsvarer = null;
			} 
			return;
		}

		lever = false;
		stopp();
		rolle.drep();
	}
	
	public void sov(){
		forsvart = false;
		forsvarer = null;
		beskyttet = false;
		beskytter = null;
		reddet = false;
		redning = null;
		skjult = false;
		skjuler = null;
		løgn = false;
		løgner = null;
		kløna = false;
		kløne = null;
		drapsmann = null;
		rolle.sov();
	}
	
	public void rens(Rolle r){
		if(beskytter == r){
			beskytter = null;
			beskyttet = false;
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
		if(drapsmann == r) {
			drapsmann = null;
		}
		if(r.id(Rolle.MAFIA) && !r.snill() && !(r.id(Rolle.MAFIA) && id(Rolle.POLITI))){
			vekk();
		}
		rolle.rens(r);
	}

	public void stopp(){
		lever = false;
		funker = false;
		rolle.funk(false);
	}

	public void vekk(){
		lever = true;
		funker = true;
		rolle.vekk();
	}

	public void beskytt(Rolle r){
		beskytter = r;
		beskyttet = true;
		if(!lever && !død) 
			vekk();
	}

	public void forsvar(Rolle r){
		forsvarer = r;
		forsvart = true;
	}
	
	public void redd(Rolle r) {
		redning = r;
		reddet = true;
	}
	
	public void lyv(Rolle r){
		løgn = true;
		løgner = r;
	}
	
	public void skjul(Rolle r){
		skjuler = r;
		skjult = true;
	}
	
	public void kløn(Rolle r) {
		kløne = r;
		kløna = true;
	}
	
	public void blokker(Rolle blokk){
		rolle.blokker(blokk);
	}

	public void setRolle(Rolle rolle){
		this.rolle = rolle;
		if(rolle != null)
			rolle.setSpiller(this);
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

	public boolean beskyttet(){
		return beskyttet;
	}
	
	public boolean forsvart(){
		if(forsvarer != null && forsvarer.id(Rolle.JESUS) && !forsvarer.lever()) forsvart = false;
		return forsvart;
	}
	
	public boolean reddet() {
		return reddet;
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
	
	public Spiller offer(){
		return rolle().offer;
	}

	public Rolle rolle(){
		return rolle;
	}

	public Rolle beskytter() {
		return beskytter;
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
	
	public Rolle drapsmann() {
		return drapsmann;
	}
	
	public int side(){
		return rolle.side;
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

}
