package ranking;

/**
 * Luokka tietokannan poikkeuksia varten
 * 
 * @author Tommi Sampo
 * @version 14.3.2014
 */
public class SailoException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Poikkeuksen muodostaja
	 * @param exception Poikkeus
	 */
	public SailoException(String exception) {
		super(exception);
	}

}
