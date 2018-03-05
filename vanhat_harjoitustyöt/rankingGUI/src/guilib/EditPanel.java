package guilib;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
/**
 * Luokka otsikon ja edit-kent‰n yhdist‰miseksi 
 * @author vesal
 * @version 4.1.2010
 */
public class EditPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel label = new JLabel("nimi");
	private final JLabel fill1 = new JLabel(" ");
	private final JTextField edit = new JTextField();
	private final JLabel fill2 = new JLabel(" ");

	/**
	 * Create the panel.
	 */
	public EditPanel() {
		edit.setColumns(10);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		label.setPreferredSize(new Dimension(100, 14));
		
		add(label);
		
		add(fill1);
		
		add(edit);
		
		add(fill2);

	}

	/**
	 * @return labelin teksti
	 */
	public String getCaption() {
		return label.getText();
	}
	/**
	 * @param text asetettava teksti
	 */
	public void setCaption(String text) {
		label.setText(text);
	}
	/**
	 * @return haettava teksti
	 */
	public String getText() {
		return edit.getText();
	}
	/**
	 * @param text_1 asetettava teksti
	 */
	public void setText(String text_1) {
		edit.setText(text_1);
	}
	/**
	 * @return sarakkeiden m‰‰r‰
	 */
	public int getColumns() {
		return edit.getColumns();
	}
	/**
	 * @param columns sarakkeiden m‰‰r‰
	 */
	public void setColumns(int columns) {
		edit.setColumns(columns);
	}
	
    /**
     * Asetetaan labelin leveys
     * @param w asetettava leveys
     */
    public void setLabelWidth(int w) {
        int h = getPreferredSize().height;
        label.setPreferredSize(new Dimension(w, h));	
    }
}
