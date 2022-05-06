package cz.cvut.fit.sp1.githubreports.service.project.commit;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.project.Commit;

import java.util.Collection;
import java.util.Optional;

public interface CommitSPI {

    Collection<Commit> readAll();

    Optional<Commit> readById(Long id);

    Commit create(Commit commit) throws EntityStateException;

    Commit update(Long id, Commit commit) throws EntityStateException;

    void delete(Long id);
}
