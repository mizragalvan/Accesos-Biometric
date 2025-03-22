package mx.engineer.utils.regex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {
    private RegexUtils() { }
    
    public static List<String> extractText(final String start, final String end, final String stringValue) {
        final String tagRegex = Pattern.quote(start) + "(.*?)" + Pattern.quote(end);
        final Pattern pattern = Pattern.compile(tagRegex);
        final Matcher matcher = pattern.matcher(stringValue);
        final Set<String> tagsSet = new HashSet<>();
        while (matcher.find()) {
            tagsSet.add(matcher.group());
        }
        return new ArrayList<>(tagsSet);
    }
}
