/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.ColegiadoHasUsuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Colegiado;
import models.Solicitacao;

/**
 *
 * @author a
 */
public class ColegiadoJpaController implements Serializable {

    public ColegiadoJpaController() {
    }

    public ColegiadoJpaController(EntityManagerFactory emf) {
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

    public List<Colegiado> selectAll() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM Colegiado u");
        List<Colegiado> qlista = query.getResultList();
        return qlista;
    }

    public void create(Colegiado colegiado) {
        if (colegiado.getColegiadoHasUsuarioList() == null) {
            colegiado.setColegiadoHasUsuarioList(new ArrayList<ColegiadoHasUsuario>());
        }
        if (colegiado.getSolicitacaoList() == null) {
            colegiado.setSolicitacaoList(new ArrayList<Solicitacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ColegiadoHasUsuario> attachedColegiadoHasUsuarioList = new ArrayList<ColegiadoHasUsuario>();
            for (ColegiadoHasUsuario colegiadoHasUsuarioListColegiadoHasUsuarioToAttach : colegiado.getColegiadoHasUsuarioList()) {
                colegiadoHasUsuarioListColegiadoHasUsuarioToAttach = em.getReference(colegiadoHasUsuarioListColegiadoHasUsuarioToAttach.getClass(), colegiadoHasUsuarioListColegiadoHasUsuarioToAttach.getIdColegiadoHasUsuario());
                attachedColegiadoHasUsuarioList.add(colegiadoHasUsuarioListColegiadoHasUsuarioToAttach);
            }
            colegiado.setColegiadoHasUsuarioList(attachedColegiadoHasUsuarioList);
            List<Solicitacao> attachedSolicitacaoList = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacaoListSolicitacaoToAttach : colegiado.getSolicitacaoList()) {
                solicitacaoListSolicitacaoToAttach = em.getReference(solicitacaoListSolicitacaoToAttach.getClass(), solicitacaoListSolicitacaoToAttach.getIdsolicitacao());
                attachedSolicitacaoList.add(solicitacaoListSolicitacaoToAttach);
            }
            colegiado.setSolicitacaoList(attachedSolicitacaoList);
            em.persist(colegiado);
            for (ColegiadoHasUsuario colegiadoHasUsuarioListColegiadoHasUsuario : colegiado.getColegiadoHasUsuarioList()) {
                Colegiado oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListColegiadoHasUsuario = colegiadoHasUsuarioListColegiadoHasUsuario.getColegiadoIdcolegiado();
                colegiadoHasUsuarioListColegiadoHasUsuario.setColegiadoIdcolegiado(colegiado);
                colegiadoHasUsuarioListColegiadoHasUsuario = em.merge(colegiadoHasUsuarioListColegiadoHasUsuario);
                if (oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListColegiadoHasUsuario != null) {
                    oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListColegiadoHasUsuario.getColegiadoHasUsuarioList().remove(colegiadoHasUsuarioListColegiadoHasUsuario);
                    oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListColegiadoHasUsuario = em.merge(oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListColegiadoHasUsuario);
                }
            }
            for (Solicitacao solicitacaoListSolicitacao : colegiado.getSolicitacaoList()) {
                Colegiado oldColegiadoIdcolegiadoOfSolicitacaoListSolicitacao = solicitacaoListSolicitacao.getColegiadoIdcolegiado();
                solicitacaoListSolicitacao.setColegiadoIdcolegiado(colegiado);
                solicitacaoListSolicitacao = em.merge(solicitacaoListSolicitacao);
                if (oldColegiadoIdcolegiadoOfSolicitacaoListSolicitacao != null) {
                    oldColegiadoIdcolegiadoOfSolicitacaoListSolicitacao.getSolicitacaoList().remove(solicitacaoListSolicitacao);
                    oldColegiadoIdcolegiadoOfSolicitacaoListSolicitacao = em.merge(oldColegiadoIdcolegiadoOfSolicitacaoListSolicitacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Colegiado colegiado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colegiado persistentColegiado = em.find(Colegiado.class, colegiado.getIdcolegiado());
            List<ColegiadoHasUsuario> colegiadoHasUsuarioListOld = persistentColegiado.getColegiadoHasUsuarioList();
            List<ColegiadoHasUsuario> colegiadoHasUsuarioListNew = colegiado.getColegiadoHasUsuarioList();
            List<Solicitacao> solicitacaoListOld = persistentColegiado.getSolicitacaoList();
            List<Solicitacao> solicitacaoListNew = colegiado.getSolicitacaoList();
            List<String> illegalOrphanMessages = null;
            for (ColegiadoHasUsuario colegiadoHasUsuarioListOldColegiadoHasUsuario : colegiadoHasUsuarioListOld) {
                if (!colegiadoHasUsuarioListNew.contains(colegiadoHasUsuarioListOldColegiadoHasUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ColegiadoHasUsuario " + colegiadoHasUsuarioListOldColegiadoHasUsuario + " since its colegiadoIdcolegiado field is not nullable.");
                }
            }
            for (Solicitacao solicitacaoListOldSolicitacao : solicitacaoListOld) {
                if (!solicitacaoListNew.contains(solicitacaoListOldSolicitacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Solicitacao " + solicitacaoListOldSolicitacao + " since its colegiadoIdcolegiado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ColegiadoHasUsuario> attachedColegiadoHasUsuarioListNew = new ArrayList<ColegiadoHasUsuario>();
            for (ColegiadoHasUsuario colegiadoHasUsuarioListNewColegiadoHasUsuarioToAttach : colegiadoHasUsuarioListNew) {
                colegiadoHasUsuarioListNewColegiadoHasUsuarioToAttach = em.getReference(colegiadoHasUsuarioListNewColegiadoHasUsuarioToAttach.getClass(), colegiadoHasUsuarioListNewColegiadoHasUsuarioToAttach.getIdColegiadoHasUsuario());
                attachedColegiadoHasUsuarioListNew.add(colegiadoHasUsuarioListNewColegiadoHasUsuarioToAttach);
            }
            colegiadoHasUsuarioListNew = attachedColegiadoHasUsuarioListNew;
            colegiado.setColegiadoHasUsuarioList(colegiadoHasUsuarioListNew);
            List<Solicitacao> attachedSolicitacaoListNew = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacaoListNewSolicitacaoToAttach : solicitacaoListNew) {
                solicitacaoListNewSolicitacaoToAttach = em.getReference(solicitacaoListNewSolicitacaoToAttach.getClass(), solicitacaoListNewSolicitacaoToAttach.getIdsolicitacao());
                attachedSolicitacaoListNew.add(solicitacaoListNewSolicitacaoToAttach);
            }
            solicitacaoListNew = attachedSolicitacaoListNew;
            colegiado.setSolicitacaoList(solicitacaoListNew);
            colegiado = em.merge(colegiado);
            for (ColegiadoHasUsuario colegiadoHasUsuarioListNewColegiadoHasUsuario : colegiadoHasUsuarioListNew) {
                if (!colegiadoHasUsuarioListOld.contains(colegiadoHasUsuarioListNewColegiadoHasUsuario)) {
                    Colegiado oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListNewColegiadoHasUsuario = colegiadoHasUsuarioListNewColegiadoHasUsuario.getColegiadoIdcolegiado();
                    colegiadoHasUsuarioListNewColegiadoHasUsuario.setColegiadoIdcolegiado(colegiado);
                    colegiadoHasUsuarioListNewColegiadoHasUsuario = em.merge(colegiadoHasUsuarioListNewColegiadoHasUsuario);
                    if (oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListNewColegiadoHasUsuario != null && !oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListNewColegiadoHasUsuario.equals(colegiado)) {
                        oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListNewColegiadoHasUsuario.getColegiadoHasUsuarioList().remove(colegiadoHasUsuarioListNewColegiadoHasUsuario);
                        oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListNewColegiadoHasUsuario = em.merge(oldColegiadoIdcolegiadoOfColegiadoHasUsuarioListNewColegiadoHasUsuario);
                    }
                }
            }
            for (Solicitacao solicitacaoListNewSolicitacao : solicitacaoListNew) {
                if (!solicitacaoListOld.contains(solicitacaoListNewSolicitacao)) {
                    Colegiado oldColegiadoIdcolegiadoOfSolicitacaoListNewSolicitacao = solicitacaoListNewSolicitacao.getColegiadoIdcolegiado();
                    solicitacaoListNewSolicitacao.setColegiadoIdcolegiado(colegiado);
                    solicitacaoListNewSolicitacao = em.merge(solicitacaoListNewSolicitacao);
                    if (oldColegiadoIdcolegiadoOfSolicitacaoListNewSolicitacao != null && !oldColegiadoIdcolegiadoOfSolicitacaoListNewSolicitacao.equals(colegiado)) {
                        oldColegiadoIdcolegiadoOfSolicitacaoListNewSolicitacao.getSolicitacaoList().remove(solicitacaoListNewSolicitacao);
                        oldColegiadoIdcolegiadoOfSolicitacaoListNewSolicitacao = em.merge(oldColegiadoIdcolegiadoOfSolicitacaoListNewSolicitacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = colegiado.getIdcolegiado();
                if (findColegiado(id) == null) {
                    throw new NonexistentEntityException("The colegiado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colegiado colegiado;
            try {
                colegiado = em.getReference(Colegiado.class, id);
                colegiado.getIdcolegiado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The colegiado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ColegiadoHasUsuario> colegiadoHasUsuarioListOrphanCheck = colegiado.getColegiadoHasUsuarioList();
            for (ColegiadoHasUsuario colegiadoHasUsuarioListOrphanCheckColegiadoHasUsuario : colegiadoHasUsuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Colegiado (" + colegiado + ") cannot be destroyed since the ColegiadoHasUsuario " + colegiadoHasUsuarioListOrphanCheckColegiadoHasUsuario + " in its colegiadoHasUsuarioList field has a non-nullable colegiadoIdcolegiado field.");
            }
            List<Solicitacao> solicitacaoListOrphanCheck = colegiado.getSolicitacaoList();
            for (Solicitacao solicitacaoListOrphanCheckSolicitacao : solicitacaoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Colegiado (" + colegiado + ") cannot be destroyed since the Solicitacao " + solicitacaoListOrphanCheckSolicitacao + " in its solicitacaoList field has a non-nullable colegiadoIdcolegiado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(colegiado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Colegiado> findColegiadoEntities() {
        return findColegiadoEntities(true, -1, -1);
    }

    public List<Colegiado> findColegiadoEntities(int maxResults, int firstResult) {
        return findColegiadoEntities(false, maxResults, firstResult);
    }

    private List<Colegiado> findColegiadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Colegiado.class));
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

    public Colegiado findColegiado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Colegiado.class, id);
        } finally {
            em.close();
        }
    }

    public int getColegiadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Colegiado> rt = cq.from(Colegiado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
