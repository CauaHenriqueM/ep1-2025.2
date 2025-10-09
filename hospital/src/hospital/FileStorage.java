package hospital;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final Path base = Paths.get(".");

    public void ensureFilesExist() throws IOException {
        String[] files = {"patients.txt","doctors.txt","plans.txt","consultations.txt","internments.txt","rooms.txt"};
        for (String f : files) {
            Path p = base.resolve(f);
            if (!Files.exists(p)) Files.createFile(p);
        }
    }

    public List<String> readAllLines(String filename) throws IOException {
        Path p = base.resolve(filename);
        if (!Files.exists(p)) return new ArrayList<>();
        return Files.readAllLines(p);
    }

    public void writeAllLines(String filename, List<String> lines) {
        Path p = base.resolve(filename);
        try {
            Files.write(p, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo " + filename + ": " + e.getMessage());
        }
    }
}
