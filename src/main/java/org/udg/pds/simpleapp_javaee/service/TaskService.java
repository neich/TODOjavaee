package org.udg.pds.simpleapp_javaee.service;

import org.udg.pds.simpleapp_javaee.model.Tag;
import org.udg.pds.simpleapp_javaee.model.Task;
import org.udg.pds.simpleapp_javaee.model.User;
import org.udg.pds.simpleapp_javaee.rest.RESTService;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;

@Stateless
@LocalBean
public class TaskService {

  @PersistenceContext
  protected EntityManager em;

  @EJB
  protected TagService tagService;

  @EJB
  protected UserService userService;

  public Collection<Task> getTasks(Long id) {
    Collection<Task> tl = null;
      User u = em.find(User.class, id);
      if (u == null)
        throw new EJBException("User does not exists");
      tl = u.getTasks();
      return tl;
  }

  public Task getTask(Long userId, Long id) {
      Task t = em.find(Task.class, id);
      if (t == null) throw new EJBException("Task does not exists");
      if (t.getUser().getId() != userId)
        throw new EJBException("User does not own this task");
      return t;
  }

  public Task addTask(String text, Long userId,
                      Date created, Date limit) {
    try {
      User user = em.find(User.class, userId);

      Task task = new Task(created, limit, false, text);

      task.setUser(user);

      user.addTask(task);

      em.persist(task);
      return task;
    } catch (Exception ex) {
      // Very important: if you want that an exception reaches the EJB caller, you have to throw an EJBException
      // We catch the normal exception and then transform it in a EJBException
      throw new EJBException(ex);
    }
  }

  public RESTService.ID remove(Long taskId) {
    Task t = em.find(Task.class, taskId);
    if (t == null) throw new EJBException("Task does not exists");
    em.remove(t);
    return new RESTService.ID(taskId);
  }

  public void addTagsToTask(Long userId, Long taskId, Collection<Long> tags) {
    Task t = em.find(Task.class, taskId);

    if (t == null) throw new EJBException("Task don't exists");

    if (t.getUser().getId() != userId)
      throw new EJBException("This user is not the owner of the task");

    try {
      for (Long tagId : tags) {
        Tag tag = tagService.getTag(tagId);
        t.addTag(tag);
      }
    } catch (Exception ex) {
      // Very important: if you want that an exception reaches the EJB caller, you have to throw an EJBException
      // We catch the normal exception and then transform it in a EJBException
      throw new EJBException(ex);
    }
  }

  public Collection<Tag> getTaskTags(Long userId, Long id) {
    Task t = this.getTask(userId, id);
    User u = t.getUser();

    if (u.getId() != userId)
      throw new EJBException("Logged user does not own the task");

    return t.getTags();
  }

}
