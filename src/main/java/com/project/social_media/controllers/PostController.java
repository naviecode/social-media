package com.project.social_media.controllers;

import com.project.social_media.dto.CreatePostRequestDTO;
import com.project.social_media.dto.PostDTO;
import com.project.social_media.dto.ReactionDTO;
import com.project.social_media.models.Posts;
import com.project.social_media.services.PostService;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger LOGGER = Logger.getLogger(PostController.class.getName());

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    @ResponseBody
    public String createPost(@RequestBody CreatePostRequestDTO request) {
        try {
            List<byte[]> decodedImages = request.getImagesData().stream()
                    .map(image -> Base64.getDecoder().decode(image))
                    .toList();
            Posts.Privacy privacy = Posts.Privacy.valueOf(request.getPrivacy().toUpperCase());
            postService.createPost(request.getContent(), privacy, decodedImages);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating post", e);
            throw e;
        }
        return "Post created successfully";
    }

    @GetMapping("/friend-and-own")
    @ResponseBody
    public List<PostDTO> getFriendAndOwnPosts(@RequestParam int page, @RequestParam int size) {
        return postService.getFriendAndOwnPosts(page, size);
    }

    @PostMapping("/like/{postId}")
    @ResponseBody
    public PostDTO likePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getLoggedInUserId();
        return postService.likePost(postId, userId);
    }

    @PostMapping("/unlike/{postId}")
    @ResponseBody
    public PostDTO unlikePost(@PathVariable Long postId) {
        Long userId = SecurityUtils.getLoggedInUserId();
        return postService.unlikePost(postId, userId);
    }

    @MessageMapping("/update-reaction/{postId}")
    @SendTo("/topic/update-reaction")
    public PostDTO updateReaction(ReactionDTO reactionDTO) {
        if (!reactionDTO.isLiked()) {
            return postService.likePost(reactionDTO.getPostId(), reactionDTO.getUserId());
        } else {
            return postService.unlikePost(reactionDTO.getPostId(), reactionDTO.getUserId());
        }
    }
}