/**
 * 
 */
package ranking;

/**
 * @author Tommi Sampo
 * @version 23.4.2014
 *
 * Tarkistaa asetettujen tietojen oikeellisuuden
 */
public class Tarkistus {

    /**
     * Tarkistaa onko merkkijono sopiva
     * @param jono tarkistettava jono
     * @param kentta mihin kentt‰‰n jonoa ollaan sijoittamassa
     * @return null jos ok, muuten virhe
     */
    public String tarkista(String jono, int kentta) {
        if (kentta == 2) {
            if (jono.matches("[0-9]*")) {
                return null;
            }
            return "Kent‰ss‰ kirjaimia";
        }
        if (kentta == 3) {
            if (jono.equals("10 man") || jono.equals("25 man")) {
                return null;
            }
            return "Bracket v‰‰r‰ss‰ muodossa";
        }
        
        if (kentta == 5) {
            if (jono.equals("Horde") || jono.equals("Alliance")){
                return null;
            }
            return "Faction v‰‰r‰ss‰ muodossa";
        }
        return null;
    }

}
