package com.utriainen.models.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface Drawable {
    void draw(GraphicsContext context);
    Color getColor();
}
