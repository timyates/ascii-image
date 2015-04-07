package com.bloidonia.asciiimage.parts;

import java.awt.geom.Point2D;

public class Pixel {
    private final Point2D pos;
    private final Character character;

    public Pixel(Point2D pos, Character character) {
        this.pos = pos;
        this.character = character;
    }

    public Point2D getPos() {
        return pos;
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return "Pixel{" + pos.getX() + "," + pos.getY() + " = '" + character + "'}";
    }
}
