/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author a
 */
@Entity
@Table(name = "colegiado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Colegiado.findAll", query = "SELECT c FROM Colegiado c"),
    @NamedQuery(name = "Colegiado.findByIdcolegiado", query = "SELECT c FROM Colegiado c WHERE c.idcolegiado = :idcolegiado"),
    @NamedQuery(name = "Colegiado.findByNome", query = "SELECT c FROM Colegiado c WHERE c.nome = :nome"),
    @NamedQuery(name = "Colegiado.findByQuantidadecursos", query = "SELECT c FROM Colegiado c WHERE c.quantidadecursos = :quantidadecursos"),
    @NamedQuery(name = "Colegiado.findByEstado", query = "SELECT c FROM Colegiado c WHERE c.estado = :estado")})
public class Colegiado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcolegiado")
    private Integer idcolegiado;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "quantidadecursos")
    private int quantidadecursos;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "colegiadoIdcolegiado")
    private List<ColegiadoHasUsuario> colegiadoHasUsuarioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "colegiadoIdcolegiado")
    private List<Solicitacao> solicitacaoList;

    public Colegiado() {
    }

    public Colegiado(Integer idcolegiado) {
        this.idcolegiado = idcolegiado;
    }

    public Colegiado(Integer idcolegiado, String nome, int quantidadecursos, String estado) {
        this.idcolegiado = idcolegiado;
        this.nome = nome;
        this.quantidadecursos = quantidadecursos;
        this.estado = estado;
    }

    public Integer getIdcolegiado() {
        return idcolegiado;
    }

    public void setIdcolegiado(Integer idcolegiado) {
        this.idcolegiado = idcolegiado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadecursos() {
        return quantidadecursos;
    }

    public void setQuantidadecursos(int quantidadecursos) {
        this.quantidadecursos = quantidadecursos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<ColegiadoHasUsuario> getColegiadoHasUsuarioList() {
        return colegiadoHasUsuarioList;
    }

    public void setColegiadoHasUsuarioList(List<ColegiadoHasUsuario> colegiadoHasUsuarioList) {
        this.colegiadoHasUsuarioList = colegiadoHasUsuarioList;
    }

    @XmlTransient
    public List<Solicitacao> getSolicitacaoList() {
        return solicitacaoList;
    }

    public void setSolicitacaoList(List<Solicitacao> solicitacaoList) {
        this.solicitacaoList = solicitacaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcolegiado != null ? idcolegiado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Colegiado)) {
            return false;
        }
        Colegiado other = (Colegiado) object;
        if ((this.idcolegiado == null && other.idcolegiado != null) || (this.idcolegiado != null && !this.idcolegiado.equals(other.idcolegiado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Colegiado[ idcolegiado=" + idcolegiado + " ]";
    }
    
}
