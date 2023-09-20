using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
    public class Posts
    {
        [Key]
        public int PostID { get; set; }

        [Required]
        public int UserID { get; set; }

        public string Conteudo { get; set; }

        [MaxLength(50)]
        public string TipoConteudo { get; set; }

        public byte[] ArquivoConteudo { get; set; }

        public DateTime DataPostagem { get; set; }
    }
}