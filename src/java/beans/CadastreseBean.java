/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controllers.ColegiadoHasUsuarioJpaController;
import controllers.DocumentoJpaController;
import controllers.SolicitacaoJpaController;
import controllers.UsuarioJpaController;
import hash.SHA;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import models.Documento;
import models.Solicitacao;
import models.Usuario;

/**
 *
 * @author a
 */
@ManagedBean
@ViewScoped
public class CadastreseBean implements Serializable {

    Usuario usuario;

    UsuarioJpaController controllerUsuario;

    private String tipo = "";
    private String nome = "";
    private String email = "";
    private String senha = "";
    private String senhaAux = "";
    private String estado = "";
    private int matricula = 0;
    private int idUsuario;

    private ArrayList<Usuario> listUsuario;

    public CadastreseBean() {
        usuario = new Usuario();
        controllerUsuario = new UsuarioJpaController();
    }

    public String cadastrarUsuario(ActionEvent submit) throws NoSuchAlgorithmException {

        List<Usuario> lista = new ArrayList();
        estado = "Inativo";
        tipo = "Professor";
        senhaAux = SHA.generateHash(senha);
        Usuario temp = new Usuario(Integer.MIN_VALUE, tipo, nome, email, senhaAux, matricula, estado);

        controllerUsuario = new UsuarioJpaController();
        lista = controllerUsuario.selectAll();

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getEmail().equalsIgnoreCase(email)) {
                email = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Este email já está cadastrado!", ""));
                break;
            }
        }
        if (!email.equals("")) {
            if ((matricula != 0) && !nome.equals("") && !tipo.equals("") && !senha.equals("") && !estado.equals("")) {
                try {
                    temp.setSenha(senhaAux);

                    controllerUsuario.create(temp);

                    tipo = "";
                    nome = "";
                    email = "";
                    senha = "";
                    estado = "";
                    matricula = 0;

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aguardando aprovação.", ""));
                } catch (Exception e) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao efetuar o cadastro!", ""));
                    e.printStackTrace();
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um ou mais campos não estão preenchidos corretamente!", ""));
            }
        }

        return "";
    }

//<editor-fold defaultstate="collapsed" desc="get e set">
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioJpaController getControllerUsuario() {
        return controllerUsuario;
    }

    public void setControllerUsuario(UsuarioJpaController controllerUsuario) {
        this.controllerUsuario = controllerUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaAux() {
        return senhaAux;
    }

    public void setSenhaAux(String senhaAux) {
        this.senhaAux = senhaAux;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ArrayList<Usuario> getListUsuario() {
        return listUsuario;
    }

    public void setListUsuario(ArrayList<Usuario> listUsuario) {
        this.listUsuario = listUsuario;
    }

//</editor-fold>
}
