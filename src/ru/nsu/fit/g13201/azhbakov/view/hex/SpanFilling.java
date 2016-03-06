package ru.nsu.fit.g13201.azhbakov.view.hex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Martin on 06.03.2016.
 */
public class SpanFilling {

    public static void fill (BufferedImage bf, int seedX, int seedY, Color newColor) {
        LinkedList<Span> stack = new LinkedList<Span>();
        try {
            int oldColor = getSeedColor(bf, seedX, seedY);
            Span span = getSpan(bf, seedX, seedY, oldColor);
            stack.offerLast(span);
            while (!stack.isEmpty()) {
                span = stack.pop();
                fillSpan(bf, span, newColor);
                addTopSpans (bf, stack, span, oldColor);
                addBottomSpans (bf, stack, span, oldColor);
            }
        } catch (WrongSeedException ex) {
            ex.printStackTrace();
        }
    }

    private static void fillSpan (BufferedImage bf, Span span, Color newColor) {
        for (int i = span.edge1+1; i < span.edge2; i++) {
            bf.setRGB(i, span.level, newColor.getRGB());
        }
    }

    private static int getSeedColor (BufferedImage bf, int seedX, int seedY) throws WrongSeedException {
        if (seedX < 0 || bf.getWidth() <= seedX ||
                seedY < 0 || bf.getHeight() <= seedY) throw new WrongSeedException();
        return bf.getRGB(seedX, seedY);
    }

    private static Span getSpan (BufferedImage bf, int seedX, int seedY, int oldColor) throws WrongSeedException {
        int x1, x2;

        int innerColor = getSeedColor(bf, seedX, seedY);
        if (innerColor != oldColor) {
            return null;
        }

        int pos = seedX;
        while (bf.getRGB(pos, seedY) == innerColor) {
            if (pos == 0) break;
            pos--;
        }
        x1 = pos;
        pos = seedX;
        while (bf.getRGB(pos, seedY) == innerColor) {
            if (pos == bf.getWidth()-1) break;
            pos++;
        }
        x2 = pos;
        return new Span(seedY, x1, x2);
    }

    private static void addTopSpans (BufferedImage bf, LinkedList<Span> stack, Span currentSpan, int oldColor) throws WrongSeedException {
        if (currentSpan.level == 0) return;
        for (int x = currentSpan.edge1+1; x < currentSpan.edge2; x++) {
            Span span = getSpan(bf, x, currentSpan.level-1, oldColor);
            if (span == null) continue;
            if (!stack.contains(span)) {
                stack.offerLast(span);
            }
            x = span.edge2+1;
        }
    }

    private static void addBottomSpans (BufferedImage bf, LinkedList<Span> stack, Span currentSpan, int oldColor) throws WrongSeedException {
        if (currentSpan.level == bf.getHeight()-1) return;
        for (int x = currentSpan.edge1+1; x < currentSpan.edge2; x++) {
            Span span = getSpan(bf, x, currentSpan.level+1, oldColor);
            if (span == null) continue;
            if (!stack.contains(span)) {
                stack.offerLast(span);
            }
            x = span.edge2+1;
        }
    }

    static class Span {
        public final int level, edge1, edge2;
        public Span(int level, int edge1, int edge2) {
            this.level = level;
            this.edge1 = edge1;
            this.edge2 = edge2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Span span = (Span) o;

            if (level != span.level) return false;
            if (edge1 != span.edge1) return false;
            return edge2 == span.edge2;

        }

        @Override
        public int hashCode() {
            int result = level;
            result = 31 * result + edge1;
            result = 31 * result + edge2;
            return result;
        }
    }
    static class WrongSeedException extends Exception{

    }
}
