package helium.video.VideoArchiveService.service;

import helium.video.VideoArchiveService.model.VideoEntry;
import helium.video.VideoArchiveService.repository.VideoRepository;
import helium.video.VideoArchiveService.util.MessageConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TIFF;
import org.apache.tika.metadata.XMPDM;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class VideoService {

  @Value("${data.storage.path}")
  @Nullable
  private String DATA_STORAGE_PATH;

  private final VideoRepository videoRepository;

  private final Tika tika;
  private final Metadata metadata;

  public VideoService(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;

    this.tika = new Tika();
    this.metadata = new Metadata();
  }

  @Async
  public CompletableFuture<String> saveVideoFile(HttpServletRequest request, String fileName)
      throws IOException {

    if (DATA_STORAGE_PATH == null) {
      throw new RuntimeException();
    }

    Path uploadDirPath = Path.of(DATA_STORAGE_PATH);
    if (!Files.exists(uploadDirPath)) {
      Files.createDirectory(uploadDirPath);
    }

    Path filePath = uploadDirPath.resolve(fileName);
    if (Files.exists(filePath)) {
      return CompletableFuture.completedFuture(MessageConstants.FILE_ALREADY_EXIST_MSG);
    }

    try (InputStream inputStream = request.getInputStream()) {
      Path destination = Path.of(DATA_STORAGE_PATH, fileName);

      Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    File localSavedFile = filePath.toFile();

    try (Reader ignored = tika.parse(localSavedFile, metadata)) {
      VideoEntry videoEntry =
          new VideoEntry(
              localSavedFile.getName(),
              LocalDateTime.now(), // TODO: TEMPORARY
              Double.parseDouble(metadata.get(XMPDM.DURATION)),
              (double) localSavedFile.length() / (1024 * 1024),
              metadata.get(TIFF.IMAGE_WIDTH) + "x" + metadata.get(TIFF.IMAGE_LENGTH));

      videoRepository.save(videoEntry);
    }

    return CompletableFuture.completedFuture(MessageConstants.FILE_SAVED_SUCCESS_MSG);
  }
}
