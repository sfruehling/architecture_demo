package de.andrena.architecturedemo.domain.user.in;

import de.andrena.architecturedemo.domain.user.User;

import java.util.List;

public interface FindUserUsecase {
    List<User> findAllUsers();

    User findUserByDomainId(String id);
}
