package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import static hr.fer.oprpp1.hw05.crypto.Util.hextobyte;

/**
 * This class represents program for encrypting/decrypting. Depending on arguments.
 */
public class Crypto {
    /**
     * This static method starts program and calls specified method depending on arguments.
     *
     * @param args
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException {

       String action = args[0];

        if(action.equals("checksha")) {
            checksha(args[1]);
        } else if(action.equals("encrypt")) {
            encrypy(args[1], args[2]);
        } else if(action.equals("decrypt")) {
            decrypt(args[1], args[2]);
        }
    }

    /**
     * This method decrypts file.
     * @param file source file that need to be decrypted
     * @param decryptedFile destination file that is decrypted
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    private static void decrypt(String file, String decryptedFile) throws InvalidAlgorithmParameterException, InvalidKeyException, IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean encrypt = false;

        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
        String keyText = br.readLine();
        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
        String ivText = br.readLine();
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

        Path iPath = Paths.get(file);
        Path oPath = Paths.get(decryptedFile);

        try (InputStream is = Files.newInputStream(iPath);
             OutputStream os = Files.newOutputStream(oPath)){
            byte[] buff = new byte[1024];
            while(true) {
                int r = is.read(buff);
                if(r < 1) break;
                os.write(cipher.update(buff, 0, r));
            }
        } catch (IOException exception) {
            System.out.println(exception);
        }

        System.out.println("Decryption completed. Generated file " + decryptedFile + " based on file " + file);

    }

    /**
     * This method encrypts file.
     * @param file source file that need to be encrypted
     * @param cryptedFile destination file that is encrypted
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    private static void encrypy(String file, String cryptedFile) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean encrypt = true;

        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
        String keyText = br.readLine();
        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
        String ivText = br.readLine();
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);

        Path iPath = Paths.get(file);
        Path oPath = Paths.get(cryptedFile);

        try (InputStream is = Files.newInputStream(iPath);
             OutputStream os = Files.newOutputStream(oPath)){
            byte[] buff = new byte[1024];
            while(true) {
                int r = is.read(buff);
                if(r < 1) break;
                os.write(cipher.update(buff, 0, r));
            }
        } catch (IOException exception) {
            System.out.println(exception);
        }

        System.out.println("Encryption completed. Generated file " + cryptedFile + " based on file " + file);

    }

    /**
     * This method checks if given file has digest same as given as input.
     * @param file File that needs to be checked
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private static void checksha(String file) throws NoSuchAlgorithmException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please provide expected sha-256 digest for " + file + ":");

        String digest = br.readLine();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        Path path = Paths.get(file);

        try (InputStream is = Files.newInputStream(path)){
            byte[] buff = new byte[1024];
            while(true) {
                int r = is.read(buff);
                if(r < 1) break;
                messageDigest.update(buff, 0, r);
            }
        } catch (IOException exception) {
            System.out.println(exception);
        }

        byte[] sum = messageDigest.digest();

        String hex = Util.bytetohex(sum);

        if(hex.equals(digest)) {
            System.out.println("Digesting completed. Digest of " + file +" matches expected digest.");
        } else {
            System.out.println("Digesting completed. Digest of hw05test.bin does not match the expected digest. Digest was: "
                                    + hex);
        }

    }
}