package dev.mccue.plugindemo.game;

import javax.swing.*;
import java.awt.*;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import dev.mccue.plugindemo.api.Entity;

public final class Main {
    public static void main(String[] args) throws Exception {
        Path pluginDir = Path.of("plugins");

        List<ModuleLayer> moduleLayers = new ArrayList<>();
        moduleLayers.add(ModuleLayer.boot());

        try (var pluginPaths = Files.list(pluginDir)) {
            for (var pluginPath : pluginPaths.toList()) {
                var moduleFinder = ModuleFinder.of(pluginPath);
                var moduleNames = moduleFinder.findAll()
                        .stream()
                        .map(ModuleReference::descriptor)
                        .map(ModuleDescriptor::name)
                        .map(f -> {
                            System.out.println(f);
                            return f;
                        })
                        .collect(Collectors.toSet());

                var parentLayer = ModuleLayer.boot();
                var configuration = parentLayer.configuration()
                        .resolve(moduleFinder, ModuleFinder.of(), moduleNames);
                var systemClassLoader = ClassLoader.getSystemClassLoader();
                moduleLayers.add(parentLayer.defineModulesWithOneLoader(
                        configuration, systemClassLoader
                ));
            }
        }

        var entities = moduleLayers
                .stream()
                .flatMap(moduleLayer ->
                        ServiceLoader.load(moduleLayer, Entity.class)
                                .stream()
                                .map(ServiceLoader.Provider::get)
                )
                .toList();

        var panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                for (int i = 0; i < entities.size(); i++) {
                    entities.get(i).draw((Graphics2D) g, 50 + i * 100, 50 + i * 100);
                }
            }
        };

        panel.setPreferredSize(new Dimension(
                new Dimension(
                        200 + entities.size() * 100,
                        200 + entities.size() * 100
                )
        ));

        var frame = new JFrame("game");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
