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
        public DbSet<InfoRezerwacja> Stu { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseMySql("server=localhost;port=3306;user=Estonteco;password=QnG96Pnn;database=estonteco");
            }
        }
    }
}
