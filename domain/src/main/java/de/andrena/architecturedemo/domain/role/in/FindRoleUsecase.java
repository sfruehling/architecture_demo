package de.andrena.architecturedemo.domain.role.in;

import de.andrena.architecturedemo.domain.role.Role;

import java.util.List;

public interface FindRoleUsecase {


    List<Role> findAllRoles();

    Role findRoleById(String id);
}
