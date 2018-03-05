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

namespace Avustus
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class AvustusWindow : Window
    {
        public AvustusWindow()
        {
            InitializeComponent();
            ohjeet.Text = 
                "Säännöt:\r" + "Yritä siirtää nappulasi vastustajan takariville. \r"
                + "Punainen joukkue aloittaa\r"
                + "Klikkaa nappulaa valitaksesi sen ja klikkaa sitten ruutua, johon haluat siirtyä\r"
                + "Valitun nappulan reunat värjäytyvät keltaisiksi\r"
                + "Nappula voi siirtyä aina yhden ruudun viistoon tai suoraan eteenpäin jos kyseessä on tyhjä ruutu.\r"
                + "Nappula voi syödä vastustajan nappulan siirtyessään viistoon mutta ei suoraan siirtyessä.\r\r"
                + "Uuden pelin voi aloittaa klikkaamalla \"Uusi peli\"-nappulaa tai valitsemalla sen menusta\r"
                + "Tietoja ohjelmasta saa valitsemalla menusta \"Tietoja\" \r"
                + "Pelin voi sulkea painamalla ruksista tai valitsemalla menusta \"Sulje\"";
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }
    }
}
