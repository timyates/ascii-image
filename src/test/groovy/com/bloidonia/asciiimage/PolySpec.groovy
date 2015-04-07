package com.bloidonia.asciiimage

import com.bloidonia.asciiimage.parts.Element
import com.bloidonia.asciiimage.parts.Type
import spock.lang.Specification

import java.awt.geom.Point2D

import static java.awt.Color.BLACK
import static java.awt.Color.WHITE

class PolySpec extends Specification {
    def input = '''· · · ·
                  |· 1 2 ·
                  |· 4 3 ·
                  |· · · ·'''.stripMargin().readLines()

    List<Integer> expected = [WHITE, WHITE, WHITE, WHITE,
                              WHITE, BLACK, BLACK, WHITE,
                              WHITE, BLACK, BLACK, WHITE,
                              WHITE, WHITE, WHITE, WHITE]*.getRGB()

    List<Element> expectedElements = [new Element([new Point2D.Double(1, 1), new Point2D.Double(2, 1), new Point2D.Double(2, 2), new Point2D.Double(1, 2)], Type.PATH, '1')]

    def "loads a list of strings, and creates the expected elements"() {
        when:
            def result = AsciImageProcessor.fromLines(input)
        then:
            result.elements == expectedElements
    }

    def "loads a list of strings, and creates a BufferedImage"() {
        when:
            def result = AsciImageProcessor.fromLines(input)
                    .asBufferedImage {c ->
                        c.graphics {g ->
                            g.paint = WHITE
                            g.fillRect(0, 0, c.width, c.height)
                            g.paint = BLACK
                        }
                    }
                    .render()

        then:
            result.height == 4
            result.width  == 4
            result.raster.dataBuffer.data == expected
    }
}
