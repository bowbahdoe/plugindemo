import dev.mccue.tools.jar.Jar;
import dev.mccue.tools.java.Java;
import dev.mccue.tools.javac.Javac;
import dev.mccue.tools.jresolve.JResolve;
import picocli.CommandLine;

import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

@CommandLine.Command(name = "project")
public class Project {
    static final Path BUILD = Path.of("build");
    static final Path PLUGINS = Path.of("plugins");
    static final Path MODULES = Path.of("modules");
    static final String MODULE_SOURCE_PATH = "./modules/*/src";

    boolean cleaned = false;
    @CommandLine.Command(name = "clean")
    public void clean() throws Exception {
        if (!cleaned) {
            if (Files.exists(BUILD)) {
                try (var pathStream = Files.walk(BUILD)) {
                    var paths = pathStream.sorted(Comparator.reverseOrder()).toList();
                    for (var path : paths) {
                        Files.deleteIfExists(path);
                    }
                }
            }

            if (Files.exists(PLUGINS)) {
                try (var pathStream = Files.walk(PLUGINS)) {
                    var paths = pathStream.sorted(Comparator.reverseOrder()).toList();
                    for (var path : paths) {
                        Files.deleteIfExists(path);
                    }
                }
            }

            cleaned = true;
        }
    }

    @CommandLine.Command(
            name = "install",
            description = "Install dependencies"
    )
    public void install() throws Exception {
        JResolve.run(arguments -> {
            arguments
                    .__output_directory(MODULES.resolve("dev.mccue.plugindemo.square/libs"))
                    .__use_module_names()
                    .__purge_output_directory()
                    .argumentFile(MODULES.resolve("dev.mccue.plugindemo.square/libs.txt"));
        });

        JResolve.run(arguments -> {
            arguments
                    .__output_directory(MODULES.resolve("dev.mccue.plugindemo.circle/libs"))
                    .__use_module_names()
                    .__purge_output_directory()
                    .argumentFile(MODULES.resolve("dev.mccue.plugindemo.circle/libs.txt"));
        });
    }

    @CommandLine.Command(
            name = "compile",
            description = "Compiles all modules"
    )
    public void compile() throws Exception {
        clean();
        install();

        Javac.run(arguments -> {
            arguments
                    ._d(BUILD.resolve("javac"))
                    ._g()
                    .__module_source_path(MODULE_SOURCE_PATH)
                    .__release(21)
                    .__module(
                            "dev.mccue.plugindemo.api",
                            "dev.mccue.plugindemo.game"
                    );
        });

        Javac.run(arguments -> {
            arguments
                    ._d(BUILD.resolve("javac"))
                    ._g()
                    .__module_source_path(MODULE_SOURCE_PATH)
                    .__release(21)
                    .__module_path(MODULES.resolve("dev.mccue.plugindemo.square/libs"))
                    .__module(
                            "dev.mccue.plugindemo.square"
                    );
        });

        Javac.run(arguments -> {
            arguments
                    ._d(BUILD.resolve("javac"))
                    ._g()
                    .__module_source_path(MODULE_SOURCE_PATH)
                    .__release(21)
                    .__module_path(MODULES.resolve("dev.mccue.plugindemo.circle/libs"))
                    .__module(
                            "dev.mccue.plugindemo.circle"
                    );
        });
    }

    @CommandLine.Command(
            name = "package",
            description = "Packages all modules"
    )
    public void package_() throws Exception {
        compile();

        Jar.run(arguments -> {
            arguments.__create()
                    .__file(BUILD.resolve("jar/game/dev.mccue.plugindemo.api.jar"))
                    ._C(BUILD.resolve("javac/dev.mccue.plugindemo.api"), ".");
        });


        Jar.run(arguments -> {
            arguments.__create()
                    .__file(BUILD.resolve("jar/game/dev.mccue.plugindemo.game.jar"))
                    ._C(BUILD.resolve("javac/dev.mccue.plugindemo.game"), ".");
        });

        Jar.run(arguments -> {
            arguments.__create()
                    .__file(PLUGINS.resolve("square/dev.mccue.plugindemo.square.jar"))
                    ._C(BUILD.resolve("javac/dev.mccue.plugindemo.square"), ".");
        });

        try (var libsStream = Files.list(MODULES.resolve("dev.mccue.plugindemo.square/libs"))) {
            for (var lib : libsStream.toList()) {
                Files.copy(
                        lib,
                        Path.of(PLUGINS.resolve("square").toString(), lib.getFileName().toString()),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        }

        Jar.run(arguments -> {
            arguments.__create()
                    .__file(PLUGINS.resolve("circle/dev.mccue.plugindemo.circle.jar"))
                    ._C(BUILD.resolve("javac/dev.mccue.plugindemo.circle"), ".");
        });

        try (var libsStream = Files.list(MODULES.resolve("dev.mccue.plugindemo.circle/libs"))) {
            for (var lib : libsStream.toList()) {
                Files.copy(
                        lib,
                        Path.of(PLUGINS.resolve("circle").toString(), lib.getFileName().toString()),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        }
    }

    @CommandLine.Command(
            name = "run",
            description = "Runs the code"
    )
    public void run() throws Exception {
        package_();
        Java.run(arguments -> {
            arguments
                    .__module_path(BUILD.resolve("jar/game"))
                    .__add_modules("ALL-MODULE-PATH")
                    .__module("dev.mccue.plugindemo.game/dev.mccue.plugindemo.game.Main");
        });
    }

    public static void main(String[] args) {
        new CommandLine(new Project()).execute(args);
    }
}
