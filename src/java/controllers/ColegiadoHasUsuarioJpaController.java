package controllers;

import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Colegiado;
import models.ColegiadoHasUsuario;
import models.Usuario;

/**
 *
 * @author a
 */
public class ColegiadoHasUsuarioJpaController implements Serializable {

    public ColegiadoHasUsuarioJpaController() {
    }

    public ColegiadoHasUsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        try {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("PSFPU");
            }

            return emf.createEntityManager();
        } catch (Exception e) {
            return null;
        }

    }

    /**
     *
     * @return
     */
    public List<Usuario> listaUsuariosSelecionados() {
        EntityManager em = getEntityManager();

        try {

            Query query = em.createNamedQuery("ColegiadoHasUsuario.listaUsuariosSelecionados");
            List<Usuario> lista = query.getResultList();
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @return
     */
    public List<ColegiadoHasUsuario> selectAll() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM ColegiadoHasUsuario u");
        List<ColegiadoHasUsuario> qlista = query.getResultList();
        return qlista;
    }

    /**
     *
     * @param u
     * @return
     */
    public List<Usuario> listaUsuariosFromColegiado(Usuario u) {
        List<Usuario> aux = new ArrayList();
        Query query = getEntityManager().createNamedQuery("ColegiadoHasUsuario.findByIdColegiadoHasUsuario")
                .setParameter("idColegiadoHasUsuario", u.getIdusuario());
        aux = query.getResultList();
        return aux;
    }

    /**
     *
     * @param colegiadoHasUsuario
     */
    public void create(ColegiadoHasUsuario colegiadoHasUsuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colegiado colegiadoIdcolegiado = colegiadoHasUsuario.getColegiadoIdcolegiado();
            if (colegiadoIdcolegiado != null) {
                colegiadoIdcolegiado = em.getReference(colegiadoIdcolegiado.getClass(), colegiadoIdcolegiado.getIdcolegiado());
                colegiadoHasUsuario.setColegiadoIdcolegiado(colegiadoIdcolegiado);
            }
            Usuario usuarioIdusuario = colegiadoHasUsuario.getUsuarioIdusuario();
            if (usuarioIdusuario != null) {
                usuarioIdusuario = em.getReference(usuarioIdusuario.getClass(), usuarioIdusuario.getIdusuario());
                colegiadoHasUsuario.setUsuarioIdusuario(usuarioIdusuario);
            }
            em.persist(colegiadoHasUsuario);
            if (colegiadoIdcolegiado != null) {
                colegiadoIdcolegiado.getColegiadoHasUsuarioList().add(colegiadoHasUsuario);
                colegiadoIdcolegiado = em.merge(colegiadoIdcolegiado);
            }
            if (usuarioIdusuario != null) {
                usuarioIdusuario.getColegiadoHasUsuarioList().add(colegiadoHasUsuario);
                usuarioIdusuario = em.merge(usuarioIdusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param colegiadoHasUsuario
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(ColegiadoHasUsuario colegiadoHasUsuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ColegiadoHasUsuario persistentColegiadoHasUsuario = em.find(ColegiadoHasUsuario.class, colegiadoHasUsuario.getIdColegiadoHasUsuario());
            Colegiado colegiadoIdcolegiadoOld = persistentColegiadoHasUsuario.getColegiadoIdcolegiado();
            Colegiado colegiadoIdcolegiadoNew = colegiadoHasUsuario.getColegiadoIdcolegiado();
            Usuario usuarioIdusuarioOld = persistentColegiadoHasUsuario.getUsuarioIdusuario();
            Usuario usuarioIdusuarioNew = colegiadoHasUsuario.getUsuarioIdusuario();
            if (colegiadoIdcolegiadoNew != null) {
                colegiadoIdcolegiadoNew = em.getReference(colegiadoIdcolegiadoNew.getClass(), colegiadoIdcolegiadoNew.getIdcolegiado());
                colegiadoHasUsuario.setColegiadoIdcolegiado(colegiadoIdcolegiadoNew);
            }
            if (usuarioIdusuarioNew != null) {
                usuarioIdusuarioNew = em.getReference(usuarioIdusuarioNew.getClass(), usuarioIdusuarioNew.getIdusuario());
                colegiadoHasUsuario.setUsuarioIdusuario(usuarioIdusuarioNew);
            }
            colegiadoHasUsuario = em.merge(colegiadoHasUsuario);
            if (colegiadoIdcolegiadoOld != null && !colegiadoIdcolegiadoOld.equals(colegiadoIdcolegiadoNew)) {
                colegiadoIdcolegiadoOld.getColegiadoHasUsuarioList().remove(colegiadoHasUsuario);
                colegiadoIdcolegiadoOld = em.merge(colegiadoIdcolegiadoOld);
            }
            if (colegiadoIdcolegiadoNew != null && !colegiadoIdcolegiadoNew.equals(colegiadoIdcolegiadoOld)) {
                colegiadoIdcolegiadoNew.getColegiadoHasUsuarioList().add(colegiadoHasUsuario);
                colegiadoIdcolegiadoNew = em.merge(colegiadoIdcolegiadoNew);
            }
            if (usuarioIdusuarioOld != null && !usuarioIdusuarioOld.equals(usuarioIdusuarioNew)) {
                usuarioIdusuarioOld.getColegiadoHasUsuarioList().remove(colegiadoHasUsuario);
                usuarioIdusuarioOld = em.merge(usuarioIdusuarioOld);
            }
            if (usuarioIdusuarioNew != null && !usuarioIdusuarioNew.equals(usuarioIdusuarioOld)) {
                usuarioIdusuarioNew.getColegiadoHasUsuarioList().add(colegiadoHasUsuario);
                usuarioIdusuarioNew = em.merge(usuarioIdusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = colegiadoHasUsuario.getIdColegiadoHasUsuario();
                if (findColegiadoHasUsuario(id) == null) {
                    throw new NonexistentEntityException("The colegiadoHasUsuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param id
     * @throws NonexistentEntityException
     */
    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ColegiadoHasUsuario colegiadoHasUsuario;
            try {
                colegiadoHasUsuario = em.getReference(ColegiadoHasUsuario.class, id);
                colegiadoHasUsuario.getIdColegiadoHasUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The colegiadoHasUsuario with id " + id + " no longer exists.", enfe);
            }
            Colegiado colegiadoIdcolegiado = colegiadoHasUsuario.getColegiadoIdcolegiado();
            if (colegiadoIdcolegiado != null) {
                colegiadoIdcolegiado.getColegiadoHasUsuarioList().remove(colegiadoHasUsuario);
                colegiadoIdcolegiado = em.merge(colegiadoIdcolegiado);
            }
            Usuario usuarioIdusuario = colegiadoHasUsuario.getUsuarioIdusuario();
            if (usuarioIdusuario != null) {
                usuarioIdusuario.getColegiadoHasUsuarioList().remove(colegiadoHasUsuario);
                usuarioIdusuario = em.merge(usuarioIdusuario);
            }
            em.remove(colegiadoHasUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @return
     */
    public List<ColegiadoHasUsuario> findColegiadoHasUsuarioEntities() {
        return findColegiadoHasUsuarioEntities(true, -1, -1);
    }

    /**
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<ColegiadoHasUsuario> findColegiadoHasUsuarioEntities(int maxResults, int firstResult) {
        return findColegiadoHasUsuarioEntities(false, maxResults, firstResult);
    }

    private List<ColegiadoHasUsuario> findColegiadoHasUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ColegiadoHasUsuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ColegiadoHasUsuario findColegiadoHasUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ColegiadoHasUsuario.class, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
    public int getColegiadoHasUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ColegiadoHasUsuario> rt = cq.from(ColegiadoHasUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
