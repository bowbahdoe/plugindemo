package dev.mccue.plugindemo.circle;

import dev.mccue.plugindemo.api.Entity;

import java.awt.*;

public final class CircleEntity implements Entity {
    @Override
    public void draw(Graphics2D graphics, int x, int y) {
        graphics.setColor(Color.GREEN);
        graphics.drawOval(x, y, 60, 60);
        graphics.drawString(com.google.common.escape.Escaper.class.toString(), x, y);
    }
}
