package personer.roller;

import personer.Rolle;
import personer.Spiller;

public class Morder extends Rolle {
	
	public Morder(){
		super("Morder");
		oppgave = "Hvem vil Morderen myrde?";
        veiledning = "Morder:\n" +
                "Morderen velger hver natt en person å drepe.\n" +
                "Når morderen har valgt, trykker du på vedkommendes navn for å drepe dem.\n" +
                "Hvis morderen velger samme offer som mafiaen, blir det en shootout, og morderen dør selv";
        guide = "Morderen er på mange måter en ensom mafia. Som navnet tilsier er han altså en morder, som dreper folk. " +
                "Han våkner hver natt og velger et offer, og akkurat som med mafiaen, dør vedkommende hvis ikke noen blokkerer Morderen eller redder offeret. " +
                "Han kan drepe mafiaer ved å besøke dem én og én, men han blir drept av mafiaen hvis han velger samme offer som dem. " +
                "Ved at de begge møtes ved samme åsted, oppstår det en shootout, som mafiaen alltid vinner, grunnet bedre organisering.";
		side = NØYTRAL;
		prioritet = MORDER;
	}

	@Override
	public boolean evne(Spiller spiller) {
		if(blokkert)
			return false;
		
		if(finnRolle(MAFIA).offer() == spiller && !finnRolle(MAFIA).blokkert() && !snill)
			this.spiller().drep(this);
		else if(snill)
			spiller.snipe(this);
		else
			spiller.drep(this);
		return true;
	}
	
	@Override
	public String rapport(){
		String ut = tittel + "(" + spiller.navn() + ")";
		if(offer != null) ut += " har valgt " + offer + "(" + offer.rolle() + ")";
		if(snill) ut += " med hjelp fra nissen";
		if(blokkert) ut += ", men ble blokkert av " + blokk + ".";
		else if(finnRolle(MAFIA).offer() == offer) ut+= ", og møter på Mafiaen!";
		return ut;
	}
}
