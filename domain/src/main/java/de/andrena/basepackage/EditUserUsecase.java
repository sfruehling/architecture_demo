package de.andrena.basepackage;

public interface EditUserUsecase {
    User saveNewUser(User user);

    User updateUser(String domainId, User updatedUser);

    void deleteUserByDomainId(String domainId);
}
