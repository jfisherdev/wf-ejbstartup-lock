package com.jfisherdev.wfejbstartuplock.commons;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Josh Fisher
 */
public class User implements Serializable {

    public static final User UNDEFINED = new User("undefined", -1L);

    private String userName;
    private Long userId;

    public User() {
    }

    public User(String userName, Long userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return userName.equals(user.userName) && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userId);
    }
}
