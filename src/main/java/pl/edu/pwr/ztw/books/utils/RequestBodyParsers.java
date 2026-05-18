package pl.edu.pwr.ztw.books.utils;

import java.util.Map;

public final class RequestBodyParsers {
    private RequestBodyParsers() {
    }

    public static String getRequiredString(Map<String, Object> body, String key) {
        Object value = body.get(key);

        if (value == null) {
            throw new IllegalArgumentException("Missing field: " + key);
        }

        String textValue = value.toString().trim();
        if (textValue.isEmpty()) {
            throw new IllegalArgumentException("Field must not be empty: " + key);
        }

        return textValue;
    }

    public static int getRequiredInt(Map<String, Object> body, String key) {
        Object value = body.get(key);

        if (value == null) {
            throw new IllegalArgumentException("Missing field: " + key);
        }

        if (value instanceof Number numberValue) {
            return numberValue.intValue();
        }

        try {
            return Integer.parseInt(value.toString().trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Field must be a number: " + key);
        }
    }
}
