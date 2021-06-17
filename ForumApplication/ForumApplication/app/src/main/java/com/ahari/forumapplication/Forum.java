package com.ahari.forumapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    HW06
    Forum
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class Forum implements Serializable {
    String title, description;
    Date createdAt;
    Account createdBy;
    String forumId;
    List<Account> likedBy;

    @Override
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                ", forumId='" + forumId + '\'' +
                ", likedBy=" + likedBy +
                '}';
    }

    public List<Account> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<Account> likedBy) {
        this.likedBy = likedBy;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Account getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Account createdBy) {
        this.createdBy = createdBy;
    }
}
