package com.portfolio.config;

import com.portfolio.entity.*;
import com.portfolio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private AdminRepository adminRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TestimonialRepository testimonialRepository;
    @Autowired private MediaRepository mediaRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create default admin
        if (!adminRepository.existsByUsername("admin")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setEmail("admin@portfolio.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setFullName("Portfolio Admin");
            adminRepository.save(admin);
            System.out.println("✅ Default admin created: admin / Admin@123");
        }

        // Create default categories
        String[] cats = {"All","Cinematic","Gaming Edits","Reels","Motion Graphics","Social Media","Commercial"};
        String[] slugs = {"all","cinematic","gaming","reels","motion-graphics","social-media","commercial"};
        for (int i = 0; i < cats.length; i++) {
            if (categoryRepository.findByName(cats[i]).isEmpty()) {
                Category c = new Category();
                c.setName(cats[i]);
                c.setSlug(slugs[i]);
                c.setDisplayOrder(i);
                categoryRepository.save(c);
            }
        }

        // Create sample testimonials
        if (testimonialRepository.count() == 0) {
            String[][] testimonials = {
                {"Alex Rivera","Creative Director","Netflix","Working with this editor was a game-changer. The cinematic quality of our promo surpassed our expectations completely.","5"},
                {"Jordan Kim","Content Creator","YouTube","Absolutely insane editing skills. My gaming montages went from 50K to 500K views after we collaborated.","5"},
                {"Priya Sharma","Marketing Head","Adidas India","The motion graphics for our campaign were stunning. Delivered ahead of schedule with zero revisions needed.","5"},
                {"Marcus Chen","Film Producer","IndieFilms Co.","Exceptional storytelling through editing. This editor understands pacing, rhythm, and emotion like no one else.","5"}
            };
            for (String[] t : testimonials) {
                Testimonial test = new Testimonial();
                test.setClientName(t[0]);
                test.setClientTitle(t[1]);
                test.setClientCompany(t[2]);
                test.setContent(t[3]);
                test.setRating(Integer.parseInt(t[4]));
                test.setIsFeatured(true);
                testimonialRepository.save(test);
            }
            System.out.println("✅ Sample testimonials created");
        }

        // Sample media entries (using YouTube embeds)
        if (mediaRepository.count() == 0) {
            Category cinematic = categoryRepository.findBySlug("cinematic").orElse(null);
            Category gaming = categoryRepository.findBySlug("gaming").orElse(null);
            Category reels = categoryRepository.findBySlug("reels").orElse(null);
            Category motion = categoryRepository.findBySlug("motion-graphics").orElse(null);

            createSampleMedia("Cinematic Travel Montage - Bali 2024", 
                "A breathtaking cinematic journey through Bali's temples and rice terraces.",
                "https://www.youtube.com/embed/dQw4w9WgXcQ", cinematic, true, true, "cinematic,travel,bali");
            createSampleMedia("FPS Gaming Highlights - Season 12",
                "Insane clutch plays and quad kills from competitive ranked matches.",
                "https://www.youtube.com/embed/dQw4w9WgXcQ", gaming, true, false, "gaming,fps,highlights");
            createSampleMedia("Instagram Reels - Fashion Collection",
                "Dynamic fashion reels with trendy transitions and color grading.",
                "https://www.youtube.com/embed/dQw4w9WgXcQ", reels, false, true, "reels,fashion,social");
            createSampleMedia("Motion Graphics - Brand Identity Package",
                "Complete brand animation package with logo reveal and transitions.",
                "https://www.youtube.com/embed/dQw4w9WgXcQ", motion, true, true, "motion,graphics,brand");
            createSampleMedia("Short Film - The Last Light",
                "Award-winning short film edit with custom color grade and sound design.",
                "https://www.youtube.com/embed/dQw4w9WgXcQ", cinematic, false, true, "shortfilm,cinematic,drama");
            createSampleMedia("Esports Fragmovie - VALORANT",
                "High-energy fragmovie with custom transitions and beat-sync editing.",
                "https://www.youtube.com/embed/dQw4w9WgXcQ", gaming, true, false, "gaming,valorant,esports");
            System.out.println("✅ Sample media created");
        }
    }

    private void createSampleMedia(String title, String desc, String url, Category cat, boolean featured, boolean trending, String tags) {
        Media m = new Media();
        m.setTitle(title);
        m.setDescription(desc);
        m.setVideoUrl(url);
        m.setCategory(cat);
        m.setIsFeatured(featured);
        m.setIsTrending(trending);
        m.setTags(tags);
        m.setViewCount((long)(Math.random() * 50000 + 1000));
        m.setLikeCount((long)(Math.random() * 2000 + 100));
        m.setMediaType(Media.MediaType.VIDEO);
        m.setDuration("3:24");
        mediaRepository.save(m);
    }
}
