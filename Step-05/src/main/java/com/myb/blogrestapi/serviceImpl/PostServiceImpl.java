package com.myb.blogrestapi.serviceImpl;

import com.myb.blogrestapi.dto.PageResponse;
import com.myb.blogrestapi.dto.PostDto;
import com.myb.blogrestapi.entity.Post;
import com.myb.blogrestapi.exception.ResourceNotFoundException;
import com.myb.blogrestapi.repository.PostRepository;
import com.myb.blogrestapi.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setPostRepository(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        //convert dto to entity
        Post post = dtoToEntity(postDto);

        Post newPost = postRepository.save(post);

        //convert entity to dto
        PostDto postResponse = mapToDto(newPost);


        return postResponse;
    }

    @Override
    public PageResponse getAllPosts(int pageNo,int pageSize,String sortBy,String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase
                (Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

       Page<Post> posts=postRepository.findAll(pageable);

       List<Post> postList = posts.getContent();

       List<PostDto> content = postList.stream()
               .map(post -> mapToDto(post))
               .collect(Collectors.toList());

       PageResponse pageResponse =new PageResponse();
       pageResponse.setContent(content);
       pageResponse.setPageNo(pageNo);
       pageResponse.setPageSize(pageSize);
       pageResponse.setTotalElements(posts.getTotalElements());
       pageResponse.setTotalPages(posts.getTotalPages());
       pageResponse.setLast(posts.isLast());

       return pageResponse;

    }

    @Override
    public PostDto getPostById( Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        postRepository.delete(post);
    }

    //convert entity into dto
    private PostDto mapToDto(Post post){
        PostDto postDto = modelMapper.map(post, PostDto.class);
        return postDto;
    }

    //convert dto into entity
    private Post dtoToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto, Post.class);
        return post;
    }
}
