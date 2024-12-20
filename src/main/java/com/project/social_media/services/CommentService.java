package com.project.social_media.services;

import com.project.social_media.dto.CommentReactionDTO;
import com.project.social_media.dto.CommentResponseDTO;
import com.project.social_media.models.CommentReactions;
import com.project.social_media.models.Comments;
import com.project.social_media.models.Posts;
import com.project.social_media.models.Users;
import com.project.social_media.repository.CommentReactionsRepository;
import com.project.social_media.repository.CommentsRepository;
import com.project.social_media.repository.PostsRepository;
import com.project.social_media.repository.UsersRepository;
import com.project.social_media.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CommentReactionsRepository commentReactionsRepository;

    @Transactional
    public Comments createComment(Long postId, Long userId, String content, Long parentCommentId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Posts post = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comments parentComment = parentCommentId != null ? commentsRepository.findById(parentCommentId).orElse(null) : null;
        Comments comment = new Comments();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setParentComment(parentComment);
        return commentsRepository.save(comment);
    }

    public long getCommentCountByPostId(Long postId) {
        return commentsRepository.countByPost_PostId(postId);
    }

    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        List<Comments> comments = commentsRepository.findByPost_PostId(postId);
        return comments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO convertToDTO(Comments comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setUserFullName(comment.getUser().getFullName());
        dto.setUserAvatarUrl(comment.getUser().getAvatarURL());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setLikeCount(comment.getReactions().size());
        dto.setIsLiked(comment.getReactions().stream().anyMatch(reaction -> reaction.getUser().getUserId().equals(SecurityUtils.getLoggedInUserId())));
        dto.setReplies(comment.getReplies().stream().map(this::convertToDTO).collect(Collectors.toList()));
        return dto;
    }

    @Transactional
    public CommentReactionDTO updateCommentReaction(CommentReactionDTO reactionDTO) {
        Users user = usersRepository.findById(reactionDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Comments comment = commentsRepository.findById(reactionDTO.getCommentId()).orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!reactionDTO.isLiked()) {
            CommentReactions reaction = new CommentReactions(user, comment);
            commentReactionsRepository.save(reaction);
        } else {
            commentReactionsRepository.deleteByCommentAndUser(comment, user);
        }

        reactionDTO.setLiked(!reactionDTO.isLiked());
        return reactionDTO;
    }
}