package com.bloidonia.asciiimage

import com.bloidonia.asciiimage.parts.Element
import com.bloidonia.asciiimage.parts.Type
import spock.lang.Specification

import java.awt.geom.Point2D

import static java.awt.Color.*

class LineSpec extends Specification {
    def input = '''1 · · 1
                  |4 · · 2
                  |4 · · ·
                  |3 · 3 2'''.stripMargin().readLines()

    List<Integer> expected = [WHITE, WHITE, WHITE, WHITE,
                              WHITE, BLACK, BLACK, WHITE,
                              WHITE, BLACK, BLACK, WHITE,
                              WHITE, WHITE, WHITE, WHITE]*.getRGB()

    List<Element> expectedElements = [new Element([new Point2D.Double(0, 0), new Point2D.Double(3, 0)], Type.LINE, '1'),
                                      new Element([new Point2D.Double(3, 1), new Point2D.Double(3, 3)], Type.LINE, '2'),
                                      new Element([new Point2D.Double(0, 3), new Point2D.Double(2, 3)], Type.LINE, '3'),
                                      new Element([new Point2D.Double(0, 1), new Point2D.Double(0, 2)], Type.LINE, '4')]

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
                            g.paint = BLACK
                            g.fillRect(0, 0, c.width, c.height)
                            g.paint = WHITE
                        }
                    }
                    .render()

        then:
            result.height == 4
            result.width  == 4
            result.raster.dataBuffer.data == expected
    }
}
