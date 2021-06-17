package com.ahari.forumapplication;

import java.util.ArrayList;
import java.util.List;

/*
    HW06
    UserComments
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class UserComments {
    List<Comment> comments;

    @Override
    public String toString() {
        return "UserComments{" +
                "comments=" + comments +
                '}';
    }

    public List<Comment> getComments() {
        comments = comments == null ? new ArrayList<>() : comments;
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
