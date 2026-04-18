/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.scheduler.db.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.DAOException;
import org.openmrs.scheduler.Schedule;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.db.SchedulerDAO;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 */
public class HibernateSchedulerDAO implements SchedulerDAO {
	
	/**
	 * Logger
	 */
	private static final Log log = LogFactory.getLog(HibernateSchedulerDAO.class);
	
	/**
	 * Hibernate session factory
	 */
	private SessionFactory sessionFactory;
	
	/**
	 * Default Public constructor
	 */
	public HibernateSchedulerDAO() {
	}
	
	/**
	 * Set session factory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Acquires a Hibernate Session, falling back to opening a new session if no
	 * CurrentSessionContext is configured (e.g., during application bootstrap
	 * before Spring's transaction management is fully initialized).
	 *
	 * @param manuallyOpened a single-element boolean array; on return,
	 *        manuallyOpened[0] is true when a new session was opened and the
	 *        caller must close it in a finally block
	 * @return a usable Hibernate Session
	 */
	private Session getSession(boolean[] manuallyOpened) {
		Session session;
		manuallyOpened[0] = false;
		try {
			session = sessionFactory.getCurrentSession();
		}
		catch (HibernateException e) {
			session = sessionFactory.openSession();
			manuallyOpened[0] = true;
		}
		return session;
	}
	
	/**
	 * Creates a new task.
	 * 
	 * @param task to be created
	 * @throws DAOException
	 */
	public void createTask(TaskDefinition task) throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			// add all data minus the password as a new user
			session.save(task);
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Get task by internal identifier
	 * 
	 * @param taskId internal task identifier
	 * @return task with given internal identifier
	 * @throws DAOException
	 */
	public TaskDefinition getTask(Integer taskId) throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			TaskDefinition task = (TaskDefinition) session.get(TaskDefinition.class, taskId);
			
			if (task == null) {
				log.warn("Task '" + taskId + "' not found");
				throw new ObjectRetrievalFailureException(TaskDefinition.class, taskId);
			}
			return task;
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Get task by public name.
	 * 
	 * @param name public task name
	 * @return task with given public name
	 * @throws DAOException
	 */
	public TaskDefinition getTaskByName(String name) throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			Criteria crit = session.createCriteria(TaskDefinition.class).add(
			    Restrictions.eq("name", name));
			
			TaskDefinition task = (TaskDefinition) crit.uniqueResult();
			
			if (task == null) {
				log.warn("Task '" + name + "' not found");
				throw new ObjectRetrievalFailureException(TaskDefinition.class, name);
			}
			return task;
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Update task
	 * 
	 * @param task to be updated
	 * @throws DAOException
	 */
	public void updateTask(TaskDefinition task) throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			session.merge(task);
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Find all tasks in the database
	 * 
	 * @return <code>List&lt;TaskDefinition&gt;</code> of all tasks
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<TaskDefinition> getTasks() throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			return session.createCriteria(TaskDefinition.class).list();
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Delete task from database.
	 * 
	 * @param taskId <code>Integer</code> identifier of task to be deleted
	 * @throws DAOException
	 */
	public void deleteTask(Integer taskId) throws DAOException {
		TaskDefinition taskConfig = getTask(taskId);
		deleteTask(taskConfig);
	}
	
	/**
	 * Delete task from database.
	 * 
	 * @param taskConfig <code>TaskDefinition</code> of task to be deleted
	 * @throws DAOException
	 */
	public void deleteTask(TaskDefinition taskConfig) throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			session.delete(taskConfig);
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Creates a new schedule.
	 * 
	 * @param schedule to be created
	 * @throws DAOException
	 */
	//public void createSchedule(Schedule schedule) throws DAOException;
	/**
	 * Get schedule by internal identifier
	 * 
	 * @param scheduleId internal schedule identifier
	 * @return schedule with given internal identifier
	 * @throws DAOException
	 */
	public Schedule getSchedule(Integer scheduleId) throws DAOException {
		boolean[] manuallyOpened = new boolean[1];
		Session session = getSession(manuallyOpened);
		try {
			Schedule schedule = (Schedule) session.get(Schedule.class, scheduleId);
			
			if (schedule == null) {
				log.error("Schedule '" + scheduleId + "' not found");
				throw new ObjectRetrievalFailureException(Schedule.class, scheduleId);
			}
			return schedule;
		}
		finally {
			if (manuallyOpened[0]) {
				session.close();
			}
		}
	}
	
	/**
	 * Update a schedule.
	 * 
	 * @param schedule to be updated
	 * @throws DAOException
	 */
	//public void updateSchedule(Schedule schedule) throws DAOException;
	/**
	 * Get all schedules.
	 * 
	 * @return set of all schedules in the database
	 * @throws DAOException
	 */
	//public Set<Schedule> getAllSchedules() throws DAOException;
	/**
	 * Delete schedule from database.
	 * 
	 * @param schedule schedule to be deleted
	 * @throws DAOException
	 */
	//public void deleteSchedule(Schedule schedule) throws DAOException;	
}
