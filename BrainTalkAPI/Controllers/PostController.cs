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
    public class PostController : ControllerBase
    {
        private readonly BrainTalkContext _context;

        public PostController(BrainTalkContext context)
        {
            _context = context;
        }

        // Método para criar um novo post no fórum
        [HttpPost]
        public async Task<ActionResult<Posts>> CreatePost(Posts post)
        {
            _context.Post.Add(post);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetPost), new { id = post.PostID }, post);
        }

        // Método para obter um post no fórum por ID
        [HttpGet("{id}")]
        public async Task<ActionResult<Post>> GetPost(int id)
        {
            var post = await _context.Post.FindAsync(id);

            if (post == null)
            {
                return NotFound();
            }

            return post;
        }

        // Método para atualizar um post no fórum
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdatePost(int id, Posts post)
        {
            if (id != post.PostID)
            {
                return BadRequest();
            }

            _context.Entry(post).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!PostExists(id))
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

        // Método para excluir um post no fórum por ID
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeletePost(int id)
        {
            var post = await _context.Posts.FindAsync(id);
            if (post == null)
            {
                return NotFound();
            }

            _context.Posts.Remove(post);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool PostExists(int id)
        {
            return _context.Posts.Any(e => e.PostID == id);
        }
    }
}
