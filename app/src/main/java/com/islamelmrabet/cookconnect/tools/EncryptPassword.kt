import android.annotation.SuppressLint
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

const val SECRET_KEY = "secret_password_for_workers_in_ITEMPOS"
const val SALT = "ssshhhhhhhhhhh!!!!"

@SuppressLint("NewApi")
fun encrypt(password: String): String {
    val iv = ByteArray(16)
    val ivSpec = IvParameterSpec(iv)
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = PBEKeySpec(SECRET_KEY.toCharArray(), SALT.toByteArray(), 65536, 256)
    val tmp: SecretKey = factory.generateSecret(spec)
    val secretKey = SecretKeySpec(tmp.encoded, "AES")

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
    return Base64.getEncoder().encodeToString(cipher.doFinal(password.toByteArray(Charsets.UTF_8)))
}

@SuppressLint("NewApi")
fun decrypt(password: String): String {
    val iv = ByteArray(16)
    val ivSpec = IvParameterSpec(iv)
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = PBEKeySpec(SECRET_KEY.toCharArray(), SALT.toByteArray(), 65536, 256)
    val tmp: SecretKey = factory.generateSecret(spec)
    val secretKey = SecretKeySpec(tmp.encoded, "AES")

    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
    return String(cipher.doFinal(Base64.getDecoder().decode(password)))
}


