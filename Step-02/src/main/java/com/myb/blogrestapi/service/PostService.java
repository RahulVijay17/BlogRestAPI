package com.myb.blogrestapi.service;

import com.myb.blogrestapi.dto.PageResponse;
import com.myb.blogrestapi.dto.PostDto;


public interface PostService {

    PostDto createPost(PostDto postDto);

    PageResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);

    PostDto getPostById(Long postId);

    PostDto updatePost(PostDto postDto,Long postId);

    void deletePostById(Long postId);

}
