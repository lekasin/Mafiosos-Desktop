package personer;

/**
 * Created by lars-erikkasin on 18.01.2016.
 */
public class FlerSpillerRolle extends Rolle {

    protected int antall = 1, levende = 1, registrert = 0;

    public FlerSpillerRolle(String navn){
        super(navn);
    }

    public boolean registrer() {
        return ++registrert == antall();
    }

    @Override
    public void reset() {
        super.reset();
        levende = antall;
        registrert = 0;
    }

    @Override
    public int antall(){
        return antall;
    }

    public void fler(){
        antall++;
        levende++;
    }

    public void fjern(){
        antall--;
        levende--;
    }

    public boolean flere(){
        return levende > 1;
    }

    @Override
    public void drep(){
        if(!flere())
            lever = false;
        levende--;
    }

    @Override
    public boolean evne(Spiller spiller) {
        return false;
    }
}
