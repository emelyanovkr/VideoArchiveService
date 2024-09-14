package helium.video.VideoArchiveService.service;

import helium.video.VideoArchiveService.model.VideoEntry;
import helium.video.VideoArchiveService.repository.VideoRepository;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TIFF;
import org.apache.tika.metadata.XMPDM;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

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

  public void saveVideoFile(MultipartFile file) throws IOException, RuntimeException {
    if (file == null || file.isEmpty()) {
      // TODO: create CUSTOM EXCEPTION
      throw new RuntimeException("File is null or empty");
    }

    if (DATA_STORAGE_PATH == null) {
      throw new RuntimeException();
    }

    File localSavedFile = writeVideoFileToFileSystem(file);

    tika.parse(localSavedFile, metadata);
    System.out.println(metadata.get(XMPDM.DURATION));

    VideoEntry videoEntry =
        new VideoEntry(
            localSavedFile.getName(),
            LocalDateTime.now(), // TODO: TEMPORARY
            Double.parseDouble(metadata.get(XMPDM.DURATION)),
            (double) localSavedFile.length() / (1024 * 1024),
            metadata.get(TIFF.IMAGE_WIDTH) + "x" + metadata.get(TIFF.IMAGE_LENGTH));

    videoRepository.save(videoEntry);
  }

  private File writeVideoFileToFileSystem(MultipartFile file) throws IOException {
    Path uploadDirPath = Path.of(DATA_STORAGE_PATH);
    if (!Files.exists(uploadDirPath)) {
      Files.createDirectory(uploadDirPath);
    }

    Path filePath = uploadDirPath.resolve(file.getOriginalFilename());
    File localSavedFile = filePath.toFile();

    try (OutputStream outputStream = new FileOutputStream(localSavedFile)) {
      outputStream.write(file.getBytes());
    }

    return localSavedFile;
  }
}
