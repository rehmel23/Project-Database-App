package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

/**
 * 
 * @author clayr
 * 
 *         ProjectsApp class; IO layer
 *
 */
public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project.",
			"2) List projects.",
			"3) Select a project.",
			"4) Update project details.",
			"5) Delete a project."
	);
	// @formatter:on

	/**
	 * 
	 * @param args
	 * 
	 *             Main class for ProjectsApp. Calls processUserSelection method.
	 */
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}

	/**
	 * Takes user selection and calls appropriate method based on user selection.
	 * Checks for valid selection. Throws an error if there is an exception. For
	 * example, user inputs letters instead of numbers.
	 */
	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				case 4:
					updateProjectDetails();
					break;
				case 5:
					deleteProject();
					break;
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e.toString());
			}
		}
	}

	/**
	 * Lists the projects stored in the database and asks the user for an integer
	 * input as to what project to delete based on ID. If the user inputs a value,
	 * it calls deleteProject in ProjectService and gives a confirmation message
	 * with the deleted project ID. If the provided project ID and the current
	 * selected project ID match, then the current Project is reset to null.
	 */
	private void deleteProject() {
		listProjects();
		Integer projectId = getIntInput("Enter the ID of the project you want to delete: ");

		if (Objects.nonNull(projectId)) {
			projectService.deleteProject(projectId);

			System.out.println("You have deleted project " + projectId);

			if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
				curProject = null;
			}
		}
	}

	/**
	 * Collects user input for all fields related to the Project entity. If user
	 * doesn't input a value (null) then input the value of the current working
	 * project for that given field (value doesn't change for selected project).
	 */
	private void updateProjectDetails() {
		if (Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project.");
			return;
		}

		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput(
				"Enter the estimated project hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput(
				"Enter the actual project hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");

		Project project = new Project();

		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);

		projectService.modifyProjectDetails(project);

		curProject = projectService.fetchProjectById(curProject.getProjectId());
	}

	/**
	 * Select a project from the list of projects based on project ID. Sets current
	 * selected project to null before project is selected by the projectService
	 * instance variable.
	 */
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");

		curProject = null;

		curProject = projectService.fetchProjectById(projectId);
	}

	/**
	 * Creates a list of all the projects and prints them to the console one at the
	 * time, starting with the project ID and then the project name.
	 */
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();

		System.out.println("\nProjects:");
//		for (Project project : projects) {
//			System.out.println("   " + project.getProjectId() + ": " + project.getProjectName());
		projects.forEach(
				project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
//		}
	}

	/**
	 * Creates a new project to be stored in the database. Uses setters for each
	 * column in Project table.
	 */
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimateHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimateHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}

	/**
	 * 
	 * @return
	 * 
	 *         Called by processUserSelection method for when user hits "enter" to
	 *         exit the menu. Returns true when complete.
	 */
	private boolean exitMenu() {
		System.out.println("\nExiting the menu.");
		return true;
	}

	/**
	 * 
	 * @return
	 * 
	 *         Calls printOperations (menu) and returns the user input. -1
	 *         otherwise.
	 */
	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("\nEnter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}

	/**
	 * 
	 * @param prompt
	 * @return Checks to see if user input is an integer (where appropriate). If
	 *         valid, takes user input. Otherwise throws a NumberFormatException as
	 *         a DbException.
	 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number. Try again.");
		}
	}

	/**
	 * 
	 * @param string
	 * @return Checks to see if user input is a decimal (where appropriate). If
	 *         valid, takes user input. Otherwise throws a NumberFormatException as
	 *         a DbException.
	 */
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	/**
	 * 
	 * @param prompt
	 * @return Checks to see if user input is a String (where appropriate). If
	 *         valid, takes user input. If user inputs nothing, returns null,
	 *         otherwise it takes any spaces off the beginning or end of string and
	 *         returns the user selection. If not a String, throws a
	 *         NumberFormatException as a DbException.
	 */
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}

	/**
	 * Populates the menu and area of user input.
	 */
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the enter key to quit:");

		operations.forEach(line -> System.out.println("   " + line));

		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

}
