using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
    public class User
    {
        [Key]
        public int UserID { get; set; }

        [Required]
        [MaxLength(255)]
        public string? Nome { get; set; }

        [Required]
        [MaxLength(255)]
        public string? Email { get; set; }

        [Required]
        [MaxLength(255)]
        public string? Senha { get; set; }

        public byte[]? FotoPerfil { get; set; }
    }
}