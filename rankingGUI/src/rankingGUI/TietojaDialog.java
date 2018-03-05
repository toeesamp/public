package rankingGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Tommi Sampo
 * @version 19.3.2014
 */


public class TietojaDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					TietojaDialog frame = new TietojaDialog();
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
	public TietojaDialog() {
		setTitle("Tietoja");
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
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
                dispose();
			}
		});
		btnOk.setBounds(170, 213, 89, 23);
		contentPane.add(btnOk);
	}
}
