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
public class Factionit {
	
	private static final int FACTIONEITA = 2;
	/** lukum‰‰r‰ */
	public int lkm;
	private Faction[] alkiot = new Faction[FACTIONEITA];
	
	private String tiedostonPerusNimi = "factionit";
	private String kokoNimi = "";

	/**
	 * Muodostaja
	 */
	public Factionit() {
		// attribuuttien alustus riitt‰‰
	}
	
	/**
	 * Palauttaa factioneiden lukum‰‰r‰n
	 * @return factioneiden lukum‰‰r‰
	 */
	public int getLkm() {
		return lkm;
	}
	
    /**
     * Palauttaa factionin koko nimen
     * @return factionin koko nimi merkkijonona
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
	 * TODO jos tiedostoa ei lˆydy niin aseta perus Horde/Alliance
	 * Lukee factionit tiedostosta.  
	 * @throws SailoException Jos tiedostosta lukeminen ei onnistu
	 */
	public void lueTiedostosta() throws SailoException {
		 try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
	            String rivi = fi.readLine();
	            //if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

	            while ( (rivi = fi.readLine()) != null ) {
	                rivi = rivi.trim();
	                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
	                Faction faction = new Faction();
	                faction.parse(rivi);
	                lisaa(faction);
	            }
	        } catch ( FileNotFoundException e ) {
	            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
	        } catch ( IOException e ) {
	            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	        }	
	}
	
	
	/**
	 * Tallentaa factionit tiedostoon.  
	 * @throws SailoException Jos tallentaminen ei onnistu
	 */
	public void tallenna() throws SailoException {
		File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak); 

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            //fo.println(getKokoNimi());
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
	 * Luo seuraavanlaisen perustiedoston factioneista:
	 * 1|Horde
	 * 2|Alliance
	 */
	public void luoPerusTiedosto() {
	    asetaArvot();
	    try {
            tallenna();
            lueTiedostosta();
        } catch (SailoException e) {
            e.printStackTrace();
        }
	    
	}
	
	/**
	 * Lis‰‰ uuden factionin 
	 * @param faction lis‰tt‰v‰n factionin viite
	 * @throws SailoException jos taulukko on t‰ynn‰
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
	 * Factionit factionit = new Factionit();
	 * Faction horde1 = new Faction(); 
	 * Faction horde2 = new Faction();
	 * factionit.getLkm() === 0;
	 * factionit.lisaa(horde1); factionit.getLkm() === 1;
	 * factionit.lisaa(horde2); factionit.getLkm() === 2;
	 * factionit.lisaa(horde1); #THROWS SailoException
	 * </pre>
	 */
	public void lisaa(Faction faction) throws SailoException {
		if (lkm >= FACTIONEITA) throw new SailoException("2 Factionia on maksimi");
		alkiot[lkm] = faction;
		lkm++;
	}
	
	/**
	 * asettaa testiarvoja
	 */
	public void asetaArvot() {
	    Faction horde = new Faction("Horde");
		alkiot[0] = horde;
		lkm++;
		Faction alliance = new Faction("Alliance");
		alkiot[1] = alliance;
		lkm++;
	}
	
    /**
     * Palauttaa viitteen factioniin, jonka id on i
     * @param i mink‰ factionin viite halutaan 
     * @return viite factioniin i, null jos alkiota ei lˆydy
     * @throws IndexOutOfBoundsException jos i on laiton
     */
	//v‰‰r‰ exception?
    public Faction anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm < i)
            throw new IndexOutOfBoundsException("Laiton numero: " + i);
        for (int j = 0; j < alkiot.length; j++) {
            if (alkiot[j].getTunnusNro() == i) 
                return alkiot[j];
        }
        return null;
    }
       
    
	/**
	 * testip‰‰ohjelma
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Factionit factionit = new Factionit();
		factionit.asetaArvot();
		
		System.out.println("============= Factionit testi =================");
		
        for (int i = 0; i < factionit.getLkm(); i++) {
            Faction faction = factionit.anna(i);
            System.out.println("Faction nro: " + i);
            faction.tulosta(System.out);
        }
	}

}
