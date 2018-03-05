package ranking;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Realm-luokka
 * 
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 8.4.2014
 */
public class Realm {
    private int tunnusNro;
    private String nimi;

    private static int seuraavaNro = 1;

    /**
     * Palautetaan realmin ID
     * @return realmin ID
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Palauttaa merkkijonon editpanelia varten
     * @return realmin nimi merkkijonona
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Asettaa tunnusnumeron ja tarkistaa ett‰ seuraava numero
     * on suurempi
     * @param nro asetettava tunnusnumero
     */
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }

    /**
     * Realmin alustaminen
     */
    public Realm() {
        // attribuuttien alustus riitt‰‰
    }

    /**
     * Realmin alustaminen
     * @param r realmin nimi
     */
    public Realm(String r) {
        this.nimi = r;
    }

    /**
     * Asetetaan testiarvot
     */
    public void asetaArvot() {
        nimi = "Lightning's Blade EU";
    }

    /**
     * Tulostetaan realmin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(nimi);
    }

    /**
     * Tulostetaan realmin tiedot
     * @param stream tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream stream) {
        tulosta(new PrintStream(stream));
    }

    /**
     * Antaa realmille seuraavan ID-numeron
     * @return realmin ID
     * @example
     * <pre name="test">
     * Realm lb1 = new Realm();
     * lb1.getTunnusNro() === 0;
     * lb1.lisaa();
     * Realm lb2 = new Realm();
     * lb2.lisaa();
     * int a = lb1.getTunnusNro();
     * int b = lb2.getTunnusNro();
     * a === b-1;
     * </pre>
     */
    public int lisaa() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }

    /**
     * Palauttaa realmin tiedot tiedostoon tallennettavana merkkijonona
     * @return realm tolppaerotettuna merkkijonona
     * @example
     * <pre name="test">
     * Realm realm = new Realm();
     * realm.parse(" 2  |  Lightning's Blade EU");
     * realm.toString().equals("2|Lightning's Blade EU") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return "" + 
                getTunnusNro() + "|" +
                nimi;
    }

    /**
     * Selvitt‰‰ factionin tiedot tolppaerotetusta merkkijonosta
     * @param rivi merkkijono, josta tiedot otetaan
     * @example
     * <pre name="test">
     * Realm realm = new Realm();
     * realm.parse(" 2  |  Lightning's Blade EU");
     * realm.toString().equals("2|Lightning's Blade EU") === true;
     * realm.getTunnusNro() === 2;
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = (Mjonot.erota(sb, '|', nimi));
    }

    /**
     * testip‰‰ohjelma
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Realm lb = new Realm();
        lb.asetaArvot();
        lb.tulosta(System.out);
    }

}
