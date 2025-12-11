package gr.hua.dit.fittrack.core.service.mapper;

import gr.hua.dit.fittrack.core.model.entity.User;
import gr.hua.dit.fittrack.core.service.impl.dto.UserView;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserView convertUserToUserView(User user) {
        if (user == null) {
            return null;
        }
        return new UserView(
                user.getId(),
                user.getEmailAddress(),
                user.getUserFirstName(),
                user.getUserLastName(),
                user.getRole()
        );
    }
}
