package com.skillconnect.server.service.serviceImpl;

import com.skillconnect.server.model.Post;
import com.skillconnect.server.model.User;
import com.skillconnect.server.repository.PostRepository;
import com.skillconnect.server.repository.UserRepository;
import com.skillconnect.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        log.info("PostServiceImpl initialized");
    }

    @Override
    public Post createPost(Post post) {
        log.info("Creating new post for user ID: {}", post.getUser().getUserId());
        User user = userRepository.findById(post.getUser().getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", post.getUser().getUserId());
                    return new RuntimeException("User not found with id: " + post.getUser().getUserId());
                });

        post.setUser(user);

        Post savedPost = postRepository.save(post);

        log.info("Post created successfully with ID: {}", savedPost.getPostId());
        return savedPost;
    }

    @Override
    public Optional<Post> findById(int postId) {
        log.debug("Finding post by ID: {}", postId);
        return postRepository.findById(postId);
    }

    @Override
    public List<Post> findPostsByUserId(int userId) {
        log.debug("Finding posts for user ID: {}", userId);
        List<Post> posts = postRepository.findByUser_UserId(userId);
        log.debug("Found {} posts for user ID: {}", posts.size(), userId);
        return posts;
    }

    @Override
    public List<Post> findAllPosts() {
        log.debug("Retrieving all posts");
        List<Post> posts = postRepository.findAll();
        log.debug("Found {} posts", posts.size());
        return posts;
    }

    @Override
    public Post updatePost(Post post) {
        log.info("Updating post with ID: {}", post.getPostId());
        if (!postRepository.existsById(post.getPostId())) {
            log.error("Post not found with ID: {}", post.getPostId());
            throw new RuntimeException("Post not found with id: " + post.getPostId());
        }
        Post updatedPost = postRepository.save(post);
        log.info("Post updated successfully: {}", post.getPostId());
        return updatedPost;
    }

    @Override
    public void deletePost(int postId) {
        log.info("Deleting post with ID: {}", postId);
        postRepository.deleteById(postId);
        log.info("Post deleted successfully: {}", postId);
    }
}
