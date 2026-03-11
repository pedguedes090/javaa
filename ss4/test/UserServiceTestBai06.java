import Bai06.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
public class UserServiceTestBai06 {
    private UserService service;
    private User existingUser;
    private List<User> users;

    @BeforeEach
    void setUp(){
        service = new UserService();
        existingUser =
                new User("old@gmail.com",
                        LocalDate.of(2000,1,1));

        users = new ArrayList<>();
        users.add(existingUser);
    }
    @Test
    void shouldUpdateProfile_whenEmailAndBirthDateValid(){
        UserProfile profile =
                new UserProfile("new@gmail.com",
                        LocalDate.of(1999,1,1));

        User result =
                service.updateProfile(existingUser, profile, users);

        assertNotNull(result);
    }
    @Test
    void shouldRejectUpdate_whenBirthDateInFuture(){
        UserProfile profile =
                new UserProfile("new@gmail.com",
                        LocalDate.now().plusDays(1));

        User result =
                service.updateProfile(existingUser, profile, users);

        assertNull(result);
    }
    @Test
    void shouldRejectUpdate_whenEmailAlreadyExists(){
        users.add(new User("duplicate@gmail.com",
                LocalDate.of(1999,1,1)));

        UserProfile profile =
                new UserProfile("duplicate@gmail.com",
                        LocalDate.of(1999,1,1));

        User result =
                service.updateProfile(existingUser, profile, users);

        assertNull(result);
    }
    @Test
    void shouldAllowUpdate_whenEmailSameAsCurrent(){
        UserProfile profile =
                new UserProfile("old@gmail.com",
                        LocalDate.of(1995,1,1));
        User result =
                service.updateProfile(existingUser, profile, users);

        assertNotNull(result);
    }
    @Test
    void shouldUpdate_whenUserListEmpty(){
        List<User> emptyList = new ArrayList<>();
        UserProfile profile =
                new UserProfile("new@gmail.com",
                        LocalDate.of(1999,1,1));

        User result =
                service.updateProfile(existingUser, profile, emptyList);

        assertNotNull(result);
    }
    @Test
    void shouldRejectUpdate_whenMultipleConstraintsViolated(){
        users.add(new User("duplicate@gmail.com",
                LocalDate.of(1999,1,1)));
        UserProfile profile =
                new UserProfile("duplicate@gmail.com",
                        LocalDate.now().plusDays(2));
        User result =
                service.updateProfile(existingUser, profile, users);

        assertNull(result);
    }
}