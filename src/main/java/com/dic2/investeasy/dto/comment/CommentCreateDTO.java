package com.dic2.investeasy.dto.comment;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private String text;
    
    private String videoId;
    private String parentId;
} 