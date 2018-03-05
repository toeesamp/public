package rankingGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;

import fi.jyu.mit.gui.ListChooser;

import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

import guilib.EditPanel;

import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import rankingSwing.RankingSwing;

import javax.swing.Box;
import fi.jyu.mit.gui.ComboBoxChooser;
import fi.jyu.mit.gui.SelectionChangeListener;
import fi.jyu.mit.gui.IStringListChooser;

/**
 * @author Tommi Sampo
 * @version 19.3.2014
 */

public class RankingGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JPanel panelNappulat = new JPanel();
	private final JSplitPane splitPaneListaJaTiedot = new JSplitPane();
	private final JPanel panelLista = new JPanel();
	private final JPanel panelTiedot = new JPanel();
	private final JPanel panelHakuJaBracket = new JPanel();
	private final JLabel lblTiedot = new JLabel(" Killan tiedot");
	private final ListChooser listChooserKiltaLista = new ListChooser();
	private final JButton btnUusi = new JButton("Lis\u00E4\u00E4 kilta");
	private final JButton btnTallenna = new JButton("Tallenna");
	private final JMenuBar menuBar = new JMenuBar();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JPanel panelKilta = new JPanel();
	private final JPanel box = new JPanel();
	private final EditPanel editPanelRank = new EditPanel();
	private final EditPanel editPanelBracket = new EditPanel();
	private final EditPanel editPanelRealm = new EditPanel();
	private final EditPanel editPanelGLeader = new EditPanel();
	private final EditPanel editPanelFaction = new EditPanel();
	private final EditPanel editPanelKieli = new EditPanel();
	private final EditPanel editPanelLisatietoja = new EditPanel();
	private final JTextField txtHaku = new JTextField();
	private final JLabel labelKillanNimi = new JLabel("Paragon");
	private final JPanel panelHaku = new JPanel();
	private final JPanel panelBracket = new JPanel();
	private final JLabel labelBracket = new JLabel(" Bracket");
	private final JMenu mnTiedosto = new JMenu("Tiedosto");
	private final JMenu mnMuokkaa = new JMenu("Muokkaa");
	private final JMenu mnApua = new JMenu("Apua");
	private final JMenuItem mntmTallenna = new JMenuItem("Tallenna");
	private final JMenuItem mntmLopeta = new JMenuItem("Lopeta");
	private final JMenuItem mntmLisUusiKilta = new JMenuItem("Lis\u00E4\u00E4 uusi kilta");
	private final JMenuItem mntmPoistaKilta = new JMenuItem("Poista kilta");
	private final JMenuItem mntmApua = new JMenuItem("Apua");
	private final JMenuItem mntmTietoja = new JMenuItem("Tietoja");
	//private final JMenuItem mntmMuokkaaKillanTietoja = new JMenuItem("Muokkaa killan tietoja");

	/**
	 * Swing-luokka
	 */
	protected final RankingSwing rankingSwing = new RankingSwing();
	private final Box boxKilta = Box.createVerticalBox();
	private final ComboBoxChooser cbBracketit = new ComboBoxChooser();
	/**
	 * Launch the application.
	 * @param args aluksi valittava bracket
	 */
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {
				try {
					RankingGUI frame = new RankingGUI();
					frame.setVisible(true);
					frame.lueTiedosto();
					if (args.length != 0) frame.setCbBracketIndex(args[0]);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RankingGUI() {
		rankingSwing.setKiltaLista(listChooserKiltaLista);
		rankingSwing.setPanelKilta(boxKilta);
		
		setTitle("WoW Progress Ranking");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 447, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(panelNappulat, BorderLayout.SOUTH);
		panelNappulat.setLayout(new BoxLayout(panelNappulat, BoxLayout.X_AXIS));
		btnUusi.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				uusiKilta();
			}
		});
		
		panelNappulat.add(btnUusi);
		btnTallenna.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent arg0) {
				rankingSwing.tallenna();
			}
		});
		
		panelNappulat.add(btnTallenna);
		menuBar.setToolTipText("");
		
		contentPane.add(menuBar, BorderLayout.NORTH);
		
		menuBar.add(mnTiedosto);
		mntmTallenna.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				rankingSwing.tallenna();
			}
		});
		
		mnTiedosto.add(mntmTallenna);
		mntmLopeta.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				setVisible(false);
                dispose();
			}
		});
		
		mnTiedosto.add(mntmLopeta);
		
		menuBar.add(mnMuokkaa);
		mntmLisUusiKilta.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
			    uusiKilta();
			}
		});
		
		mnMuokkaa.add(mntmLisUusiKilta);

		mntmPoistaKilta.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				rankingSwing.poistaKilta();
			}
		});
		
		mnMuokkaa.add(mntmPoistaKilta);
		
		menuBar.add(mnApua);
		mntmApua.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				rankingSwing.apua();
			}
		});
		
		mnApua.add(mntmApua);
		mntmTietoja.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				rankingSwing.tietoja();
			}
		});
		
		mnApua.add(mntmTietoja);
		splitPaneListaJaTiedot.setResizeWeight(0.2);
		
		contentPane.add(splitPaneListaJaTiedot, BorderLayout.CENTER);
		
		splitPaneListaJaTiedot.setLeftComponent(panelLista);
		panelLista.setLayout(new BorderLayout(0, 0));
		
		panelLista.add(panelHakuJaBracket, BorderLayout.NORTH);
		panelHakuJaBracket.setLayout(new BorderLayout(0, 0));
		
		panelHakuJaBracket.add(panelHaku, BorderLayout.SOUTH);
		panelHaku.setLayout(new BorderLayout(0, 0));
		rankingSwing.setEditHaku(txtHaku);
		panelHaku.add(txtHaku);
		txtHaku.setText("");
		txtHaku.setColumns(10);
		
		panelHakuJaBracket.add(panelBracket, BorderLayout.CENTER);
		panelBracket.setLayout(new BorderLayout(0, 0));
		panelBracket.add(labelBracket, BorderLayout.WEST);
		labelBracket.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rankingSwing.setCbBracketit(cbBracketit);
		cbBracketit.addSelectionChangeListener(new SelectionChangeListener<String>() {
		    @Override
            public void selectionChange(IStringListChooser<String> arg0) {
		        rankingSwing.vaihdaBracket();
		    }
		});
		cbBracketit.setKohteet(new String[] {"10", "25"});
		cbBracketit.setCaption("");
		
		panelBracket.add(cbBracketit, BorderLayout.EAST);
		/*
		listChooserKiltaLista.getCaptionLabel().setText(" Killan nimi");
		listChooserKiltaLista.getList().setModel(new AbstractListModel<Object>() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"Paragon", "Method", "Exorsus", "Envy", "Midwinter"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listChooserKiltaLista.getCaptionLabel().setFont(new Font("Tahoma", Font.PLAIN, 12));
		*/
		listChooserKiltaLista.getCaptionLabel().setText("Killan nimi");
		panelLista.add(listChooserKiltaLista, BorderLayout.CENTER);
		
		splitPaneListaJaTiedot.setRightComponent(panelTiedot);
		panelTiedot.setLayout(new BorderLayout(0, 0));
		
		panelTiedot.add(boxKilta, BorderLayout.NORTH);
		boxKilta.add(lblTiedot);
		lblTiedot.setFont(new Font("Tahoma", Font.PLAIN, 12));
		boxKilta.add(scrollPane);
		
		scrollPane.setViewportView(panelKilta);
		panelKilta.setLayout(new BorderLayout(0, 0));
		
		panelKilta.add(box, BorderLayout.NORTH);
		box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
		labelKillanNimi.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelKillanNimi.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		box.add(labelKillanNimi);

		editPanelRank.setText("1");
		editPanelRank.setCaption("Rank");
		
		box.add(editPanelRank);
		editPanelBracket.setText("10 man");
		editPanelBracket.setCaption("Bracket");
		
		box.add(editPanelBracket);
		editPanelRealm.setText("Lightning's Blade EU");
		editPanelRealm.setCaption("Realm");
		
		box.add(editPanelRealm);
        editPanelGLeader.setText("Sejta");
        editPanelGLeader.setCaption("Guild leader");
		
		box.add(editPanelGLeader);
		editPanelFaction.setText("Horde");
		
		editPanelFaction.setCaption("Faction");
		
		box.add(editPanelFaction);
		editPanelKieli.setText("Suomi");
		editPanelKieli.setCaption("Kieli");
		
		box.add(editPanelKieli);
		editPanelLisatietoja.setText("Torilla tavataan");
		editPanelLisatietoja.setCaption("Lis\u00E4tietoja");
		
		box.add(editPanelLisatietoja);
		
	}

	/**
	 *  Lukee tiedostot
	 */
	protected void lueTiedosto() {
		rankingSwing.lueTiedosto();
	}
	
	/**
	 * Asettaa cbBrackettiin oikean indeksin
	 * @param s asetettava bracket
	 */
	protected void setCbBracketIndex(String s) {
	    rankingSwing.setCbBracketitIndex(Integer.parseInt(s));
	}
	
	
	/**
	 * Uuden killan luominen
	 */
	protected void uusiKilta() {
	    UusiKilta lisaaDialog = new UusiKilta();
	    lisaaDialog.tuoSwing(rankingSwing);
	    lisaaDialog.setVisible(true);
	}
}
