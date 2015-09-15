package beans;

import controllers.ColegiadoHasUsuarioJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import models.Usuario;

/**
 *
 * @author a
 */
@ManagedBean
@SessionScoped
public class ColegiadoHasUsuarioBean {

    private Usuario u;
    
    public ColegiadoHasUsuarioBean(){
        u = new Usuario();
    }
    
    /**
     *
     * @return
     */
    public List<Usuario> listaProfessoresFromColegiado() {
        List<Usuario> listaU = new ArrayList();
        
        ColegiadoHasUsuarioJpaController cu = new ColegiadoHasUsuarioJpaController();
        listaU = cu.listaUsuariosFromColegiado(u);
        
        return listaU;
    }

}
