package com.jfisherdev.wfejbstartuplock.ejb2;

import com.jfisherdev.wfejbstartuplock.commons.User;
import com.jfisherdev.wfejbstartuplock.commons.ejb.UserManagementEjbClient;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * @author Josh Fisher
 */
@Singleton
@Startup
public class SecurityInitializer {

    private static final Logger logger = Logger.getLogger(SecurityInitializer.class.getName());

    private final UserManagementEjbClient userManagementEjbClient = new UserManagementEjbClient();

    @PostConstruct
    private void postConstruct() {
        checkUser("admin");
        checkUser("superuser");
    }

    private void checkUser(String userName) {
        logger.info("Checking for '" + userName + "' user");
        final User user = userManagementEjbClient.getUserByUserName(userName);
        if (Objects.equals(User.UNDEFINED, user)) {
            logger.severe("User '" + userName + "' not found");
        } else {
            logger.info("User '" + userName + "' is available");
        }
    }
}
