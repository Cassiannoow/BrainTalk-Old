using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
     public class DiscussionCategory
    {
        [Key]
        public int CategoryID { get; set; }

        [Required]
        [MaxLength(100)]
        public string NomeCategoria { get; set; }

        public string DescricaoCategoria { get; set; }
    }
}