/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author a
 */
@Entity
@Table(name = "documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d"),
    @NamedQuery(name = "Documento.findByIddocumento", query = "SELECT d FROM Documento d WHERE d.iddocumento = :iddocumento"),
    @NamedQuery(name = "Documento.findByNomedocumento", query = "SELECT d FROM Documento d WHERE d.nomedocumento = :nomedocumento"),
    @NamedQuery(name = "Documento.findByQuantidadecopias", query = "SELECT d FROM Documento d WHERE d.quantidadecopias = :quantidadecopias"),
    @NamedQuery(name = "Documento.findByEnderecodocumento", query = "SELECT d FROM Documento d WHERE d.enderecodocumento = :enderecodocumento"),
    @NamedQuery(name = "Documento.findByQuantidadepaginas", query = "SELECT d FROM Documento d WHERE d.quantidadepaginas = :quantidadepaginas"),
   
    @NamedQuery(name = "Documento.listaPaginasInfo",
            query = "SELECT u.nome, u.email, sum(d.quantidadepaginas) FROM Documento d "
            + "join d.solicitacaoIdsolicitacao.usuarioIdusuario u "
            + "join d.solicitacaoIdsolicitacao.colegiadoIdcolegiado c "
            + "join d.solicitacaoIdsolicitacao s "
            + "where c.idcolegiado = :idColegiado"
    ),
    @NamedQuery(name = "Documento.paginasColegiado",
            query = "SELECT u.nome, u.email, sum(d.quantidadepaginas * d.quantidadecopias) FROM Documento d "
            + "join d.solicitacaoIdsolicitacao.usuarioIdusuario u "
            + "join d.solicitacaoIdsolicitacao.colegiadoIdcolegiado c "
            + "join d.solicitacaoIdsolicitacao s "
            + "where c.idcolegiado = :idColegiado and "
            + "u.idusuario = :idUsuario"),

    @NamedQuery(name = "Documento.findBySolicitacao",
            query = "SELECT d FROM Documento d WHERE d.solicitacaoIdsolicitacao = :idsolicitacao"),})


public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddocumento")
    private Integer iddocumento;
    @Basic(optional = false)
    @Column(name = "nomedocumento")
    private String nomedocumento;
    @Basic(optional = false)
    @Column(name = "quantidadecopias")
    private int quantidadecopias;
    @Basic(optional = false)
    @Column(name = "enderecodocumento")
    private String enderecodocumento;
    @Basic(optional = false)
    @Column(name = "quantidadepaginas")
    private int quantidadepaginas;
    @JoinColumn(name = "solicitacao_idsolicitacao", referencedColumnName = "idsolicitacao")
    @ManyToOne(optional = false)
    private Solicitacao solicitacaoIdsolicitacao;

    public Documento() {
    }

    public Documento(Integer iddocumento) {
        this.iddocumento = iddocumento;
    }

    public Documento(Integer iddocumento, String nomedocumento, int quantidadecopias, String enderecodocumento, int quantidadepaginas) {
        this.iddocumento = iddocumento;
        this.nomedocumento = nomedocumento;
        this.quantidadecopias = quantidadecopias;
        this.enderecodocumento = enderecodocumento;
        this.quantidadepaginas = quantidadepaginas;
    }

    public Integer getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(Integer iddocumento) {
        this.iddocumento = iddocumento;
    }

    public String getNomedocumento() {
        return nomedocumento;
    }

    public void setNomedocumento(String nomedocumento) {
        this.nomedocumento = nomedocumento;
    }

    public int getQuantidadecopias() {
        return quantidadecopias;
    }

    public void setQuantidadecopias(int quantidadecopias) {
        this.quantidadecopias = quantidadecopias;
    }

    public String getEnderecodocumento() {
        return enderecodocumento;
    }

    public void setEnderecodocumento(String enderecodocumento) {
        this.enderecodocumento = enderecodocumento;
    }

    public int getQuantidadepaginas() {
        return quantidadepaginas;
    }

    public void setQuantidadepaginas(int quantidadepaginas) {
        this.quantidadepaginas = quantidadepaginas;
    }

    public Solicitacao getSolicitacaoIdsolicitacao() {
        return solicitacaoIdsolicitacao;
    }

    public void setSolicitacaoIdsolicitacao(Solicitacao solicitacaoIdsolicitacao) {
        this.solicitacaoIdsolicitacao = solicitacaoIdsolicitacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddocumento != null ? iddocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.iddocumento == null && other.iddocumento != null) || (this.iddocumento != null && !this.iddocumento.equals(other.iddocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Documento[ iddocumento=" + iddocumento + " ]";
    }

}
