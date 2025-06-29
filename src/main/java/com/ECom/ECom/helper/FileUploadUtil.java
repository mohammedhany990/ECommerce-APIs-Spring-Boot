package com.ECom.ECom.helper;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {

//    public static String saveFile(String uploadDir, MultipartFile file) throws IOException {
//        String uploadPath = Paths.get("src/main/resources/static/images/" + uploadDir).toString();
//
//        // Ensure directory exists
//        Files.createDirectories(Paths.get(uploadPath));
//
//        // Unique file name
//        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
//
//        // Target path
//        Path filePath = Paths.get(uploadPath, fileName);
//
//        // Copy file
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        // Return relative path for DB
//        return "images/" + uploadDir + "/" + fileName;
//    }

    public static String saveFile(String folderName, MultipartFile file) throws IOException {
        // 1. Get located folder path
        String folderPath = System.getProperty("user.dir") + "/src/main/resources/static/images/" + folderName;
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 2. Generate unique file name
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();

        // 3. Create file path
        String filePath = folderPath + "/" + fileName;

        // 4. Save file as streams
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        }

        // Return relative path (as you did with "images/{folder}/{filename}")
        return "images/" + folderName + "/" + fileName;
    }

    public static void deleteFile(String folderName, String fileName) {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/static/images/" + folderName + "/" + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
