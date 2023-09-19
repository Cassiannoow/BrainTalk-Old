using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
    public class Friend
    {
        [Key]
        public int FriendshipID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        public int FriendUserID { get; set; }

        [MaxLength(20)]
        public string Status { get; set; }
    }
}