package dev.mccue.plugindemo.game;

import javax.swing.*;
import java.awt.*;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import dev.mccue.plugindemo.api.Entity;

public final class Main {
    public static void main(String[] args) {
        Path pluginPath = null;
        if (args.length > 0) {
            pluginPath = Path.of(args[0]);
        }

        ModuleLayer moduleLayer;
        if (pluginPath == null) {
            moduleLayer = ModuleLayer.boot();
        }
        else {
            var moduleFinder = ModuleFinder.of(pluginPath);
            var moduleNames = moduleFinder.findAll()
                    .stream()
                    .map(ModuleReference::descriptor)
                    .map(ModuleDescriptor::name)
                    .collect(Collectors.toSet());

            var parentLayer = ModuleLayer.boot();
            var configuration = parentLayer.configuration()
                    .resolve(moduleFinder, ModuleFinder.of(), moduleNames);
            var systemClassLoader = ClassLoader.getSystemClassLoader();
            moduleLayer = parentLayer.defineModulesWithOneLoader(
                    configuration, systemClassLoader
            );
        }

        var entities = ServiceLoader.load(moduleLayer, Entity.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();

        var panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                for (int i = 0; i < entities.size(); i++) {
                    entities.get(i).draw((Graphics2D) g, i * 100, i * 100);
                }
            }
        };

        panel.setPreferredSize(new Dimension(
                new Dimension(
                        entities.size() * 100,
                        entities.size() * 100
                )
        ));

        var frame = new JFrame("game");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
