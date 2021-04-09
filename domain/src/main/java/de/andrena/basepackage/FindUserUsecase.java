package de.andrena.basepackage;

import java.util.List;

public interface FindUserUsecase {
    List<User> findAllUsers();

    User findUserByDomainId(String id);
}
