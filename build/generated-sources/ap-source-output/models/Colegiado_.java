package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.ColegiadoHasUsuario;
import models.Solicitacao;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-09-15T15:47:46")
@StaticMetamodel(Colegiado.class)
public class Colegiado_ { 

    public static volatile ListAttribute<Colegiado, ColegiadoHasUsuario> colegiadoHasUsuarioList;
    public static volatile ListAttribute<Colegiado, Solicitacao> solicitacaoList;
    public static volatile SingularAttribute<Colegiado, String> estado;
    public static volatile SingularAttribute<Colegiado, Integer> idcolegiado;
    public static volatile SingularAttribute<Colegiado, Integer> quantidadecursos;
    public static volatile SingularAttribute<Colegiado, String> nome;

}