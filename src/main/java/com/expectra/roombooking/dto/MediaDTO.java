package com.expectra.roombooking.dto;

import lombok.Data;

@Data
public class MediaDTO {
    private Long mediaId;
    private String mediaType;
    private String mediaUrl;
    private String mediaDescription;
    private Long roomId;
    private Long hotelId;
}
