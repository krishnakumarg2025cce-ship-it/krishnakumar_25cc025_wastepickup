package com.example.wastepickup.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@CrossOrigin(origins = "*")
public class DownloadController {

    @GetMapping(value = {"/download", "/download/WastePickup.zip", "/api/download"})
    public ResponseEntity<Resource> downloadZip() {
        File file = new File("WastePickup.zip");
        if (!file.exists()) {
            file = new File("C:/Users/gkris/.gemini/antigravity/scratch/WastePickup/WastePickup.zip");
        }
        if (!file.exists()) {
            file = new File("C:/Users/gkris/Downloads/WastePickup.zip");
        }
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"WastePickup.zip\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .contentLength(file.length())
                .body(resource);
    }
}
