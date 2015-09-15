package beans;

import controllers.DocumentoJpaController;
import controllers.UsuarioJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import models.Documento;
import models.Usuario;
import util.PaginasInfo;

/**
 *
 * @author a
 */
@ManagedBean
@ViewScoped
public class DocumentoBean {

    Usuario usuario;
    Documento documento;

    private String nome;
    private String email;
    private int quantidadepaginas;

    private int idColegiadoSelecionado;
    private String nomeColegiadoSelecionado;
    private int idUsuarioLogado;

    List<PaginasInfo> lista = new ArrayList<PaginasInfo>();

    DocumentoJpaController controllerDocumento;

    public DocumentoBean() {
        usuario = new Usuario();
        documento = new Documento();
        controllerDocumento = new DocumentoJpaController();
    }

    @PostConstruct
    public void PostConstruct() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idColegiado") != null) {
            idColegiadoSelecionado = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idColegiado");
            nomeColegiadoSelecionado = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("nomeColegiado");
            idUsuarioLogado = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("USUARIO_LOGADO_ID");
        }
    }

    /**
     *
     * @return
     */
    public List<PaginasInfo> listaUsuariosSelecionados() {
        List<Usuario> lu = new ArrayList();

        UsuarioJpaController jpaU = new UsuarioJpaController();
        lu = jpaU.selectAll();
        lista.clear();

        for (int i = 0; i < lu.size(); i++) {
            PaginasInfo piAux = controllerDocumento.listaPaginasInfo(this.idColegiadoSelecionado, lu.get(i).getIdusuario());
            System.out.println("piAux" + piAux);
            if (piAux.getQtTotal() != null) {
                lista.add(controllerDocumento.listaPaginasInfo(this.idColegiadoSelecionado, lu.get(i).getIdusuario()));
            }
        }
        return lista;
    }

    /**
     *
     * @return
     */
    public int paginasPorColegiado() {
        int cntTotal = 0;

        for (PaginasInfo listaTotal : lista) {
            cntTotal += listaTotal.getQtTotal();
        }

        return cntTotal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
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

    public int getQuantidadepaginas() {
        return quantidadepaginas;
    }

    public void setQuantidadepaginas(int quantidadepaginas) {
        this.quantidadepaginas = quantidadepaginas;
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

    public DocumentoJpaController getControllerDocumento() {
        return controllerDocumento;
    }

    public void setControllerDocumento(DocumentoJpaController controllerDocumento) {
        this.controllerDocumento = controllerDocumento;
    }

}
