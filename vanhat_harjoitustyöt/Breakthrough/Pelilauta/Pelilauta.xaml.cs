using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Pelilauta
{
    /// <summary>
    /// Where the magic happens
    /// </summary>
    public partial class PelilautaClass : UserControl
    {
        int koko = 8; // voidaan tehä vain neliön muotoisia pelialustoja
        string vuoro = "pelaaja2"; //asetetaan aloittaja
        SolidColorBrush punainen = new SolidColorBrush(Colors.Red);
        SolidColorBrush musta = new SolidColorBrush(Colors.Black);
        SolidColorBrush valkoinen = new SolidColorBrush(Colors.White);

        Nappula.NappulaClass tempNappula = null;

        /// <summary>
        /// Konstruktori
        /// </summary>
        public PelilautaClass()
        {
            InitializeComponent();

            ruudukko.ShowGridLines = true;
            for (int i = 0; i < koko; i++)
            {
                ruudukko.RowDefinitions.Add(new RowDefinition());
                ruudukko.ColumnDefinitions.Add(new ColumnDefinition());
            }

            UusiPeli();

        }

        /// <summary>
        /// Aloittaa uuden pelin
        /// </summary>
        public void UusiPeli()
        {
            ruudukko.Children.Clear();
            vuoro = "pelaaja2";
            LuoNappulat();
            for (int i = 0; i < ruudukko.ColumnDefinitions.Count * 2; i++)
            {
                Nappula.NappulaClass nappula = (Nappula.NappulaClass)ruudukko.Children[i];
                nappula.pelaaja = "pelaaja1";
                nappula.nappulaFill = musta;
            }

            for (int i = ruudukko.Children.Count - 1; i > ruudukko.Children.Count - 1 - ruudukko.ColumnDefinitions.Count * 2; i--)
            {
                Nappula.NappulaClass nappula = (Nappula.NappulaClass)ruudukko.Children[i];
                nappula.pelaaja = "pelaaja2";
                nappula.nappulaFill = punainen;
            }
        }

        /// <summary>
        /// Luo nappulat gridiin
        /// </summary>
        private void LuoNappulat()
        {
            for (int i = 0; i < ruudukko.RowDefinitions.Count; i++)
            {
                for (int j = 0; j < ruudukko.ColumnDefinitions.Count; j++)
                {
                    Nappula.NappulaClass nappula = new Nappula.NappulaClass();
                    Grid.SetRow(nappula, i);
                    nappula.rivi = i;
                    Grid.SetColumn(nappula, j);
                    nappula.sarake = j;
                    nappula.nappulaFill = valkoinen;
                    ruudukko.Children.Add(nappula);
                }
            }
        }

        /// <summary>
        /// Ensimmäisenä klikattu nappula otetaan temp-muuttujaan, toisena klikattua
        /// verrataan temp nappulan pelaajaan ja sijaintiin, jonka perusteella
        /// määritetään siirron laillisuus
        /// </summary>
        /// <param name="sender">mistä tapahtumaan tullaan</param>
        /// <param name="e">tapahtuman argumentit</param>
        private void nappulaClick(object sender, RoutedEventArgs e)
        {
            // lokaali temp-nappula
            Nappula.NappulaClass n = (Nappula.NappulaClass)e.OriginalSource;

            // katsotaan onko tempnappulassa mitään ja kuuluuko klikattu nappula kummallekaan pelaajalle
            if (tempNappula == null && n.pelaaja.Equals(vuoro))
            {
                tempNappula = n;
                n.nappulaStroke = Brushes.Yellow;
                //this.Background = Brushes.Blue;
            }
            else if (tempNappula != null)
            {
                // hylätään siirto jos kyseessä on väärä vuoro
                if (!tempNappula.pelaaja.Equals(vuoro))
                {
                    HylkaaSiirto();
                    return;
                }

                // nappula, johon globaalia tempnappulaa verrataan
                Nappula.NappulaClass temp2 = (Nappula.NappulaClass)e.OriginalSource;

                // laillisia siirtoja ovat vain suoraan tai yksi viistoon, muuten hylätään
                if (!(temp2.sarake == tempNappula.sarake || temp2.sarake == tempNappula.sarake - 1 || temp2.sarake == tempNappula.sarake + 1))
                {
                    HylkaaSiirto();
                    return;
                }

                // punaisilla siirto pitää olla aina yksi ylöspäin
                if (vuoro.Equals("pelaaja2") && temp2.rivi != tempNappula.rivi - 1)
                {
                    HylkaaSiirto();
                    return;
                }

                // mustilla siirto pitää olla aina yksi alaspäin
                if (vuoro.Equals("pelaaja1") && temp2.rivi != tempNappula.rivi + 1)
                {
                    HylkaaSiirto();
                    return;
                }

                // voi liikuttaa suoraan vain jos edessä on tyhjä nappula
                if (temp2.sarake == tempNappula.sarake && !temp2.pelaaja.Equals(""))
                {
                    HylkaaSiirto();
                    return;
                }

                // SYÖNTI

                // tarkistetaan ettei kumpikaan ole tyhjä nappula
                if (!temp2.pelaaja.Equals("") && !tempNappula.pelaaja.Equals(""))
                {
                    temp2.pelaaja = tempNappula.pelaaja;
                    temp2.nappulaFill = tempNappula.nappulaFill;
                    tempNappula.pelaaja = "";
                    tempNappula.nappulaFill = valkoinen;
                    HylkaaSiirto();
                    VaihdaVuoroa();
                    return;
                }

                // ETENEMINEN

                // kolmas nappula tietojen vaihtamista varten
                Nappula.NappulaClass kopio = new Nappula.NappulaClass();
                kopio.pelaaja = tempNappula.pelaaja;
                kopio.nappulaFill = tempNappula.nappulaFill;

                // jos kaikki menee ok niin vaihdetaan nappuloiden väriä ja omistajaa
                tempNappula.pelaaja = temp2.pelaaja;
                tempNappula.nappulaFill = temp2.nappulaFill;
                temp2.pelaaja = kopio.pelaaja;
                temp2.nappulaFill = kopio.nappulaFill;

                // onnistuneen siirron jälkeen asetetaan tempnappula taas nulliksi ja vaihdetaan vuoroa
                HylkaaSiirto();
                VaihdaVuoroa();
            }

        }

        /// <summary>
        /// Hylkää siirron, mikäli se on laiton
        /// </summary>
        private void HylkaaSiirto()
        {
            tempNappula.nappulaStroke = Brushes.Transparent;
            tempNappula = null;
            //this.Background = Brushes.Pink;
        }

        /// <summary>
        /// Vaihtaa pelin vuoroa ja tarkistaa voittajan
        /// </summary>
        private void VaihdaVuoroa()
        {
            TarkistaVoittaja();
            if (vuoro.Equals("pelaaja1"))
            {
                vuoro = "pelaaja2";
            }
            else if (vuoro.Equals("pelaaja2"))
            {
                vuoro = "pelaaja1";
            }
        }

        /// <summary>
        /// Tarkistaa, onko nappula päässyt läpi
        /// </summary>
        private void TarkistaVoittaja()
        {
            foreach (var UIElement in ruudukko.Children)
            {
                Nappula.NappulaClass n = (Nappula.NappulaClass)UIElement;
                if (n.pelaaja.Equals("pelaaja1") && n.rivi == koko - 1)
                {
                    n.nappulaFill = Brushes.Green;
                    vuoro = "loppu";
                }
                else if (n.pelaaja.Equals("pelaaja2") && n.rivi == 0)
                {
                    n.nappulaFill = Brushes.Green;
                    vuoro = "loppu";
                }
            }
        }
    }
}