using Microsoft.AspNetCore.Mvc;
using BrainTalkAPI.Data;
using BrainTalkAPI.Models;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.IdentityModel.Tokens;
using System.Text;

namespace BrainTalkAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class HomeController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private BrainTalkContext _context;
        public HomeController(IConfiguration configuration, BrainTalkContext context)
        {
            _configuration = configuration;
            _context = context;
        }

        [HttpPost]
        [Route("login")]
        [AllowAnonymous]
        public ActionResult<dynamic> Login([FromBody] User usuario)
        {
            var user = _context.Users.Where(u => u.Email == usuario.Email 
            && u.Senha == usuario.Senha).FirstOrDefault();

            if (user == null)
                return Unauthorized("Usuário ou senha inválidos");

            var authClaims = new List<Claim> {
                new Claim(ClaimTypes.Email, user.Email),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
            };

            var token = GetToken(authClaims);
            user.Senha = "";
            return Ok(new
            {
                token = new JwtSecurityTokenHandler().WriteToken(token),
                user = user
            });
        }

        [HttpGet]
        [Route("anonymous")]
        [AllowAnonymous]
        public string Anonymous() => "Anônimo";

        [HttpGet("{username}/authenticated")]
        [Authorize]
        public string Authenticated() => String.Format("Autenticado - {0}", User.Identity.Name);

        private JwtSecurityToken GetToken(List<Claim> authClaims)
        {
            var authSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));

            var token = new JwtSecurityToken(
                expires: DateTime.Now.AddHours(3),
                issuer: _configuration["JWT:ValidIssuer"],
                audience: _configuration["JWT:ValidAudience"],
                claims: authClaims,
                signingCredentials: new SigningCredentials(authSigningKey,

                SecurityAlgorithms.HmacSha256)

            );
            return token;
        }
    }
}