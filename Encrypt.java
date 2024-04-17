import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

class Encrypt
{
    public static final String ALGORITHM = "AES";
    public static final int KEY_SIZE = 256;

    public SecretKey generateSecretKey(String key) throws NoSuchAlgorithmException
	{
        byte[] keyInBytes = key.getBytes();
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(keyInBytes);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, random);

        return keyGenerator.generateKey();
    }

    public void encryptFile(String originalFile, String encryptedFile, SecretKey secretKey) throws IOException, GeneralSecurityException
	{
        byte[] content = Files.readAllBytes(Paths.get(originalFile));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encriptedContent = cipher.doFinal(content);

        try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
            fos.write(encriptedContent);
        }
    }

    public String decryptedFile(String encryptedFile, SecretKey secretKey) throws IOException, GeneralSecurityException
	{
        byte[] encryptedContent = Files.readAllBytes(Paths.get(encryptedFile));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(encryptedContent);

        return new String(decryptedContent);
    }
}
