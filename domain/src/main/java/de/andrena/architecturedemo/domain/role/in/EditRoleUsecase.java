package de.andrena.architecturedemo.domain.role.in;

import de.andrena.architecturedemo.domain.role.Role;

public interface EditRoleUsecase {
    Role saveNewRole(Role role);

    Role update(Role role);

    void deleteRoleById(String id);
}
