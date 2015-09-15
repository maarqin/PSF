package models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Colegiado;
import models.Documento;
import models.Usuario;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-09-15T15:47:46")
@StaticMetamodel(Solicitacao.class)
public class Solicitacao_ { 

    public static volatile SingularAttribute<Solicitacao, String> periodoaplicacao;
    public static volatile SingularAttribute<Solicitacao, String> estado;
    public static volatile ListAttribute<Solicitacao, Documento> documentoList;
    public static volatile SingularAttribute<Solicitacao, Integer> atendente;
    public static volatile SingularAttribute<Solicitacao, Date> datacriacao;
    public static volatile SingularAttribute<Solicitacao, Short> frenteVerso;
    public static volatile SingularAttribute<Solicitacao, Integer> numprotocolo;
    public static volatile SingularAttribute<Solicitacao, String> motivorecusa;
    public static volatile SingularAttribute<Solicitacao, Usuario> usuarioIdusuario;
    public static volatile SingularAttribute<Solicitacao, Integer> idsolicitacao;
    public static volatile SingularAttribute<Solicitacao, Date> dataaplicacao;
    public static volatile SingularAttribute<Solicitacao, Colegiado> colegiadoIdcolegiado;

}