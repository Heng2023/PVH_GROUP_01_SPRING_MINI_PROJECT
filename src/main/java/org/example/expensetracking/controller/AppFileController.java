package org.example.expensetracking.controller;

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

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
public class AppFileController {
    private final FileService fileService;

    public AppFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) {
        try {
            String fileName = fileService.saveFile(file);
            String fileUrl = "http://localhost:8080/" + fileName;
            FileResponse fileResponse = new FileResponse(fileName,fileUrl,file.getContentType(), file.getSize());
            ApiFileResponse<FileResponse> response = ApiFileResponse.<FileResponse>builder()
                    .message("successfully uploaded file")
                    .status(HttpStatus.OK).code(200)
                    .payload(fileResponse).build();
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new MultipartException("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getFile(@RequestParam String fileName) throws MissingServletRequestPartException {
        try {
            // Convert file extension to lowercase for case-insensitive comparison
            String extension = StringUtils.getFilenameExtension(fileName);
            if (extension != null) {
                extension = extension.toLowerCase();
            }

            Resource resource = fileService.getFileByFileName(fileName);
            MediaType mediaType;

            if (extension != null && (extension.equals("pdf") || extension.equals("jpg") ||
                    extension.equals("jpeg") || extension.equals("png") || extension.equals("gif"))) {
                mediaType = MediaType.IMAGE_PNG;
            } else {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .contentType(mediaType).body(resource);
        } catch (IOException e) {
            throw new MissingServletRequestPartException("File not found: " + e.getMessage());
        }
    }
}
