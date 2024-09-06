package dev.mccue.plugindemo.circle;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.math.BigDecimalMath;
import dev.mccue.plugindemo.api.Entity;

import java.awt.*;
import java.util.function.Predicate;

public final class CircleEntity implements Entity {
    @Override
    public void draw(Graphics2D graphics, int x, int y) {
        graphics = (Graphics2D) graphics.create();
        graphics.setColor(Color.BLACK);
        graphics.fillOval(x, y, 60, 60);
        graphics.drawString(
                // subtypeOf is only in newer guavas
                Predicates.subtypeOf(String.class).toString(),
                x,
                y
        );
    }
}
