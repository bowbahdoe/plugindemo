package dev.mccue.plugindemo.circle;

import com.google.common.collect.ImmutableList;
import dev.mccue.plugindemo.api.Entity;

import java.awt.*;

public final class CircleEntity implements Entity {
    @Override
    public void draw(Graphics2D graphics, int x, int y) {
        graphics = (Graphics2D) graphics.create();
        graphics.setColor(Color.BLACK);
        graphics.fillOval(x, y, 60, 60);
        graphics.drawString(
                ImmutableList.class.getSimpleName() + ": " + ImmutableList.class.hashCode(),
                x,
                y
        );
    }
}
