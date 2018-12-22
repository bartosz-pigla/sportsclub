package query.model.baseEntity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
public abstract class BaseEntity {

    public static final int LONG_STRING_MAX_LENGTH = 3000;

    @Id
    protected UUID id = UUID.randomUUID();
    private boolean deleted;
}
