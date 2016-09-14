package dao;

public class SQLUtils {

    public static String joinLines(String... lines) {
        String result = "";
        for (String line : lines) {
            result += "\n" + line;
        }
        return result;
    }

}
