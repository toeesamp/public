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

namespace ht
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        /// <summary>
        /// Konstruktori
        /// </summary>
        public MainWindow()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Avaa tietodialogin
        /// </summary>
        /// <param name="sender">mistä tapahtumaan tullaan</param>
        /// <param name="e">tapahtuman argumentit</param>
        private void tietoja_Click(object sender, RoutedEventArgs e)
        {
            Tietoja.TietojaDialog tietoja = new Tietoja.TietojaDialog();
            tietoja.Show();
        }

        /// <summary>
        /// aloittaa uuden pelin
        /// </summary>
        /// <param name="sender">mistä tapahtumaan tullaan</param>
        /// <param name="e">tapahtuman argumentit</param>
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            lauta.UusiPeli();
        }

        /// <summary>
        /// Aloittaa uuden pelin, menuversio
        /// </summary>
        /// <param name="sender">mistä tapahtumaan tullaan</param>
        /// <param name="e">tapahtuman argumentit</param>
        private void uusiPeli_Click(object sender, RoutedEventArgs e)
        {
            lauta.UusiPeli();
        }

        /// <summary>
        /// Sulkee ikkunan
        /// </summary>
        /// <param name="sender">mistä tapahtumaan tullaan</param>
        /// <param name="e">tapahtuman argumentit</param>
        private void sulje_Click(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        private void avustus_Click(object sender, RoutedEventArgs e)
        {
            Avustus.AvustusWindow avustus = new Avustus.AvustusWindow();
            avustus.Show();
        }
    }
}
