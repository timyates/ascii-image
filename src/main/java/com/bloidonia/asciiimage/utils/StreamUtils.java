package com.bloidonia.asciiimage.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {
    public static <T> List<List<T>> collate( List<T> list, int size, int step ) {
        return Stream.iterate(0, i -> i + step)
                .limit((list.size() / step) + 1)
                .map(i -> list.stream()
                        .skip(i)
                        .limit(size)
                        .collect(Collectors.toList()))
                .filter(i -> !i.isEmpty() && i.size() == size)
                .collect(Collectors.toList());
    }
}
