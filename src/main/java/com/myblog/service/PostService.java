package com.myblog.service;

import com.myblog.payload.PostResponse;
import com.myblog.payload.postDto;

import java.util.List;

public interface PostService {
    postDto  createPost(postDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    postDto getPostById(long id);

    postDto updatePost(postDto postDto, long id);

    void deletePostById(long id);
}
