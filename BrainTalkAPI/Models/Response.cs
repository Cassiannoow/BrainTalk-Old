using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
     public class Response
    {
        [Key]
        public int ResponseID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        public int TopicID { get; set; }

        public string? ConteudoResposta { get; set; }

        public DateTime DataResposta { get; set; }
    }
}