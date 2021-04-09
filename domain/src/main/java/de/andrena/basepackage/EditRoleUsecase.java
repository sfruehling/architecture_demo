package de.andrena.basepackage;

public interface EditRoleUsecase {
    Role saveNewRole(Role role);

    Role update(Role role);

    void deleteRoleById(String id);
}
