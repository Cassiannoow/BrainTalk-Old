using Microsoft.EntityFrameworkCore;
using BrainTalkAPI.Models;
using System.Diagnostics.CodeAnalysis;

namespace BrainTalkAPI.Data
{
    public class BrainTalkContext: DbContext
    {
        protected readonly IConfiguration Configuration;
        public BrainTalkContext(IConfiguration configuration)
        {
            Configuration = configuration;
        }
        protected override void OnConfiguring(DbContextOptionsBuilder options)
        {
            options.UseSqlServer(Configuration.GetConnectionString("StringConexaoSQLServer"));
        }

        public DbSet<User> Users {get; set;}
        public DbSet<Friend> Friend { get; set; }
        public DbSet<Posts> Posts { get; set; }
        public DbSet<Like> Like { get; set; }
        public DbSet<DiscussionTopic> DiscussionTopic { get; set; }
        public DbSet<Response> Response { get; set; }
        public DbSet<StudyMaterial> StudyMaterial { get; set; }
        public DbSet<DiscussionCategory> DiscussionCategory { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
	        {
	            modelBuilder.HasDefaultSchema("BrainTalk");    
        }
    }
}