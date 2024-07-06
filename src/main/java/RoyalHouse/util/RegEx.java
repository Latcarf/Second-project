package RoyalHouse.util;

import java.util.regex.Pattern;

public class RegEx {
    public static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z0-9]{3,50}$");

    public static final Pattern EMAIL_REGEX = Pattern.compile("^(?=.{1,64}@)[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9.]+)*\\.[a-zA-Z]{2,}$");

    public static final Pattern PHONE_REGEX = Pattern.compile(
            "^(380(\\d{2})(\\d{7})|380 (\\d{2}) (\\d{3}) (\\d{2}) (\\d{2})|380-(\\d{2})-(\\d{3})-(\\d{2})-(\\d{2})|" +
                    "0(\\d{2})(\\d{7})|0(\\d{2}) (\\d{3}) (\\d{2}) (\\d{2})|0(\\d{2})-(\\d{3})-(\\d{2})-(\\d{2}))$"
    );
}
