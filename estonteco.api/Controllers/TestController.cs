using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using estonteco.api.DAL;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace estonteco.api.Controllers
{
    [Produces("application/json")]
    [Route("api/Test")]
    public class TestController : Controller
    {
        EstontecoDbContext _dbContext;
        ILogger<TestController> _log;

        public TestController(
            EstontecoDbContext db,
            ILogger<TestController> logger)
        {
            _dbContext = db;
            _log = logger;
        }

        // GET: api/Test/test
        [HttpPost]
        public IActionResult Test()
        {
            return Ok("OK");
        }
    }
}