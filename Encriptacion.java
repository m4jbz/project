import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

class Encriptacion {
    public static final String ALGORITMO = "AES";
    public static final int LONGITUD_LLAVE = 256;

    public SecretKey generarLlaveSecreta(String llave) throws NoSuchAlgorithmException {
        byte[] llaveEnBites = llave.getBytes();
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(llaveEnBites);
        KeyGenerator gll = KeyGenerator.getInstance(ALGORITMO);
        gll.init(LONGITUD_LLAVE, random);

        return gll.generateKey();
    }

    public void encriptarArchivo(String archivoTexto, String archivoEncriptado,
			SecretKey llaveSecreta) throws IOException, GeneralSecurityException {
        byte[] contenido = Files.readAllBytes(Paths.get(archivoTexto));

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, llaveSecreta);
        byte[] encriptedContent = cipher.doFinal(contenido);

        try (FileOutputStream fos = new FileOutputStream(archivoEncriptado)) {
            fos.write(encriptedContent);
        }
    }

    public String archivoDesencriptado(String archivoEncriptado, SecretKey llaveSecreta)
			throws IOException, GeneralSecurityException {
        byte[] contenidoEncriptado = Files.readAllBytes(Paths.get(archivoEncriptado));

        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, llaveSecreta);
        byte[] contenidoDesencriptado = cipher.doFinal(contenidoEncriptado);

        return new String(contenidoDesencriptado);
    }
}