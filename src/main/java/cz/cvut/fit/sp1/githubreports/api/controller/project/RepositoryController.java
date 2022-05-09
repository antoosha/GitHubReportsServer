package cz.cvut.fit.sp1.githubreports.api.controller.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.RepositoryDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryConverter;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositorySPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final RepositorySPI repositorySPI;
    private final RepositoryConverter repositoryConverter;

    /**
         GET: "/repositories"
         @return collection of all repositories in our database.
     */
    @GetMapping()
    public Collection<RepositoryDTO> getAll() {
        return repositoryConverter.fromModelsMany(repositorySPI.readAll());
    }

    /**
         GET: "/repositories/{id}"
         @return repository by id.
     */
    @GetMapping("/{id}")
    public RepositoryDTO getOne(@PathVariable Long id) {
        return repositoryConverter.fromModel(repositorySPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    /**
     Create new repository.

     POST: "/repositories"

     Request body example:
     {
         "repositoryName": "repositoryName",
         "commitsIDs": [],
         "projectsIDs": [
            1
         ]
     }
     projectsIDs - cannot be empty.
     @return created repository.
     */
    @PostMapping()
    public RepositoryDTO create(@RequestBody RepositoryDTO repositoryDTO) throws EntityStateException {
        return repositoryConverter.fromModel(repositorySPI.create(repositoryConverter.toModel(repositoryDTO)));
    }

    /**
     Update repository by id.
     PUT: "/repositories/{id}"

     Request body example:
     {
         "repositoryID": 1,
         "repositoryName": "repository",
         "commitsIDs": [],
         "projectsIDs": [
            1
         ]
     }
     @return updated repository.
     */
    @PutMapping("/{id}")
    public RepositoryDTO update(@PathVariable Long id, @RequestBody RepositoryDTO repositoryDTO) throws IncorrectRequestException, EntityStateException {
        if (!repositoryDTO.getRepositoryID().equals(id))
            throw new IncorrectRequestException();
        return repositoryConverter.fromModel(repositorySPI.update(repositoryDTO.getRepositoryID(), repositoryConverter.toModel(repositoryDTO)));
    }

    /**
     Delete repository by id.
     DELETE: "/repositories/{id}"
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repositorySPI.readById(id).isEmpty())
            throw new NoEntityFoundException();
        repositorySPI.delete(id);
    }
}
