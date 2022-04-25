package cz.cvut.fit.sp1.githubreports.service.project.project;

import cz.cvut.fit.sp1.githubreports.api.dto.project.ProjectDTO;
import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.project.Repository;
import cz.cvut.fit.sp1.githubreports.model.project.Tag;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import cz.cvut.fit.sp1.githubreports.service.project.repository.RepositoryService;
import cz.cvut.fit.sp1.githubreports.service.project.tag.TagService;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticService;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import lombok.AllArgsConstructor;

import javax.persistence.Converter;
import java.util.List;
import java.util.stream.Collectors;

@Converter
@AllArgsConstructor
public class ProjectConverter {

    private final UserService userService;
    private final RepositoryService repositoryService;
    private final StatisticService statisticService;
    private final TagService tagService;

    public Project toModel(ProjectDTO projectDTO) {
        return new Project(projectDTO.getProjectID(), projectDTO.getCreatedDate(), projectDTO.getProjectName(), projectDTO.getDescription(),
                userService.readUserById(projectDTO.getAuthorID()).orElseThrow(RuntimeException::new),
                projectDTO.getRepositoriesIDs().stream().map(repositoryID -> repositoryService.readRepositoryById(repositoryID).orElseThrow(RuntimeException::new)).collect(Collectors.toList()),
                projectDTO.getStatisticsIDs().stream().map(statisticID -> statisticService.readStatisticById(statisticID).orElseThrow(RuntimeException::new)).collect(Collectors.toList()),
                projectDTO.getUsersIDs().stream().map(userID -> userService.readUserById(userID).orElseThrow(RuntimeException::new)).collect(Collectors.toList()),
                projectDTO.getTagsIDs().stream().map(tagID -> tagService.readTagById(tagID).orElseThrow(RuntimeException::new)).collect(Collectors.toList())
        );
    }

    public ProjectDTO fromModel(Project project) {
        return new ProjectDTO(project.getProjectId(), project.getCreatedDate(), project.getProjectName(), project.getDescription(), project.getAuthor().getUserId(),
                project.getRepositories().stream().map(Repository::getRepositoryId).collect(Collectors.toList()),
                project.getStatistics().stream().map(Statistic::getStatisticId).collect(Collectors.toList()),
                project.getUsers().stream().map(User::getUserId).collect(Collectors.toList()),
                project.getTags().stream().map(Tag::getTagId).collect(Collectors.toList())
        );
    }

    public List<Project> toModelsMany(List<ProjectDTO> projectDTOs) {
        return projectDTOs.stream().map(this::toModel).toList();
    }

    public List<ProjectDTO> fromModelsMany(List<Project> projects) {
        return projects.stream().map(this::fromModel).toList();
    }
}
