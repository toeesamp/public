package ranking;

import java.io.*;
import java.util.Arrays;

/**
 * Kiltoja k‰sittelev‰ luokka
 * 
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 8.4.2014
 */
public class Killat {
	private static final int MAX_KILTOJA = Integer.MAX_VALUE;
	private int lkm = 0;
	private String kokoNimi = "";
	private String tiedostonPerusNimi = "killat";
	private Kilta[] alkiot = new Kilta[5];
	
	/**
	 * Muodostaja
	 */
	public Killat() {
		// attribuuttien alustus riitt‰‰
	}
	
	/**
	 * Palauttaa kiltojen lukum‰‰r‰n
	 * @return kiltojen lukum‰‰r‰
	 */
	public int getLkm() {
		return lkm;
	}
	
    /**
     * Palautetaan killan bracketin ID
     * @return killan bracketin ID
     */
    public int getBracketID() {
        return alkiot[0].getBracketID();
    }
    
    /**
     * Palautetaan killan realmin ID
     * @return killan realmin ID
     */
    public int getRealmID() {
        return alkiot[0].getRealmID();
    }
    
    /**
     * Palautetaan killan factionin ID
     * @return killan factionin ID
     */
    public int getFactionID() {
        return alkiot[0].getFactionID();
    }
    
    /**
     * Palautetaan killan kielen ID
     * @return killan kielen ID
     */
    public int getKieliID() {
        return alkiot[0].getKieliID();
    }
    
    /**
     * Asetetaan killalle bracket
     * @param bracket asetettavan bracketin numero
     */
    public void setBracketID(int bracket) {
        alkiot[0].setBracketID(bracket);;
    }
    
    /**
     * Asetetaan killalle realm
     * @param realm asetettavan realmin numero
     */
    public void setRealmID(int realm) {
        alkiot[0].setRealmID(realm);
    }
    
    /**
     * Asetetaan killalle faction
     * @param faction asetettavan factionin numero
     */
    public void setFactionID(int faction) {
        alkiot[0].setFactionID(faction);
    }
    
    
    /**
     * Asetetaan killalle kieli
     * @param kieli asetettavan realmin numero
     */
    public void setKieliID(int kieli) {
        alkiot[0].setKieliID(kieli);;
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
	 * Palauttaa tiedoston nimen p‰‰tten kera
	 * @return tiedoston nimi p‰‰tten kera
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
     * Palauttaa killan koko nimen
     * @return killan koko nimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
	/**
	 * Lis‰‰ tietorakenteeseeen uuden killan
	 * @param kilta lis‰tt‰v‰n killan viite
	 * @throws SailoException jos alkiotaulukko on t‰ynn‰
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
	 * Killat killat = new Killat();
	 * Kilta kilta1 = new Kilta(); 
	 * Kilta kilta2 = new Kilta();
	 * killat.getLkm() === 0;
	 * killat.lisaa(kilta1); killat.getLkm() === 1;
	 * killat.lisaa(kilta2); killat.getLkm() === 2;
	 * </pre>
	 */
	public void lisaa(Kilta kilta) throws SailoException {
		if (lkm >= MAX_KILTOJA) throw new SailoException("Alkiotaulukko t‰ynn‰");
		if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm + 10);
		alkiot[lkm] = kilta;
		lkm++;
	}
	
	/**
	 * Palauttaa viitteen haluttuun (i) kiltaan
	 * @param i halutun killan indeksi alkiotaulukossa
	 * @return halutun killan viite
	 * @throws IndexOutOfBoundsException indeksi p‰in prinkkalaa
	 */
	public Kilta anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Indeksi p‰in prinkkalaa: " + i);
		return alkiot[i];
	}
	
    /**
     * Poistaa killan jolla on valittu tunnusnumero 
     * @param id poistettavan killan tunnusnumero
     * @return 1 jos poistettiin, 0 jos ei lˆydy
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++)
            alkiot[i] = alkiot[i + 1];
        alkiot[lkm] = null;
        return 1;
    }
	
    /**
     * Etsii killan id:n perusteella
     * @param id tunnusnumero, jonka mukaan etsit‰‰n
     * @return lˆytyneen j‰senen indeksi tai -1 jos ei lˆydy
     */
    public int etsiId(int id) {
        for (int i = 0; i < lkm; i++)
            if (id == alkiot[i].getTunnusNro()) return i;
        return -1;
    }
    
	/**
	 * Lukee tiedostosta kiltojen tiedot
	 * @throws SailoException Jos tiedostosta lukeminen ei onnistu
	 */
	public void lueTiedostosta() throws SailoException {
		 try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
	            String rivi = fi.readLine();
	            //if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");
	            while ( (rivi = fi.readLine()) != null ) {
	                rivi = rivi.trim();
	                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
	                Kilta kilta = new Kilta();
	                kilta.parse(rivi); // voisi olla virhek‰sittely
	                lisaa(kilta);
	            }
	        } catch ( FileNotFoundException e ) {
	            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
	        } catch ( IOException e ) {
	            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	        }
	}
	
	/**
	 * Tallentaa kiltojen tiedot
	 * @throws SailoException Jos tallentaminen ei onnistu
	 */
	public void tallenna() throws SailoException {
		File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimet‰");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(lkm);
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
	 * asettaa testiarvoja
	 */
	public void asetaArvot() {
		alkiot[0] = new Kilta();
		alkiot[0].asetaArvot();
		lkm++;
		alkiot[1] = new Kilta();
		alkiot[1].asetaArvot();
		lkm++;
	}
	

	/**
	 * Korvataan olemassa oleva kilta jos lˆytyy, 
	 * jos ei niin luodaan uusi
	 * @param kilta lis‰tt‰v‰n killan viite
	 * @throws SailoException jos tietorakenne on t‰ynn‰
	 */
	public void korvaaTaiLisaa(Kilta kilta) throws SailoException {
	    if (kilta == null) return;
	    int id = kilta.getTunnusNro();
	    for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = kilta;
                return;
            }
        }
	    lisaa(kilta);
	}
	
	/**
	 * Testip‰‰ohjelma
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Killat killat= new Killat();
		killat.asetaArvot();
		
		System.out.println("============= Killat testi =================");
		
        for (int i = 0; i < killat.getLkm(); i++) {
            Kilta kilta = killat.anna(i);
            System.out.println("Kilta nro: " + i);
            kilta.tulosta(System.out);
        }
	}



}
