## Java 8 copy of the ASCIImage project

For original, see: http://cocoamine.net/blog/2015/03/20/replacing-photoshop-with-nsstring/

Todo:
=====

1. Scaling doesn't work right (pixels on the extremes of the image, don't stay there
2. Need to output JavaFX Groups

Examples:
=========

A filled circle:

![Circle](https://raw.githubusercontent.com/timyates/ascii-image/master/examples/circle.png)

```java
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
            g.setPaint(Color.RED);
        }));
ImageIO.write(img, "png", new File("/tmp/circle.png"));
```

A beetle:

![Beetle](https://raw.githubusercontent.com/timyates/ascii-image/master/examples/beetles.png)

```java
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
```
