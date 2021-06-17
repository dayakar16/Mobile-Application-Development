package com.ahari.forumapplication;

import java.util.Date;

/*
    HW06
    Comment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class Comment {
    String text;
    Account createdBy;
    Date createdAt;
    String commentId;

    public Comment(String text, Account createdBy, Date createdAt) {
        this.text = text;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", commentId='" + commentId + '\'' +
                '}';
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
