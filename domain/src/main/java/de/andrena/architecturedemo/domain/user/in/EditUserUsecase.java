package de.andrena.architecturedemo.domain.user.in;

import de.andrena.architecturedemo.domain.user.User;

public interface EditUserUsecase {
    User saveNewUser(User user);

    User updateUser(String domainId, User updatedUser);

    void deleteUserByDomainId(String domainId);
}
