package com.example.webApp.DataTransferObjects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentResponseDTO {
    private long commentId;
    private String body;
    private LocalDateTime creationTime;
    private long userId;
    private long postId;
    private Long parentCommentId;
    private List<CommentResponseDTO> replies = new ArrayList<>();

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public List<CommentResponseDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentResponseDTO> replies) {
        this.replies = replies;
    }
}
