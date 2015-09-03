/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package email;

import java.util.HashMap;
import java.util.Map;
import models.Solicitacao;

/**
 *
 * @author a
 */
public class MainTester {

    public void emailSender(String email, String senha) {
        MailJava mj = new MailJava();

        //configuracoes de envio
        mj.setSmtpHostMail("smtp.gmail.com"); //trocar esse
        mj.setSmtpPortMail("587");
        mj.setSmtpAuth("true");
        mj.setSmtpStarttls("true");
        mj.setUserMail("psfnpi@gmail.com"); //trocar esse
        mj.setFromNameMail("NPI Unifil");
        mj.setPassMail("psfnpi12345"); //trocar esse
        mj.setCharsetMail("ISO-8859-1");
        mj.setSubjectMail("Nova senha de acesso à PSF");
        mj.setBodyMail(htmlMessage(senha));
        mj.setTypeTextMail(MailJava.TYPE_TEXT_HTML);

        //sete quantos destinatarios desejar
        Map<String, String> map = new HashMap<String, String>();
        map.put(email, "email");
        mj.setToMailsUsers(map);

        try {
            new MailJavaSender().senderMail(mj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emailSenderEstado(String email, Solicitacao s) {
        MailJava mj = new MailJava();

        //configuracoes de envio
        mj.setSmtpHostMail("smtp.gmail.com"); //trocar esse
        mj.setSmtpPortMail("587");
        mj.setSmtpAuth("true");
        mj.setSmtpStarttls("true");
        mj.setUserMail("psfnpi@gmail.com"); //trocar esse
        mj.setFromNameMail("NPI Unifil");
        mj.setPassMail("psfnpi12345"); //trocar esse
        mj.setCharsetMail("ISO-8859-1");
        mj.setSubjectMail("Alteração no estado da solicitação: " + s.getNumprotocolo());
        mj.setBodyMail(htmlMessageEstado(s));
        mj.setTypeTextMail(MailJava.TYPE_TEXT_HTML);

        //sete quantos destinatarios desejar
        Map<String, String> map = new HashMap<String, String>();
        map.put(email, "email");
        mj.setToMailsUsers(map);
        try {
            new MailJavaSender().senderMail(mj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String htmlMessage(String senha) {
        return "<html>\n"
                + "\t<head>\n"
                + "\t\t<title>"
                + "Nova senha de acesso à Plataforma de Solicitação de Fotocópias (PSF)"
                + "</title> \n"
                + "\t</head>\n"
                + "\t<body>\n"
                + "Sua nova senha de acesso à Plataforma de Solicitação de Fotocópias (PSF) é: " + senha + "."
                + "\n<br/><br/>"
                + "\nEste e-mail é somente para notificação.<br/>"
                + "\t</body> \n"
                + "</html>";
    }

    private static String htmlMessageEstado(Solicitacao s) {
        return "<html>\n"
                + "\t<head>\n"
                + "\t\t<title>"
                + "Alteração no estado da solicitação: " + s.getNumprotocolo() + ""
                + "</title> \n"
                + "\t</head>\n"
                + "\t<body>\n"
                + "Número de protocolo: " + s.getNumprotocolo()
                + "\n<br/><br/>"
                + "\n<br/> Estado da solicitação: " + s.getEstado() + "<br/>"
                + "\n<br/><br/>"
                + "\nEste e-mail é somente para notificação.<br/>"
                + "\t</body> \n"
                + "</html>";
    }

    public static void main(String[] args) {
        new MainTester();
    }
}
