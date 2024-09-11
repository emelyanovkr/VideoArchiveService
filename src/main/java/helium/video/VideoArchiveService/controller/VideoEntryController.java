package helium.video.VideoArchiveService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/input/video")
public class VideoEntryController {

  @GetMapping("/info")
  public String info() {
    return "input/video/info";
  }

  @PostMapping("/upload")
  public ResponseEntity<String> acceptVideoController(@RequestParam("file") MultipartFile file) {
    try {
      Path uploadDirPath = Paths.get("uploads");
      if (!Files.exists(uploadDirPath)) {
        Files.createDirectory(uploadDirPath);
      }

      if (file.getOriginalFilename() == null) {
        // TODO: create custom exception
        throw new RuntimeException();
      }
      Path filePath = uploadDirPath.resolve(file.getOriginalFilename());

      try (OutputStream outputStream = new FileOutputStream(filePath.toFile())) {
        outputStream.write(file.getBytes());
      } catch (IOException e) {
        // TODO: LOGGING
        throw new RuntimeException(e);
      }

      return ResponseEntity.created(filePath.toUri())
          .body("File created: " + filePath.toAbsolutePath());

    } catch (IOException e) {
      throw new RuntimeException("Error creating file", e);
    }
  }
}
