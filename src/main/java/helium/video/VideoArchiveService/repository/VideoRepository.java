package helium.video.VideoArchiveService.repository;

import helium.video.VideoArchiveService.model.VideoEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntry, Integer> {}
