package projects.service;

import java.util.List;
import java.util.NoSuchElementException;

import projects.dao.ProjectDao;
import projects.entity.Project;

/**
 * 
 * @author clayr Projects service layer
 */
public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();

	/**
	 * 
	 * @param project
	 * @return Calls insertProject method in Dao.
	 */
	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

	/**
	 * 
	 * @return Calls fetchAllProjects method in Dao.
	 */
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	/**
	 * 
	 * @param projectId
	 * @return Calls fetchProjectById in Dao, if a projectId that doesn't exist is
	 *         passed, throws exception with message.
	 */
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException("Project with project ID=" + projectId + " does not exist."));
	}

}
