package com.jfisherdev.wfejbstartuplock.commonejb;

import com.jfisherdev.wfejbstartuplock.commons.User;
import com.jfisherdev.wfejbstartuplock.commons.ejb.UserManagement;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Set;

/**
 * @author Josh Fisher
 */
@Stateless(name = UserManagement.BEAN_NAME)
@Remote(UserManagement.class)
public class UserManagementBean implements UserManagement {

    private final UserManagementDAO userManagementDAO = new UserManagementDAO();

    @Override
    public Set<User> listUsers() {
        return userManagementDAO.listUsers();
    }

    @Override
    public User getUserByName(String userName) {
        return userManagementDAO.getUserByUserName(userName);
    }

    @Override
    public User getUserById(Long userId) {
        return userManagementDAO.getUserByUserId(userId);
    }

}
