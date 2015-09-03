/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controllers.ColegiadoHasUsuarioJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Query;
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
    
    public List<Usuario> listaProfessoresFromColegiado() {
        List<Usuario> listaU = new ArrayList();
        
        ColegiadoHasUsuarioJpaController cu = new ColegiadoHasUsuarioJpaController();
        listaU = cu.listaUsuariosFromColegiado(u);
        
        return listaU;
    }

}
