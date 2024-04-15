package org.example.expensetracking.service.serviceimpl;

import org.example.expensetracking.service.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final Path path = Paths.get("src/main/resources/images");

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        // get filename with extension (cute-cat.png)
        String fileName = file.getOriginalFilename();
        // cute-cat.png => [cute-cat, png]
        assert fileName != null;
        String extension = StringUtils.getFilenameExtension(fileName);
        if (extension != null) {
            extension = extension.toLowerCase(); // Convert extension to lowercase
        }
        if (extension != null && (extension.equals("png") || extension.equals("jpg") ||
                extension.equals("jpeg") || extension.equals("gif"))) {
            // convert file name to uuid format form
            fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);
            // if the folder not exist create one
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            // copy byte that from input stream to file
            Files.copy(file.getInputStream(), path.resolve(fileName));
            return fileName;
        } else {
            throw new MultipartException("Invalid file format: " + fileName);
        }
    }

    @Override
    public Resource getFileByFileName(String fileName) throws IOException {
        // get file path
        Path filePath = Paths.get("src/main/resources/images/" + fileName);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found: " + fileName);
        }
        // read file as byte
        return new ByteArrayResource(Files.readAllBytes(filePath));
    }
}
