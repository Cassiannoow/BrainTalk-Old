using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
    public class Like
    {
        [Key]
        public int LikeID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        public int PostID { get; set; }

        public DateTime DataCurtida { get; set; }
    }
}