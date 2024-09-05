package dev.mccue.plugindemo.square;

import dev.mccue.plugindemo.api.Entity;

import java.awt.*;

public final class SquareEntity implements Entity {
    @Override
    public void draw(Graphics2D graphics, int x, int y) {
        graphics.setColor(Color.BLUE);
        graphics.fillRect(x, y, 60, 60);

        graphics.drawString(
                org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement.class.getSimpleName(),
                x,
                y
        );
    }
}
