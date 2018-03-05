package ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 18.3.2014
 * @version 8.4.2014
 */
public class Kielet implements Iterable<Kieli>{

    private String tiedostonPerusNimi = "kielet";
    private String kokoNimi = "";

    /** Taulukko kielist‰ */
    private final Collection<Kieli> alkiot = new ArrayList<Kieli>();


    /**
     * Kielten alustaminen
     */
    public Kielet() {
        //attribuuttien alustus riitt‰‰
    }

    /**
     * Palauttaa kielten lukum‰‰r‰n
     * @return kielten lukum‰‰r‰
     */
    public int getLkm() {
        return alkiot.size();
    }

    /**
     * Palauttaa kielen koko nimen
     * @return kielen koko nimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }

    /**
     * asettaa tiedoston nimen ilman p‰‰tett‰
     * @param nimi tiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }

    /**
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return "ranking/" + tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota k‰ytet‰‰n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }

    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return "ranking/" + tiedostonPerusNimi + ".bak";
    }

    /**
     * Lis‰‰ uuden kielen tietorakenteeseen
     * @param k lis‰tt‰v‰ kieli
     */
    public void lisaa(Kieli k) {
        alkiot.add(k);
    }

    /**
     * Asettaa testiarvot
     */
    public void asetaArvot() {
        Kieli suomi = new Kieli("Suomi");
        suomi.lisaa();
        alkiot.add(suomi);
        Kieli englanti = new Kieli("Englanti");
        englanti.lisaa();
        alkiot.add(englanti);

    }

    /**
     * Lukee kielet tiedostosta.  
     * @throws SailoException Jos tiedostosta lukeminen ei onnistu
     */
    public void lueTiedostosta() throws SailoException {
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            //if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Kieli kieli = new Kieli();
                kieli.parse(rivi);
                lisaa(kieli);
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }	
    }


    /**
     * Tallentaa kielet tiedostoon.  
     * @throws SailoException Jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.size());
            for (Kieli kieli : this) {
                fo.println(kieli.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
    }


    /**
     * Iteraattori kielien l‰pik‰ymiseen
     * @return kieli-iteraattori
     * @example
     * <pre name="test">
     * #import java.util.*;
     *  Kielet kielet = new Kielet();
     *  Kieli kieli21 = new Kieli(); kielet.lisaa(kieli21);
     *  Kieli kieli11 = new Kieli(); kielet.lisaa(kieli11);
     *  Kieli kieli22 = new Kieli(); kielet.lisaa(kieli22);
     *  Kieli kieli12 = new Kieli(); kielet.lisaa(kieli12);
     *  Kieli kieli23 = new Kieli(); kielet.lisaa(kieli23);
     * 
     *  Iterator<Kieli> i2=kielet.iterator();
     *  i2.next() === kieli21;
     *  i2.next() === kieli11;
     *  i2.next() === kieli22;
     *  i2.next() === kieli12;
     *  i2.next() === kieli23;
     *  i2.next() === kieli12;  #THROWS NoSuchElementException  
     * </pre>
     */
    @Override
    public Iterator<Kieli> iterator() {
        return alkiot.iterator();
    }

    /**
     * Palauttaa viitteen kieleen, jonka id on i
     * @param i mink‰ kielen viite halutaan 
     * @return viite kieleen i, null jos alkiota ei lˆydy
     * @throws IndexOutOfBoundsException jos i on laiton
     */
    public Kieli anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || getLkm() < i)
            throw new IndexOutOfBoundsException("Laiton numero: " + i);
        for (Kieli k : alkiot)
            if (k.getTunnusNro() == i)
                return k;
        return null;
    }

    /** Wanha (turha?)
     * Haetaan kaikki kielet 
     * @return tietorakenne jossa viitteet lˆydetteyihin kieliin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Kielet kielet = new Kielet();
     *  Kieli kieli21 = new Kieli(); kielet.lisaa(kieli21);
     *  Kieli kieli11 = new Kieli(); kielet.lisaa(kieli11);
     *  Kieli kieli22 = new Kieli(); kielet.lisaa(kieli22);
     *  Kieli kieli12 = new Kieli(); kielet.lisaa(kieli12);
     *  Kieli kieli23 = new Kieli(); kielet.lisaa(kieli23);
     *  Kieli kieli51 = new Kieli(); kielet.lisaa(kieli51);
     *  
     *  List<Kieli> loytyneet;
     *  loytyneet = kielet.annaKielet();
     *  loytyneet.size() === 6;
     *  loytyneet.get(0) == kieli21 === true;
     * </pre> 

    public List<Kieli> annaKielet() {
        List<Kieli> loydetyt = new ArrayList<Kieli>();
        for (Kieli k : alkiot)
            loydetyt.add(k);
        return loydetyt;
    }
     */

    /**
     * testip‰‰ohjelma
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Kielet kielet = new Kielet();
        Kieli kieli1 = new Kieli();
        kieli1.asetaArvot();

        kielet.lisaa(kieli1);

        System.out.println("============= Kielet testi =================");
        /*
        List<Kieli> kielet2= kielet.annaKielet();

        for (Kieli k : kielet2) {
            System.out.print(k.getTunnusNro() + " ");
            k.tulosta(System.out);
        }
         */
    }
}
