using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using estonteco.api.Model;
using Microsoft.EntityFrameworkCore;

namespace estonteco.api.DAL
{
    public class EstontecoDbContext : DbContext
    {
        public DbSet<InfoRezerwacja> Rezerwacje { get; set; }
        public DbSet<Parking> Parkingi { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseSqlServer("Server=localhost;Database=estonteco;Trusted_Connection=True;User Id=estonteco_app;Password=3bXwH_Bn#e}@H/,%;");
        }
    }
}
