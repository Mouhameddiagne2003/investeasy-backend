package com.dic2.investeasy.dto.video;

import lombok.Data;

@Data
public class VideoCreateDTO {

    private String title;

    private String description;

    private String url;

    private String thumbnail;

    private String category;

    private Integer duration;

    private Boolean isPublished = false;
} 