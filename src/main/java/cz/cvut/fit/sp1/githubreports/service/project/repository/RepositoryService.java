package cz.cvut.fit.sp1.githubreports.service.project.repository;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.project.ProjectJpaRepository;
import cz.cvut.fit.sp1.githubreports.dao.project.RepositoryJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service("RepositoryService")
public class RepositoryService implements RepositorySPI {

    RepositoryJpaRepository jpaRepository;
    ProjectJpaRepository projectJpaRepository;

    private void checkValidation(Repository repository) {
        if (repository.getProjects().isEmpty() ||
                repository.getProjects().stream()
                .anyMatch(project -> project.getRepositories().stream()
                        .anyMatch(rep -> rep.getRepositoryName().equals(repository.getRepositoryName()))))
            throw new EntityStateException();
    }

    @Override
    public Collection<Repository> readAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Repository> readById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Repository create(Repository repository) throws EntityStateException {
        if (repository.getRepositoryId() != null) {
            if (jpaRepository.existsById(repository.getRepositoryId()))
                throw new EntityStateException();
        }
        checkValidation(repository);
        return jpaRepository.save(repository);
    }

    @Override
    public Repository update(Long id, Repository repository) throws EntityStateException {
        if (id == null || !jpaRepository.existsById(id))
            throw new NoEntityFoundException();
        checkValidation(repository);

//        /*
//            If we update in Project list of repositories we should manually add this repository to collection of repositories in project
//            https://stackoverflow.com/questions/52203892/updating-manytomany-relationships-in-jpa-or-hibernate
//        */
//        repository.getProjects().forEach(p->
//                {
//                    if(p.getRepositories().stream().noneMatch(r->r.getRepositoryId().equals(id))) {
//                        List<Repository> repositories = p.getRepositories();
//                        repositories.add(repository);
//                        p.setRepositories(repositories);
//                        projectJpaRepository.save(p);
//                    }
//                }
//        );

        return jpaRepository.save(repository);
    }

    @Override
    public void delete(Long id) {
        if (jpaRepository.existsById(id))
            jpaRepository.deleteById(id);
    }
}
