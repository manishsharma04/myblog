package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.PostResponse;
import com.myblog.payload.postDto;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public postDto createPost(postDto postDto) {
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);
      postDto newPostDto = mapToDto(newPost);
        return newPostDto;
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();




        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> contents = posts.getContent();



        List<postDto> postDtos = contents.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(postDtos);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setTotalElements(posts.getTotalElements() );
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setPageSize(posts.getSize());
        postResponse.setLast(posts.isLast());

        return postResponse;



    }

    @Override
    public postDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", id)
        );

        return mapToDto(post);
    }









    @Override
    public postDto updatePost(postDto postDto, long id) {
       Post post = postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("post","id",id)
        );
        post.setTitle(postDto.getTitle());
        post.setTitle(postDto.getDescription());
        post.setTitle(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);

    }

    @Override
    public void deletePostById(long id) {

       Post post = postRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Post "," id" ,id)
        );
       postRepository.delete(post);


    }

    Post mapToEntity(postDto postDto){
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    postDto mapToDto(Post post){
        postDto postDto = new postDto();
        postDto.setId(post.getId());

        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        return postDto;

    }
}
