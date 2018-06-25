using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace estonteco.api.Model
{
    public class InfoRezerwacja
    {
        public int Id { get; set; }
        public int IdParkingu { get; set; }
        public int Miejsce { get; set; }
        public bool CzyZarezerwowane { get; set; }
        public bool CzyZajete { get; set; }
        public double DlugoscGeo { get; set; }
        public double SzerokoscGeo { get; set; }
    }
}
