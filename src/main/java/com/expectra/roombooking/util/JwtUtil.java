package com.expectra.roombooking.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private JwtUtil() {} // Prevent instantiation

    public static RSAPrivateKey getPrivateKey(String filename) throws Exception {
        logger.info("Directorio actual de ejecución: {}", System.getProperty("user.dir"));
        logger.info("Intentando leer la clave privada desde: {}", filename);

        if (!Files.exists(Paths.get(filename))) {
            logger.error("El archivo de clave privada NO existe en la ruta: {}", filename);
            throw new RuntimeException("Archivo de clave privada no encontrado: " + filename);
        }

        String key = new String(Files.readAllBytes(Paths.get(filename)));
        logger.info("Archivo leído correctamente. Primeras líneas:\n{}", key.substring(0, Math.min(100, key.length())));

        key = key
            .replaceAll("-----BEGIN (.*)-----", "")
            .replaceAll("-----END (.*)----", "")
            .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        logger.info("Clave privada decodificada correctamente.");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }
} 