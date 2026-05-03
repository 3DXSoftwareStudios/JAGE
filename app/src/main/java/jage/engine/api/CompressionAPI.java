package jage.engine.api;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.tukaani.xz.*;

public class CompressionAPI {

    public String compress(String content, String algo, int level) {
        try {
            byte[] input = content.getBytes(StandardCharsets.UTF_8);
            byte[] output;

            switch (algo.toLowerCase()) {

                case "deflate":
                    output = compressDeflate(input, level);
                    break;

                case "xz":
                    output = compressXZ(input, level);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown algorithm: " + algo);
            }

            return Base64.getEncoder().encodeToString(output);

        } catch (Exception e) {
            throw new RuntimeException("Compression failed: " + e.getMessage(), e);
        }
    }

    public String decompress(String content, String algo, int level) {
        try {
            byte[] input = Base64.getDecoder().decode(content);
            byte[] output;

            switch (algo.toLowerCase()) {

                case "deflate":
                    output = decompressDeflate(input);
                    break;

                case "xz":
                    output = decompressXZ(input);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown algorithm: " + algo);
            }

            return new String(output, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Decompression failed: " + e.getMessage(), e);
        }
    }

    // -----------------------------
    // Deflate
    // -----------------------------
    private byte[] compressDeflate(byte[] data, int level) throws IOException {
        Deflater deflater = new Deflater(level);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            baos.write(buffer, 0, count);
        }

        return baos.toByteArray();
    }

    private byte[] decompressDeflate(byte[] data) throws IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                baos.write(buffer, 0, count);
            }
        } catch (Exception e) {
            throw new IOException("Invalid Deflate data", e);
        }

        return baos.toByteArray();
    }

    // -----------------------------
    // XZ
    // -----------------------------
    private byte[] compressXZ(byte[] data, int level) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XZOutputStream xzOut = new XZOutputStream(baos, new LZMA2Options(level));
        xzOut.write(data);
        xzOut.finish();
        return baos.toByteArray();
    }

    private byte[] decompressXZ(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        XZInputStream xzIn = new XZInputStream(bais);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;

        while ((n = xzIn.read(buffer)) != -1) {
            baos.write(buffer, 0, n);
        }

        return baos.toByteArray();
    }
   
}