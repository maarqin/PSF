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
import models.Solicitacao;
import models.Usuario;

/**
 *
 * @author a
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController() {
    }

    public UsuarioJpaController(EntityManagerFactory emf) {
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

    public List<Usuario> selectAll() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM Usuario u");
        List<Usuario> qlista = query.getResultList();
        return qlista;
    }

    public List<Solicitacao> selectAllSolicitacoes(Usuario idUsuario) {
        EntityManagerFactory fac = Persistence.createEntityManagerFactory("PSFPU");
        EntityManager em = fac.createEntityManager();
        Query query = em.createQuery("SELECT s FROM Solicitacao s WHERE s.usuarioIdusuario = :idUsuario").setParameter("idUsuario", idUsuario);
        List<Solicitacao> qlista = query.getResultList();

        return qlista;
    }

    public List getQuantidadeDePaginas(int idColegiado) {
        List lista = new ArrayList();

        EntityManager em = getEntityManager();
        Query query = em.createQuery("select d.quantidadepaginas, c.nome , c.idcolegiado from usuario u\n"
                + "join colegiado_has_usuario c on u.idusuario = c.usuario_idusuario\n"
                + "join colegiado c on c.idcolegiado = c.colegiado_idcolegiado\n"
                + "join solicitacao s on u.idusuario = s.usuario_idusuario\n"
                + "join documento d on s.idsolicitacao = d.solicitacao_idsolicitacao\n"
                + "where c.idcolegiado = :idColegiado").setParameter("idColegiado", idColegiado);
        lista = query.getResultList();

        return lista;
    }

    public List<Usuario> findUsuarioList(Integer id) {
        EntityManager em = getEntityManager();
        try {

            Query q = em.createNamedQuery("Usuario.getUsuarioBanco");
            q.setParameter("valor", id);
            return q.getResultList();

        } finally {
            em.close();
        }
    }

    public void create(Usuario usuario) {
        if (usuario.getColegiadoHasUsuarioList() == null) {
            usuario.setColegiadoHasUsuarioList(new ArrayList<ColegiadoHasUsuario>());
        }
        if (usuario.getSolicitacaoList() == null) {
            usuario.setSolicitacaoList(new ArrayList<Solicitacao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ColegiadoHasUsuario> attachedColegiadoHasUsuarioList = new ArrayList<ColegiadoHasUsuario>();
            for (ColegiadoHasUsuario colegiadoHasUsuarioListColegiadoHasUsuarioToAttach : usuario.getColegiadoHasUsuarioList()) {
                colegiadoHasUsuarioListColegiadoHasUsuarioToAttach = em.getReference(colegiadoHasUsuarioListColegiadoHasUsuarioToAttach.getClass(), colegiadoHasUsuarioListColegiadoHasUsuarioToAttach.getIdColegiadoHasUsuario());
                attachedColegiadoHasUsuarioList.add(colegiadoHasUsuarioListColegiadoHasUsuarioToAttach);
            }
            usuario.setColegiadoHasUsuarioList(attachedColegiadoHasUsuarioList);
            List<Solicitacao> attachedSolicitacaoList = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacaoListSolicitacaoToAttach : usuario.getSolicitacaoList()) {
                solicitacaoListSolicitacaoToAttach = em.getReference(solicitacaoListSolicitacaoToAttach.getClass(), solicitacaoListSolicitacaoToAttach.getIdsolicitacao());
                attachedSolicitacaoList.add(solicitacaoListSolicitacaoToAttach);
            }
            usuario.setSolicitacaoList(attachedSolicitacaoList);
            em.persist(usuario);
            for (ColegiadoHasUsuario colegiadoHasUsuarioListColegiadoHasUsuario : usuario.getColegiadoHasUsuarioList()) {
                Usuario oldUsuarioIdusuarioOfColegiadoHasUsuarioListColegiadoHasUsuario = colegiadoHasUsuarioListColegiadoHasUsuario.getUsuarioIdusuario();
                colegiadoHasUsuarioListColegiadoHasUsuario.setUsuarioIdusuario(usuario);
                colegiadoHasUsuarioListColegiadoHasUsuario = em.merge(colegiadoHasUsuarioListColegiadoHasUsuario);
                if (oldUsuarioIdusuarioOfColegiadoHasUsuarioListColegiadoHasUsuario != null) {
                    oldUsuarioIdusuarioOfColegiadoHasUsuarioListColegiadoHasUsuario.getColegiadoHasUsuarioList().remove(colegiadoHasUsuarioListColegiadoHasUsuario);
                    oldUsuarioIdusuarioOfColegiadoHasUsuarioListColegiadoHasUsuario = em.merge(oldUsuarioIdusuarioOfColegiadoHasUsuarioListColegiadoHasUsuario);
                }
            }
            for (Solicitacao solicitacaoListSolicitacao : usuario.getSolicitacaoList()) {
                Usuario oldUsuarioIdusuarioOfSolicitacaoListSolicitacao = solicitacaoListSolicitacao.getUsuarioIdusuario();
                solicitacaoListSolicitacao.setUsuarioIdusuario(usuario);
                solicitacaoListSolicitacao = em.merge(solicitacaoListSolicitacao);
                if (oldUsuarioIdusuarioOfSolicitacaoListSolicitacao != null) {
                    oldUsuarioIdusuarioOfSolicitacaoListSolicitacao.getSolicitacaoList().remove(solicitacaoListSolicitacao);
                    oldUsuarioIdusuarioOfSolicitacaoListSolicitacao = em.merge(oldUsuarioIdusuarioOfSolicitacaoListSolicitacao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdusuario());
            List<ColegiadoHasUsuario> colegiadoHasUsuarioListOld = persistentUsuario.getColegiadoHasUsuarioList();
            List<ColegiadoHasUsuario> colegiadoHasUsuarioListNew = usuario.getColegiadoHasUsuarioList();
            List<Solicitacao> solicitacaoListOld = persistentUsuario.getSolicitacaoList();
            List<Solicitacao> solicitacaoListNew = usuario.getSolicitacaoList();
            List<String> illegalOrphanMessages = null;
            for (ColegiadoHasUsuario colegiadoHasUsuarioListOldColegiadoHasUsuario : colegiadoHasUsuarioListOld) {
                if (!colegiadoHasUsuarioListNew.contains(colegiadoHasUsuarioListOldColegiadoHasUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ColegiadoHasUsuario " + colegiadoHasUsuarioListOldColegiadoHasUsuario + " since its usuarioIdusuario field is not nullable.");
                }
            }
            for (Solicitacao solicitacaoListOldSolicitacao : solicitacaoListOld) {
                if (!solicitacaoListNew.contains(solicitacaoListOldSolicitacao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Solicitacao " + solicitacaoListOldSolicitacao + " since its usuarioIdusuario field is not nullable.");
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
            usuario.setColegiadoHasUsuarioList(colegiadoHasUsuarioListNew);
            List<Solicitacao> attachedSolicitacaoListNew = new ArrayList<Solicitacao>();
            for (Solicitacao solicitacaoListNewSolicitacaoToAttach : solicitacaoListNew) {
                solicitacaoListNewSolicitacaoToAttach = em.getReference(solicitacaoListNewSolicitacaoToAttach.getClass(), solicitacaoListNewSolicitacaoToAttach.getIdsolicitacao());
                attachedSolicitacaoListNew.add(solicitacaoListNewSolicitacaoToAttach);
            }
            solicitacaoListNew = attachedSolicitacaoListNew;
            usuario.setSolicitacaoList(solicitacaoListNew);
            usuario = em.merge(usuario);
            for (ColegiadoHasUsuario colegiadoHasUsuarioListNewColegiadoHasUsuario : colegiadoHasUsuarioListNew) {
                if (!colegiadoHasUsuarioListOld.contains(colegiadoHasUsuarioListNewColegiadoHasUsuario)) {
                    Usuario oldUsuarioIdusuarioOfColegiadoHasUsuarioListNewColegiadoHasUsuario = colegiadoHasUsuarioListNewColegiadoHasUsuario.getUsuarioIdusuario();
                    colegiadoHasUsuarioListNewColegiadoHasUsuario.setUsuarioIdusuario(usuario);
                    colegiadoHasUsuarioListNewColegiadoHasUsuario = em.merge(colegiadoHasUsuarioListNewColegiadoHasUsuario);
                    if (oldUsuarioIdusuarioOfColegiadoHasUsuarioListNewColegiadoHasUsuario != null && !oldUsuarioIdusuarioOfColegiadoHasUsuarioListNewColegiadoHasUsuario.equals(usuario)) {
                        oldUsuarioIdusuarioOfColegiadoHasUsuarioListNewColegiadoHasUsuario.getColegiadoHasUsuarioList().remove(colegiadoHasUsuarioListNewColegiadoHasUsuario);
                        oldUsuarioIdusuarioOfColegiadoHasUsuarioListNewColegiadoHasUsuario = em.merge(oldUsuarioIdusuarioOfColegiadoHasUsuarioListNewColegiadoHasUsuario);
                    }
                }
            }
            for (Solicitacao solicitacaoListNewSolicitacao : solicitacaoListNew) {
                if (!solicitacaoListOld.contains(solicitacaoListNewSolicitacao)) {
                    Usuario oldUsuarioIdusuarioOfSolicitacaoListNewSolicitacao = solicitacaoListNewSolicitacao.getUsuarioIdusuario();
                    solicitacaoListNewSolicitacao.setUsuarioIdusuario(usuario);
                    solicitacaoListNewSolicitacao = em.merge(solicitacaoListNewSolicitacao);
                    if (oldUsuarioIdusuarioOfSolicitacaoListNewSolicitacao != null && !oldUsuarioIdusuarioOfSolicitacaoListNewSolicitacao.equals(usuario)) {
                        oldUsuarioIdusuarioOfSolicitacaoListNewSolicitacao.getSolicitacaoList().remove(solicitacaoListNewSolicitacao);
                        oldUsuarioIdusuarioOfSolicitacaoListNewSolicitacao = em.merge(oldUsuarioIdusuarioOfSolicitacaoListNewSolicitacao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdusuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ColegiadoHasUsuario> colegiadoHasUsuarioListOrphanCheck = usuario.getColegiadoHasUsuarioList();
            for (ColegiadoHasUsuario colegiadoHasUsuarioListOrphanCheckColegiadoHasUsuario : colegiadoHasUsuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ColegiadoHasUsuario " + colegiadoHasUsuarioListOrphanCheckColegiadoHasUsuario + " in its colegiadoHasUsuarioList field has a non-nullable usuarioIdusuario field.");
            }
            List<Solicitacao> solicitacaoListOrphanCheck = usuario.getSolicitacaoList();
            for (Solicitacao solicitacaoListOrphanCheckSolicitacao : solicitacaoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Solicitacao " + solicitacaoListOrphanCheckSolicitacao + " in its solicitacaoList field has a non-nullable usuarioIdusuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
