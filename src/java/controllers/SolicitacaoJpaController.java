package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Colegiado;
import models.Usuario;
import models.Documento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Solicitacao;

/**
 *
 * @author a
 */
public class SolicitacaoJpaController implements Serializable {

    public SolicitacaoJpaController() {
    }

    public SolicitacaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /**
     *
     * @return
     */
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
    public List<Solicitacao> selectAll() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM Solicitacao s");
        List<Solicitacao> qlista = query.getResultList();
        return qlista;
    }

    /**
     *
     * @param u
     * @return
     */
    public List<Solicitacao> getListSolProfessor(Usuario u) {
        List<Solicitacao> lista = new ArrayList();
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Solicitacao.findByUsuario").setParameter("idusuario", u);

        lista = query.getResultList();
        return lista;
    }

    /**
     *
     * @param solicitacao
     */
    public void create(Solicitacao solicitacao) {
        if (solicitacao.getDocumentoList() == null) {
            solicitacao.setDocumentoList(new ArrayList<Documento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Colegiado colegiadoIdcolegiado = solicitacao.getColegiadoIdcolegiado();
            if (colegiadoIdcolegiado != null) {
                colegiadoIdcolegiado = em.getReference(colegiadoIdcolegiado.getClass(), colegiadoIdcolegiado.getIdcolegiado());
                solicitacao.setColegiadoIdcolegiado(colegiadoIdcolegiado);
            }
            Usuario usuarioIdusuario = solicitacao.getUsuarioIdusuario();
            if (usuarioIdusuario != null) {
                usuarioIdusuario = em.getReference(usuarioIdusuario.getClass(), usuarioIdusuario.getIdusuario());
                solicitacao.setUsuarioIdusuario(usuarioIdusuario);
            }
            List<Documento> attachedDocumentoList = new ArrayList<Documento>();
            for (Documento documentoListDocumentoToAttach : solicitacao.getDocumentoList()) {
                documentoListDocumentoToAttach = em.getReference(documentoListDocumentoToAttach.getClass(), documentoListDocumentoToAttach.getIddocumento());
                attachedDocumentoList.add(documentoListDocumentoToAttach);
            }
            solicitacao.setDocumentoList(attachedDocumentoList);
            em.persist(solicitacao);
            if (colegiadoIdcolegiado != null) {
                colegiadoIdcolegiado.getSolicitacaoList().add(solicitacao);
                colegiadoIdcolegiado = em.merge(colegiadoIdcolegiado);
            }
            if (usuarioIdusuario != null) {
                usuarioIdusuario.getSolicitacaoList().add(solicitacao);
                usuarioIdusuario = em.merge(usuarioIdusuario);
            }
            for (Documento documentoListDocumento : solicitacao.getDocumentoList()) {
                Solicitacao oldSolicitacaoIdsolicitacaoOfDocumentoListDocumento = documentoListDocumento.getSolicitacaoIdsolicitacao();
                documentoListDocumento.setSolicitacaoIdsolicitacao(solicitacao);
                documentoListDocumento = em.merge(documentoListDocumento);
                if (oldSolicitacaoIdsolicitacaoOfDocumentoListDocumento != null) {
                    oldSolicitacaoIdsolicitacaoOfDocumentoListDocumento.getDocumentoList().remove(documentoListDocumento);
                    oldSolicitacaoIdsolicitacaoOfDocumentoListDocumento = em.merge(oldSolicitacaoIdsolicitacaoOfDocumentoListDocumento);
                }
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
     * @param solicitacao
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(Solicitacao solicitacao) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitacao persistentSolicitacao = em.find(Solicitacao.class, solicitacao.getIdsolicitacao());
            Colegiado colegiadoIdcolegiadoOld = persistentSolicitacao.getColegiadoIdcolegiado();
            Colegiado colegiadoIdcolegiadoNew = solicitacao.getColegiadoIdcolegiado();
            Usuario usuarioIdusuarioOld = persistentSolicitacao.getUsuarioIdusuario();
            Usuario usuarioIdusuarioNew = solicitacao.getUsuarioIdusuario();
            List<Documento> documentoListOld = persistentSolicitacao.getDocumentoList();
            List<Documento> documentoListNew = solicitacao.getDocumentoList();
            List<String> illegalOrphanMessages = null;
            for (Documento documentoListOldDocumento : documentoListOld) {
                if (!documentoListNew.contains(documentoListOldDocumento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Documento " + documentoListOldDocumento + 
                            " since its solicitacaoIdsolicitacao field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (colegiadoIdcolegiadoNew != null) {
                colegiadoIdcolegiadoNew = em.getReference(colegiadoIdcolegiadoNew.getClass(), colegiadoIdcolegiadoNew.getIdcolegiado());
                solicitacao.setColegiadoIdcolegiado(colegiadoIdcolegiadoNew);
            }
            if (usuarioIdusuarioNew != null) {
                usuarioIdusuarioNew = em.getReference(usuarioIdusuarioNew.getClass(), usuarioIdusuarioNew.getIdusuario());
                solicitacao.setUsuarioIdusuario(usuarioIdusuarioNew);
            }
            List<Documento> attachedDocumentoListNew = new ArrayList<Documento>();
            for (Documento documentoListNewDocumentoToAttach : documentoListNew) {
                documentoListNewDocumentoToAttach = em.getReference(documentoListNewDocumentoToAttach.getClass(), documentoListNewDocumentoToAttach.getIddocumento());
                attachedDocumentoListNew.add(documentoListNewDocumentoToAttach);
            }
            documentoListNew = attachedDocumentoListNew;
            solicitacao.setDocumentoList(documentoListNew);
            solicitacao = em.merge(solicitacao);
            if (colegiadoIdcolegiadoOld != null && !colegiadoIdcolegiadoOld.equals(colegiadoIdcolegiadoNew)) {
                colegiadoIdcolegiadoOld.getSolicitacaoList().remove(solicitacao);
                colegiadoIdcolegiadoOld = em.merge(colegiadoIdcolegiadoOld);
            }
            if (colegiadoIdcolegiadoNew != null && !colegiadoIdcolegiadoNew.equals(colegiadoIdcolegiadoOld)) {
                colegiadoIdcolegiadoNew.getSolicitacaoList().add(solicitacao);
                colegiadoIdcolegiadoNew = em.merge(colegiadoIdcolegiadoNew);
            }
            if (usuarioIdusuarioOld != null && !usuarioIdusuarioOld.equals(usuarioIdusuarioNew)) {
                usuarioIdusuarioOld.getSolicitacaoList().remove(solicitacao);
                usuarioIdusuarioOld = em.merge(usuarioIdusuarioOld);
            }
            if (usuarioIdusuarioNew != null && !usuarioIdusuarioNew.equals(usuarioIdusuarioOld)) {
                usuarioIdusuarioNew.getSolicitacaoList().add(solicitacao);
                usuarioIdusuarioNew = em.merge(usuarioIdusuarioNew);
            }
            for (Documento documentoListNewDocumento : documentoListNew) {
                if (!documentoListOld.contains(documentoListNewDocumento)) {
                    Solicitacao oldSolicitacaoIdsolicitacaoOfDocumentoListNewDocumento = documentoListNewDocumento.getSolicitacaoIdsolicitacao();
                    documentoListNewDocumento.setSolicitacaoIdsolicitacao(solicitacao);
                    documentoListNewDocumento = em.merge(documentoListNewDocumento);
                    if (oldSolicitacaoIdsolicitacaoOfDocumentoListNewDocumento != null && !oldSolicitacaoIdsolicitacaoOfDocumentoListNewDocumento.equals(solicitacao)) {
                        oldSolicitacaoIdsolicitacaoOfDocumentoListNewDocumento.getDocumentoList().remove(documentoListNewDocumento);
                        oldSolicitacaoIdsolicitacaoOfDocumentoListNewDocumento = em.merge(oldSolicitacaoIdsolicitacaoOfDocumentoListNewDocumento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = solicitacao.getIdsolicitacao();
                if (findSolicitacao(id) == null) {
                    throw new NonexistentEntityException("The solicitacao with id " + id + " no longer exists.");
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
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     */
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Solicitacao solicitacao;
            try {
                solicitacao = em.getReference(Solicitacao.class, id);
                solicitacao.getIdsolicitacao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The solicitacao with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Documento> documentoListOrphanCheck = solicitacao.getDocumentoList();
            for (Documento documentoListOrphanCheckDocumento : documentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Solicitacao (" + solicitacao + ") cannot be destroyed since the Documento " + documentoListOrphanCheckDocumento + " in its documentoList field has a non-nullable solicitacaoIdsolicitacao field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Colegiado colegiadoIdcolegiado = solicitacao.getColegiadoIdcolegiado();
            if (colegiadoIdcolegiado != null) {
                colegiadoIdcolegiado.getSolicitacaoList().remove(solicitacao);
                colegiadoIdcolegiado = em.merge(colegiadoIdcolegiado);
            }
            Usuario usuarioIdusuario = solicitacao.getUsuarioIdusuario();
            if (usuarioIdusuario != null) {
                usuarioIdusuario.getSolicitacaoList().remove(solicitacao);
                usuarioIdusuario = em.merge(usuarioIdusuario);
            }
            em.remove(solicitacao);
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
    public List<Solicitacao> findSolicitacaoEntities() {
        return findSolicitacaoEntities(true, -1, -1);
    }

    /**
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<Solicitacao> findSolicitacaoEntities(int maxResults, int firstResult) {
        return findSolicitacaoEntities(false, maxResults, firstResult);
    }

    private List<Solicitacao> findSolicitacaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Solicitacao.class));
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
    public Solicitacao findSolicitacao(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Solicitacao.class, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
    public int getSolicitacaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Solicitacao> rt = cq.from(Solicitacao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
