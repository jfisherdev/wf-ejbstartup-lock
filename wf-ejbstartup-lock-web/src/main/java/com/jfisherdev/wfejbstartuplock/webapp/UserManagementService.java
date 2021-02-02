package com.jfisherdev.wfejbstartuplock.webapp;

import com.jfisherdev.wfejbstartuplock.commons.User;
import com.jfisherdev.wfejbstartuplock.commons.ejb.UserManagementEjbClient;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Josh Fisher
 */
@Path("/users")
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
public class UserManagementService {

    private final UserManagementEjbClient userManagementEjbClient = new UserManagementEjbClient();

    @GET
    public Set<User> listUsers(@QueryParam("userName") @DefaultValue("") String userName,
                               @QueryParam("userId") @DefaultValue("-1") Long userId) {
        final Set<User> users = userManagementEjbClient.listUsers();

        return users.stream().filter(user -> {
            if (!userName.isEmpty() && !userName.equals(user.getUserName())) {
                return false;
            }
            if (userId > -1 && !userId.equals(user.getUserId())) {
                return false;
            }
            return true;
        }).collect(Collectors.toSet());
    }

}
