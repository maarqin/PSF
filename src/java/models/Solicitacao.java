/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author a
 */
@Entity
@Table(name = "solicitacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Solicitacao.findAll", query = "SELECT s FROM Solicitacao s"),
    @NamedQuery(name = "Solicitacao.findByIdsolicitacao", query = "SELECT s FROM Solicitacao s WHERE s.idsolicitacao = :idsolicitacao"),
    @NamedQuery(name = "Solicitacao.findByNumprotocolo", query = "SELECT s FROM Solicitacao s WHERE s.numprotocolo = :numprotocolo"),
    @NamedQuery(name = "Solicitacao.findByDataaplicacao", query = "SELECT s FROM Solicitacao s WHERE s.dataaplicacao = :dataaplicacao"),
    @NamedQuery(name = "Solicitacao.findByPeriodoaplicacao", query = "SELECT s FROM Solicitacao s WHERE s.periodoaplicacao = :periodoaplicacao"),
    @NamedQuery(name = "Solicitacao.findByDatacriacao", query = "SELECT s FROM Solicitacao s WHERE s.datacriacao = :datacriacao"),
    @NamedQuery(name = "Solicitacao.findByEstado", query = "SELECT s FROM Solicitacao s WHERE s.estado = :estado"),
    @NamedQuery(name = "Solicitacao.findByMotivorecusa", query = "SELECT s FROM Solicitacao s WHERE s.motivorecusa = :motivorecusa"),
    @NamedQuery(name = "Solicitacao.findByAtendente", query = "SELECT s FROM Solicitacao s WHERE s.atendente = :atendente"),
    @NamedQuery(name = "Solicitacao.findByUsuario", query = "SELECT s FROM Solicitacao s WHERE s.usuarioIdusuario = :idusuario"),
    @NamedQuery(name = "Solicitacao.findByFrenteVerso", query = "SELECT s FROM Solicitacao s WHERE s.frenteVerso = :frenteVerso")})
public class Solicitacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idsolicitacao")
    private Integer idsolicitacao;
    @Basic(optional = false)
    @Column(name = "numprotocolo")
    private int numprotocolo;
    @Basic(optional = false)
    @Column(name = "dataaplicacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataaplicacao;
    @Basic(optional = false)
    @Column(name = "periodoaplicacao")
    private String periodoaplicacao;
    @Basic(optional = false)
    @Column(name = "datacriacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datacriacao;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Column(name = "motivorecusa")
    private String motivorecusa;
    @Column(name = "atendente")
    private Integer atendente;
    @Basic(optional = false)
    @Column(name = "frenteVerso")
    private short frenteVerso;
    @JoinColumn(name = "colegiado_idcolegiado", referencedColumnName = "idcolegiado")
    @ManyToOne(optional = false)
    private Colegiado colegiadoIdcolegiado;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioIdusuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitacaoIdsolicitacao")
    private List<Documento> documentoList;

    public Solicitacao() {
    }

    public Solicitacao(Integer idsolicitacao) {
        this.idsolicitacao = idsolicitacao;
    }

    public Solicitacao(Integer idsolicitacao, int numprotocolo, Date dataaplicacao, String periodoaplicacao, Date datacriacao, String estado, short frenteVerso) {
        this.idsolicitacao = idsolicitacao;
        this.numprotocolo = numprotocolo;
        this.dataaplicacao = dataaplicacao;
        this.periodoaplicacao = periodoaplicacao;
        this.datacriacao = datacriacao;
        this.estado = estado;
        this.frenteVerso = frenteVerso;
    }

    public Integer getIdsolicitacao() {
        return idsolicitacao;
    }

    public void setIdsolicitacao(Integer idsolicitacao) {
        this.idsolicitacao = idsolicitacao;
    }

    public int getNumprotocolo() {
        return numprotocolo;
    }

    public void setNumprotocolo(int numprotocolo) {
        this.numprotocolo = numprotocolo;
    }

    public Date getDataaplicacao() {
        return dataaplicacao;
    }

    public void setDataaplicacao(Date dataaplicacao) {
        this.dataaplicacao = dataaplicacao;
    }

    public String getPeriodoaplicacao() {
        return periodoaplicacao;
    }

    public void setPeriodoaplicacao(String periodoaplicacao) {
        this.periodoaplicacao = periodoaplicacao;
    }

    public Date getDatacriacao() {
        return datacriacao;
    }

    public void setDatacriacao(Date datacriacao) {
        this.datacriacao = datacriacao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivorecusa() {
        return motivorecusa;
    }

    public void setMotivorecusa(String motivorecusa) {
        this.motivorecusa = motivorecusa;
    }

    public Integer getAtendente() {
        return atendente;
    }

    public void setAtendente(Integer atendente) {
        this.atendente = atendente;
    }

    public String getFrenteVerso() {
        if (frenteVerso == 1) {
            return "Sim";
        } else {
            return "Não";
        }
    }

    public void setFrenteVerso(short frenteVerso) {
        this.frenteVerso = frenteVerso;
    }

    public Colegiado getColegiadoIdcolegiado() {
        return colegiadoIdcolegiado;
    }

    public void setColegiadoIdcolegiado(Colegiado colegiadoIdcolegiado) {
        this.colegiadoIdcolegiado = colegiadoIdcolegiado;
    }

    public Usuario getUsuarioIdusuario() {
        return usuarioIdusuario;
    }

    public void setUsuarioIdusuario(Usuario usuarioIdusuario) {
        this.usuarioIdusuario = usuarioIdusuario;
    }

    @XmlTransient
    public List<Documento> getDocumentoList() {
        return documentoList;
    }

    public void setDocumentoList(List<Documento> documentoList) {
        this.documentoList = documentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idsolicitacao != null ? idsolicitacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Solicitacao)) {
            return false;
        }
        Solicitacao other = (Solicitacao) object;
        if ((this.idsolicitacao == null && other.idsolicitacao != null) || (this.idsolicitacao != null && !this.idsolicitacao.equals(other.idsolicitacao))) {
            return false;
        }
        return true;
    }

    public void clean() {
        this.idsolicitacao = null;
        this.numprotocolo = 0;
        this.dataaplicacao = null;
        this.periodoaplicacao = null;
        this.datacriacao = null;
        this.estado = null;
        this.documentoList = null;
    }

    @Override
    public String toString() {
        return "models.Solicitacao[ idsolicitacao=" + idsolicitacao + " ]";
    }

}
