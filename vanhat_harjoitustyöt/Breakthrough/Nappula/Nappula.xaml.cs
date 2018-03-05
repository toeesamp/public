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

namespace Nappula
{
    /// <summary>
    /// Interaction logic for UserControl1.xaml
    /// </summary>
    public partial class NappulaClass : UserControl
    {
        public NappulaClass()
        {
            InitializeComponent();
            ellipsi.StrokeThickness = 2;
            this.pelaaja = "";
        }

        public int rivi
        {
            get { return (int)GetValue(riviProperty); }
            set { SetValue(riviProperty, value); }
        }

        public int sarake
        {
            get { return (int)GetValue(sarakeProperty); }
            set { SetValue(sarakeProperty, value); }
        }
        public string pelaaja
        {
            get { return (string)GetValue(pelaajaProperty); }
            set { SetValue(pelaajaProperty, value); }
        }

        public Brush nappulaFill
        {
            get { return (Brush)GetValue(nappulaFillProperty); }
            set { SetValue(nappulaFillProperty, value); }
        }

        public Brush nappulaStroke
        {
            get { return (Brush)GetValue(nappulaStrokeProperty); }
            set { SetValue(nappulaStrokeProperty, value); }
        }



        public static readonly DependencyProperty riviProperty =
            DependencyProperty.Register("rivi", typeof(int), typeof(NappulaClass));
        public static readonly DependencyProperty sarakeProperty =
            DependencyProperty.Register("sarake", typeof(int), typeof(NappulaClass));
        public static readonly DependencyProperty pelaajaProperty =
            DependencyProperty.Register("pelaaja", typeof(string), typeof(NappulaClass));
        public static readonly DependencyProperty nappulaFillProperty =
            DependencyProperty.Register("nappulaFill", typeof(Brush), typeof(NappulaClass));
        public static readonly DependencyProperty nappulaStrokeProperty =
            DependencyProperty.Register("nappulaStroke", typeof(Brush), typeof(NappulaClass));


        public static readonly RoutedEvent LiikutusEvent =
            EventManager.RegisterRoutedEvent("Liikutus", RoutingStrategy.Bubble,
            typeof(RoutedEventHandler), typeof(NappulaClass));

        /// <summary>
        /// Eventhandler nappuloiden siirtämiselle
        /// </summary>
        public event RoutedEventHandler Liikutus
        {
            add { AddHandler(LiikutusEvent, value); }
            remove { RemoveHandler(LiikutusEvent, value); }
        }

        void RaiseOmaEvent()
        {
            RoutedEventArgs newEventArgs = new RoutedEventArgs(NappulaClass.LiikutusEvent);
            RaiseEvent(newEventArgs);
        }

        private void ellipsi_MouseUp(object sender, MouseButtonEventArgs e)
        {
            RaiseOmaEvent();
        }
    }
}
