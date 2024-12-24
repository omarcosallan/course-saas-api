package com.omarcosallan.saas_api.utils;

import java.text.Normalizer;

public class SlugUtils {

    private SlugUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String createSlug(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replaceAll("[^\\w\\s]", "").trim().replaceAll("\\s+", "-").toLowerCase();
    }
}
