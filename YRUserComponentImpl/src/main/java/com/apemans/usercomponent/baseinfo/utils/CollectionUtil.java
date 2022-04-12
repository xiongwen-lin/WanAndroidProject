package com.apemans.usercomponent.baseinfo.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CollectionUtil {
    private CollectionUtil() {
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    public static boolean isIndexSafe(int index, int size) {
        return index >= 0 && index < size;
    }

    public static <T> List<T> safeFor(List<T> other) {
        return other == null ? Collections.EMPTY_LIST : other;
    }

    public static List emptyList() {
        return Collections.emptyList();
    }

    public static <T> int size(List<T> other) {
        return other == null ? 0 : other.size();
    }
}
