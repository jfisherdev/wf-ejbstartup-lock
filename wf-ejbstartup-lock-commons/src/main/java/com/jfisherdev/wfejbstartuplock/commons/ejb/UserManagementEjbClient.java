package com.jfisherdev.wfejbstartuplock.commons.ejb;

import com.jfisherdev.wfejbstartuplock.commons.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Set;

/**
 * @author Josh Fisher
 */
public class UserManagementEjbClient {

    private static final String LOOKUP_NAME =
            "java:global/" + EjbNamespaceConstants.APP_NAME + "/" +
                    EjbNamespaceConstants.COMMON_MODULE_NAME + "/" +
                    UserManagement.BEAN_NAME + "!" + UserManagement.class.getCanonicalName();

    public Set<User> listUsers() {
        return getUserManagementBean().listUsers();
    }

    public User getUserByUserName(String userName) {
        return getUserManagementBean().getUserByName(userName);
    }

    public User getUserByUserId(Long userId) {
        return getUserManagementBean().getUserById(userId);
    }

    private UserManagement getUserManagementBean() {
        try {
            return InitialContext.doLookup(LOOKUP_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

}
