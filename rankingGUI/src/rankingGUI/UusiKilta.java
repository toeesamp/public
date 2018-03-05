package rankingGUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import rankingSwing.RankingSwing;
import guilib.EditPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * @author Tommi Sampo
 * @version 23.4.2014
 *
 * Ikkuna uuden killan luomista varten
 */
public class UusiKilta extends JDialog {

        private static final long serialVersionUID = 1L;
        private final JPanel contentPanel = new JPanel();
        private EditPanel editPanelNimi;
        
        /***/
        protected RankingSwing rankingSwing;
        
        /**
         * Launch the application.
         * @param args ei käytössä
         */
        public static void main(String[] args) {
                try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Throwable e) {
                        e.printStackTrace();
                }
                try {
                        UusiKilta dialog = new UusiKilta();
                        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        dialog.setVisible(true);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        /**
         * Create the dialog.
         */
        public UusiKilta() {
                setTitle("Uusi kilta");
                setBounds(100, 100, 324, 161);
                getContentPane().setLayout(new BorderLayout());
                contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
                getContentPane().add(contentPanel, BorderLayout.CENTER);
                contentPanel.setLayout(null);
               
                editPanelNimi = new EditPanel();
                editPanelNimi.setCaption("Nimi");
                editPanelNimi.setBounds(0, 30, 213, 20);
                contentPanel.add(editPanelNimi);
                {
                        JPanel buttonPane = new JPanel();
                        getContentPane().add(buttonPane, BorderLayout.SOUTH);
                        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                        {
                                JButton okButton = new JButton("Ok");
                                okButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent arg0) {
                                        rankingSwing.uusiKilta(getKillanNimi().getText());
                                        setVisible(false);
                                        dispose();
                                    }
                                });
                                buttonPane.add(okButton);
                                okButton.setActionCommand("OK");
                                getRootPane().setDefaultButton(okButton);
                        }
                        {
                                JButton cancelButton = new JButton("Cancel");
                                buttonPane.add(cancelButton);
                                cancelButton.setActionCommand("Cancel");
                        }
                }
        }
        
        /**
         * @return uuden killan nimi
         */
        public EditPanel getKillanNimi() {
            return editPanelNimi;
        }
        
        /**
         * Tuodaan muualla käytetty rankingSwing-olio, jotta lisääminen onnistuu
         * @param rankingSwingerinoPepperinoFrappuchinoDongerinoPleaseNoCopypasterino muualla käytetty swing-olio
         */
        public void tuoSwing(RankingSwing rankingSwingerinoPepperinoFrappuchinoDongerinoPleaseNoCopypasterino) {
            this.rankingSwing = rankingSwingerinoPepperinoFrappuchinoDongerinoPleaseNoCopypasterino;
            
        }
        
}