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
    public class AddressController : Controller
    {
        EstontecoDbContext _dba;

        public AddressController(EstontecoDbContext context)
        {
            _dba = context;
        }
        // GET: api/Address
        public IEnumerable<Parking> Get()
        {
            return _dba.Parkingi.ToList();
        }

        // GET: api/Address/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }
        
        // POST: api/Address
        [HttpPost]
        public void Post([FromBody]Parking value)
        {
            _dba.Parkingi.Add(value);
        }
        
        // PUT: api/Address/5
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
