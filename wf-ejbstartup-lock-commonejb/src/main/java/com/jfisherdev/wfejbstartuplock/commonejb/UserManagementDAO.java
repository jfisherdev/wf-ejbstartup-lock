package com.jfisherdev.wfejbstartuplock.commonejb;

import com.jfisherdev.wfejbstartuplock.commons.User;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Josh Fisher
 */
public class UserManagementDAO {

    private static final Set<User> users = new LinkedHashSet<>();

    static {
        final Set<User> defaultUsersData = new LinkedHashSet<>();
        final User adminUser = new User("admin", 1L);
        final User reportUser = new User("report", 2L);
        final User guestUser = new User("guest", 3L);

        defaultUsersData.add(adminUser);
        defaultUsersData.add(reportUser);
        defaultUsersData.add(guestUser);
        users.addAll(defaultUsersData);
    }

    Set<User> listUsers() {
        return users;
    }

    User getUserByUserName(String userName) {
        return users.stream().
                filter(user -> user.getUserName().equals(userName)).
                findFirst().
                orElse(User.UNDEFINED);
    }

    User getUserByUserId(Long userId) {
        return users.stream().
                filter(user -> user.getUserId().equals(userId)).
                findFirst().
                orElse(User.UNDEFINED);
    }

}
