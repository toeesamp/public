package ranking;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Faction-luokka
 * 
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 18.3.2014
 * @version 8.4.2014
 */
public class Faction {
	private int tunnusNro;
	private String nimi = "";
	
	private static int seuraavaNro = 1;
	
	/**
	 * Palautetaan factionin ID
	 * @return factionin ID
	 */
	public int getTunnusNro() {
		return tunnusNro;
	}
	   
    /**
     * Palauttaa merkkijonon editpanelia varten
     * @return factionin nimi merkkijonona
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
	 * Muodostaja
	 */
	public Faction() {
		// attribuuttien alustus riitt‰‰
	}
	
	/**
	 * Alustaa factionin tietyll‰ merkkijonolla
	 * @param f factionin nimi
	 */
	public Faction(String f) {
		this.nimi = f;
		lisaa();
	}
	
	/**
	* Tulostetaan factionin tiedot
	* @param out tietovirta johon tulostetaan
	*/
	public void tulosta(PrintStream out) {
	  	out.println(nimi);
	}
	
	/**
	* Tulostetaan factionin tiedot
	* @param stream tietovirta johon tulostetaan
	*/
	public void tulosta(OutputStream stream) {
	  	tulosta(new PrintStream(stream));
	}
	
	/**
	 * Lis‰‰ factionille seuraavan tunnusnumeron
	 * @return tunnusnumero
	 * @example
	 * <pre name="test">
	 * Faction horde = new Faction();
	 * horde.getTunnusNro() === 0;
	 * horde.lisaa();
	 * Faction horde2 = new Faction();
	 * horde2.lisaa();
	 * int a = horde.getTunnusNro();
	 * int b = horde2.getTunnusNro();
	 * a === b-1;
	 * </pre>
	 */
	public int lisaa() {
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}
	
	/**
	 * Testiarvojen asettaminen
	 */
	public void asetaArvot() {
		this.nimi = "Horde";
	}
	
	/**
	 * Palauttaa factionin tiedot tiedostoon tallennettavana merkkijonona
	 * @return faction tolppaerotettuna merkkijonona
	 * @example
	 * <pre name="test">
	 * Faction faction = new Faction();
	 * faction.parse(" 2  |  Horde");
	 * faction.toString().equals("2|Horde") === true;
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
	 * Faction faction = new Faction();
	 * faction.parse(" 2  |  Horde");
	 * faction.getTunnusNro() === 2;
	 * faction.toString().equals("2|Horde") === true;
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
		Faction horde = new Faction();
		horde.asetaArvot();
		horde.tulosta(System.out);
		
		Faction alliance = new Faction("Alliance");
		alliance.tulosta(System.out);
	}

}
