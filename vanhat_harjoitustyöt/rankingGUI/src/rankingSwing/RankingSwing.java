package rankingSwing;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fi.jyu.mit.gui.AbstractChooser;
import fi.jyu.mit.gui.EditPanel;
import fi.jyu.mit.gui.IStringListChooser;
import fi.jyu.mit.gui.SelectionChangeListener;
import rankingGUI.*;
import ranking.*;

/**
 * Luokka joka k‰sittelee rankingia Swing-komponenttien kautta
 * @author Tommi Sampo
 * @version 19.3.2014
 */
public class RankingSwing {

    private AbstractChooser<Kilta> kiltaLista; 
    private AbstractChooser<String> cbBracketit;
    private JComponent panelKilta;
    private JTextField editHaku;
    private final Ranking ranking;
    private final static Kilta apukilta = new Kilta();
    private static Color virheVari = new Color(255, 0, 0);
    private static Color normaaliVari = new Color(255, 255, 255);

    private Kilta kiltaKohdalla;
    private Kilta editKilta;
    private JLabel killanNimi;
    private EditPanel[] editKiltaKentta;

    /** Muodostaja */
    public RankingSwing() {
        ranking = new Ranking();
    }


    /**
     * @return lista johon killat laitetaan 
     */
    public AbstractChooser<Kilta> getKiltaLista() {
        return kiltaLista;
    }

    /**
     * @param kiltaLista lista johon killat laitetaan 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void setKiltaLista(AbstractChooser kiltaLista) {
        this.kiltaLista = kiltaLista;
    }

    /**
     * @return combobox jossa on bracketit
     */
    public AbstractChooser<String> getCbBracketit() {
        return cbBracketit;
    }

    /**
     * @param cbBracketit comboboxkentt‰listaa varten
     */
    public void setCbBracketit(AbstractChooser<String> cbBracketit) {
        this.cbBracketit = cbBracketit;
    }

    /**
     * valitsee haettavat killat oikeasta bracketista
     * @param i bracket
     */
    public void setCbBracketitIndex(int i) {
        if (i == 10) 
            cbBracketit.setSelectedIndex(0);
        if (i == 25)
            cbBracketit.setSelectedIndex(1);

    }

    /**
     * @return alue johon lis‰t‰‰n killan yksitt‰iset kent‰t
     */
    public JComponent getPanelKilta() {
        return panelKilta;
    }

    /**
     * @return edit jossa hakuehto
     */
    public JTextField getEditHaku() {
        return editHaku;
    }
    
    /**
     * @param editHaku edit johon saa kirjoittaa hakuehdon
     */
    public void setEditHaku(JTextField editHaku) {
        this.editHaku = editHaku;
    }
    
    /**
     * @param panelKilta alue johon lis‰t‰‰n killan tiedot
     */
    public void setPanelKilta(JComponent panelKilta) {
        this.panelKilta = panelKilta;
    }

    /**
     * Asetetaan editoitava kilta
     * @param k uusi viite editKillalle
     */
    private void setEditKilta(Kilta k) {
        editKilta = k;
    }

    /**
     * Alustaa kent‰t
     */
    public void alusta() {
        editHaku.addKeyListener(new KeyAdapter() {
            @Override  
            public void keyReleased(KeyEvent e) { hae(-1); } 
        });

        if (cbBracketit != null) cbBracketit.clear();
        cbBracketit.add("10");
        cbBracketit.add("25");

        kiltaLista.addSelectionChangeListener(new SelectionChangeListener<Kilta>() {
            @Override
            public void selectionChange(IStringListChooser<Kilta> sender) { naytaKilta(); }
        });
        luoNaytto();
        hae(0);
    }

    /**
     * Luo uuden killan
     * @param nimi killan nimi
     */
    public void uusiKilta(String nimi) {
        kiltaKohdalla = new Kilta(nimi);
        kiltaKohdalla.lisaa();
        kiltaKohdalla.setNimi(nimi);

        // asetetaan oletusbracket valitun bracketin mukaan
        if (cbBracketit.getSelectedIndex() == 0) {
            kiltaKohdalla.setBracketID(1);
        }
        if (cbBracketit.getSelectedIndex() == 1) {
            kiltaKohdalla.setBracketID(2);
        }
        

        try {
            ranking.lisaa(kiltaKohdalla);
        } catch (SailoException e) {
            JOptionPane.showMessageDialog(null, "Lis‰‰minen ep‰onnistui: " + e.getMessage());
        }

        hae(kiltaKohdalla.getTunnusNro());
    }

    /**
     * Lukee rankingin tiedot tiedostosta
     * @return null jos kaikki ok, muuten virheen tiedot
     */
    public String lueTiedosto() {
        alusta();
        try {
            ranking.lueTiedosto();
        } catch (SailoException e) {
            return (e.getMessage());
        }
        hae(0);
        return null;
    }

    /**
     * Tallentaa kaikki tiedot
     * @return null jos kaikki ok, muuten virheen tiedot
     */
    public String tallenna() {
        try {
            ranking.tallennaKentat(editKiltaKentta, editKilta);
            ranking.korvaaTaiLisaa(editKilta);
            kiltaKohdalla = editKilta;
            ranking.tallenna();
            return null;
        } catch (SailoException e) {
            JOptionPane.showMessageDialog(null, "Tallentaminen ep‰onnistui " + e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Listaa ne j‰senet, jotka t‰ytt‰v‰t hakuehdot
     * @param nro kilta joka valitaan listaamisen j‰lkeen
     */
    protected void hae(int nro) {

        String ehto = editHaku.getText();
        if (ehto.indexOf('*') < 0) {
            ehto = "*" + ehto + "*";
        }
        
        if (cbBracketit == null) return;
        
        int bracketIndex = cbBracketit.getSelectedIndex();
        Killat haetut = ranking.etsi(bracketIndex, ehto);

        kiltaLista.clear();
        int index = 0;
        if ( haetut != null) {
            for (int i = 0; i < haetut.getLkm(); i++) {
                Kilta kilta = haetut.anna(i);
                if (kilta.getTunnusNro() == nro) index = i;
                kiltaLista.add(kilta.getNimi(),kilta);
            }
        }

        kiltaLista.setSelectedIndex(index);
    }

    /**
     * N‰ytt‰‰ killan tiedot omassa paneelissaan
     */
    private void laitaKilta() {
        if (kiltaKohdalla == null) return;

        killanNimi.setText(kiltaKohdalla.anna(1));

        for ( int i = 0, k = kiltaKohdalla.ekaEditPanelKentta(); k < kiltaKohdalla.getKenttia(); k++, i++) {
            editKiltaKentta[i].setText(laitaTeksti(k));
            editKiltaKentta[i].setToolTipText("");
        }
    }

    
    /**
     * @param nro killan kent‰n numero
     * @return editpaneeliin laitettava teksti
     */
    public String laitaTeksti(int nro) {
        switch (nro) {
        case 0: case 1: case 2: case 6: case 8: 
            return kiltaKohdalla.anna(nro);
        case 3:
            return ranking.annaRaidsize(kiltaKohdalla.getBracketID()).getNimi(); // pit‰isi aina olla != null
        case 4:
            Realm r = ranking.annaRealm(kiltaKohdalla.getRealmID());
            if (r == null) return "";
            return r.getNimi();
        case 5:
            Faction f = ranking.annaFaction(kiltaKohdalla.getFactionID());
            if (f == null) return "";
            return f.getNimi();
        case 7:
            Kieli k =ranking.annaKieli(kiltaKohdalla.getKieliID());
            if (k == null) return "";
            return k.getNimi();
        default:
            return "K-maurin kosto";

        }    
    }

    /**
     * N‰ytt‰‰ valitun killan tiedot
     */
    public void naytaKilta() {
        int i = kiltaLista.getSelectedIndex();
        if (i < 0) return;
        kiltaKohdalla = kiltaLista.getSelectedObject();
        laitaKilta();
    }

    /**
     * Luo komponentit, joihin killan tiedot laitetaan
     */
    private void luoNaytto() {
        panelKilta.removeAll();

        killanNimi = new JLabel();
        killanNimi.setFont(new Font("Tahoma", Font.PLAIN, 20));
        panelKilta.add(killanNimi);

        int n = apukilta.getKenttia() - apukilta.ekaEditPanelKentta();
        editKiltaKentta = new EditPanel[n];
        
        for (int i = 0, k = apukilta.ekaEditPanelKentta(); k < apukilta.getKenttia(); k++, i++) {
            EditPanel edit = new EditPanel();
            edit.setCaption(apukilta.getKysymys(k));
            editKiltaKentta[i] = edit;
            edit.setName("ek" + k);
            edit.getEdit().setName("tk" +k);
            panelKilta.add(edit);
            edit.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    kasitteleMuutosKiltaan((JTextField)e.getComponent());
                }
            });
        }
    }


    /**
     * @param edit muuttunut kentt‰
     */
    protected void kasitteleMuutosKiltaan(JTextField edit) {
        if (kiltaKohdalla == null) {
            return;
        }
        if (editKilta == null )
            try {
                setEditKilta(kiltaKohdalla.clone());
            } catch (CloneNotSupportedException e) { //ei tuu virhett‰
            }
        String s = edit.getText();
        int k = Integer.parseInt(edit.getName().substring(2));
        String virhe = ranking.aseta(k, s);
        if (virhe == null) {
            edit.setToolTipText("");
            edit.setBackground(normaaliVari);
        } else {
            edit.setToolTipText(virhe);
            edit.setBackground(virheVari);
        }
    }




    


    /**
     * Poistaa valitun killan
     */
    public void poistaKilta() {
        if (kiltaKohdalla == null) return;
        int vastaus = JOptionPane.showConfirmDialog(null, "Poistetaanko kilta: " + kiltaKohdalla.getNimi(), "Poisto?", JOptionPane.YES_NO_OPTION); 
        if (vastaus == JOptionPane.NO_OPTION ) return;
        ranking.poista(kiltaKohdalla.getTunnusNro());
        int index = kiltaLista.getSelectedIndex();
        kiltaLista.setSelectedIndex(index);
        hae(0);
    }

    /**
     * Avataan selain apua varten
     */
    public void apua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://trac.cc.jyu.fi/projects/ohj2ht/wiki/k2014/suunnitelmat/toeesamp");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }


    /**
     * Tulostaa killan tiedot
     * @param os tietovirta johon tulostetaan
     * @param kilta tulostettava kilta
     */
    public void tulosta(PrintStream os, final Kilta kilta) {
        os.println("----------------------------------------");
        kilta.tulosta(os);
        ranking.annaRaidsize(kilta.getBracketID()).tulosta(os);
        ranking.annaFaction(kilta.getFactionID()).tulosta(os);
        ranking.annaKieli(kilta.getKieliID()).tulosta(os);
        ranking.annaRealm(kilta.getRealmID()).tulosta(os);
        os.println("----------------------------------------");
    }


    /**
     * N‰ytt‰‰ ohjelman tietoja
     */
    public void tietoja() {
        TietojaDialog frame = new TietojaDialog();
        frame.setVisible(true);

    }

    /**
     * Vaihtaa n‰kyviin valittuun brackettiin kuuluvat killat
     */
    public void vaihdaBracket() {
        hae(0);
    }

    /**
     * Avaa ohjelman p‰‰n‰kym‰n
     * @param bracket aluksi valittava bracket
     */
    public void avaaPaaIkkuna(int bracket) {
        String s[] = new String[] {""+bracket};
        RankingGUI.main(s);
    }

    /**
     * Luo tiedoston josta tiedostot luetaan ja johon ne tallennetaan
     */
    public void luoKansio() {
        File tiedosto = new File("ranking");
        tiedosto.mkdir();
    }
}
