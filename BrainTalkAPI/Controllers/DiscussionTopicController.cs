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
    public class DiscussionTopicsController : ControllerBase
    {
        private readonly BrainTalkContext _context;

        public DiscussionTopicsController(BrainTalkContext context)
        {
            _context = context;
        }

        // Método para criar um novo tópico de discussão
        [HttpPost]
        public async Task<ActionResult<DiscussionTopic>> CreateDiscussionTopic(DiscussionTopic topic)
        {
            _context.DiscussionTopic.Add(topic);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetDiscussionTopic), new { id = topic.TopicID }, topic);
        }

        // Método para obter um tópico de discussão por ID
        [HttpGet("{id}")]
        public async Task<ActionResult<DiscussionTopic>> GetDiscussionTopic(int id)
        {
            var topic = await _context.DiscussionTopic.FindAsync(id);

            if (topic == null)
            {
                return NotFound();
            }

            return topic;
        }

        // Método para atualizar um tópico de discussão
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateDiscussionTopic(int id, DiscussionTopic topic)
        {
            if (id != topic.TopicID)
            {
                return BadRequest();
            }

            _context.Entry(topic).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DiscussionTopicExists(id))
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

        // Método para excluir um tópico de discussão por ID
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDiscussionTopic(int id)
        {
            var topic = await _context.DiscussionTopic.FindAsync(id);
            if (topic == null)
            {
                return NotFound();
            }

            _context.DiscussionTopic.Remove(topic);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DiscussionTopicExists(int id)
        {
            return _context.DiscussionTopic.Any(e => e.TopicID == id);
        }
    }
}
