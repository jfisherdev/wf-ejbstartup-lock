package com.jfisherdev.wfejbstartuplock.webapp;

import com.jfisherdev.wfejbstartuplock.commons.User;
import com.jfisherdev.wfejbstartuplock.commons.ejb.UserManagementEjbClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Josh Fisher
 */
@WebListener
public class WebContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(WebContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Checking for users");
        final Set<User> availableUsers = new UserManagementEjbClient().listUsers();
        logger.info("Available users: " + availableUsers);
    }

}
