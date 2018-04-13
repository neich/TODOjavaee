package org.udg.pds.simpleapp_javaee.service;

import org.udg.pds.simpleapp_javaee.model.Task;
import org.udg.pds.simpleapp_javaee.model.User;
import org.udg.pds.simpleapp_javaee.rest.RESTService;

import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class UserService {

  @PersistenceContext
  private EntityManager em;

  public User matchPassword(String username, String password) {
    Query q = em.createQuery("select u from users u where u.username=:username");
    q.setParameter("username", username);

    User u;
    try {
      u = (User) q.getSingleResult();
    } catch (Exception e) {
      throw new EJBException("No user with this username exists");
    }

    if (u.getPassword().equals(password))
      return u;
    else
      throw new EJBException("Password does not match");
  }

  public User register(String username, String email, String password) {

    Query q = em.createQuery("select u from users u where u.email=:email");
    q.setParameter("email", email);
    if (q.getResultList().size() > 0)
      throw new EJBException("Email already exist");


    q = em.createQuery("select u from users u where u.username=:username");
    q.setParameter("username", username);
    if (q.getResultList().size() > 0)
      throw new EJBException("Username already exists");

    User nu = new User(username, email, password);
    em.persist(nu);
    return nu;
  }

  public User getUser(long id) {
    return em.find(User.class, id);
  }

  public User getUserProfile(long id) {
    User u = this.getUser(id);
    for (Task t : u.getTasks())
        t.getTags();
    return u;
  }

  public RESTService.ID remove(Long userId) {
    User u = getUser(userId);
    em.remove(u);
    return new RESTService.ID(userId);
  }

  public User getUserComplete(Long loggedId) {
    User u = getUser(loggedId);
    u.getTasks().size();
    return u;
  }
}
