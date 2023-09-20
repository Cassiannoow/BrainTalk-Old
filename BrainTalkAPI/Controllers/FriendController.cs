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
    public class FriendsController : ControllerBase
    {
        private readonly BrainTalkContext _context;

        public FriendsController(BrainTalkContext context)
        {
            _context = context;
        }

        // Método para criar uma nova solicitação de amizade
        [HttpPost]
        public async Task<ActionResult<Friend>> CreateFriendRequest(Friend friend)
        {
            _context.Friend.Add(friend);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetFriend), new { id = friend.FriendshipID }, friend);
        }

        // Método para obter uma solicitação de amizade por ID
        [HttpGet("{id}")]
        public async Task<ActionResult<Friend>> GetFriend(int id)
        {
            var friend = await _context.Friend.FindAsync(id);

            if (friend == null)
            {
                return NotFound();
            }

            return friend;
        }

        // Método para atualizar o status de uma solicitação de amizade (Aceitar, Recusar, etc.)
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateFriend(int id, Friend friend)
        {
            if (id != friend.FriendshipID)
            {
                return BadRequest();
            }

            _context.Entry(friend).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!FriendExists(id))
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

        // Método para excluir uma solicitação de amizade por ID
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteFriend(int id)
        {
            var friend = await _context.Friend.FindAsync(id);
            if (friend == null)
            {
                return NotFound();
            }

            _context.Friend.Remove(friend);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool FriendExists(int id)
        {
            return _context.Friend.Any(e => e.FriendshipID == id);
        }
    }
}
