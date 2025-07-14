package com.karan.online.certificate.generator.service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PinataService {

    @Value("${pinata.api.key:#{null}}")
    private String pinataApiKey;

    @Value("${pinata.secret.key:#{null}}")
    private String pinataSecretKey;

    private static final String PINATA_URL = "https://api.pinata.cloud/pinning/pinFileToIPFS";

    public String uploadToIPFS(byte[] data, String fileName) throws IOException {
        if (pinataApiKey == null || pinataSecretKey == null ||
                pinataApiKey.trim().isEmpty() || pinataSecretKey.trim().isEmpty()) {
            throw new IllegalStateException("Pinata API credentials are missing. Check application.properties or environment variables.");
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(data, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(PINATA_URL)
                .addHeader("pinata_api_key", pinataApiKey.trim())
                .addHeader("pinata_secret_api_key", pinataSecretKey.trim())
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                System.err.println("Pinata Error Response: " + responseBody);
                throw new IOException("Failed to upload to IPFS via Pinata. Status " + response.code() + ": " + responseBody);
            }

            JSONObject json = new JSONObject(responseBody);
            String ipfsHash = json.getString("IpfsHash");

            return "https://ipfs.io/ipfs/" + ipfsHash;
        }
    }
}
