///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package email;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// *
// * @author a
// */
//public class MailEntrega {
//
//    public void emailSender(String email, String mensagem)
//    {
//        MailJava mj = new MailJava();
//        //configuracoes de envio
//        mj.setSmtpHostMail("smtp.gmail.com"); //trocar esse
//        mj.setSmtpPortMail("587");
//        mj.setSmtpAuth("true");
//        mj.setSmtpStarttls("true");
//        mj.setUserMail("unifilnpi@gmail.com"); //trocar esse
//        mj.setFromNameMail("NPI No Reply");
//        mj.setPassMail("pomarola"); //trocar esse
//        mj.setCharsetMail("ISO-8859-1");
//        mj.setSubjectMail("Nova Senha Ponto Web");
//        mj.setBodyMail(htmlMessage(mensagem));
//        mj.setTypeTextMail(MailJava.TYPE_TEXT_HTML);
//
//        //sete quantos destinatarios desejar
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(email, "email"); //@com, 'para'
////        map.put("sha.rear@hotmail.com", "email hotmail");        
////        map.put("eduardodudutesser555@gmail.com", "email gmail");
//        mj.setToMailsUsers(map);
//        try{
//            new MailJavaSender().senderMail(mj);
//        }catch(Exception e){e.printStackTrace();}
//    }
//    
//    private static String textMessage(String mensagem)
//    {
//        return  "O seu arquivo enviado em  " + mensagem +"\n\n"
//                + "https://dc.unifil.br:8181/NPI/";
//    }
//
//    private static String htmlMessage(String mensagem)
//    {
//        return  
//        "<html>\n" +
//        "\t<head>\n" +
//        "\t\t<title>O seu arquivo enviado em  " + mensagem + "</title> \n" +
//        "\t</head>\n" +
//        "\t<body>\n" +
//                "O seu arquivo enviado em  " + mensagem + ".<br/>" +
//                "https://dc.unifil.br:8181/NPI/" +
//        "\t</body> \n" +
//        "</html>";
//    }
//    public static void main(String[] args) {
//        new MailEntrega();
//    }
//    
//}