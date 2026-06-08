package com.fixmate.config;

import com.fixmate.model.ServiceEntity;
import com.fixmate.model.User;
import com.fixmate.repository.ServiceRepository;
import com.fixmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .name("Admin")
                    .email("admin@fixmate.com")
                    .password("admin123")
                    .role(User.Role.ADMIN)
                    .build());

            userRepository.save(User.builder()
                    .name("Worker")
                    .email("worker@fixmate.com")
                    .password("worker123")
                    .role(User.Role.WORKER)
                    .build());

            System.out.println("[DataSeeder] Users seeded.");
        }

        if (serviceRepository.count() == 0) {
            serviceRepository.save(ServiceEntity.builder()
                    .name("Plumbing")
                    .description("Pipe and water leakage repair")
                    .build());

            serviceRepository.save(ServiceEntity.builder()
                    .name("Electrical")
                    .description("Wiring, fuse, and electrical fault repair")
                    .build());

            serviceRepository.save(ServiceEntity.builder()
                    .name("Cleaning")
                    .description("Home and office deep cleaning")
                    .build());

            System.out.println("[DataSeeder] Services seeded.");
        }
    }
}
