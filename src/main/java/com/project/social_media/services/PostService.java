package com.project.social_media.services;

import com.project.social_media.dto.PostDTO;
import com.project.social_media.dto.ReactionDTO;
import com.project.social_media.models.PostImages;
import com.project.social_media.models.Posts;
import com.project.social_media.models.Reactions;
import com.project.social_media.models.Users;
import com.project.social_media.repository.*;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger LOGGER = Logger.getLogger(PostService.class.getName());

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private FriendsRepository friendsRepository;

    @Autowired
    private ReactionsRepository reactionsRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostImagesRepository postImagesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public void createPost(String content, Posts.Privacy privacy, List<byte[]> imagesData) {
        try {
            // Get the logged-in user
            Long userId = SecurityUtils.getLoggedInUserId();
            Users user = new Users();
            user.setUserId(userId);

            // Create a new post
            Posts post = new Posts();
            post.setUser(user);
            post.setContent(content);
            post.setPrivacy(privacy);
            post.setCreatedAt(LocalDateTime.now());

            // Save the post to the database
            Posts savedPost = postsRepository.save(post);

            // Save the images associated with the post
            for (byte[] imageData : imagesData) {
                PostImages postImage = new PostImages();
                postImage.setPost(savedPost);
                postImage.setImageData(imageData);
                postImagesRepository.save(postImage);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating post", e);
            throw e;
        }
    }

    @Transactional
    public List<PostDTO> getFriendAndOwnPosts(int page, int size) {
        Long userId = SecurityUtils.getLoggedInUserId();
        List<Long> friendIds = friendsRepository.findAcceptedFriendIds(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Posts> postPage = postsRepository.findPostsByUserIdOrFriendIds(userId, friendIds, pageable);
        List<Posts> posts = postPage.getContent();

        var postDTOs = posts.stream().map(post -> {
            Users user = post.getUser();
            List<byte[]> imagesData = postImagesRepository.findByPost(post).stream()
                    .map(PostImages::getImageData)
                    .collect(Collectors.toList());
            long reactionCount = reactionsRepository.countByPost(post);
            long commentCount = commentsRepository.countByPost(post);

            // Check if the post is liked by the current user
            boolean isLiked = reactionsRepository.existsByPostAndUserId(post, userId);

            // Get the current user's avatar URL
            Users currentUser = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            String currentUserAvatarUrl = currentUser.getAvatarURL();

            return new PostDTO(
                    post.getPostId(),
                    post.getContent(),
                    post.getPrivacy(),
                    currentUserAvatarUrl,
                    post.getCreatedAt(),
                    reactionCount,
                    commentCount,
                    imagesData,
                    user.getFullName(),
                    user.getAvatarURL(),
                    isLiked
            );
        }).collect(Collectors.toList());

        return postDTOs;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public PostDTO likePost(Long postId, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        Reactions reaction = new Reactions(user, post);
        reactionsRepository.save(reaction);

        PostDTO postDTO = getPostDTO(post, userId);
        postDTO.setUserId(userId);
        messagingTemplate.convertAndSend("/topic/update-reaction", postDTO);
        return postDTO;
    }

    @Transactional
    public PostDTO unlikePost(Long postId, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        reactionsRepository.deleteByPostAndUserId(post, userId);

        PostDTO postDTO = getPostDTO(post, userId);
        postDTO.setUserId(userId);
        messagingTemplate.convertAndSend("/topic/update-reaction", postDTO);
        return postDTO;
    }

    private PostDTO getPostDTO(Posts post, Long userId) {
        Users user = post.getUser();
        List<byte[]> imagesData = postImagesRepository.findByPost(post).stream()
                .map(PostImages::getImageData)
                .collect(Collectors.toList());
        long reactionCount = reactionsRepository.countByPost(post);
        long commentCount = commentsRepository.countByPost(post);

        boolean isLiked = reactionsRepository.existsByPostAndUserId(post, userId);

        Users currentUser = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String currentUserAvatarUrl = currentUser.getAvatarURL();

        return new PostDTO(
                post.getPostId(),
                post.getContent(),
                post.getPrivacy(),
                currentUserAvatarUrl,
                post.getCreatedAt(),
                reactionCount,
                commentCount,
                imagesData,
                user.getFullName(),
                user.getAvatarURL(),
                isLiked
        );
    }

    @Transactional
    public List<PostDTO> getUserPosts(Long userId, Long loggedInUserId, int page, int size) {
        Page<Posts> postPage;
        if (userId.equals(loggedInUserId)) {
            postPage = postsRepository.findAllByUser_UserId(userId, PageRequest.of(page, size));
        } else if (friendsRepository.existsByUserId1AndUserId2(loggedInUserId, userId)) {
            postPage = postsRepository.findByUser_UserIdAndPrivacyIn(userId, List.of(Posts.Privacy.FRIENDS, Posts.Privacy.PUBLIC), PageRequest.of(page, size));
        } else {
            postPage = postsRepository.findByUser_UserIdAndPrivacy(userId, Posts.Privacy.PUBLIC, PageRequest.of(page, size));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Posts> posts = postPage.getContent();

        var postDTOs = posts.stream().map(post -> {
            Users user = post.getUser();
            List<byte[]> imagesData = postImagesRepository.findByPost(post).stream()
                    .map(PostImages::getImageData)
                    .collect(Collectors.toList());
            long reactionCount = reactionsRepository.countByPost(post);
            long commentCount = commentsRepository.countByPost(post);

            // Check if the post is liked by the current user
            boolean isLiked = reactionsRepository.existsByPostAndUserId(post, userId);

            // Get the current user's avatar URL
            Users currentUser = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            String currentUserAvatarUrl = currentUser.getAvatarURL();

            return new PostDTO(
                    post.getPostId(),
                    post.getContent(),
                    post.getPrivacy(),
                    currentUserAvatarUrl,
                    post.getCreatedAt(),
                    reactionCount,
                    commentCount,
                    imagesData,
                    user.getFullName(),
                    user.getAvatarURL(),
                    isLiked
            );
        }).collect(Collectors.toList());

        return postDTOs;
    }

    public long countPostsByUserId(Long userId) {
        return postsRepository.countPostsByUserId(userId);
    }

}