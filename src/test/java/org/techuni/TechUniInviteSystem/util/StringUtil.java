package org.techuni.TechUniInviteSystem.util;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtil {

    public static String shuffleString(String string) {
        if (isNull(string)) {
            return null;
        }
        List<String> letters = new ArrayList<>(List.of(string.split("")));
        Collections.shuffle(letters);
        return String.join("", letters);
    }
}
