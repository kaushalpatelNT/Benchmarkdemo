package com.nichetech.smartonsite.benchmark.Common;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * CONECT_Working(com.collabera.conect.commons) <br />
 * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>  <br />
 * on 11/11/16.
 *
 * @author Suthar Rohit
 */
public class Validate {

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Returns true if the string is null or 0-length.<br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isNull(String str) {
        return str == null || str.equalsIgnoreCase("null") || str.trim().length() == 0;
    }

    /**
     * Returns true if the string is null or 0-length.<br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param editText the {@link EditText} to be examined
     * @return true if str is null or zero length
     */
    public static boolean isNull(EditText editText) {
        String str = editText.getText().toString().trim();
        return str.equalsIgnoreCase("null") || str.trim().length() == 0;
    }

    /**
     * Returns true if the <u>string is not null</u> and <u>length > 0</u>.<br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param str the string to be examined
     * @return true if str is not null and length > 0
     */
    public static boolean isNotNull(String str) {
        return !(str == null || str.equalsIgnoreCase("null") || str.trim().length() == 0);
    }

    /**
     * Check Email Address is valid or not.?<br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param emailAddress {@link String} Email Address
     * @return true if Email Address is valid
     */
    public static boolean checkEmail(String emailAddress) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile(EMAIL_PATTERN);
        return EMAIL_ADDRESS_PATTERN.matcher(emailAddress).matches();
    }

    /**
     * Check Mobile Number is valid or not.?<br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param mobileNumber {@link String} mobile number
     * @return true if mobile number is valid
     */
    public static boolean checkPhone(String mobileNumber) {
        // FOR 10 NUMBER
        // Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^([1-9])*([0-9]{9})$");
        // FOR 8 to 15 NUMBERS WITH +
        //Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^\\+*([1-9])*([0-9]{8,14})$");
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^(\\d{3}\\-)?(\\(\\d{3}\\))?\\d{3}\\-\\d{4}$");
        return EMAIL_ADDRESS_PATTERN.matcher(mobileNumber).matches();
    }

    /**
     * Check Mobile Number is valid or not.?<br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param mobileNumber {@link String} mobile number
     * @return true if mobile number is valid
     */
    public static boolean checkMobileNumberInternational(String mobileNumber) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^(\\+?\\d{1,4}[\\s-])?(?!0+\\s+,?$)\\d{10}\\s*,?$");
        return EMAIL_ADDRESS_PATTERN.matcher(mobileNumber).matches();
    }

    /**
     * Check date is proper in following format : <b>DD MMM, YYYY</b><br />
     * <br />
     * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>
     *
     * @param date {@link String} date time
     * @return true if date is valid
     */
    public static boolean isValidDate(String date) {
        Pattern DATE_PATTERN = Pattern.compile("^([0-9]{2})* ([A-Z,a-z]{3})*(, )([0-9]{4})$");
        return DATE_PATTERN.matcher(date).matches();
    }

}
