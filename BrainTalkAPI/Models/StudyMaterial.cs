using System.ComponentModel.DataAnnotations;

namespace BrainTalkAPI.Models
{
    public class StudyMaterial
    {
        [Key]
        public int MaterialID { get; set; }

        [Required]
        public int UserID { get; set; }

        [Required]
        [MaxLength(255)]
        public string TituloMaterial { get; set; }

        public string DescricaoMaterial { get; set; }

        [MaxLength(50)]
        public string Formato { get; set; }

        public byte[] Arquivo { get; set; }

        public DateTime DataUpload { get; set; }
    }
}