/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controllers.ColegiadoHasUsuarioJpaController;
import controllers.ColegiadoJpaController;
import controllers.DocumentoJpaController;
import controllers.SolicitacaoJpaController;
import controllers.UsuarioJpaController;
import email.MainTester;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Colegiado;
import models.ColegiadoHasUsuario;
import models.Documento;
import models.Solicitacao;
import models.Usuario;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author
 */
@ManagedBean
@ViewScoped
public class SolicitacaoDocumentoBean implements Serializable {

    private String PATH = "/Users/thomaz/NetBeansProjects/PSF/uploads/";

    SolicitacaoJpaController controllerSolicitacao;
    DocumentoJpaController controllerDocumento;
    UsuarioJpaController controllerUsuario;
    ColegiadoJpaController controllerColegiado;
    ColegiadoHasUsuarioJpaController controllerColegiadoHasUsuario;

    Solicitacao solicitacao, solicitacaoDetalhada;
    Documento documento;
    Usuario usuario, atendente;
    String nomeAtendente, atendenteAux;
    Colegiado colegiado;
    private int colegiadoSelecionado;
    ColegiadoHasUsuario colegiadoHasUsuario;

    private String estado, periodoAplicacao, turma, nomeArquivo, estadoSolicitacaoDetalhada, motivoRecusa;
    private Date dataAplicacao, dataCriacao;
    int quantidadecopias, quantidadepaginas;
    private int idUsuario, idSolicitacao, numProtocolo;

    private String usuario_logado = "";
    private InputText input;
    private DefaultStreamedContent dataDownload;
    private String parametro;
    private UploadedFile file;
    private int idUsuarioLogado;
    private boolean frenteVerso;
    boolean teste;

    List<Documento> listaDocumentos;
    List<Solicitacao> listaSolicitacoes;
    List<Colegiado> listaColegiados;

    FileUploadEvent evt;

    public SolicitacaoDocumentoBean() {
        
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        usuario_logado = (String) session.getAttribute("USUARIO_LOGADO_NOME");
        idUsuarioLogado = (int) session.getAttribute("USUARIO_LOGADO_ID");
        colegiado = new Colegiado();
        controllerDocumento = new DocumentoJpaController();
        controllerSolicitacao = new SolicitacaoJpaController();
        controllerUsuario = new UsuarioJpaController();
        controllerColegiado = new ColegiadoJpaController();
//        controllerColegiadoHasUsuario = new ColegiadoHasUsuarioJpaController();

        listaDocumentos = new ArrayList();
//        listaColegiados = new ArrayList();

        input = new InputText();
        idUsuario = (int) session.getAttribute("USUARIO_LOGADO_ID");
        usuario = controllerUsuario.findUsuario(idUsuario);
        solicitacao = new Solicitacao();

        listaColegiados = controllerColegiado.selectAll();
    }

//    public SolicitacaoDocumentoBean() {
//
//    }
//
//    public SolicitacaoDocumentoBean(Solicitacao s, Usuario u) {
//        this.solicitacao = s;
//        this.usuario = u;
//    }
// <editor-fold defaultstate="collapsed" desc="Documento">
    //Modificar o a variavel PATH antes da entrega e na implantação 
    
    public int totalPaginas(Documento doc){
        return doc.getQuantidadecopias() * doc.getQuantidadepaginas();
    }
    
    public void upload(FileUploadEvent evt) throws IOException {
        Calendar calendar = Calendar.getInstance();
        controllerUsuario = new UsuarioJpaController();
        Usuario u = controllerUsuario.findUsuario(idUsuarioLogado);

        if (listaDocumentos.size() <= 5) {
            String pasta = criarPasta();
            FacesMessage msg = new FacesMessage("Seu documento foi enviado.", evt.getFile().getFileName() + " foi enviado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            InputStream input = evt.getFile().getInputstream();
            OutputStream output = new FileOutputStream(new File(pasta, evt.getFile().getFileName()));

            Documento doc = new Documento();
            doc.setQuantidadecopias(quantidadecopias);
            doc.setEnderecodocumento(PATH + u.getNome() + "/" + evt.getFile().getFileName());
            doc.setNomedocumento(evt.getFile().getFileName());
            doc.setIddocumento(Integer.MIN_VALUE);
            doc.setQuantidadepaginas(quantidadepaginas);

            try {
                IOUtils.copy(input, output);
//                System.out.println("doc end:" + doc.getEnderecodocumento());
                doc.setQuantidadepaginas((int) PDDocument.load(doc.getEnderecodocumento()).getNumberOfPages());
                //criar o arquivo e adicionar para a lista
                try {
                    listaDocumentos.add(doc);
                    doc = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
        } else {
//            FacesMessage msg = new FacesMessage("Você não pode enviar mais de cinco arquivos na solicitação");

        }
    }

    public String prepararDownload(ActionEvent submit) throws FileNotFoundException {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String dataDownload = params.get("linkFile");

        System.out.println("Preparando arquivo para download: " + dataDownload);

        File down = new File(dataDownload);
        InputStream input = new FileInputStream(down);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        setDataDownload(new DefaultStreamedContent(input, externalContext.getMimeType(down.getName()), down.getName()));
        System.out.println("Nome do arquivo de download: " + dataDownload);
        return "";

    }

    private String criarPasta() {
        String pasta = PATH + "/" + usuario_logado;
        try {
            if (!new File(pasta).exists()) { // Verifica se o diretório existe.   
                (new File(pasta)).mkdir();   // Cria o diretório   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pasta;
    }

    public void removerDaLista(ActionEvent submit) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String documento = params.get("idDocumento");
        System.out.println("Tamanho da lista de documentos: " + listaDocumentos.size());

        if (listaDocumentos.size() <= 1) {
            listaDocumentos.clear();
            try {
                controllerDocumento.destroy(Integer.parseInt(documento));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            for (int i = 0; i < listaDocumentos.size(); i++) {
                if (listaDocumentos.get(i).getIddocumento() == Integer.parseInt(documento)) {
                    listaDocumentos.remove(i);
                    try {
                        controllerDocumento.destroy(listaDocumentos.get(i).getIddocumento());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="Solicitacao">
    public String escoherArquivo(ActionEvent submit) {
        try {
            solicitacao.setDocumentoList(getListaDocumentos());

            controllerSolicitacao.create(solicitacao);
            dataAplicacao = null;
            periodoAplicacao = "";
            dataCriacao = null;
            turma = "";
            listaDocumentos.clear();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação enviada com sucesso!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pãn.", ""));
            e.printStackTrace();
        }

        try {

        } catch (Exception e) {
        }
        return "";
    }

    private String CriarPasta() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        String usuarioNome = (String) session.getAttribute("USUARIO_LOGADO_NOME");
        String pasta = PATH + "/" + usuarioNome + "/";

        try {

            if (!new File(pasta).exists()) { // Verifica se o diretório existe.   
                (new File(pasta)).mkdir();   // Cria o diretório   
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pasta;
    }

    public String downloadArquivo(ActionEvent submit) throws FileNotFoundException {
        System.out.println("dataDowload" + dataDownload);

        File file = new File(PATH + dataDownload);
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setHeader("Content-Disposition", "attachment;filename=" + dataDownload);
        response.setContentLength((int) file.length());
        ServletOutputStream out = null;
        try {
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                out.write(buffer);
                out.flush();
            }
            FacesContext.getCurrentInstance().getResponseComplete();
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }

        return "";
    }

    public String novaSolicitacao() {
        List<Documento> lista = new ArrayList();

        if (frenteVerso == true) {
            solicitacao.setFrenteVerso((byte) 1);
        } else if (frenteVerso == false) {
            solicitacao.setFrenteVerso((byte) 0);
        }

        solicitacao.setEstado("Solicitado");
        solicitacao.setUsuarioIdusuario(usuario);
        solicitacao.setNumprotocolo(solicitacao.getUsuarioIdusuario().getIdusuario());
        solicitacao.setColegiadoIdcolegiado(colegiado);
        solicitacao.setDatacriacao(new Date());

        try {
            controllerSolicitacao.create(solicitacao);
            solicitacao.setNumprotocolo(solicitacao.getIdsolicitacao());
            controllerSolicitacao.edit(solicitacao);

            for (int i = 0; i < listaDocumentos.size(); i++) {
                listaDocumentos.get(i).setSolicitacaoIdsolicitacao(solicitacao);
                try {
                    if(solicitacao.getFrenteVerso().equalsIgnoreCase("Sim")){
                        System.out.println("É frente e verso, então QTD PAGINA É: " + listaDocumentos.get(i).getQuantidadepaginas());
                        System.out.println("E a metade é: " + (int)Math.ceil((double)listaDocumentos.get(i).getQuantidadepaginas()/2));
                    }
                    
                    controllerDocumento.create(listaDocumentos.get(i));
                    lista.add(listaDocumentos.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

            }
            solicitacao.setDocumentoList(lista);

            controllerSolicitacao.edit(solicitacao);
            solicitacao.clean();
            listaDocumentos.clear();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação enviada!", ""));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao criar uma solicitação.", ""));
            e.printStackTrace();
        }
        return "";
    }

    public void apagarSolicitacao(int idSolicitacao) {
        controllerSolicitacao = new SolicitacaoJpaController();
        Solicitacao temp = new Solicitacao();
        List<Documento> list = new ArrayList();
        DocumentoJpaController dc = new DocumentoJpaController();
        temp = controllerSolicitacao.findSolicitacao(idSolicitacao);
        list = dc.getListDocSolicitacao(temp);

        try {
            for (int i = 0; i < list.size(); i++) {
                dc.destroy(list.get(i).getIddocumento());
            }

            controllerSolicitacao.destroy(idSolicitacao);

//            controllerUsuario.destroy(idUsuario);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um ou mais campos não estão preenchidos corretamente!", ""));
        }

        try {
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um ou mais campos não estão preenchidos corretamente!", ""));
        }
    }

    public List<Solicitacao> listaDoProfessor() {
        List<Solicitacao> lista = new ArrayList();
        lista = controllerUsuario.selectAllSolicitacoes(usuario);
        return lista;
    }

    public List<Documento> listaDoscumentosSelecionado() {

        List<Documento> lista = new ArrayList();
        try {

            lista = solicitacao.getDocumentoList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public String mudarEstadoSolicitacao() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String datap = params.get("idSolicitacao");
        String estadop = params.get("estadoSolicitacao");
        Solicitacao temp = controllerSolicitacao.findSolicitacao(Integer.parseInt(datap));
        Usuario user = controllerUsuario.findUsuario(temp.getUsuarioIdusuario().getIdusuario());
        temp.setEstado(estadop);
        try {
            controllerSolicitacao.edit(temp);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Solicitação alterada com sucesso!", ""));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na alteração da solicitação.", ""));
            e.printStackTrace();
        }
        return "";
    }

    public void encontrarSolicitacao(ActionEvent submit) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        String data = params.get("idSolicitacao");
        if (!data.equals(null)) {
            solicitacao = controllerSolicitacao.findSolicitacao(Integer.parseInt(data));
        }

    }

    public List<Solicitacao> listaSolicitacoes() {
        List<Solicitacao> lista = new ArrayList();
        lista = controllerSolicitacao.selectAll();
        return lista;
    }

    public String retornaAtendente(Solicitacao s) {
        Usuario usuario = new Usuario();
        String nome = "";
        UsuarioJpaController jpau = new UsuarioJpaController();
        List<Usuario> list = jpau.findUsuarioList(s.getAtendente());
        if (list.isEmpty()) {
            nome = "";
        } else {
            usuario = list.get(0);
            if (usuario.getTipo().equals("Funcionario")) {
                nome = usuario.getNome();
                System.out.println("retornaAtendente: " + nome);
                return nome;
            }

        }
        return nome;
    }

    public void mensagemEmail(){
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage("Enviando e-mail..."));
    }
    
    
    public void updateSolicitacaoDetalhada() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        int user = (int) session.getAttribute("USUARIO_LOGADO_ID");
        atendente = controllerUsuario.findUsuario(user);//atendente é o funcinario logado que der o update;

        solicitacaoDetalhada.setAtendente(user);
//        solicitacao.setAtendente(user);
        System.out.println("Atendente: " + atendente.getNome());
        nomeAtendente = atendente.getNome();
//        System.out.println("Nome atendente: " + nomeAtendente);
        estado = estadoSolicitacaoDetalhada;
        solicitacaoDetalhada.setMotivorecusa(motivoRecusa);
        solicitacaoDetalhada.setEstado(estadoSolicitacaoDetalhada);

        try {
            controllerSolicitacao.edit(solicitacaoDetalhada);

            try {
                MainTester m = new MainTester();
                m.emailSenderEstado(solicitacaoDetalhada.getUsuarioIdusuario().getEmail(), solicitacaoDetalhada); //envia para o email digitado a senha gerada
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage("E-mail enviado."));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        } else {
//            System.out.println("");
//        }
    }

//<editor-fold defaultstate="collapsed" desc="calendar">
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }
    //</editor-fold>

//</editor-fold>
// <editor-fold defaultstate="collapsed" desc="gettes e setters">
    // <editor-fold defaultstate="collapsed" desc="documento">
    public List<Documento> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<Documento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public int getQuantidadecopias() {
        return quantidadecopias;
    }

    public void setQuantidadecopias(int quantidadecopias) {
        this.quantidadecopias = quantidadecopias;
    }

    public InputText getInput() {
        return input;
    }

    public void setInput(InputText input) {
        this.input = input;
    }

    public DefaultStreamedContent getDataDownload() {
        return dataDownload;
    }

    public void setDataDownload(DefaultStreamedContent dataDownload) {
        this.dataDownload = dataDownload;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public DocumentoJpaController getControllerDocumento() {
        return controllerDocumento;
    }

    public void setControllerDocumento(DocumentoJpaController controllerDocumento) {
        this.controllerDocumento = controllerDocumento;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="solicitacao">
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String onFlowProcess(FlowEvent event) {

        return event.getNewStep();

    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public SolicitacaoJpaController getControllerSolicitacao() {
        return controllerSolicitacao;
    }

    public void setControllerSolicitacao(SolicitacaoJpaController controllerSolicitacao) {
        this.controllerSolicitacao = controllerSolicitacao;
    }

    public UsuarioJpaController getControllerUsuario() {
        return controllerUsuario;
    }

    public void setControllerUsuario(UsuarioJpaController controllerUsuario) {
        this.controllerUsuario = controllerUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

//    public List<Solicitacao> getListaSolicitacoes() {
//        return listaSolicitacoes;
//    }
//
//    public void setListaSolicitacoes(List<Solicitacao> listaSolicitacoes) {
//        this.listaSolicitacoes = listaSolicitacoes;
//    }
    public Date getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(Date dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getPeriodoAplicacao() {
        return periodoAplicacao;
    }

    public void setPeriodoAplicacao(String periodoAplicacao) {
        this.periodoAplicacao = periodoAplicacao;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public Usuario getAtendente() {
        return atendente;
    }

    public void setAtendente(Usuario atendente) {
        this.atendente = atendente;
    }

    public int getIdSolicitacao() {
        return idSolicitacao;
    }

    public void setIdSolicitacao(int idSolicitacao) {
        this.idSolicitacao = idSolicitacao;
    }

    public int getNumProtocolo() {
        return numProtocolo;
    }

    public void setNumProtocolo(int numProtocolo) {
        this.numProtocolo = numProtocolo;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

//</editor-fold>
    public ColegiadoJpaController getControllerColegiado() {
        return controllerColegiado;
    }

    public void setControllerColegiado(ColegiadoJpaController controllerColegiado) {
        this.controllerColegiado = controllerColegiado;
    }

    public int getQuantidadepaginas() {
        return quantidadepaginas;
    }

    public void setQuantidadepaginas(int quantidadepaginas) {
        this.quantidadepaginas = quantidadepaginas;
    }

    public String getUsuario_logado() {
        return usuario_logado;
    }

    public void setUsuario_logado(String usuario_logado) {
        this.usuario_logado = usuario_logado;
    }

    public int getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public void setIdUsuarioLogado(int idUsuarioLogado) {
        this.idUsuarioLogado = idUsuarioLogado;
    }

    public List<Solicitacao> getListaSolicitacoes() {
        return listaSolicitacoes;
    }

    public void setListaSolicitacoes(List<Solicitacao> listaSolicitacoes) {
        this.listaSolicitacoes = listaSolicitacoes;
    }

    public FileUploadEvent getEvt() {
        return evt;
    }

    public void setEvt(FileUploadEvent evt) {
        this.evt = evt;
    }

    public Colegiado getColegiado() {
        return colegiado;
    }

    public void setColegiado(Colegiado colegiado) {
        this.colegiado = colegiado;
    }

    public List<Colegiado> getListaColegiados() {
        return listaColegiados;
    }

    public void setListaColegiados(List<Colegiado> listaColegiados) {
        this.listaColegiados = listaColegiados;
    }

    public ColegiadoHasUsuarioJpaController getControllerColegiadoHasUsuario() {
        return controllerColegiadoHasUsuario;
    }

    public void setControllerColegiadoHasUsuario(ColegiadoHasUsuarioJpaController controllerColegiadoHasUsuario) {
        this.controllerColegiadoHasUsuario = controllerColegiadoHasUsuario;
    }

    public ColegiadoHasUsuario getColegiadoHasUsuario() {
        return colegiadoHasUsuario;
    }

    public void setColegiadoHasUsuario(ColegiadoHasUsuario colegiadoHasUsuario) {
        this.colegiadoHasUsuario = colegiadoHasUsuario;
    }
//</editor-fold>

    public int getColegiadoSelecionado() {
        return colegiadoSelecionado;
    }

    public void setColegiadoSelecionado(int colegiadoSelecionado) {
        this.colegiadoSelecionado = colegiadoSelecionado;
    }

    public Solicitacao getSolicitacaoDetalhada() {
        return solicitacaoDetalhada;
    }

    public void setSolicitacaoDetalhada(Solicitacao solicitacaoDetalhada) {
        this.solicitacaoDetalhada = solicitacaoDetalhada;
    }

    public String getEstadoSolicitacaoDetalhada() {
        return estadoSolicitacaoDetalhada;
    }

    public void setEstadoSolicitacaoDetalhada(String estadoSolicitacaoDetalhada) {
        this.estadoSolicitacaoDetalhada = estadoSolicitacaoDetalhada;
    }

    public void render(String valor) {
        if (valor.equals("Recusado")) {
            teste = true;
        } else {
            teste = false;
        }
    }

    public boolean isTeste() {
        return teste;
    }

    public void setTeste(boolean teste) {
        this.teste = teste;
    }

    public String getNomeAtendente() {
        return nomeAtendente;
    }

    public void setNomeAtendente(String nomeAtendente) {
        this.nomeAtendente = nomeAtendente;
    }

    public String getMotivoRecusa() {
        return motivoRecusa;
    }

    public void setMotivoRecusa(String motivoRecusa) {
        this.motivoRecusa = motivoRecusa;
    }

    public boolean isFrenteVerso() {
        return frenteVerso;
    }

    public void setFrenteVerso(boolean frenteVerso) {
        this.frenteVerso = frenteVerso;
    }

}
