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
    public class CarParkController : Controller
    {
        EstontecoDbContext _dba;

        public CarParkController(EstontecoDbContext context)
        {
            _dba = context;
        }

        // GET: api/CarPark
        public IEnumerable<Parking> Get()
        {
            return _dba.Parkingi.ToList();
        }

        // GET: api/CarPark/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        // POST: api/CarPark
        [HttpPost]
        public void Post([FromBody]Parking value)
        {
            _dba.Parkingi.Add(value);
        }

        // PUT: api/CarPark/5
        [HttpPut("{id}")]
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE: api/CarPark/5
        [HttpDelete("{id}")]
        public void Delete(int id)
        {
        }
    }
}
