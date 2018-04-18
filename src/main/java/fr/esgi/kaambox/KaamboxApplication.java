package fr.esgi.kaambox;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

@SpringBootApplication
@RestController
public class KaamboxApplication {
	
	private static final String MP3_EXT = ".mp3";
	
	private final Random random;
	private final Path soundsPath;
	
	public KaamboxApplication(@Value("${info.sounds.path}") final String soundsPath) {
		this.random = new Random();
		this.soundsPath = Paths.get(soundsPath);
	}
	
	private List<Path> getAllSounds(final Path directory) throws IOException {
		final List<Path> sounds = new ArrayList<>();
		Files.newDirectoryStream(directory).forEach(sounds::add);
		
		return sounds;
	}
	
	private Path getRandomSound(final List<Path> sounds) {
		final int index = random.nextInt(sounds.size());
		return sounds.get(index);
	}
	
	@GetMapping(value = "/", produces = APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getSound() throws IOException {
		final List<Path> sounds = getAllSounds(soundsPath);
		final Path randomSound = getRandomSound(sounds);
		
		return Files.readAllBytes(randomSound);
	}

	public static void main(String[] args) {
		SpringApplication.run(KaamboxApplication.class, args);
	}
}
