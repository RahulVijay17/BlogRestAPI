package com.myb.blogrestapi.serviceImpl;

import com.myb.blogrestapi.dto.CommentDto;
import com.myb.blogrestapi.entity.Comment;
import com.myb.blogrestapi.entity.Post;
import com.myb.blogrestapi.exception.BlogAPIException;
import com.myb.blogrestapi.exception.ResourceNotFoundException;
import com.myb.blogrestapi.repository.CommentRepository;
import com.myb.blogrestapi.repository.PostRepository;
import com.myb.blogrestapi.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Comment comment= mapToEntity(commentDto);

        //retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        //set post to comment entity
        comment.setPost(post);

        //comment entity to db
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        //retrieve comment by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

            //convert list of comment entity to list of comment dtos
            return comments.stream()
                    .map(comment -> mapToDto(comment))
                    .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

          //retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        //comment not belong to particular post
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {

        //retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        //comment not belong to particular post
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comment does not belong to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {

        //retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));

        //retrieve comment entity by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        //comment not belong to particular post
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "comment does not belong to post");
        }

        commentRepository.delete(comment);

    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto =modelMapper.map(comment,CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }
}
