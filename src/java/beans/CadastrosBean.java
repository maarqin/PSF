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
@SessionScoped
public class CadastrosBean implements Serializable {

    Usuario usuario, usuarioAux;

    UsuarioJpaController controllerUsuario;
    ColegiadoHasUsuarioJpaController controllerColegiadoHasUsuario;
    private String tipo = "";
    private String nome = "";
    private String email = "";
    private String senha = "";
    private String senhaAux = "";
    private String estado = "";
    private int matricula = 0;

    private String tipo2 = "";
    private String nome2 = "";
    private String email2 = "";
    private String senha2 = "";
    private String senhaAux2 = "";
    private String estado2 = "";
    private int matricula2 = 0;

    private int idColegiadoSelecionado;
    private String nomeColegiadoSelecionado;
    private int idUsuario;

    private ArrayList<Usuario> listUsuario;

    public CadastrosBean() {
        usuario = new Usuario();
        usuarioAux = new Usuario();
        controllerUsuario = new UsuarioJpaController();
        controllerColegiadoHasUsuario = new ColegiadoHasUsuarioJpaController();

        idUsuario = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USUARIO_LOGADO_ID");
        usuarioAux = controllerUsuario.findUsuario(idUsuario);

        if (!usuarioAux.getTipo().equals("Administrador")) {
            System.out.println("é func ou prof");
            tipo2 = usuarioAux.getTipo();
            nome2 = usuarioAux.getNome();
            estado2 = usuarioAux.getEstado();
            matricula2 = usuarioAux.getMatricula();
            email2 = usuarioAux.getEmail();
        }
    }

//<editor-fold defaultstate="collapsed" desc="Metodos Usuarios">
    @PostConstruct
    public void PostConstruct() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idColegiado") != null) {
            idColegiadoSelecionado = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idColegiado");
            nomeColegiadoSelecionado = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nomeColegiado");
        }
    }

    public void listaUsuariosSelecionados() {
        controllerColegiadoHasUsuario.listaUsuariosSelecionados();//this.idColegiadoSelecionado
    }

    public String cadastrarUsuario(ActionEvent submit) throws NoSuchAlgorithmException {

        List<Usuario> lista = new ArrayList();
        estado = "Ativo";
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
        
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getMatricula() == matricula) {
                matricula = 0;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esta matrícula já está cadastrada!", ""));
                break;
            }
        }
        
        if (!email.equals("")) {
            if ((matricula != 0) && !nome.equals("") && !tipo.equals("") && !senha.equals("") && !estado.equals("")) {
                try {
                    String senhaHash;
//                    senhaHash = hash.SHA.generateHash(senha);
//                    senhaHash = senha;
                    temp.setSenha(senhaAux);

                    controllerUsuario.create(temp);

                    tipo = "";
                    nome = "";
                    email = "";
                    senha = "";
                    estado = "";
                    matricula = 0;

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário cadastrado!", ""));
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

    public void apagarUsuario(Integer idUsuario) {
        controllerUsuario = new UsuarioJpaController();
        Usuario temp = new Usuario();
        List<Solicitacao> list = new ArrayList();
        SolicitacaoJpaController cs = new SolicitacaoJpaController();
        DocumentoJpaController dc = new DocumentoJpaController();
        temp = controllerUsuario.findUsuario(idUsuario);
        list = cs.getListSolProfessor(temp);

        try {

            if (temp.getTipo().equals("Professor")) {
                for (int i = 0; i < list.size(); i++) {
                    List<Documento> ld = list.get(i).getDocumentoList();
                    for (int j = 0; j < ld.size(); j++) {
                        dc.destroy(ld.get(j).getIddocumento());

                    }

                }

                for (int i = 0; i < list.size(); i++) {
                    cs.destroy(list.get(i).getIdsolicitacao());

                }
            }
            controllerUsuario.destroy(idUsuario);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deletado com sucesso!", ""));

//            controllerUsuario.destroy(idUsuario);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um ou mais campos não estão preenchidos corretamente!", ""));
        }
    }

    public String encontrarUsuario(ActionEvent submit) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String data = params.get("idUsuario");
        controllerUsuario = new UsuarioJpaController();

        try {
            usuario = controllerUsuario.findUsuario(Integer.parseInt(data));
            usuario.setSenha(SHA.generateHash(usuario.getSenha()));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao encontrar o usuário.", ""));
        }

        return "";
    }

    public String alterarUsuario(ActionEvent submit) {
        controllerUsuario = new UsuarioJpaController();

        if (!usuarioAux.getTipo().equals("Administrador")) {
            System.out.println("entrou");
            if (senha.equals(senha2)) {
                usuarioAux.setEmail(email2);
                usuarioAux.setEstado(estado2);
                usuarioAux.setMatricula(matricula2);
                usuarioAux.setNome(nome2);
                usuarioAux.setTipo(tipo2);

                try {
                    senhaAux = SHA.generateHash(senha);
                } catch (NoSuchAlgorithmException ex) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Não foi possível converder a senha em hash.", ""));
                }

                usuarioAux.setSenha(senhaAux);

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Senhas não compatíveis!", ""));
            }

            try {
                controllerUsuario.edit(usuarioAux);

                usuarioAux.setEmail("");
                usuarioAux.setEmail(email2);
                usuarioAux.setEstado("");
                usuarioAux.setMatricula(0);
                usuarioAux.setNome("");
                usuarioAux.setTipo("");
                usuarioAux.setSenha("");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alterado com sucesso!", ""));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um ou mais campos não estão preenchidos corretamente!", ""));
            }
        } else {//se for adm
            controllerUsuario = new UsuarioJpaController();
            usuario.setNome(usuario.getNome());
            try {
                controllerUsuario.edit(usuario);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Alterado com sucesso!", ""));

            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um ou mais campos não estão preenchidos corretamente!", ""));
            }
        }
        return "";
    }

    public List<Usuario> listaUsuarios() {
        List<Usuario> lista = new ArrayList();
        controllerUsuario = new UsuarioJpaController();
        lista = controllerUsuario.selectAll();

        return lista;
    }
//</editor-fold>

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

    public ColegiadoHasUsuarioJpaController getControllerColegiadoHasUsuario() {
        return controllerColegiadoHasUsuario;
    }

    public void setControllerColegiadoHasUsuario(ColegiadoHasUsuarioJpaController controllerColegiadoHasUsuario) {
        this.controllerColegiadoHasUsuario = controllerColegiadoHasUsuario;
    }

    public int getIdColegiadoSelecionado() {
        return idColegiadoSelecionado;
    }

    public void setIdColegiadoSelecionado(int idColegiadoSelecionado) {
        this.idColegiadoSelecionado = idColegiadoSelecionado;
    }

    public String getNomeColegiadoSelecionado() {
        return nomeColegiadoSelecionado;
    }

    public void setNomeColegiadoSelecionado(String nomeColegiadoSelecionado) {
        this.nomeColegiadoSelecionado = nomeColegiadoSelecionado;
    }

    public String getSenhaAux() {
        return senhaAux;
    }

    public void setSenhaAux(String senhaAux) {
        this.senhaAux = senhaAux;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getUsuarioAux() {
        return usuarioAux;
    }

    public void setUsuarioAux(Usuario usuarioAux) {
        this.usuarioAux = usuarioAux;
    }

    public String getTipo2() {
        return tipo2;
    }

    public void setTipo2(String tipo2) {
        this.tipo2 = tipo2;
    }

    public String getNome2() {
        return nome2;
    }

    public void setNome2(String nome2) {
        this.nome2 = nome2;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getSenha2() {
        return senha2;
    }

    public void setSenha2(String senha2) {
        this.senha2 = senha2;
    }

    public String getSenhaAux2() {
        return senhaAux2;
    }

    public void setSenhaAux2(String senhaAux2) {
        this.senhaAux2 = senhaAux2;
    }

    public String getEstado2() {
        return estado2;
    }

    public void setEstado2(String estado2) {
        this.estado2 = estado2;
    }

    public int getMatricula2() {
        return matricula2;
    }

    public void setMatricula2(int matricula2) {
        this.matricula2 = matricula2;
    }

    public ArrayList<Usuario> getListUsuario() {
        return listUsuario;
    }

    public void setListUsuario(ArrayList<Usuario> listUsuario) {
        this.listUsuario = listUsuario;
    }
//</editor-fold>

}
