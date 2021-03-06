package gspd.ispd.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringPropertiesBind is string solver pased on a given properties object
 */
public class StringPropertiesBind {

    Properties properties;

    StringPropertiesBind(Properties properties)  {
        this.properties = properties;
    }

    public String resolveString(String text) {
        Pattern pattern = Pattern.compile("\\$\\{(\\w|\\.|\\:)+\\}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            text = substituteMatched(text, matcher);
        }
        return text;
    }

    private String substituteMatched(String text, Matcher matcher) {
        // the hole string matched
        String group = matcher.group();
        // the key name
        String key = group.substring(2, group.length() - 1);
        // the value of the specified key
        String value = properties.getProperty(key);
        // substitute in string
        String before = text.substring(0, matcher.start());
        String after = text.substring(matcher.end());
        return before + value + after;
    }
}