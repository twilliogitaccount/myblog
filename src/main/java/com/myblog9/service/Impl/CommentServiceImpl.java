package com.myblog9.service.Impl;

import com.myblog9.entity.Comment;
import com.myblog9.entity.Post;
import com.myblog9.payload.CommentDto;
import com.myblog9.repository.CommentRepository;
import com.myblog9.repository.PostRepository;
import com.myblog9.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepo;
    private PostRepository postRepo;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepo.findById(postId).get();
        Comment comment=mapToEntity(commentDto);
        comment.setPost(post);
        Comment c=commentRepo.save(comment);

        return mapToDto(c);
    }

    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepo.findById(postId).get();
        commentRepo.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> dtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public CommentDto updateComment(long commentId, CommentDto commentDto) {
        Comment com=commentRepo.findById(commentId).get();
        Post post=postRepo.findById(com.getPost().getId()).get();
        Comment comment=mapToEntity(commentDto);
        comment.setId(commentId);

        Comment savedComment=commentRepo.save(comment);
        CommentDto dto=mapToDto(savedComment);
        return dto;
    }

    Comment mapToEntity(CommentDto dto){
        Comment comment=new Comment();
        comment.setName(dto.getName());
        comment.setEmail(dto.getEmail());
        comment.setBody(dto.getBody());
        return comment;
    }
    CommentDto mapToDto(Comment comment){
        CommentDto commentDto=new CommentDto();
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }
}
