package org.example.expensetracking.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.expensetracking.model.dto.response.ApiFileResponse;
import org.example.expensetracking.model.dto.response.FileResponse;
import org.example.expensetracking.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@SecurityRequirement(name = "bearerAuth")
public class AppFileController {

    private final FileService fileService;

    public AppFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = fileService.saveFile(file);
        String fileUrl = "http://localhost:8080/" + fileName;
        FileResponse fileResponse = new FileResponse(fileName, fileUrl, file.getContentType(), file.getSize());
        ApiFileResponse<FileResponse> response = ApiFileResponse.<FileResponse>builder()
                .message("successfully uploaded file")
                .status(HttpStatus.OK).code(200)
                .payload(fileResponse).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getFile(@RequestParam String fileName) throws Exception {
        // Convert file extension to lowercase for case-insensitive comparison
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension != null) {
            extension = extension.toLowerCase();
        }

        Resource resource = fileService.getFileByFileName(fileName);
        if (resource == null || !resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
        }

        MediaType mediaType;

        if (extension != null && (extension.equals("jpg") ||
                extension.equals("jpeg") || extension.equals("png") || extension.equals("gif"))) {
            mediaType = MediaType.IMAGE_PNG;
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(mediaType).body(resource);
    }
}
