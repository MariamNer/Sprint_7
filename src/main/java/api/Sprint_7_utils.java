package api;

import org.apache.commons.lang3.RandomStringUtils;

public class Sprint_7_utils {

    public static String generateRandomString() {
        String generatedString = RandomStringUtils.randomAlphanumeric(12);

        return generatedString;
    }
}
