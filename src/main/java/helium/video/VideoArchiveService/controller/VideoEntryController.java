package helium.video.VideoArchiveService.controller;

import helium.video.VideoArchiveService.service.VideoService;
import helium.video.VideoArchiveService.util.MessageConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/input/video")
public class VideoEntryController {

  private final VideoService videoService;
  private final Logger LOGGER = LoggerFactory.getLogger(VideoEntryController.class);

  public VideoEntryController(VideoService videoService) {
    this.videoService = videoService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> handleFileUpload(
      HttpServletRequest request, @RequestHeader("File-Name") String fileName) {
    try {
      String operationResult = videoService.saveVideoFile(request, fileName).get();
      LOGGER.info("{}: {}", operationResult, fileName);
      return ResponseEntity.status(HttpStatus.OK).body(operationResult + ": " + fileName);
    } catch (IOException | InterruptedException | ExecutionException ex) {
      LOGGER.error("ERROR SAVING FILE - ", ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
  }
}
