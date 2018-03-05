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
public class Realmit implements Iterable<Realm>{

	private String tiedostonPerusNimi = "realmit";
	private String kokoNimi = "";
	
	/** Taulukko realmeista */
	private final Collection<Realm> alkiot = new ArrayList<Realm>();
	
	/**
	 * Realmien alustaminen
	 */
	public Realmit() {
		// attribuuttien alustus riitt‰‰
	}
	
	/**
	 * Palauttaa realmien lukum‰‰r‰n
	 * @return realmien lukum‰‰r‰
	 */
	public int getLkm() {
		return alkiot.size();
	}
	
    /**
     * Palauttaa realmin koko nimen
     * @return realmin koko nimi merkkijonona
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
	 * Lis‰‰ uuden realmin tietorakenteeseen
	 * @param r lis‰tt‰v‰ realm
	 */
	public void lisaa(Realm r) {
		alkiot.add(r);
	}
	
    /**
     * Asettaa testiarvot
     */
    public void asetaArvot() {
        Realm lb = new Realm("Lightning's Blade EU");
        lb.lisaa();
        alkiot.add(lb);
    }
	
	/**
	 * Lukee realmit tiedostosta.  
	 * @throws SailoException Jos tiedostosta lukeminen ei onnistu
	 */
	public void lueTiedostosta() throws SailoException {
		 try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
	            String rivi = fi.readLine();
	            //if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

	            while ( (rivi = fi.readLine()) != null ) {
	                rivi = rivi.trim();
	                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
	                Realm realm = new Realm();
	                realm.parse(rivi);
	                lisaa(realm);
	            }
	        } catch ( FileNotFoundException e ) {
	            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
	        } catch ( IOException e ) {
	            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	        }	
	}
	
	
	/**
	 * Tallentaa realmit tiedostoon. 
	 * @throws SailoException Jos tallentaminen ei onnistu
	 */
	public void tallenna() throws SailoException {
		File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(alkiot.size());
            for (Realm realm : this) {
                fo.println(realm.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
	}
	
	
	/**
	 * Iteraattori realmien l‰pik‰ymiseen
	 * @return realm-iteraattori
	 * @example
	 * <pre name="test">
	 * #import java.util.*;
	 * 
     *  Realmit realmit = new Realmit();
     *  Realm realm21 = new Realm(); realmit.lisaa(realm21);
     *  Realm realm11 = new Realm(); realmit.lisaa(realm11);
     *  Realm realm22 = new Realm(); realmit.lisaa(realm22);
     *  Realm realm12 = new Realm(); realmit.lisaa(realm12);
     *  Realm realm23 = new Realm(); realmit.lisaa(realm23);
     * 
     *  Iterator<Realm> i2=realmit.iterator();
     *  i2.next() === realm21;
     *  i2.next() === realm11;
     *  i2.next() === realm22;
     *  i2.next() === realm12;
     *  i2.next() === realm23;
     *  i2.next() === realm12;  #THROWS NoSuchElementException  
     * </pre>
	 */
	@Override
	public Iterator<Realm> iterator() {
		return alkiot.iterator();
	}
	
    /**
     * Palauttaa viitteen realmiin, jonka id on i
     * @param i mink‰ realmin viite halutaan 
     * @return viite realmiin i, null jos alkiota ei lˆydy
     * @throws IndexOutOfBoundsException jos i on laiton
     */
    public Realm anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || getLkm() < i)
            throw new IndexOutOfBoundsException("Laiton numero: " + i);
        for (Realm r : alkiot)
            if (r.getTunnusNro() == i)
                return r;
        return null;
    }
    
    /**
     * Haetaan kaikki realmit
     * @return tietorakenne jossa viitteet lˆydetteyihin realmeihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Realmit realmit = new Realmit();
     *  Realm realm21 = new Realm(); realmit.lisaa(realm21);
     *  Realm realm11 = new Realm(); realmit.lisaa(realm11);
     *  Realm realm22 = new Realm(); realmit.lisaa(realm22);
     *  Realm realm12 = new Realm(); realmit.lisaa(realm12);
     *  Realm realm23 = new Realm(); realmit.lisaa(realm23);
     *  Realm realm51 = new Realm(); realmit.lisaa(realm51);
     *  
     *  List<Realm> loytyneet;
     *  loytyneet = realmit.annaRealmit();
     *  loytyneet.size() === 6;
     *  loytyneet.get(0) == realm21 === true;
     * </pre> 
     
    public List<Realm> annaRealmit() {
        List<Realm> loydetyt = new ArrayList<Realm>();
        for (Realm k : alkiot)
            loydetyt.add(k);
        return loydetyt;
    }
	*/
	
	/**
	 * testip‰‰ohjelma
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Realmit realmit = new Realmit();
		Realm realm1 = new Realm();
		realm1.asetaArvot();
		
		realmit.lisaa(realm1);
	
		System.out.println("============= Realmit testi =================");
		/*
        List<Realm> realmit2 = realmit.annaRealmit();

        for (Realm r : realmit2) {
            System.out.print(r.getTunnusNro() + " ");
            r.tulosta(System.out);
        }
        */
	}
}
