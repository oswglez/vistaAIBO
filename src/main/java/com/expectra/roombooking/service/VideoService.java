package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Video;
import com.expectra.roombooking.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Video createVideo(Video video) {
        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<Video> getVideoById(Long id) {
        return videoRepository.findById(id);
    }

    public Video updateVideo(Long id, Video videoDetails) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));

        video.setCode(videoDetails.getCode());
        video.setVideo(videoDetails.getVideo());

        return videoRepository.save(video);
    }

    public void deleteVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        videoRepository.delete(video);
    }
}


