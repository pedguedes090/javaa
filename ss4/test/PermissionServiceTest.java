import Bai05.User;
import Bai05.Role;
import Bai05.Action;
import Bai05.PermissionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PermissionServiceTest {
    private PermissionService test = new PermissionService();
    @Test
    void testAdminPermissions() {
        User admin = new User("admin1", Role.ADMIN);
        assertTrue(test.canPerformAction(admin, Action.DELETE_USER));
        assertTrue(test.canPerformAction(admin, Action.LOCK_USER));
        assertTrue(test.canPerformAction(admin, Action.VIEW_PROFILE));
    }
    @Test
    void testModeratorPermissions() {
        User mod = new User("mod1", Role.MODERATOR);
        assertFalse(test.canPerformAction(mod, Action.DELETE_USER));
        assertTrue(test.canPerformAction(mod, Action.LOCK_USER));
        assertTrue(test.canPerformAction(mod, Action.VIEW_PROFILE));
    }
    @Test
    void testUserPermissions() {
        User user = new User("user1", Role.USER);
        assertFalse(test.canPerformAction(user, Action.DELETE_USER));
        assertFalse(test.canPerformAction(user, Action.LOCK_USER));
        assertTrue(test.canPerformAction(user, Action.VIEW_PROFILE));
    }
}
