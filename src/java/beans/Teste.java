package beans;

import controllers.ColegiadoHasUsuarioJpaController;
import controllers.ColegiadoJpaController;
import controllers.DocumentoJpaController;
import controllers.SolicitacaoJpaController;
import controllers.UsuarioJpaController;
import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import hash.SHA;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import models.Colegiado;
import models.Documento;
import models.Solicitacao;
import models.Usuario;
import org.apache.pdfbox.pdmodel.PDDocument;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Guti Ivanagava
 */
public class Teste {

    public static Colegiado c1, c2, c3, c4, c5, c6, c7;
    public static Usuario u1, u2, u3;
    public static List<Documento> listaDocumentos = new ArrayList();
    public static Documento d1, d2;
    public static Solicitacao s1;

    public static void cadastrarColegiados() throws IllegalOrphanException, NonexistentEntityException {
        ColegiadoJpaController ctrl = new ColegiadoJpaController();

        List<Colegiado> lista = new ArrayList();
        lista = ctrl.selectAll();
        
        for (int i = 0; i < lista.size(); i++) {
            ctrl.destroy(lista.get(i).getIdcolegiado());
        }
        
        c1 = new Colegiado(Integer.MIN_VALUE, "Agrárias", 2, "Ativo");
        c2 = new Colegiado(Integer.MIN_VALUE, "Saúde", 6, "Ativo");
        c3 = new Colegiado(Integer.MIN_VALUE, "Humanas", 2, "Ativo");
        c4 = new Colegiado(Integer.MIN_VALUE, "Exatas", 2, "Ativo");
        c5 = new Colegiado(Integer.MIN_VALUE, "Sociais", 4, "Ativo");
        c6 = new Colegiado(Integer.MIN_VALUE, "Engenharia", 1, "Ativo");
        c7 = new Colegiado(Integer.MIN_VALUE, "Técnico", 3, "Ativo");

        try {
            ctrl.create(c1);
            ctrl.create(c2);
            ctrl.create(c3);
            ctrl.create(c4);
            ctrl.create(c5);
            ctrl.create(c6);
            ctrl.create(c7);

            System.out.println("Colegiado cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cadastrarUsuarios() throws NoSuchAlgorithmException, NonexistentEntityException, IllegalOrphanException {
        UsuarioJpaController ctrl = new UsuarioJpaController();

        
        List<Usuario> lista = new ArrayList();
        lista = ctrl.selectAll();
        
//        for (int i = 0; i < lista.size(); i++) {
//            ctrl.destroy(lista.get(i).getIdusuario());
//        }
//        
        String senha1 = SHA.generateHash("aaa");
        String senha2 = SHA.generateHash("ppp");
        String senha3 = SHA.generateHash("fff");
        

        u1 = new Usuario(1,"Administrador", "a", "a@a.com", senha1, 1111111, "Ativo");
        u2 = new Usuario(1,"Professor", "p", "p@p.com", senha2, 2222222, "Ativo");
        u3 = new Usuario(1,"Funcionario", "f", "f@f.com", senha3, 3333333, "Ativo");

        try {
            ctrl.create(u1);
//            ctrl.create(u2);
//            ctrl.create(u3);
            System.out.println("Usuários cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cadastrarDocumentos() {
        DocumentoJpaController ctrl = new DocumentoJpaController();

        d1 = new Documento();
        d1.setIddocumento(Integer.MIN_VALUE);
        d1.setNomedocumento("RONALDO");
        d1.setQuantidadecopias(20);
        d1.setEnderecodocumento("Endereço documento 1");
        d1.setQuantidadepaginas(2);
        d1.setSolicitacaoIdsolicitacao(s1);

        d2 = new Documento();
        d2.setIddocumento(Integer.MIN_VALUE);
        d2.setNomedocumento("BRILA MUITO NO CORINTHIANS");
        d2.setQuantidadecopias(30);
        d2.setEnderecodocumento("Endereço documento 2");
        d2.setQuantidadepaginas(1);
        d2.setSolicitacaoIdsolicitacao(s1);

        listaDocumentos.add(d1);
        listaDocumentos.add(d2);

        try {
            ctrl.create(d1);
            ctrl.create(d2);
            System.out.println("Documentos cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cadastrarSolicitacoes() {
        SolicitacaoJpaController ctrl = new SolicitacaoJpaController();
        Calendar calendar = Calendar.getInstance();
        s1 = new Solicitacao();
        s1.setIdsolicitacao(Integer.MIN_VALUE);
        s1.setAtendente(null);
        s1.setDataaplicacao(calendar.getTime());
        s1.setDatacriacao(calendar.getTime());
        s1.setDocumentoList(listaDocumentos);
        s1.setNumprotocolo(Integer.MIN_VALUE);
        s1.setPeriodoaplicacao("Tarde");
        s1.setEstado("Solicitado");
        s1.setUsuarioIdusuario(u2);

        try {
            ctrl.create(s1);
            System.out.println("Solicitação criada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, IllegalOrphanException, NonexistentEntityException {
//        cadastrarColegiados();//ok
//        cadastrarUsuarios();//ok
//        cadastrarSolicitacoes();//ok
//        cadastrarDocumentos();//ok
        
//        System.out.println(PDDocument.load("C:/Users/Guti Ivanagava/Documents/NetBeansProjects/PSF/uploads/p/Diagrama de Estados.pdf").getNumberOfPages());
        
//        SolicitacaoJpaController cs = new SolicitacaoJpaController();
//        SolicitacaoDocumentoBean sb = new SolicitacaoDocumentoBean();
//        
//        UsuarioJpaController cu = new UsuarioJpaController();
////        System.out.println("Teste: " + cu.findUsuario(9));
//        System.out.println("Teste: " + sb.retornaAtendente(new Solicitacao(10)));
        
    }
}
