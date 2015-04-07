package com.bloidonia.asciiimage;

import com.bloidonia.asciiimage.parts.Element;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Consumer;

public class BufferedImageProcessor {
    private final List<Element> elements;
    private final BufferedImage image;
    private final Graphics2D graphics;
    private final int width;
    private final int height;

    public BufferedImageProcessor(int width, int height, List<Element> elements) {
        this(width, height, elements, c -> {});
    }

    public BufferedImageProcessor(int width, int height, List<Element> elements, Consumer<BufferedImageConfig> configConsumer) {
        this.width = width;
        this.height = height;
        this.elements = elements;
        BufferedImageConfig cfg = new BufferedImageConfig();
        configConsumer.accept(cfg);
        double scale = cfg.scale;
        this.image = new BufferedImage((int)(width * scale), (int)(height * scale), cfg.type);
        this.graphics = image.createGraphics();
        graphics.setTransform(AffineTransform.getScaleInstance(cfg.scale, cfg.scale));
        cfg.graphics.accept(graphics);
    }

    public BufferedImage render() {
        return render(e -> {});
    }

    public BufferedImage render(Consumer<BufferedImageElementConfig> elementConfigConsumer) {
        BufferedImageElementConfig cfg = new BufferedImageElementConfig();
        elementConfigConsumer.accept(cfg);
        elements.stream().map(e -> {
            switch (e.getType()) {
                case LINE:
                    return new Line2D.Double(e.getPoints().get(0), e.getPoints().get(1));
                case POINT:
                    return new Line2D.Double(e.getPoints().get(0), e.getPoints().get(0));
                case PATH:
                    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, e.getPoints().size());
                    path.moveTo(e.getPoints().get(0).getX(), e.getPoints().get(0).getY());
                    e.getPoints().stream().skip(1).forEach(p -> path.lineTo(p.getX(), p.getY()));
                    path.closePath();
                    return path;
                case ELLIPSE:
                    return new Ellipse2D.Double(e.minX(), e.minY(), e.maxX() - e.minX(), e.maxY() - e.minY());
                default:
                    throw new RuntimeException("Unknown type $element.type");
            }
        }).forEach(shape -> {
            cfg.graphics.accept(graphics);
            graphics.draw(shape);
            graphics.fill(shape);
        });
        graphics.dispose();
        return image;
    }

    class BufferedImageElementConfig {
        private Consumer<Graphics2D> graphics;

        public BufferedImageElementConfig() {
            this.graphics = g -> {};
        }

        public BufferedImageElementConfig graphics(Consumer<Graphics2D> graphics2DConsumer) {
            this.graphics = graphics2DConsumer;
            return this;
        }
    }

    class BufferedImageConfig {
        private double scale;
        private int type;
        private Consumer<Graphics2D> graphics;

        public BufferedImageConfig() {
            this.scale = 1.0;
            this.type = BufferedImage.TYPE_INT_RGB;
            this.graphics = g -> {};
        }

        public BufferedImageConfig scale(double scale) {
            this.scale = scale;
            return this;
        }

        public BufferedImageConfig type(int type) {
            this.type = type;
            return this;
        }

        public BufferedImageConfig graphics(Consumer<Graphics2D> graphics2DConsumer) {
            this.graphics = graphics2DConsumer;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
