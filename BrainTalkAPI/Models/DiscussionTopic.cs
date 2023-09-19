using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
    public class DiscussionTopic
    {
        [Key]
        public int TopicID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        [MaxLength(255)]
        public string Titulo { get; set; }

        public string Descricao { get; set; }

        public DateTime DataCriacao { get; set; }

        [MaxLength(100)]
        public string Categoria { get; set; }
    }
}