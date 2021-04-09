package de.andrena.basepackage;

import java.util.List;

public interface FindRoleUsecase {


    List<Role> findAllRoles();

    Role findRoleById(String id);
}
