using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using estonteco.api.DAL;
using estonteco.api.Model;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace estonteco.api.Controllers
{
    [Produces("application/json")]
    [Route("api/[controller]/[action]")]
    public class ReservationController : Controller
    {
        EstontecoDbContext _db;

        public ReservationController(EstontecoDbContext context)
        {
            _db = context;
        }


        // GET: api/Reservation
        [HttpGet("api/reservation")]
        public IEnumerable<InfoRezerwacja> Get()
        {
            return _db.Rezerwacje.ToList();

        }

        // GET: api/Reservation/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST: api/Reservation
        [HttpPost]
        public void Post([FromBody]InfoRezerwacja value)
        {
            _db.Rezerwacje.Add(value);
        }

        // PUT: api/Reservation/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE: api/ApiWithActions/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
