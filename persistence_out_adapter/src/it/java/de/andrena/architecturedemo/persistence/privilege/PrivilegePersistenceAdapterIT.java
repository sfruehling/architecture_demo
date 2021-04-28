package de.andrena.architecturedemo.persistence.privilege;

import de.andrena.architecturedemo.domain.privilege.Privilege;
import de.andrena.architecturedemo.persistence.WithArchitectureDemoPostgresTestContainer;
import de.andrena.architecturedemo.persistence.exception.IdAlreadyInUseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithArchitectureDemoPostgresTestContainer
class PrivilegePersistenceAdapterIT {

    @Autowired
    PrivilegePersistenceAdapter privilegePersistenceAdapterImpl;

    @Test
    void saveNewPrivilegeShouldThrowIdAlreadyInUseExceptionWhenUniqueConstraintOfDomainIdIsViolated() {

        Privilege privilege = new Privilege("doubleId", "name", "desc", 0L);

        assertThrows(IdAlreadyInUseException.class, () -> {
            privilegePersistenceAdapterImpl.saveNewPrivilege(privilege);
            privilegePersistenceAdapterImpl.saveNewPrivilege(privilege);
        });
    }

}
