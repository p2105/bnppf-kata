package Utilities;

public class CommonUtilities 
{
    public static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    public static Boolean parseBooleanOrNull(String value) {
        String normalized = blankToNull(value);
        return normalized == null ? null : Boolean.parseBoolean(normalized);
    }

}
