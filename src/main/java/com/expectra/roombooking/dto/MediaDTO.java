package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.MediaTypes;
import lombok.Data;
import java.io.Serializable;


@Data
public class MediaDTO implements Serializable {
    private Long mediaId;
    private Integer mediaCode;
    private MediaTypes mediaType;
    private String mediaDescription;
    private String mediaUrl;
}
