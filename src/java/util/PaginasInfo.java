/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controllers.ColegiadoJpaController;
import controllers.DocumentoJpaController;
import controllers.SolicitacaoJpaController;
import controllers.UsuarioJpaController;
import java.util.ArrayList;
import java.util.List;
import models.Colegiado;
import models.Documento;
import models.Solicitacao;
import models.Usuario;

/**
 *
 * @author a
 */
public class PaginasInfo {

    private Integer qtCopias;
    private Integer qtPaginas;
    private Long qtTotal = 0l;
    private String nomeProfessor;
    private String emailProfessor;

    public PaginasInfo() {
    }

    public PaginasInfo(Long qtTotal, String nomeProfessor, String emailProfessor) {
        this.qtTotal = qtTotal;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
    }

    public PaginasInfo(Integer qtPaginas, String nomeProfessor, String emailProfessor, Integer qtCopias) {
        this.qtPaginas = qtPaginas;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
        this.qtCopias = qtCopias;
//        qtTotal = qtCopias * qtPaginas;
    }

    public PaginasInfo(Integer qtPaginas, String nomeProfessor, String emailProfessor) {
        this.qtPaginas = qtPaginas;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
    }

//    public static PaginasInfo getPaginasInfo(Usuario objUser, Colegiado objColegiado) {
//        List<Solicitacao> lsol = ctSol.selectAll();
//        List<Documento> docList = ctDoc.selectAll();
//
//        List<PaginasInfo> lista = new ArrayList();
//
//        PaginasInfo pinfo = new PaginasInfo();
//
//        pinfo.nomeProfessor = objUser.getNome();
//        pinfo.emailProfessor = objUser.getEmail();
//
//        for (int i = 0; i < lsol.size(); i++) {
//            if (true) {
//                for (int x = 0; i < docList.size(); x++) {
//                    if (doc.idsolicitacao == lsol.geti.getid) {
//                        pinfo.qtPaginas += doc.geti.getpg;
//                    }
//
//                }
//
//            }
//
//        }
//
//        return null;
//
//    }
    public Integer getQtPaginas() {
        return qtPaginas;
    }

    public void setQtPaginas(Integer qtPaginas) {
        this.qtPaginas = qtPaginas;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getEmailProfessor() {
        return emailProfessor;
    }

    public void setEmailProfessor(String emailProfessor) {
        this.emailProfessor = emailProfessor;
    }

    public Integer getQtCopias() {
        return qtCopias;
    }

    public void setQtCopias(Integer qtCopias) {
        this.qtCopias = qtCopias;
    }

    public Long getQtTotal() {
        return qtTotal;
    }

    @Override
    public String toString() {
        return "PaginasInfo{" + "qtCopias=" + qtCopias + ", qtPaginas=" + qtPaginas + ", qtTotal=" + qtTotal + ", nomeProfessor=" + nomeProfessor + ", emailProfessor=" + emailProfessor + '}';
    }

}
