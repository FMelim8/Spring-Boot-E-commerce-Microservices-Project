package com.example.gamecatalog.seeders;

import com.example.gamecatalog.models.Game;
import com.example.gamecatalog.repository.GameRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@Log4j2
public class GameSeeder implements CommandLineRunner {

    @Autowired
    GameRepository gameRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${service.name}")
    String serviceName;

    @Override
    public void run(String... args) throws Exception {
        loadGames();
    }

    private void loadGames() {
        if(gameRepository.findAllDeleted().isEmpty()) {
            List<Game> tempGameList = new ArrayList<>();
            tempGameList.add(new Game("Elden Ring", "An open-world action RPG set in the Lands Between.", "RPG", "PC", BigDecimal.valueOf(59.99), 500, LocalDate.of(2022, 2, 25)));
            tempGameList.add(new Game("God of War Ragnarok", "Kratos and Atreus face Ragnarok in this Norse mythology epic.", "Action", "PS5",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2022, 11, 9)));
            tempGameList.add(new Game("The Legend of Zelda: Tears of the Kingdom", "Explore a vast world above and below Hyrule.", "Adventure", "Nintendo Switch",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 5, 12)));
            tempGameList.add(new Game("Cyberpunk 2077", "A futuristic RPG set in Night City.", "RPG", "PC",  BigDecimal.valueOf(49.99), 500, LocalDate.of(2020, 12, 10)));
            tempGameList.add(new Game("Red Dead Redemption 2", "An epic Western story in an open world.", "Action", "PC",  BigDecimal.valueOf(39.99), 500, LocalDate.of(2018, 10, 26)));
            tempGameList.add(new Game("Spider-Man 2", "Peter Parker and Miles Morales battle new villains.", "Action", "PS5",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 10, 20)));
            tempGameList.add(new Game("Hogwarts Legacy", "An open-world RPG set in the Wizarding World.", "RPG", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 2, 10)));
            tempGameList.add(new Game("Baldur's Gate 3", "A deep RPG based on Dungeons & Dragons.", "RPG", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 8, 3)));
            tempGameList.add(new Game("Final Fantasy XVI", "The latest epic in the Final Fantasy series.", "RPG", "PS5",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 6, 22)));
            tempGameList.add(new Game("Resident Evil 4 Remake", "A remake of the classic survival horror game.", "Horror", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 3, 24)));
            tempGameList.add(new Game("Starfield", "Explore space in Bethesda's massive sci-fi RPG.", "RPG", "PC",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 9, 6)));
            tempGameList.add(new Game("The Witcher 3: Wild Hunt", "Geralt of Rivia’s journey in a vast open world.", "RPG", "PC",  BigDecimal.valueOf(29.99), 500, LocalDate.of(2015, 5, 19)));
            tempGameList.add(new Game("FIFA 24", "The latest installment of the FIFA franchise.", "Sports", "PS5",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 9, 29)));
            tempGameList.add(new Game("NBA 2K24", "Basketball simulation game with realistic gameplay.", "Sports", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 9, 8)));
            tempGameList.add(new Game("Forza Horizon 5", "An open-world racing game set in Mexico.", "Racing", "PC",  BigDecimal.valueOf(49.99), 500, LocalDate.of(2021, 11, 9)));
            tempGameList.add(new Game("Gran Turismo 7", "Realistic racing simulator with stunning visuals.", "Racing", "PS5",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2022, 3, 4)));
            tempGameList.add(new Game("Mortal Kombat 1", "A reboot of the iconic fighting franchise.", "Fighting", "PC",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 9, 19)));
            tempGameList.add(new Game("Tekken 8", "The latest installment in the Tekken fighting series.", "Fighting", "PC",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2024, 1, 26)));
            tempGameList.add(new Game("Street Fighter 6", "A new era of the legendary fighting franchise.", "Fighting", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 6, 2)));
            tempGameList.add(new Game("Call of Duty: Modern Warfare III", "A high-paced military shooter.", "Shooter", "PC",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 11, 10)));
            tempGameList.add(new Game("Battlefield 2042", "A large-scale multiplayer shooter.", "Shooter", "PC",  BigDecimal.valueOf(49.99), 500, LocalDate.of(2021, 11, 19)));
            tempGameList.add(new Game("Halo Infinite", "Master Chief returns in a new sci-fi FPS.", "Shooter", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2021, 12, 8)));
            tempGameList.add(new Game("Dead Space Remake", "A horror sci-fi remake of the classic game.", "Horror", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 1, 27)));
            tempGameList.add(new Game("Alan Wake 2", "A psychological thriller sequel.", "Horror", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 10, 17)));
            tempGameList.add(new Game("Lies of P", "A dark Souls-like game based on Pinocchio.", "RPG", "PC",  BigDecimal.valueOf(59.99), 500, LocalDate.of(2023, 9, 19)));
            tempGameList.add(new Game("Assassin's Creed Mirage", "A return to classic Assassin's Creed gameplay.", "Action", "PC",  BigDecimal.valueOf(49.99), 500, LocalDate.of(2023, 10, 5)));
            tempGameList.add(new Game("Horizon Forbidden West", "A futuristic open-world RPG with stunning visuals.", "RPG", "PS5",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2022, 2, 18)));
            tempGameList.add(new Game("Diablo IV", "A dark action RPG with deep character customization.", "RPG", "PC",  BigDecimal.valueOf(69.99), 500, LocalDate.of(2023, 6, 6)));
            tempGameList.add(new Game("Persona 5 Royal", "An enhanced version of the beloved JRPG.", "RPG", "PC",  BigDecimal.valueOf(49.99), 500, LocalDate.of(2020, 3, 31)));

            gameRepository.saveAll(tempGameList);

            logger.info("Game Seeding",
                kv("Service", serviceName),
                kv("Operation", "Seed")
            );
        }
        logger.info("Game Count",
            kv("Service", serviceName),
            kv("Operation", "countCheck"),
            kv("GameCount", gameRepository.count())
        );
    }
}