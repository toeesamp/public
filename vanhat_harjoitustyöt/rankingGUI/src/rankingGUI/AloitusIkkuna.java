package rankingGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import rankingSwing.RankingSwing;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Tommi Sampo
 * @version 19.3.2014
 */

public class AloitusIkkuna extends JFrame {

	/** Swing luokka */
	protected final RankingSwing rankingSwing = new RankingSwing();
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					AloitusIkkuna frame = new AloitusIkkuna();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AloitusIkkuna() {
		setTitle("WoW progress ranking");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("WoW progress ranking");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(116, 11, 207, 52);
		contentPane.add(lblNewLabel);
		
		JLabel lblVersio = new JLabel("v1.0");
		lblVersio.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblVersio.setBounds(200, 71, 38, 29);
		contentPane.add(lblVersio);
		
		JLabel lblTekija = new JLabel("Tommi Sampo");
		lblTekija.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblTekija.setBounds(154, 109, 137, 29);
		contentPane.add(lblTekija);
		
		JLabel lblBracket = new JLabel("Valitse bracket:");
		lblBracket.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBracket.setBounds(178, 189, 92, 14);
		contentPane.add(lblBracket);
		
		JButton btnman = new JButton("10 man");
		btnman.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				avaaPaaIkkuna(10);
			}
		});
		btnman.setBounds(116, 216, 89, 23);
		contentPane.add(btnman);
		
		JButton btnMan = new JButton("25 man");
		btnMan.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				avaaPaaIkkuna(25);
			}
		});
		btnMan.setBounds(225, 216, 89, 23);
		contentPane.add(btnMan);
	}
	
	/**
	 * Avaa ohjelman pääikkunan
	 * @param bracket aluksi valittava bracket
	 */
    public void avaaPaaIkkuna(int bracket) {
		rankingSwing.luoKansio();
		setVisible(false);
        dispose();
		rankingSwing.avaaPaaIkkuna(bracket);
	}
}
