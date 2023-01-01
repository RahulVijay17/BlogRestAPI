package com.myb.blogrestapi.service;

import com.myb.blogrestapi.dto.PostDto;
import com.myb.blogrestapi.entity.Post;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts();

    PostDto getPostById(Long postId);

    PostDto updatePost(PostDto postDto,Long postId);

    void deletePostById(Long postId);

}
