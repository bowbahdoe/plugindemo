package dev.mccue.plugindemo.square;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import dev.mccue.plugindemo.api.Entity;

import java.awt.*;

public final class SquareEntity implements Entity {
    @Override
    public void draw(Graphics2D graphics, int x, int y) {
        graphics = (Graphics2D) graphics.create();
        graphics.setColor(Color.BLUE);
        graphics.fillRect(x, y, 60, 60);

        graphics.drawString(
                // assignableFrom is only in older guavas
                Predicates.assignableFrom(String.class).toString(),
                x,
                y
        );
    }
}
