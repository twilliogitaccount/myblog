package com.myblog9.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> dto;
    private int pageNo;
    private int pageSize;
    private boolean lastPage;
    private int totalPages;
}
