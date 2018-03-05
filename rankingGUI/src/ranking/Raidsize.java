package ranking;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Raidsize-luokka
 * 
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 8.4.2014
 */
public class Raidsize {
	private int tunnusNro;
	private String bracket;

	private static int seuraavaNro = 1;
	/**
	 * Palautetaan raidsizen ID
	 * @return raidsizen ID
	 */
	public int getTunnusNro() {
		return tunnusNro;
	}
	
	/**
	 * Palauttaa merkkijonon editpanelia varten
	 * @return raidsize merkkijonona
	 */
	public String getNimi() {
	    return bracket;
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
	 * raidsizen alustaminen
	 */
	public Raidsize () {
		// attribuuttien alustus riitt‰‰
	}
	
	/**
	 * Alustaa factionin tietyll‰ merkkijonolla
	 * @param r factionin nimi
	 */
	public Raidsize(String r) {
		this.bracket = r;
		lisaa();
	}
	
	
	/**
	 * Asetetaan testiarvot
	 */
	public void asetaArvot() {
		bracket = "10 man";
	}
	
	/**
	* Tulostetaan raidsizen tiedot
	* @param out tietovirta johon tulostetaan
	*/
	public void tulosta(PrintStream out) {
	  	out.println(bracket);
	}
	
	/**
	* Tulostetaan raidsizen tiedot
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
	 * Raidsize kyb‰ = new Raidsize();
	 * kyb‰.getTunnusNro() === 0;
	 * kyb‰.lisaa();
	 * Raidsize kyb‰2 = new Raidsize();
	 * kyb‰2.lisaa();
	 * int a = kyb‰.getTunnusNro();
	 * int b = kyb‰2.getTunnusNro();
	 * a === b-1;
	 * </pre>
	 */
	public int lisaa() {
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}
	
	/**
	 * Palauttaa raidsizen tiedot tiedostoon tallennettavana merkkijonona
	 * @return raidsize tolppaerotettuna merkkijonona
	 * @example
	 * <pre name="test">
	 * Raidsize rdsz = new Raidsize();
	 * rdsz.parse(" 2  |  10 man");
	 * rdsz.toString().equals("2|10 man") === true;
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" + 
				getTunnusNro() + "|" +
				bracket;
	}
	
	/**
	 * Selvitt‰‰ factionin tiedot tolppaerotetusta merkkijonosta
	 * @param rivi merkkijono, josta tiedot otetaan
	 * @example
	 * <pre name="test">
	 * Raidsize rdsz = new Raidsize();
	 * rdsz.parse(" 2  |  10 man");
	 * rdsz.getTunnusNro() === 2;
	 * rdsz.toString().equals("2|10 man") === true;
	 * </pre>
	 */
	public void parse(String rivi) {
		StringBuilder sb = new StringBuilder(rivi);
		setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
		bracket = (Mjonot.erota(sb, '|', bracket));
	}
	
	/**
	 * testip‰‰ohjelma
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Raidsize kyb‰ = new Raidsize();
		kyb‰.asetaArvot();
		kyb‰.tulosta(System.out);
	}

}
