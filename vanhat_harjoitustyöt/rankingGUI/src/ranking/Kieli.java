package ranking;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Kieli-luokka
 * 
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 8.4.2014
 */
public class Kieli {
    private int tunnusNro;
    private String nimi;

    private static int seuraavaNro = 1;

    /**
     * Palautetaan kielen ID
     * @return kielen ID
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Palauttaa merkkijonon editpanelia varten
     * @return kielen nimi merkkijonona
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
     * Kielen alustaminen
     */
    public Kieli() {
        // attribuuttien alustus riitt‰‰
    }

    /**
     * Kielen alustaminen
     * @param k alustettavan kielen nimi
     */
    public Kieli(String k) {
        this.nimi = k;
    }


    /**
     * Asetetaan testiarvot
     */
    public void asetaArvot() {
        nimi = "Suomi";
    }

    /**
     * Tulostetaan kielen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(nimi);
    }

    /**
     * Tulostetaan kielen tiedot
     * @param stream tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream stream) {
        tulosta(new PrintStream(stream));
    }

    /**
     * Antaa kielelle seuraavan ID-numeron
     * @return kielen ID
     * @example
     * <pre name="test">
     * Kieli kieli1 = new Kieli();
     * kieli1.getTunnusNro() === 0;
     * kieli1.lisaa();
     * Kieli kieli2 = new Kieli();
     * kieli2.lisaa();
     * int a = kieli1.getTunnusNro();
     * int b = kieli2.getTunnusNro();
     * a === b-1;
     * </pre>
     */
    public int lisaa() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }

    /**
     * Palauttaa kielen tiedot tiedostoon tallennettavana merkkijonona
     * @return kieli tolppaerotettuna merkkijonona
     * @example
     * <pre name="test">
     * Kieli kieli = new Kieli();
     * kieli.parse(" 2  |  Suomi");
     * kieli.toString().equals("2|Suomi") === true;
     * </pre>
     */
    @Override
    public String toString() {
        return "" + 
                getTunnusNro() + "|" +
                nimi;
    }

    /**
     * Selvitt‰‰ kielen tiedot tolppaerotetusta merkkijonosta
     * @param rivi merkkijono, josta tiedot otetaan
     * @example
     * <pre name="test">
     * Kieli kieli = new Kieli();
     * kieli.parse(" 2  |  Suomi");
     * kieli.toString().equals("2|Suomi") === true;
     * kieli.getTunnusNro() === 2;
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
        Kieli k = new Kieli();
        k.asetaArvot();
        k.tulosta(System.out);
    }

}
