package com.bloidonia.asciiimage;

import com.bloidonia.asciiimage.parts.Element;
import com.bloidonia.asciiimage.parts.Pixel;
import com.bloidonia.asciiimage.parts.Row;
import com.bloidonia.asciiimage.parts.Type;
import com.bloidonia.asciiimage.utils.StreamUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AsciImageProcessor {
    private static final String TOKENS = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnpqrstuvwxyz";
    private final Map<Character,List<Pixel>> tokens;
    private final List<Element> elements;
    private final int rows;
    private final int columns;

    private AsciImageProcessor(List<String> lines) {
        List<String> cleaned = lines.stream().filter(s -> !s.isEmpty()).map(s -> s.replaceAll("\\s+", "")).collect(Collectors.toList());
        columns = maxStringLength(cleaned);
        rows = cleaned.size();

        if(rows == 0)    throw new RuntimeException("Lines cannot be empty");
        if(columns == 0) throw new RuntimeException("Lines must have some length");

        tokens = IntStream.range(0, rows)
                .mapToObj(row -> new Row(row, cleaned.get(row)))
                .flatMap(r -> IntStream.range(0, columns).mapToObj(col -> new Pixel(new Point2D.Double(col, r.getRow()), r.getRowString().charAt(col))))
                .filter(p -> TOKENS.indexOf(p.getCharacter()) > -1)
                .collect(Collectors.groupingBy(Pixel::getCharacter));

        elements = new ArrayList<>();
        List<String> tokenList = new ArrayList<>();
        tokenList.add(" ");
        tokenList.addAll(Arrays.asList(TOKENS.split("")));
        tokenList.add(" ");
        StreamUtils.collate(tokenList, 3, 1).stream().forEach(pcn -> {
            if (tokens.get(pcn.get(1).charAt(0)) != null) {
                if (tokens.get(pcn.get(1).charAt(0)).size() == 2) {
                    elements.add(new Element(tokens.get(pcn.get(1).charAt(0)).stream().map(Pixel::getPos).collect(Collectors.toList()), Type.LINE, pcn.get(1)));
                }
                else if (tokens.get(pcn.get(0).charAt(0)) != null && tokens.get(pcn.get(0).charAt(0)).size() == 1) {
                    Element old = elements.get(elements.size() - 1);
                    ArrayList<Point2D> newPoints = new ArrayList<>(old.getPoints());
                    newPoints.addAll(tokens.get(pcn.get(1).charAt(0)).stream().map(Pixel::getPos).collect(Collectors.toList()));
                    elements.set(elements.size() - 1, new Element(newPoints, Type.PATH, old.getName()));
                }
                else {
                    elements.add(new Element(tokens.get(pcn.get(1).charAt(0)).stream().map(Pixel::getPos).collect(Collectors.toList()),
                            tokens.get(pcn.get(1).charAt(0)).size() == 1 ? Type.POINT : Type.ELLIPSE, pcn.get(1)));
                }
            }

        });
    }

    Map<Character, List<Pixel>> getTokens() {
        return tokens;
    }

    List<Element> getElements() {
        return elements;
    }

    int getRows() {
        return rows;
    }

    int getColumns() {
        return columns;
    }

    public static AsciImageProcessor fromLines(List<String> lines) {
        return new AsciImageProcessor(lines);
    }

    public BufferedImageProcessor asBufferedImage() {
        return asBufferedImage(c -> {});
    }

    public BufferedImageProcessor asBufferedImage(Consumer<BufferedImageProcessor.BufferedImageConfig> bufferedImageConfigFunction) {
        return new BufferedImageProcessor(this.columns, this.rows, this.elements, bufferedImageConfigFunction);
    }

    private Integer maxStringLength(List<String> in) {
        return in.stream().max(Comparator.comparing(String::length)).orElseThrow(() -> new RuntimeException("")).length();
    }

    public static void main(String[] args) throws Exception {
        List<String> circle = Arrays.asList(
            "· · · · · 1 · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "· · · · · · · · · · ·",
            "1 · · · · · · · · · 1");
        BufferedImage img = AsciImageProcessor
                .fromLines(circle)
                .asBufferedImage(c -> c.type(BufferedImage.TYPE_INT_ARGB).graphics(g -> {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }))
                .render(e -> e.graphics(g -> {
                    g.setPaint(Color.DARK_GRAY);
                }));
        ImageIO.write(img, "png", new File("/tmp/circle.png"));

        List<String> arrowAndStuff = Arrays.asList(
                "· · · · · · · · · · · · ·",
                "· A · · · · · · · · A · D",
                "· · · · · · · · · · B · ·",
                "· · · · · · 1 · · · · · ·",
                "· · · · · · o o · · · · ·",
                "· · · 6 o o 7 o o · · · ·",
                "· · · o o o o o o 2 · · ·",
                "· · · 5 o o 4 o o · · · ·",
                "· · · · · · o o · · B · ·",
                "· · · · · · 3 · · · · · ·",
                "· · · · · · · G · · G · ·",
                "· · · · · · · · · · · · ·",
                "I · · · · · · G · · G · ·"
        );
        img = AsciImageProcessor
                .fromLines(arrowAndStuff)
                .asBufferedImage(c -> c.type(BufferedImage.TYPE_INT_ARGB).graphics(g -> {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                }))
                .render(e -> e.graphics(g -> {
                    g.setPaint(new Color((int) (Math.random() * 0xFFFFFF)));
                }));
        ImageIO.write(img, "png", new File("/tmp/arrowAndStuff.png"));
        System.out.println("BEETLES");
        List<String> beetles = Arrays.asList(
                "· · · · 3 · · · · · · · · · · · · ·",
                "· · · · · · · · · 7 · · · · · · · ·",
                "· · · · · · · · · · · · · · · · · ·",
                "· · · 1 3 · · 1 · · · · · · · · · ·",
                "5 · · 5 · · · · · · · · · · · B · ·",
                "· · · · · 2 · · · 7 · · · · 2 · · ·",
                "· · · · · · · · · · · · · · · · · ·",
                "· · · 1 · · · 1 · · · · · · · · · ·",
                "· · · · · · · · · · · B · 9 · · · ·",
                "· 8 · · · 8 · · · · · · · · · · · ·",
                "· · · · · · · · · · · · · · · · · ·",
                "· · · · · · · · C · · · · · · · · ·",
                "· · · · · · · · · · · · · · · · · 9",
                "· · · · · · · · A · · · · · · · · ·",
                "· · · · · 2 · · · · · · · · 2 · · ·",
                "· · · · C · · · · · · · · · · · · ·",
                "· · · · · · · · · · · · · · · · · ·",
                "· · · · · · · · · · · · A · · · · ·");
        img = AsciImageProcessor
                .fromLines(beetles)
                .asBufferedImage(c -> c.scale(5.0).type(BufferedImage.TYPE_INT_ARGB).graphics(g -> {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setPaint(Color.BLACK);
                }))
                .render();
        ImageIO.write(img, "png", new File("/tmp/beetles.png"));
    }
}
