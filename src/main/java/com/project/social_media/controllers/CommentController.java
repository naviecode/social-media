package com.project.social_media.controllers;

import com.project.social_media.dto.CommentDTO;
import com.project.social_media.models.Comments;
import com.project.social_media.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @MessageMapping("/send-comment/{postId}")
    @SendTo("/topic/comments/{postId}")
    public long sendComment(CommentDTO commentDTO) {
        Comments comment = commentService.createComment(commentDTO.getPostId(), commentDTO.getUserId(), commentDTO.getContent(), null);
        return commentService.getCommentCountByPostId(commentDTO.getPostId());
    }
}