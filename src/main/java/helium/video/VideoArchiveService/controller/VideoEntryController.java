package helium.video.VideoArchiveService.controller;

import helium.video.VideoArchiveService.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/input/video")
public class VideoEntryController {

  private final VideoService videoService;
  private final Logger LOGGER = LoggerFactory.getLogger(VideoEntryController.class);

  public VideoEntryController(VideoService videoService) {
    this.videoService = videoService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> acceptVideoController(@RequestParam("file") MultipartFile file) {
    try {
      videoService.saveVideoFile(file);
      LOGGER.info("Successfully created file - {}", file.getOriginalFilename());
      return ResponseEntity.status(HttpStatus.CREATED).body("CREATED FILE - " + file.getOriginalFilename());
    } catch (Exception e) {
      // TODO: CUSTOM EXCEPTION HANLDING
      LOGGER.error("EXCEPTION SAVING FILE - ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
