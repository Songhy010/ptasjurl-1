package khay.dy.ptasjurl.util;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptor {

    private Cipher cipher;
    private String secretKey;
    private String iv;
    private String cipher_mode;

    private SecretKey keySpec;
    private IvParameterSpec ivSpec;
    private Charset CHARSET;

    public Cryptor() {
        try {
            // Initialize
            secretKey = "123!#@abc*&$(kh)";
            iv = "123!#@abc*&$(kh)";
            cipher_mode = "AES/CBC/PKCS5Padding";
            CHARSET = Charset.forName("UTF8");

            keySpec = new SecretKeySpec(secretKey.getBytes(CHARSET), "AES");
            ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));
            cipher = Cipher.getInstance(cipher_mode);

        } catch (Exception e) {
            //Log.e("eror ini crypto", e.getMessage());
        }
    }

/*    protected String decryptToString(String text) {
        try {
            if (text != null && text.length() > 0) {
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
                return new String(cipher.doFinal(Base64.decode(text, Base64.DEFAULT))).trim();
            }
        } catch (Exception e) {
            //Log.e("error decrypt to strng", e.getMessage());
        }
        return "";
    }

    public byte[] decryptToByte(File fileIn) {
        try {
            FileInputStream fis = new FileInputStream(fileIn);

            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                content.append(line);
            }

            fis.close();

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(Base64.decode(content.toString(), Base64.DEFAULT));
        } catch (Exception e) {
            //Log.e("error decrypt to byte", e.getMessage());
        }
        return null;
    }

    protected String encryptToString(String text) {
        try {
            if (text != null && text.length() > 0) {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                //return DatatypeConverter.printBase64Binary().trim();
                return new String(Base64.encode(cipher.doFinal(text.getBytes(CHARSET)), Base64.DEFAULT)).trim();
                //return Base64.decode(new String(cipher.doFinal(input.getBytes(CHARSET))), Base64.DEFAULT);
            }
        } catch (Exception e) {
            //Log.e("error encrypt to string", e.getMessage());
        }
        return "";
    }*/

    public byte[] decryptToByte1(Context context, String filePath) {
        try {
            InputStream istr = context.getAssets().open(filePath);

            //FileInputStream fis = new FileInputStream(fileIn);
            BufferedReader r = new BufferedReader(new InputStreamReader(istr));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                content.append(line);
            }
            //fis.close();
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            //return cipher.doFinal(MessageDigest.getInstance("SHA-256").digest(content.toString().getBytes(StandardCharsets.UTF_8)));
            byte[] bytes = Base64.encode(content.toString().getBytes("UTF-8"), Base64.DEFAULT);
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            Log.e("Err", e.getMessage() + "");
        }
        return null;
    }

}
