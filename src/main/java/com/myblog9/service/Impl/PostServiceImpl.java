package com.myblog9.service.Impl;

import com.myblog9.entity.Post;
import com.myblog9.payload.PostDto;
import com.myblog9.payload.PostResponse;
import com.myblog9.repository.PostRepository;
import com.myblog9.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepo;

    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepo, ModelMapper modelMapper) {

        this.postRepo = postRepo;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post savedPost = postRepo.save(post);
        PostDto dto = mapToDto(savedPost);
        return dto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNo,pageSize,sort);
        Page<Post> pagePostObject=postRepo.findAll(pageable);
        List<Post> posts = pagePostObject.getContent();
        List<PostDto> dtos =  posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse response=new PostResponse();
        response.setDto(dtos);
        response.setPageNo(pagePostObject.getNumber());
        response.setTotalPages(pagePostObject.getTotalPages());
        response.setLastPage(pagePostObject.isLast());
        response.setPageSize(pagePostObject.getSize());

        return response;
    }


    PostDto mapToDto(Post savedPost) {
        PostDto postDto=modelMapper.map(savedPost,PostDto.class);
        //PostDto postDto=new PostDto();
       //postDto.setTitle(savedPost.getTitle());
        //postDto.setDescription(savedPost.getDescription());
       // postDto.setContent(savedPost.getContent());
        return postDto;
    }

    Post mapToEntity(PostDto postDto){
        Post post=modelMapper.map(postDto,Post.class);
//        Post post=new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;

    }
}
