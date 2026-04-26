package com.safenutri.safenutri_api.api.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

    public Resource carregarImagem(String nomeArquivo) {
        try {
            Path basePath = Paths.get(directoryReceita).toAbsolutePath().normalize();
            Path filePath = basePath.resolve(nomeArquivo).normalize();

            if (!filePath.startsWith(basePath)) {
                throw new SecurityException("Tentativa de acesso inválido ao diretório.");
            }

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                throw new FileNotFoundException("Arquivo não encontrado no disco.");
            }

            return new UrlResource(filePath.toUri());
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo", e);
        }
    }
}
