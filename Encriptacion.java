import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

class Encriptacion 
{
    public static final String ALGORITMO = "AES";
    public static final int LONGITUD_LLAVE = 256;

    public SecretKey generarLlaveSecreta(String llave) throws NoSuchAlgorithmException
	{
        byte[] llaveEnBites = llave.getBytes();
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(llaveEnBites);
        KeyGenerator generadorLlave = KeyGenerator.getInstance(ALGORITMO);
        generadorLlave.init(LONGITUD_LLAVE, random);

        return generadorLlave.generateKey();
    }

    public void encriptarArchivo(String archivoOriginal, String archivoEncriptado, SecretKey llaveSecreta) throws IOException, GeneralSecurityException
	{
        byte[] content = Files.readAllBytes(Paths.get(archivoOriginal));

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, llaveSecreta);
        byte[] encriptedContent = cipher.doFinal(content);

        try (FileOutputStream fos = new FileOutputStream(archivoEncriptado)) {
            fos.write(encriptedContent);
        }
    }

    public String archivoDesencriptado(String archivoEncriptado, SecretKey llaveSecreta) throws IOException, GeneralSecurityException
	{
        byte[] encryptedContent = Files.readAllBytes(Paths.get(archivoEncriptado));

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, llaveSecreta);
        byte[] decryptedContent = cipher.doFinal(encryptedContent);

        return new String(decryptedContent);
    }
}
