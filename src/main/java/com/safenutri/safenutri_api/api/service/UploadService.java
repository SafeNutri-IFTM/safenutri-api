package com.safenutri.safenutri_api.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadService {
    @Value("${filediretorioReceita}")
    private String directoryReceita;

    public String saveFotoReceita(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("Arquivo não é uma imagem válida");
        }

        String fileName = UUID.randomUUID() + ".png";
        Path path = Paths.get(directoryReceita, fileName);

        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        return fileName;   
    }

    public String getImageReceita(String filename) {
        return directoryReceita + filename;
    }
}
