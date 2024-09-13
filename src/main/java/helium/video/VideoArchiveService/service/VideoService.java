package helium.video.VideoArchiveService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VideoService {

  @Value("${data.storage.path}")
  @Nullable
  private String DATA_STORAGE_PATH;

  public void saveVideoFile(MultipartFile file) throws IOException, RuntimeException {
    if(file == null || file.isEmpty())
    {
      // TODO: create CUSTOM EXCEPTION
      throw new RuntimeException("File is null or empty");
    }

    if (DATA_STORAGE_PATH == null) {
      throw new RuntimeException();
    }

    Path uploadDirPath = Path.of(DATA_STORAGE_PATH);
    if (!Files.exists(uploadDirPath)) {
      Files.createDirectory(uploadDirPath);
    }

    Path filePath = uploadDirPath.resolve(file.getOriginalFilename());

    try (OutputStream outputStream = new FileOutputStream(filePath.toFile())) {
      outputStream.write(file.getBytes());
    }
  }
}
