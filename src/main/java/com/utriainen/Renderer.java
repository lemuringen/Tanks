package com.utriainen;


import com.utriainen.models.drawables.Drawable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    Canvas canvas;
    GraphicsContext context;
    Image background;
    List<Drawable> drawables = new ArrayList<>();

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        this.context = canvas.getGraphicsContext2D();
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    public void removeDrawable(Drawable drawable) {
        drawables.remove(drawable);
    }

    public void clearDrawables() {
        drawables.clear();
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public void render() {
        context.save();
        if (background != null) {
            context.drawImage(background, 0, 0);
        }
        for (Drawable drawable : drawables) {
            // transformContext(drawable);
            drawable.draw(context);

        }
        context.restore();
    }

    public void prepare() {
        context.setFill(new Color(0.68, 0.68, 0.68, 1.0));
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void transformContext(Drawable drawable) {
        //  Point2D centre = drawable.getCenter();
        // Rotate r = new Rotate(drawable.getRotation(), centre.getX(), centre.getY());
        //   context.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}