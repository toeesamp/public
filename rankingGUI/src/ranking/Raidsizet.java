package ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 18.3.2014
 * @version 8.4.2014
 */
public class Raidsizet {

    private static final int RAIDSIZEJA = 2;
    private int lkm;
    private Raidsize[] alkiot = new Raidsize[RAIDSIZEJA];

    private String tiedostonPerusNimi = "raidsizet";
    private String kokoNimi = "";

    /**
     * Muodostaja
     */
    public Raidsizet() {
        // attribuuttien alustus riitt‰‰
    }

    /**
     * Palauttaa raidsizejen lukum‰‰r‰n
     * @return raidsizejen lukum‰‰r‰
     */
    public int getLkm() {
        return lkm;
    }

    /**
     * Palauttaa raidsizen koko nimen
     * @return raidsizen koko nimi merkkijonona
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
     * Lukee raidsizet tiedostosta.  
     * @throws SailoException Jos tiedostosta lukeminen ei onnistu
     */
    public void lueTiedostosta() throws SailoException {
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            String rivi = fi.readLine();
            //if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Raidsize raidsize = new Raidsize();
                raidsize.parse(rivi);
                lisaa(raidsize);
            }
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
        if (alkiot[0].getNimi() != "10 man" && alkiot[1].getNimi() != "25 man" &&
                alkiot[0].getTunnusNro() != 1 && alkiot[2].getTunnusNro() != 2) 
            throw new SailoException("Tiedosto on v‰‰r‰ss‰ muodossa");
    }


    /**
     * Tallentaa raidsizet tiedostoon.  
     * @throws SailoException Jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.length);
            for (int i = 0; i < lkm; i++) {
                fo.println(alkiot[i].toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
    }

    /**
     * Lis‰‰ uuden raidsizen
     * @param r lis‰tt‰v‰n raidsizen viite
     * @throws SailoException jos taulukko on t‰ynn‰
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Raidsizet raidsizet = new Raidsizet();
     * Raidsize eka = new Raidsize(); 
     * Raidsize toka = new Raidsize();
     * raidsizet.getLkm() === 0;
     * raidsizet.lisaa(eka); raidsizet.getLkm() === 1;
     * raidsizet.lisaa(toka); raidsizet.getLkm() === 2;
     * raidsizet.lisaa(eka); #THROWS IndexOutOfBoundsException
     * </pre>
     */
    public void lisaa(Raidsize r) throws SailoException {
        alkiot[lkm] = r;
        lkm++;
    }

    /**
     * Palauttaa viitteen raidsizeen, jonka id on i
     * @param i mink‰ raidsizen viite halutaan 
     * @return viite raidsizeen i, null jos alkiota ei lˆydy
     * @throws IndexOutOfBoundsException jos i on laiton
     */
    public Raidsize anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm < i)
            throw new IndexOutOfBoundsException("Laiton numero: " + i);
        for (int j = 0; j < alkiot.length; j++) {
            if (alkiot[j].getTunnusNro() == i) 
                return alkiot[j];
        }
        return null;
    }

    /**
     * asettaa testiarvoja
     */
    public void asetaArvot() {
        Raidsize kymppi = new Raidsize("10 man");
        alkiot[0] = kymppi;
        lkm++;
        Raidsize kaksviis = new Raidsize("25 man");
        alkiot[1] = kaksviis;
        lkm++;
    }


    /**
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {
        Raidsizet raidsizet = new Raidsizet();
        raidsizet.asetaArvot();

        System.out.println("============= Raidsizet testi =================");

        for (int i = 0; i < raidsizet.getLkm(); i++) {
            Raidsize r = raidsizet.anna(i);
            System.out.println("Faction nro: " + i);
            r.tulosta(System.out);
        }
    }

}
