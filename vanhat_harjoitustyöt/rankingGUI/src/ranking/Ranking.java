package ranking;

import fi.jyu.mit.gui.EditPanel;
import fi.jyu.mit.ohj2.Mjonot;
import fi.jyu.mit.ohj2.WildChars;


/**
 * @author Tommi Sampo
 * @version 16.3.2014
 * @version 25.3.2014
 * @version 8.4.2014
 */
public class Ranking {

    private final Killat killat = new Killat();
    private final Factionit factionit = new Factionit();
    private final Kielet kielet = new Kielet();
    private final Raidsizet raidsizet = new Raidsizet();
    private final Realmit realmit = new Realmit();

    /**
     * Palauttaa kiltojen m‰‰r‰n
     * @return kiltojen m‰‰r‰
     */
    public int getKiltoja() {
        return killat.getLkm();
    }



    /**
     * Palauttaa factioneiden lukum‰‰r‰n
     * @return factioneiden lukum‰‰r‰
     *
	public int getFactioneita() {
		return factionit.getLkm();
	}

	/**
     * Palauttaa kielten lukum‰‰r‰n
     * @return kielten lukum‰‰r‰
     *
	public int getKieli‰() {
		return kielet.getLkm();
	}

	/**
     * Palauttaa raidsizejen lukum‰‰r‰n
     * @return raidsizejen lukum‰‰r‰
     *
	public int getRaidsizeja() {
		return raidsizet.getLkm();
	}

	/**
     * Palauttaa realmien lukum‰‰r‰n
     * @return realmien lukum‰‰r‰
     *
	public int getRealmeja() {
		return realmit.getLkm();
	}

     */


    /**
     * Poistaa halutut killat
     * @param nro viite, jonka mukaan poistetaan
     * @return montako kiltaa poistetaan
     */
    public int poista(int nro) {
        return killat.poista(nro);
    }

    /**
     * Lis‰‰ rankingiin uuden killan
     * @param kilta lis‰tt‰v‰ kilta
     * @throws SailoException jos lis‰ys ep‰onnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Ranking ranking = new Ranking();
     * Kilta paragon = new Kilta(), paragon2 = new Kilta();
     * paragon.lisaa(); paragon2.lisaa();
     * ranking.getKiltoja() === 0;
     * ranking.lisaa(paragon); ranking.getKiltoja() === 1;
     * ranking.lisaa(paragon2); ranking.getKiltoja() === 2;
     * ranking.lisaa(paragon); ranking.getKiltoja() === 3;
     * ranking.getKiltoja() === 3;
     * ranking.annaKilta(0) === paragon;
     * ranking.annaKilta(1) === paragon2;
     * ranking.annaKilta(2) === paragon;
     * ranking.annaKilta(3) === paragon; #THROWS IndexOutOfBoundsException 
     * ranking.lisaa(paragon); ranking.getKiltoja() === 4;
     * ranking.lisaa(paragon); ranking.getKiltoja() === 5;
     * </pre>
     */
    public void lisaa(Kilta kilta) throws SailoException {
        killat.lisaa(kilta);
    }

    /**
     * Lis‰‰ rankingiin uuden kielen
     * @param kieli lis‰tt‰v‰ kieli
     * @throws SailoException jos lis‰ys ep‰onnistuu
     */
    public void lisaa(Kieli kieli) throws SailoException {
        kielet.lisaa(kieli);
    }

    /**
     * Lis‰‰ rankingiin uuden realmin
     * @param realm lis‰tt‰v‰ realm
     * @throws SailoException jos lis‰ys ep‰onnistuu
     */
    public void lisaa(Realm realm) throws SailoException {
        realmit.lisaa(realm);
    }


    /**
     * Palauttaa i:n killan
     * @param i monesko kilta palautetaan
     * @return viite j‰seneen i
     * @throws IndexOutOfBoundsException i p‰in prinkkalaa
     */
    public Kilta annaKilta(int i) throws IndexOutOfBoundsException {
        return killat.anna(i);
    }

    /**
     * Palauttaa factionin jonka id on i
     * @param i mik‰ faction palautetaan
     * @return viite factioniin i
     * @throws IndexOutOfBoundsException i p‰in prinkkalaa
     */
    public Faction annaFaction(int i) throws IndexOutOfBoundsException {
        return factionit.anna(i);
    }

    /**
     * Palauttaa raidsizen jonka id on i
     * @param i mik‰ raidsize palautetaan
     * @return viite raidsizeen i
     * @throws IndexOutOfBoundsException i p‰in prinkkalaa
     */
    public Raidsize annaRaidsize(int i) throws IndexOutOfBoundsException {
        return raidsizet.anna(i);
    }


    /**
     * Palauttaa kielen jonka id on i
     * @param i mik‰ kieli palautetaan
     * @return viite kieleen i
     * @throws IndexOutOfBoundsException i p‰in prinkkalaa
     */
    public Kieli annaKieli(int i) throws IndexOutOfBoundsException {
        return kielet.anna(i);
    }

    /**
     * Palauttaa realmin jonka id on i
     * @param i mik‰ realm palautetaan
     * @return viite realmiin i
     * @throws IndexOutOfBoundsException i p‰in prinkkalaa
     */
    public Realm annaRealm(int i) throws IndexOutOfBoundsException {
        return realmit.anna(i);
    }

    /**
     * Lukee tiedostosta rankingin tiedot
     * @throws SailoException Jos tiedostosta lukeminen ei onnistu
     */
    public void lueTiedosto() throws SailoException {
        raidsizet.lueTiedostosta();
        factionit.lueTiedostosta();
        killat.lueTiedostosta();
        kielet.lueTiedostosta();
        realmit.lueTiedostosta();
    }

    /**
     * Tallentaa rankingin tiedot
     * @throws SailoException Jos tallentaminen ei onnistu
     */
    public void tallenna() throws SailoException {
        killat.tallenna();
        factionit.tallenna();
        kielet.tallenna();
        raidsizet.tallenna();
        realmit.tallenna();
    }

    /**
     * 
     * @param bracketIndex bracketin indeksi, johon kuuluvat killat haetaan
     * @param ehto hakuehto
     * @return killat, jotka kuuluvat haluttuun brackettiin
     */
    public Killat etsi(int bracketIndex, String ehto) {
        //if (bracketIndex != 0 || bracketIndex != 1) return null;
        Killat haetut = new Killat();
        try {

            if (bracketIndex == 0) {
                for (int i = 0; i < killat.getLkm(); i++) {

                    if (Mjonot.erotaInt((annaRaidsize(annaKilta(i).getBracketID()).getNimi()), 0) == 10
                            && WildChars.onkoSamat(killat.anna(i).getNimi(), ehto))
                        haetut.lisaa(annaKilta(i));
                }
            }
            else if (bracketIndex == 1) {
                for (int i = 0; i < killat.getLkm(); i++) {
                    if (Mjonot.erotaInt((annaRaidsize(annaKilta(i).getBracketID()).getNimi()), 0) == 25
                            && WildChars.onkoSamat(killat.anna(i).getNimi(), ehto))
                        haetut.lisaa(annaKilta(i));
                }
            }

        } catch (SailoException e) {
            System.err.println(e.getMessage());
        }
        return haetut;
    }


    /**
     * Asettaa perusarvot kaikille muille tietokantaluokille
     * paitsi killoille, jottei ohjelma antaisi erroreita
     * kun sit‰ yritt‰‰ testata ilman valmiiksi olevia tiedostoja
     * (koska lis‰‰minen ei ole viel‰ lopullisessa muodossaan)
     */
    public void asetaArvotKaikille(){
        factionit.asetaArvot();
        kielet.asetaArvot();
        raidsizet.asetaArvot();
        realmit.asetaArvot();
    }

    /**
     * Asettaa muokattavat kent‰t kohdilleen
     * @param k muokattava kentt‰
     * @param jono jono, jota kentt‰‰n yritet‰‰n syˆtt‰‰
     * @return null jos ok, muuten virhe
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        switch ( k ) {
        case 0: case 1: case 4: case 6: case 7: case 8:
            return null;
        case 2:
            Tarkistus t3 = new Tarkistus();
            String virhe3 = t3.tarkista(tjono, 2);
            if (virhe3 != null) return virhe3;
            return null;
        case 3:
            Tarkistus t = new Tarkistus();
            String virhe = t.tarkista(tjono, 3);
            if (virhe != null) return virhe;
            return null;
        case 5:
            Tarkistus t2 = new Tarkistus();
            String virhe2 = t2.tarkista(tjono, 5);
            if (virhe2 != null) return virhe2;
            return null;
        default:
            return "K-maurin kosto";
        }
    }
    /**
     * Asettaa muokattavat kent‰t kohdilleen, tapahtuu tallennuksen yhteydess‰
     * @param kentat editpaneelit joissa tekstit on
     * @param editKilta2 kilta, jolle syˆtetyt arvot asetetaan
     * @return null jos ok, muuten virhe
     */
    public String tallennaKentat(EditPanel[] kentat, Kilta editKilta2) {

        if (editKilta2 == null) return "Ei kiltaa valittuna";
        /**
         * 0 RANK
         * 1 BRACKET
         * 2 REALM
         * 3 FACTION
         * 4 GUILD LEADER
         * 5 KIELI
         * 6 LISƒTIETOJA
         */

        boolean realmLoytyi = false;
        boolean kieliLoytyi = false;

        // Rank
        editKilta2.setRank(kentat[0].getText());

        // Bracket
        for (int i = 1; i <= raidsizet.getLkm(); i++) {
            if ( kentat[1].getText().equals(annaRaidsize(i).getNimi())) {
                editKilta2.setBracketID(annaRaidsize(i).getTunnusNro());
            }
        }

        // Realm
        for (int i = 1; i <= realmit.getLkm(); i++) {
            if (kentat[2].getText().equals(annaRealm(i).getNimi())) {
                editKilta2.setRealmID(annaRealm(i).getTunnusNro());
                realmLoytyi = true;
            }
        }
        if (!realmLoytyi) {
            Realm r = new Realm(kentat[2].getText());
            r.lisaa();
            realmit.lisaa(r);
            editKilta2.setRealmID(r.getTunnusNro());
        }
        
        // Faction
        for (int i = 1; i <= factionit.getLkm(); i++) {
            if ( kentat[3].getText().equals(annaFaction(i).getNimi())) {
                editKilta2.setFactionID(annaFaction(i).getTunnusNro());
            }
        }

        // Guild leader
        editKilta2.setGuildLeader(kentat[4].getText());

        // Kieli
        for (int i = 1; i <= kielet.getLkm(); i++) {
            if (kentat[5].getText().equals(annaKieli(i).getNimi())) {
                editKilta2.setKieliID(annaKieli(i).getTunnusNro());
                kieliLoytyi = true;
            }
        }
        if (!kieliLoytyi) {
            Kieli ki = new Kieli(kentat[5].getText());
            ki.lisaa();
            kielet.lisaa(ki);
            editKilta2.setKieliID(ki.getTunnusNro());
        }
        // Lis‰tiedot
        editKilta2.setLisatietoja(kentat[6].getText());

        return null;
    }

    /**
     * Korvataan olemassa oleva kilta jos lˆytyy, 
     * jos ei niin luodaan uusi
     * @param kilta lis‰tt‰v‰n killan viite
     * @throws SailoException jos tietorakenne on t‰ynn‰
     */
    public void korvaaTaiLisaa(Kilta kilta) throws SailoException {
        killat.korvaaTaiLisaa(kilta);
    }

    /**
     * testip‰‰ohjelma
     * @param args ei k‰ytˆss‰
     */
    public static void main(String[] args) {

        Ranking ranking = new Ranking();

        try {
            Kilta paragon = new Kilta(), paragon2 = new Kilta();
            paragon.lisaa();
            paragon.asetaArvot();
            paragon2.lisaa();
            paragon2.asetaArvot();

            ranking.lisaa(paragon);
            ranking.lisaa(paragon2);

            System.out.println("============= Rankingin testi =================");

            for (int i = 0; i < ranking.getKiltoja(); i++) {
                Kilta kilta = ranking.annaKilta(i);
                System.out.println("Kilta paikassa: " + i);
                kilta.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
