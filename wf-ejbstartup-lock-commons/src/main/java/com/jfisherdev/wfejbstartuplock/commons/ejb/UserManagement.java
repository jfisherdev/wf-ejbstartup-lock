package com.jfisherdev.wfejbstartuplock.commons.ejb;

import com.jfisherdev.wfejbstartuplock.commons.User;

import java.util.Set;

/**
 * @author Josh Fisher
 */
public interface UserManagement {

    String BEAN_NAME = "UserManagementBean";

    Set<User> listUsers();

    User getUserByName(String userName);

    User getUserById(Long userId);
}
