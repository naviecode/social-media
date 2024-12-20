package com.project.social_media.controllers;

import com.project.social_media.dto.CommentDTO;
import com.project.social_media.dto.CommentReactionDTO;
import com.project.social_media.dto.CommentResponseDTO;
import com.project.social_media.models.Comments;
import com.project.social_media.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @MessageMapping("/send-comment/{postId}")
    @SendTo("/topic/comments/{postId}")
    public long sendComment(CommentDTO commentDTO) {
        Comments comment = commentService.createComment(commentDTO.getPostId(), commentDTO.getUserId(), commentDTO.getContent(), commentDTO.getParentCommentId());
        return commentService.getCommentCountByPostId(commentDTO.getPostId());
    }

    @GetMapping("/{postId}")
    @ResponseBody
    public List<CommentResponseDTO> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @MessageMapping("/update-comment-reaction/{commentId}")
    @SendTo("/topic/update-comment-reaction")
    public CommentReactionDTO updateCommentReaction(CommentReactionDTO reactionDTO) {
        return commentService.updateCommentReaction(reactionDTO);
    }
}