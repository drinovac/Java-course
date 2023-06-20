package hr.fer.oprpp1.hw05.crypto;

/**
 * This class implements two static methods for converting Strings to byte arrays and opposite.
 */
public class Util {

    /**
     * This method converts hex String to byte array
     * @param keyText
     * @return bytes from String in byte array
     */
    public static byte[] hextobyte(String keyText) {
        byte[] array = new byte[keyText.length()/2];
        String[] splitted = keyText.split("(?<=\\G..)");
        int i = 0;
        for(String s: splitted) {
            array[i++] = Short.valueOf(s, 16).byteValue();
        }
        return array;
    }

    /**
     * This method converts byte array to hex String
     * @param array byte array
     * @return hex String
     */
    public static String bytetohex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for(byte b: array) {
             sb.append(String.format("%02X", b));
        }

        return sb.toString().toLowerCase();
    }

}
