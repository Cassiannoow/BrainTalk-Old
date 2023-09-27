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
        public DbSet<Friend> Friends { get; set; }
        public DbSet<Posts> Posts { get; set; }
        public DbSet<Like> Likes { get; set; }
        public DbSet<DiscussionTopic> DiscussionTopics { get; set; }
        public DbSet<Response> Responses { get; set; }
        public DbSet<StudyMaterial> StudyMaterial { get; set; }
        public DbSet<DiscussionCategory> DiscussionCategory { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
	        {
	            modelBuilder.HasDefaultSchema("BrainTalk");    
        }
    }
}