package pe.edu.upc.fixcampus;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
    private final UserRepository users;
    public CurrentUser(UserRepository users) { this.users = users; }
    public AppUser get() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new ApiException(401,"unauthorized");
        return users.findByEmailIgnoreCase(auth.getName()).filter(user -> user.active)
            .orElseThrow(() -> new ApiException(401,"unauthorized"));
    }
    public AppUser admin() {
        AppUser actor = get();
        if (actor.role != Role.ADMIN) throw ApiException.forbidden();
        return actor;
    }
}
