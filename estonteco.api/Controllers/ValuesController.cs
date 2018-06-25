using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using estonteco.api.DAL;
using estonteco.api.Model;
using Microsoft.AspNetCore.Mvc;

namespace estonteco.api.Controllers
{
    [Route("api/[controller]")]
    public class ValuesController : Controller
    {
        EstontecoDbContext _dbv;

        public ValuesController(EstontecoDbContext context)
        {
            _dbv = context;
        }

        // GET api/values/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }
        [HttpGet("/api/wypelnij")]
        public string wypelnijdanymi()
        {
             
            if (_dbv.Rezerwacje.Count() != 0)
            {
                return "rezerwacje nie zostaly dodane";
            }
            else
            {
                var p = new Parking() { Nazwa = "parking podziemny" };
                _dbv.Parkingi.Add(p);
                _dbv.SaveChanges();
                _dbv.Rezerwacje.Add(new InfoRezerwacja()
                {
                    IdParkingu = p.Id,
                    Miejsce = 5,
                    CzyZarezerwowane = false,
                    CzyZajete = false,
                    DlugoscGeo = 54.5164814,
                    SzerokoscGeo = 18.5425182
                });
                _dbv.Rezerwacje.Add(new InfoRezerwacja()
                {
                    IdParkingu = p.Id,
                    Miejsce = 6,
                    CzyZarezerwowane = false,
                    CzyZajete = false,
                    DlugoscGeo = 54.3514945,
                    SzerokoscGeo = 18.6350564
                }
                );
                _dbv.Rezerwacje.Add(new InfoRezerwacja()
                {
                    IdParkingu = p.Id,
                    Miejsce = 7,
                    CzyZarezerwowane = false,
                    CzyZajete = false,
                    DlugoscGeo = 54.3516539,
                    SzerokoscGeo = 18.6392943
                }
                );
                _dbv.Rezerwacje.Add(new InfoRezerwacja()
                {
                    IdParkingu = p.Id,
                    Miejsce = 8,
                    CzyZarezerwowane = false,
                    CzyZajete = false,
                    DlugoscGeo = 54.364205,
                    SzerokoscGeo = 18.6233121
                }
                );
                _dbv.SaveChanges();
                return "rezerwacje zostaly dodane";
            }
                
        }
        // POST api/values
        [HttpPost]
        public void Post([FromBody]string value)
        {

        }

        // PUT api/values/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/values/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
