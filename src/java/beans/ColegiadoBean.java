package beans;

import controllers.ColegiadoJpaController;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.swing.Action;
import models.Colegiado;

/**
 *
 * @author a
 */
@ManagedBean
@SessionScoped
public class ColegiadoBean {

    public int idColegiadoSelecionado;

    private Integer idColegiado;
    private String nome, estado;
    private int quantidadecursos;

    Colegiado c;
    ColegiadoJpaController controllerColegiado;
    private Colegiado colegiadoSelecionado;
    private List<Colegiado> colegiadosSelecionados;
    private ColegiadoDataModel coldata;
    private static List<Colegiado> listaColegiados2;

    private List<String> listaNomesColegiados;

    public ColegiadoBean() {
        controllerColegiado = new ColegiadoJpaController();
        coldata = new ColegiadoDataModel(listaColegiados());
        listaColegiados2 = listaColegiados();
        c = new Colegiado();

        listaNomesColegiados = new ArrayList();
        for (int i = 0; i < listaColegiados2.size(); i++) {
            listaNomesColegiados.add(listaColegiados2.get(i).getNome());

        }
    }

    /**
     *
     * @return
     */
    public static List<Colegiado> getListaColegiados2() {
        return listaColegiados2;
    }

    /**
     *
     * @param listaColegiados2
     */
    public static void setListaColegiados2(List<Colegiado> listaColegiados2) {
        ColegiadoBean.listaColegiados2 = listaColegiados2;
    }

    /**
     *
     * @return
     */
    public List<String> getListaNomesColegiados() {
        return listaNomesColegiados;
    }

    /**
     *
     * @param listaNomesColegiados
     */
    public void setListaNomesColegiados(List<String> listaNomesColegiados) {
        this.listaNomesColegiados = listaNomesColegiados;
    }

    /**
     * Pega o id do Colegiado, salva na variável "idColegiado" e vai pra página
     * fotocopiasProfessor.xhtml
     *
     * @param idColegiado o id do colegiado selecionado na tabela
     * @param nomeColegiado é o nome que será passado para a página
     * "fotocopiasProfessor.xhtml" para que seja concatenado na string do nome
     * @return a url "fotocopiasProfessor.xhtml"
     */
    public String irProfessorPage(int idColegiado, String nomeColegiado) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idColegiado", idColegiado);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nomeColegiado", nomeColegiado);
        return "/paginas/adm/fotocopia/fotocopiasProfessor.xhtml?faces-redirect=true";
    }

    public ColegiadoDataModel getColdata() {
        return coldata;
    }

    public void setColdata(ColegiadoDataModel coldata) {
        this.coldata = coldata;
    }

    public List<Colegiado> listaColegiados() {
        List<Colegiado> lista = new ArrayList();
        lista = controllerColegiado.selectAll();

        return lista;
    }

    /**
     *
     * @param submit
     * @return
     */
    public String novoColegiado(Action submit) {
        c = new Colegiado(Integer.MIN_VALUE, nome, quantidadecursos, "Ativo");

        try {
            controllerColegiado.create(c);
            estado = nome = "";
            quantidadecursos = 0;
            c = new Colegiado(Integer.MIN_VALUE, nome, quantidadecursos, "Ativo");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Colegiado cadastrado.", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Esse colegiado já está cadastrado.", ""));

            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param idColegiado
     * @return
     */
    public String deleteColegiado(Integer idColegiado) {
        try {
            controllerColegiado.destroy(idColegiado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Colegiado deletado com sucesso!", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param submit
     * @return
     */
    public String editarColegiado(Action submit) {
        try {
            controllerColegiado.edit(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

// <editor-fold defaultstate="collapsed" desc="getters and setters">
    public List<Colegiado> getColegiadosSelecionados() {
        return colegiadosSelecionados;
    }

    public void setColegiadosSelecionados(List<Colegiado> colegiadosSelecionados) {
        this.colegiadosSelecionados = colegiadosSelecionados;
    }

    public Colegiado getC() {
        return c;
    }

    public void setC(Colegiado c) {
        this.c = c;
    }

    public ColegiadoJpaController getControllerColegiado() {
        return controllerColegiado;
    }

    public void setControllerColegiado(ColegiadoJpaController controllerColegiado) {
        this.controllerColegiado = controllerColegiado;
    }

    public Integer getIdColegiado() {
        return idColegiado;
    }

    public void setIdColegiado(Integer idColegiado) {
        this.idColegiado = idColegiado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getQuantidadecursos() {
        return quantidadecursos;
    }

    public void setQuantidadecursos(int quantidadeCursos) {
        this.quantidadecursos = quantidadeCursos;
    }

    public void setColegiadoSelecionado(Colegiado colegiadoSelecionado) {
        this.colegiadoSelecionado = colegiadoSelecionado;
    }

    public Colegiado getColegiadoSelecionado() {
        return colegiadoSelecionado;
    }

    public void setIdColegiadoSelecionado(int idColegiadoSelecionado) {
        this.idColegiadoSelecionado = idColegiadoSelecionado;
    }

    public int getIdColegiadoSelecionado() {
        return idColegiadoSelecionado;
    }
//</editor-fold>

}
