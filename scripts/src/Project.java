import dev.mccue.tools.java.Java;
import dev.mccue.tools.javac.Javac;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Comparator;

@CommandLine.Command(name = "project")
public class Project {
    static final Path BUILD = Path.of("build");
    static final String MODULE_SOURCE_PATH = "./modules/*/src";

    boolean cleaned = false;
    @CommandLine.Command(name = "clean")
    public void clean() throws Exception {
        if (!cleaned && Files.exists(BUILD)) {
            try (var pathStream = Files.walk(BUILD)) {
                var paths = pathStream.sorted(Comparator.reverseOrder()).toList();
                for (var path : paths) {
                    Files.deleteIfExists(path);
                }
            }
            cleaned = true;
        }
    }

    @CommandLine.Command(
            name = "compile",
            description = "Compiles all modules"
    )
    public void compile() throws Exception {
        clean();

        Javac.run(arguments -> {
            arguments
                    ._d(BUILD.resolve("javac"))
                    ._g()
                    .__module_source_path(MODULE_SOURCE_PATH)
                    .__release(21)
                    .__module(
                            "dev.mccue.plugindemo.api",
                            "dev.mccue.plugindemo.game",
                            "dev.mccue.plugindemo.square",
                            "dev.mccue.plugindemo.circle"
                    );
        });
    }

    @CommandLine.Command(
            name = "run",
            description = "Runs the code"
    )
    public void run() throws Exception {
        compile();
        Java.run(arguments -> {
            arguments
                    .__module_path(BUILD.resolve("javac"))
                    .__add_modules("ALL-MODULE-PATH")
                    .__module("dev.mccue.plugindemo.game/dev.mccue.plugindemo.game.Main");
        });
    }

    public static void main(String[] args) {
        new CommandLine(new Project()).execute(args);
    }
}