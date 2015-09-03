/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controllers.UsuarioJpaController;
import email.MainTester;
import hash.GenerateSenha;
import hash.SHA;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import models.Usuario;

/**
 *
 * @author Guti Ivanagava
 */
@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {

    private String email, senha;
    private HttpSession session;
    private FacesContext fc;
//    private HttpServletRequest servletRequest;
    private Usuario USUARIO_LOGADO;
    UsuarioJpaController controllerUsuario;

    public String logar() throws NoSuchAlgorithmException {
        int controle = 0;
        //senha = SHA.generateHash(senha);

        controllerUsuario = new UsuarioJpaController();
        List<Usuario> listaUsuario = new ArrayList();
        listaUsuario = controllerUsuario.selectAll();
        fc = FacesContext.getCurrentInstance();
        session = (HttpSession) fc.getExternalContext().getSession(true);

        for (int i = 0; i < listaUsuario.size(); i++) {
            
            if (listaUsuario.get(i).getEmail().equals(email) && 
                    listaUsuario.get(i).getSenha().equals(senha) && 
                    listaUsuario.get(i).getEstado().equals("Ativo")) {
                
                USUARIO_LOGADO = listaUsuario.get(i);
                session.setAttribute("USUARIO_LOGADO_ID", USUARIO_LOGADO.getIdusuario());
                session.setAttribute("USUARIO_LOGADO_TIPO", USUARIO_LOGADO.getTipo());
                session.setAttribute("USUARIO_LOGADO_NOME", USUARIO_LOGADO.getNome());
                session.setAttribute("USUARIO_LOGADO_EMAIL", USUARIO_LOGADO.getEmail());
                session.setAttribute("authenticator", USUARIO_LOGADO.getMatricula());
                session.setAttribute("USUARIO_LOGADO_ESTADO", USUARIO_LOGADO.getEstado());
                USUARIO_LOGADO.setSenha(SHA.generateHash(USUARIO_LOGADO.getSenha()));

                controle = 0;
                if (USUARIO_LOGADO.getTipo().equalsIgnoreCase("Administrador")
                        && USUARIO_LOGADO.getEstado().equalsIgnoreCase("Ativo")) {
                    session.setAttribute("USUARIO_LOGADO_TIPO", "Administrador");
                    System.out.println("Entrou no sistema administrador.");

                    return "/layout/adm.xhtml?faces-redirect=true";
                } else if (USUARIO_LOGADO.getTipo().equalsIgnoreCase("Funcionario")
                        && USUARIO_LOGADO.getEstado().equalsIgnoreCase("Ativo")) {
                    session.setAttribute("USUARIO_LOGADO_TIPO", "Funcionario");
                    System.out.println("Entrou no sistema como funcionario.");

                    return "/layout/funcionario.xhtml?faces-redirect=true";
                } else if (USUARIO_LOGADO.getTipo().equalsIgnoreCase("Professor")
                        && USUARIO_LOGADO.getEstado().equalsIgnoreCase("Ativo")) {
                    session.setAttribute("USUARIO_LOGADO_TIPO", "Professor");
                    System.out.println("Entrou no sistema como professor.");

                    return "/layout/professor.xhtml?faces-redirect=true";
                } else {
                    System.out.println("Tipo ou estado invalido");
                    FacesMessage message = null;
                    message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                controle = 1;
            }

        }

        if (controle > 0) {
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Estado, email ou senha inválido.", ""));
        }

        return "";
    }

    public String logOut() {
        fc = FacesContext.getCurrentInstance();
        session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("authenticator", null);
        USUARIO_LOGADO = null;
        session.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("Saiu do sistema.");
        return "/faces/index?faces-redirect=true";
    }

    public String esqueceuSenha() {
        controllerUsuario = new UsuarioJpaController();
        List<Usuario> listaUsuario = new ArrayList();
        listaUsuario = controllerUsuario.selectAll();
        Usuario aux = new Usuario();

        for (int i = 0; i < listaUsuario.size(); i++) {
            if (listaUsuario.get(i).getEmail().equalsIgnoreCase(email)) {
                aux = listaUsuario.get(i);
                try {
                    String senhaNova = GenerateSenha.generateNewSenha(); //gera nova senha de 16 digitos numericos
                    String senhaNovaHash = SHA.generateHash(senhaNova); //gera o hash da senha aleatoria construida
                    aux.setSenha(senhaNovaHash);
                    controllerUsuario.edit(aux);
                    MainTester m = new MainTester();
                    m.emailSender(aux.getEmail(), senhaNova); //envia para o email digitado a senha gerada
                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage("E-mail enviado"));
                    return "index.xhtml?faces-redirect=true";

                } catch (Exception e) {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Email incorreto.", ""));
                }
            } else {

            }

        }
        return "";
    }

    public String interceptor() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        String alterID = (String) session.getAttribute("USUARIO_LOGADO_ID");
        String alterNome = (String) session.getAttribute("USUARIO_LOGADO_NOME");
        String authenticator = (String) session.getAttribute("authenticator");

        if (alterID == null || alterNome == null || authenticator == null) {
            return "errorPage?faces-redirect=true";

        }

        return "";
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

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public FacesContext getFc() {
        return fc;
    }

    public void setFc(FacesContext fc) {
        this.fc = fc;
    }

//    public HttpServletRequest getServletRequest() {
//        return servletRequest;
//    }
//
//    public void setServletRequest(HttpServletRequest servletRequest) {
//        this.servletRequest = servletRequest;
//    }
    public Usuario getUSUARIO_LOGADO() {
        return USUARIO_LOGADO;
    }

    public void setUSUARIO_LOGADO(Usuario USUARIO_LOGADO) {
        this.USUARIO_LOGADO = USUARIO_LOGADO;
    }

    public UsuarioJpaController getControllerUsuario() {
        return controllerUsuario;
    }

    public void setControllerUsuario(UsuarioJpaController controllerUsuario) {
        this.controllerUsuario = controllerUsuario;
    }

}
