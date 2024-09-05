package dev.mccue.plugindemo.game;

import javax.swing.*;
import java.awt.*;
import java.util.ServiceLoader;
import dev.mccue.plugindemo.api.Entity;

public final class Main {
    public static void main(String[] args) {
        var frame = new JFrame("game");

        var entities = ServiceLoader.load(Entity.class)
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

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
