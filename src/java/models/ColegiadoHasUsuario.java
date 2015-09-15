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
@Table(name = "colegiado_has_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ColegiadoHasUsuario.findAll", query = "SELECT c FROM ColegiadoHasUsuario c"),
    @NamedQuery(name = "ColegiadoHasUsuario.findByIdColegiadoHasUsuario", 
            query = "SELECT c FROM ColegiadoHasUsuario c WHERE c.idColegiadoHasUsuario = :idColegiadoHasUsuario")})
public class ColegiadoHasUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_colegiado_has_usuario")
    private Integer idColegiadoHasUsuario;
    @JoinColumn(name = "colegiado_idcolegiado", referencedColumnName = "idcolegiado")
    @ManyToOne(optional = false)
    private Colegiado colegiadoIdcolegiado;
    @JoinColumn(name = "usuario_idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuario usuarioIdusuario;

    public ColegiadoHasUsuario() {
    }

    public ColegiadoHasUsuario(Integer idColegiadoHasUsuario) {
        this.idColegiadoHasUsuario = idColegiadoHasUsuario;
    }

    public Integer getIdColegiadoHasUsuario() {
        return idColegiadoHasUsuario;
    }

    public void setIdColegiadoHasUsuario(Integer idColegiadoHasUsuario) {
        this.idColegiadoHasUsuario = idColegiadoHasUsuario;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idColegiadoHasUsuario != null ? idColegiadoHasUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ColegiadoHasUsuario)) {
            return false;
        }
        ColegiadoHasUsuario other = (ColegiadoHasUsuario) object;
        if ((this.idColegiadoHasUsuario == null && other.idColegiadoHasUsuario != null) || (this.idColegiadoHasUsuario != null && !this.idColegiadoHasUsuario.equals(other.idColegiadoHasUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.ColegiadoHasUsuario[ idColegiadoHasUsuario=" + idColegiadoHasUsuario + " ]";
    }
    
}
