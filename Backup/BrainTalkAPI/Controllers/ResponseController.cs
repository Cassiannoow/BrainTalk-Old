using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using BrainTalkAPI.Models;
using BrainTalkAPI.Data;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BrainTalkAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ResponsesController : ControllerBase
    {
        private readonly BrainTalkContext _context;

        public ResponsesController(BrainTalkContext context)
        {
            _context = context;
        }

        // Método para criar uma nova resposta
        [HttpPost]
        public async Task<ActionResult<Response>> CreateResponse(Response response)
        {
            _context.Responses.Add(response);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetResponse), new { id = response.ResponseID }, response);
        }

        // Método para obter uma resposta por ID
        [HttpGet("{id}")]
        public async Task<ActionResult<Response>> GetResponse(int id)
        {
            var response = await _context.Responses.FindAsync(id);

            if (response == null)
            {
                return NotFound();
            }

            return response;
        }

        // Método para atualizar uma resposta
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateResponse(int id, Response response)
        {
            if (id != response.ResponseID)
            {
                return BadRequest();
            }

            _context.Entry(response).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ResponseExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // Método para excluir uma resposta por ID
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteResponse(int id)
        {
            var response = await _context.Responses.FindAsync(id);
            if (response == null)
            {
                return NotFound();
            }

            _context.Responses.Remove(response);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool ResponseExists(int id)
        {
            return _context.Responses.Any(e => e.ResponseID == id);
        }
    }
}
