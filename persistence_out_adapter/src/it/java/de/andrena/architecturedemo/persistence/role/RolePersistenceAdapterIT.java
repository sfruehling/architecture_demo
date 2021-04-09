package de.andrena.architecturedemo.persistence.role;

import de.andrena.architecturedemo.persistence.privilege.IdAlreadyInUseException;
import de.andrena.architecturedemo.domain.role.Role;
import de.andrena.architecturedemo.persistence.WithArchitectureDemoPostgresTestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithArchitectureDemoPostgresTestContainer
class RolePersistenceAdapterIT {
    @Autowired
    RolePersistenceAdapter rolePersistenceAdapterImpl;

    @Test
    void saveNewRoleShouldThrowIdAlreadyInUseExceptionWhenUniqueConstraintOfDomainIdIsViolated() {
        Role role = new Role("doubleId", "name", "desc", 0L, new ArrayList<>());

        Assertions.assertThrows(IdAlreadyInUseException.class, () -> {
            rolePersistenceAdapterImpl.saveNewRole(role);
            rolePersistenceAdapterImpl.saveNewRole(role);
        });
    }

}
