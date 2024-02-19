package com.application.backend.file;

import com.application.backend.exception.CatchException;
import com.application.backend.helper.Helper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class ImageHandler {
    private static String currentWorkingDirectory = System.getProperty("user.dir");
    // Đường dẫn tương đối đến thư mục "images"
    private static String relativePath = "images";

    // Tạo đường dẫn tuyệt đối bằng cách nối đường dẫn hiện tại với đường dẫn tương đối
    private static String uploadPath = Paths.get(currentWorkingDirectory, relativePath).toString();



    public static String saveImage(MultipartFile file) throws IOException {
        // Tạo đường dẫn đến thư mục lưu trữ ảnh
        Path uploadDir = Path.of(uploadPath);
        Files.createDirectories(uploadDir);

        // Lưu file vào thư mục lưu trữ
        String fileName = String.valueOf(System.currentTimeMillis())+"_"+file.getOriginalFilename() ;

        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public static Resource loadImage(String imageName) throws MalformedURLException {
        // Tạo đường dẫn đến file ảnh
        Path imagePath = Path.of(uploadPath).resolve(imageName);
        Resource resource = new UrlResource(imagePath.toUri());

        // Kiểm tra xem tệp có tồn tại không
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }

    public static void deleteImage(String imageName) throws MalformedURLException {
        Path imagePath = Path.of(uploadPath).resolve(imageName);

        File fileToDelete = new File(imagePath.toString());

        if (fileToDelete.exists()) {
             fileToDelete.delete();
        } else {
            throw new CatchException("Image not found!", HttpStatus.NOT_FOUND);
        }
    }

}
