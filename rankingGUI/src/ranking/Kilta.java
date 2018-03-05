package ranking;

import java.io.*;
import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Tommi Sampo
 * @version 14.3.2014
 * @version 19.3.2014
 * @version 8.4.2014
 */
public class Kilta implements Cloneable {
	private int tunnusNro;
	private String nimi = "";
	private String rank = "";
	private int bracketID;
	private int realmID;
	private int factionID;
	private String guildLeader ="";
	private int kieliID;
	private String lisatietoja = "";
	
	private static int seuraavaNro = 1;

	/**
	 * Killan alustaminen
	 */
	public Kilta() {
		// attribuuttien alustus riitt‰‰
	}
	
    /**
     * Killan alustaminen nimen kera
     * @param nimi killan nimi
     */
    public Kilta(String nimi) {
        this.nimi = nimi;
    }
	/**
     * Palautetaan killan ID
     * @return killan ID
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * @return palauttaa kenttien lukum‰‰r‰n
     */
    public int getKenttia() {
        return 9;
    }
    
    /**
     * @return ensimm‰inen kentt‰
     */
    public int ekaEditPanelKentta() {
        return 2;
    }
    
    /**
     * Palautetaan killan bracketin ID
     * @return killan bracketin ID
     */
    public int getBracketID() {
        return bracketID;
    }
    
    /**
     * Palautetaan killan realmin ID
     * @return killan realmin ID
     */
    public int getRealmID() {
        return realmID;
    }
    
    /**
     * Palautetaan killan factionin ID
     * @return killan factionin ID
     */
    public int getFactionID() {
        return factionID;
    }
    
    /**
     * Palautetaan killan kielen ID
     * @return killan kielen ID
     */
    public int getKieliID() {
        return kieliID;
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
     * Asetetaan killalle nimi
     * @param nimi asetettava nimi
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    /**
     * Asetetaan killalle rank
     * @param rank asetettavan bracketin numero
     */
    public void setRank(String rank) {
        this.rank = rank;
    }
    
    /**
     * Asetetaan killalle bracket
     * @param bracket asetettavan bracketin numero
     */
    public void setBracketID(int bracket) {
        this.bracketID = bracket;
    }
	
    /**
     * Asetetaan killalle realm
     * @param realm asetettavan realmin numero
     */
    public void setRealmID(int realm) {
        this.realmID = realm;
    }
    
    /**
     * Asetetaan killalle faction
     * @param faction asetettavan factionin numero
     */
    public void setFactionID(int faction) {
        this.factionID = faction;
    }
    
    /**
     * Asetetaan killalle guild leader
     * @param nimi asetettavan guild leaderin nimi
     */
    public void setGuildLeader(String nimi) {
        this.guildLeader = nimi;
    }
    
    /**
     * Asetetaan killalle kieli
     * @param kieli asetettavan realmin numero
     */
    public void setKieliID(int kieli) {
        this.kieliID = kieli;
    }
    
    /**
     * Asetetaan killalle lis‰tietoja
     * @param nimi asetettava lis‰tieto
     */
    public void setLisatietoja(String nimi) {
        this.lisatietoja = nimi;
    }
    
	/**
	 * Palauttaa killan nimen
	 * @return killan nimi
	 * @example
	 * <pre name="test">
	 * Kilta paragon = new Kilta();
	 * paragon.asetaArvot();
	 * paragon.getNimi() === "Paragon";
	 * </pre>
	 */
	public String getNimi() {
		return nimi;
	}
	
	
	/**
	 * Tulostetaan killan tiedot
	 * @param out tietovirta johon halutaan tulostaa
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%02d", tunnusNro) + " " + nimi);
		out.println(" #" + rank);// + " " + bracketID);
		//out.println(" " + realmID + " " + factionID);
		out.println(" " + guildLeader);    // TULOSTUKSESTA POISTETTU
		//out.println(" " + kieliID);      // RELAATIOATTRIBUUTIT
		out.println(" " + lisatietoja);
	}
	
	/**
	 * Tulostetaan killan tiedot
	 * @param stream tietovirta johon halutaan tulostaa
	 */
	public void tulosta(OutputStream stream) {
		tulosta(new PrintStream(stream));
	}
	
	/**
	 * Antaa killalle ID-numeron
	 * @return uusi ID
	 * @example
	 * <pre name="test">
	 *   Kilta paragon1 = new Kilta();
	 *   paragon1.getTunnusNro() === 0;
	 *   paragon1.lisaa();
	 *   Kilta paragon2 = new Kilta();
	 *   paragon2.lisaa();
	 *   int n1 = paragon1.getTunnusNro();
	 *   int n2 = paragon2.getTunnusNro();
	 *   n1 === n2-1;
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
		nimi = "Paragon";
		rank =  "1";
		bracketID = RNG();
		realmID = 1;
		guildLeader = "Sejta";
		factionID = RNG();
		kieliID = RNG();
		lisatietoja = "Tortillat avataan";
	}
	
	/**
	 * @return 1 tai 2
	 */
	public int RNG () {
	    return (Math.random() <= 0.5) ? 1 : 2;
	}
	
    /**
     * Antaa k:n kent‰n sis‰llˆn merkkijonona
     * @param k monenenko kent‰n sis‰ltˆ palautetaan
     * @return kent‰n sis‰ltˆ merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0:
            return "" + tunnusNro;
        case 1:
            return "" + nimi;
        case 2:
            return "" + rank;
        case 3:
            return "" + bracketID;
        case 4:
            return "" + realmID;
        case 5:
            return "" + factionID;
        case 6:
            return "" + guildLeader;
        case 7:
            return "" + kieliID;
        case 8:
            return "" + lisatietoja;
        default:
            return "K-maurin kosto";
        }
    }
    
    /**
     * Palauttaa k:tta j‰senen kentt‰‰ vastaavan kysymyksen
     * @param k kuinka monennen kent‰n kysymys palautetaan (0-alkuinen)
     * @return k:netta kentt‰‰ vastaava kysymys
     */
    public String getKysymys(int k) {
        switch ( k ) {
        case 0:
            return "Tunnus nro";
        case 1:
            return "Nimi";
        case 2:
            return "Rank";
        case 3:
            return "Bracket";
        case 4:
            return "Realm";
        case 5:
            return "Faction";
        case 6:
            return "Guild leader";
        case 7:
            return "Kieli";
        case 8:
            return "Lis‰tietoja";
        default:
            return "K-maurin kosto";
        }
    }
    
   
    
	/**
	 * Palauttaa killan tiedot tiedostoon tallennettavana merkkijonona
	 * @return kilta tolppaerotettuna merkkijonona
	 * @example
	 * <pre name="test">
	 * Kilta kilta = new Kilta();
	 * kilta.parse(" 2  |  Paragon | 1");
	 * kilta.toString().startsWith("2|Paragon|1|") === true;
	 * </pre>
	 */
	@Override
	public String toString() {
		return "" + 
				getTunnusNro() + "|" +
				nimi + "|" +
				rank + "|" +
				bracketID + "|" +
				realmID + "|" +
				factionID + "|" +
				guildLeader + "|" +
				kieliID + "|" +
				lisatietoja;
	}
	
	/**
	 * Selvitt‰‰ killan tiedot tolppaerotetusta merkkijonosta
	 * @param rivi merkkijono, josta tiedot otetaan
	 * @example
	 * <pre name="test">
	 * Kilta kilta = new Kilta();
	 * kilta.parse(" 2  |  Paragon | 1");
	 * kilta.getTunnusNro() === 2;
	 * kilta.toString().startsWith("2|Paragon|1|") === true;
	 * </pre>
	 */
	public void parse(String rivi) {
		StringBuilder sb = new StringBuilder(rivi);
		setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
		nimi = (Mjonot.erota(sb, '|', nimi));
		rank = (Mjonot.erota(sb, '|', rank));
		bracketID = (Mjonot.erota(sb, '|', bracketID));
		realmID = (Mjonot.erota(sb, '|', realmID));
		factionID = (Mjonot.erota(sb, '|', factionID));
		guildLeader = (Mjonot.erota(sb, '|', guildLeader));
		kieliID = (Mjonot.erota(sb, '|', kieliID));
		lisatietoja = (Mjonot.erota(sb, '|', lisatietoja));	
	}
	
	
	/**
	 * Tehd‰‰n klooni killasta
	 * @return Object kloonattu kilta
	 */
    @Override
    public Kilta clone() throws CloneNotSupportedException {
        Kilta uusi;
        uusi = (Kilta) super.clone();
        return uusi;
    }
	
	/**
	 * Testip‰‰ohjelma
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Kilta paragon = new Kilta(), paragon2 = new Kilta();
		paragon.lisaa();
		paragon2.lisaa();
		paragon.tulosta(System.out);
		paragon.asetaArvot();
		paragon.tulosta(System.out);
		paragon2.asetaArvot();
		paragon2.tulosta(System.out);
		paragon2.asetaArvot();
		paragon2.tulosta(System.out);
	}

}
