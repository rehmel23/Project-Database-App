package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import projects.entity.Category;
import projects.entity.Material;
import projects.entity.Project;
import projects.entity.Step;
import projects.exception.DbException;
import provided.util.DaoBase;

/**
 * 
 * @author clayr Data access object layer for Projects. Uses JDBC to perform
 *         CRUD operations on the tables.
 */
public class ProjectDao extends DaoBase {

	/**
	 * Creates the tables in schema
	 */
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";

	/**
	 * 
	 * @param project
	 * @return SQL to insert project into the Project table
	 */
	public Project insertProject(Project project) {
		// @formatter:off
		String sql = ""
				+ "INSERT INTO " + PROJECT_TABLE + " "
				+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?)";
		// @formatter:on
		// If connection, then start transaction
		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			// Sets values for the 5 values in SQL statement.
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				// Update values in SQL statement
				stmt.executeUpdate();
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);

				commitTransaction(conn);

				project.setProjectId(projectId);
				return project;
				// If exception, rollback transaction completely.
			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 
	 * @return Write SQL statement that reads all columns from Project table and
	 *         order by the project name. Obtains connection to database, creates a
	 *         list of Project and adds project from database to the LinkedList.
	 *         Returns list of projects.
	 */
	public List<Project> fetchAllProjects() {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY project_name";

		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				try (ResultSet rs = stmt.executeQuery()) {
					List<Project> projects = new LinkedList<>();

					while (rs.next()) {
						projects.add(extract(rs, Project.class));
					}

					return projects;
				}

			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 
	 * @param projectId
	 * @return Write SQL statement that reads all columns from Project table and
	 *         returns all rows that contain the given project ID. Obtains
	 *         connection to database and runs query on database. Rolls back the
	 *         transaction if there are any exceptions thrown.
	 */
	public Optional<Project> fetchProjectById(Integer projectId) {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE project_id = ?";

		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);

			try {
				Project project = null;

				try (PreparedStatement stmt = conn.prepareStatement(sql)) {
					setParameter(stmt, 1, projectId, Integer.class);

					try (ResultSet rs = stmt.executeQuery()) {
						if (rs.next()) {
							project = extract(rs, Project.class);
						}
					}
				}

				if (Objects.nonNull(project)) {
					project.getMaterials().addAll(fetchProjectMaterials(conn, projectId));

					project.getSteps().addAll(fetchProjectSteps(conn, projectId));

					project.getCategories().addAll(fetchProjectCategories(conn, projectId));
				}
				commitTransaction(conn);

				return Optional.ofNullable(project);
			} catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}

	/**
	 * 
	 * @param conn
	 * @param projectId
	 * @return
	 * @throws SQLException Fetch all columns from category table and adds them to
	 *                      the category list. Returns the category list.
	 */
	private List<Category> fetchProjectCategories(Connection conn, Integer projectId) throws SQLException {
		// @formatter:off
		String sql = ""
				+ "SELECT c.* FROM " + CATEGORY_TABLE + " c "
				+ "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) "
				+ "WHERE project_id = ?";
		// @formatter:on

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);

			try (ResultSet rs = stmt.executeQuery()) {
				List<Category> categories = new LinkedList<>();

				while (rs.next()) {
					categories.add(extract(rs, Category.class));
				}

				return categories;
			}
		}
	}

	/**
	 * 
	 * @param conn
	 * @param projectId
	 * @return
	 * @throws SQLException Fetch all columns from step table and adds them to the
	 *                      step list. Returns the step list.
	 */
	private List<Step> fetchProjectSteps(Connection conn, Integer projectId) throws SQLException {
		// @formatter:off
		String sql = ""
				+ "SELECT s.* FROM " + STEP_TABLE + " s "
				+ "WHERE project_id = ?";
		// @formatter:on

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);

			try (ResultSet rs = stmt.executeQuery()) {
				List<Step> steps = new LinkedList<>();

				while (rs.next()) {
					steps.add(extract(rs, Step.class));
				}

				return steps;
			}
		}
	}

	/**
	 * 
	 * @param conn
	 * @param projectId
	 * @return
	 * @throws SQLException Fetch all columns from materials table and adds them to
	 *                      the materials list. Returns the material list.
	 */
	private List<Material> fetchProjectMaterials(Connection conn, Integer projectId) throws SQLException {
		// @formatter:off
		String sql = ""
				+ "SELECT m.* FROM " + MATERIAL_TABLE + " m "
				+ "WHERE project_id = ?";
		// @formatter:on

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);

			try (ResultSet rs = stmt.executeQuery()) {
				List<Material> materials = new LinkedList<>();

				while (rs.next()) {
					materials.add(extract(rs, Material.class));
				}

				return materials;
			}
		}
	}

}
