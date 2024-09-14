package helium.video.VideoArchiveService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "entry_metadata")
public class VideoEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  @NotEmpty(message = "filename can't be empty")
  private String name;

  @Column(name = "create_date")
  @NotNull(message = "create_date must be defined")
  private LocalDateTime create_date;

  @Column(name = "length")
  @NotNull(message = "file_length must be defined")
  private double duration;

  @Column(name = "size_mb")
  @NotNull(message = "file size must be defined")
  private double size_mb;

  @Column(name = "resolution")
  @NotEmpty(message = "resolution must be defined")
  private String resolution;

  public VideoEntry() {}

  public VideoEntry(
      String name, LocalDateTime create_date, double duration, double size_mb, String resolution) {
    this.name = name;
    this.create_date = create_date;
    this.duration = duration;
    this.size_mb = size_mb;
    this.resolution = resolution;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getCreate_date() {
    return create_date;
  }

  public void setCreate_date(LocalDateTime create_date) {
    this.create_date = create_date;
  }

  public double getLength() {
    return duration;
  }

  public void setLength(double duration) {
    this.duration = duration;
  }

  public double getSize_mb() {
    return size_mb;
  }

  public void setSize_mb(double size_mb) {
    this.size_mb = size_mb;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }
}
