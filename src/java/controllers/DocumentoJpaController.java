/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import models.Documento;
import models.Solicitacao;
import util.PaginasInfo;

/**
 *
 * @author a
 */
public class DocumentoJpaController implements Serializable {

    public DocumentoJpaController() {
    }

    public DocumentoJpaController(EntityManagerFactory emf) {
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

    public List<Documento> getListDocSolicitacao(Solicitacao s) {
        List<Documento> lista = new ArrayList();
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Documento.findBySolicitacao").setParameter("idsolicitacao", s);

        lista = query.getResultList();
        return lista;
    }

    public List<Documento> selectAll() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM Documento u");
        List<Documento> qlista = query.getResultList();
        return qlista;
    }

    public PaginasInfo listaPaginasInfo(int idColegiado, int idUsuario) {
        Query query = getEntityManager().createNamedQuery("Documento.paginasColegiado")
                .setParameter("idColegiado", idColegiado);
        query.setParameter("idUsuario", idUsuario);

//        List<PaginasInfo> lista = new ArrayList<PaginasInfo>();
        List<Object[]> lo = query.getResultList();
        PaginasInfo pi = null;
//        System.out.println("SIZE "+lo.size());
            for (Object[] ob : lo) {
            pi = new PaginasInfo((Long) ob[2], (String) ob[0], (String) ob[1]);
//            System.out.println("qtd " + ob[2]);
//            System.out.println("nome " + ob[0]);
//            System.out.println("email " + ob[1]);
//            System.out.println("Copias " + ob[3]);
//            if(tipo do usuario for = a profess){
//            lista.add(pi);
//            }
        }
        return pi;
    }

    public int paginasPorColegiado(int idColegiado) {
        int paginasPorColegiado = 0;

        Query query = getEntityManager().createNamedQuery("Documento.listaPaginasInfo")
                .setParameter("idColegiado", idColegiado);
        List<PaginasInfo> lista = new ArrayList<PaginasInfo>();
        List<Object[]> lo = query.getResultList();
        PaginasInfo pi;
//        System.out.println("SIZE "+lo.size());
        for (Object[] ob : lo) {
            pi = new PaginasInfo((Integer) ob[2], (String) ob[0], (String) ob[1]);
//            System.out.println("qtd " + ob[2]);
//            System.out.println("nome " + ob[0]);
//            System.out.println("email " + ob[1]);
//            if(tipo do usuario for = a profess){
            lista.add(pi);
            if (pi.getQtPaginas() != null) {
                System.out.println("qtd:" + pi.getQtPaginas());
                paginasPorColegiado += pi.getQtPaginas();
            }
//            }
        }
        return paginasPorColegiado;
    }

    public void create(Documento documento) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitacao solicitacaoIdsolicitacao = documento.getSolicitacaoIdsolicitacao();
            if (solicitacaoIdsolicitacao != null) {
                solicitacaoIdsolicitacao = em.getReference(solicitacaoIdsolicitacao.getClass(), solicitacaoIdsolicitacao.getIdsolicitacao());
                documento.setSolicitacaoIdsolicitacao(solicitacaoIdsolicitacao);
            }
            em.persist(documento);
            if (solicitacaoIdsolicitacao != null) {
                solicitacaoIdsolicitacao.getDocumentoList().add(documento);
                solicitacaoIdsolicitacao = em.merge(solicitacaoIdsolicitacao);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documento documento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento persistentDocumento = em.find(Documento.class, documento.getIddocumento());
            Solicitacao solicitacaoIdsolicitacaoOld = persistentDocumento.getSolicitacaoIdsolicitacao();
            Solicitacao solicitacaoIdsolicitacaoNew = documento.getSolicitacaoIdsolicitacao();
            if (solicitacaoIdsolicitacaoNew != null) {
                solicitacaoIdsolicitacaoNew = em.getReference(solicitacaoIdsolicitacaoNew.getClass(), solicitacaoIdsolicitacaoNew.getIdsolicitacao());
                documento.setSolicitacaoIdsolicitacao(solicitacaoIdsolicitacaoNew);
            }
            documento = em.merge(documento);
            if (solicitacaoIdsolicitacaoOld != null && !solicitacaoIdsolicitacaoOld.equals(solicitacaoIdsolicitacaoNew)) {
                solicitacaoIdsolicitacaoOld.getDocumentoList().remove(documento);
                solicitacaoIdsolicitacaoOld = em.merge(solicitacaoIdsolicitacaoOld);
            }
            if (solicitacaoIdsolicitacaoNew != null && !solicitacaoIdsolicitacaoNew.equals(solicitacaoIdsolicitacaoOld)) {
                solicitacaoIdsolicitacaoNew.getDocumentoList().add(documento);
                solicitacaoIdsolicitacaoNew = em.merge(solicitacaoIdsolicitacaoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documento.getIddocumento();
                if (findDocumento(id) == null) {
                    throw new NonexistentEntityException("The documento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documento documento;
            try {
                documento = em.getReference(Documento.class, id);
                documento.getIddocumento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documento with id " + id + " no longer exists.", enfe);
            }
            Solicitacao solicitacaoIdsolicitacao = documento.getSolicitacaoIdsolicitacao();
            if (solicitacaoIdsolicitacao != null) {
                solicitacaoIdsolicitacao.getDocumentoList().remove(documento);
                solicitacaoIdsolicitacao = em.merge(solicitacaoIdsolicitacao);
            }
            em.remove(documento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documento> findDocumentoEntities() {
        return findDocumentoEntities(true, -1, -1);
    }

    public List<Documento> findDocumentoEntities(int maxResults, int firstResult) {
        return findDocumentoEntities(false, maxResults, firstResult);
    }

    private List<Documento> findDocumentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documento.class));
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

    public Documento findDocumento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documento> rt = cq.from(Documento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
