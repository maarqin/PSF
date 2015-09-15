package models;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import models.Solicitacao;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-09-15T15:47:46")
@StaticMetamodel(Documento.class)
public class Documento_ { 

    public static volatile SingularAttribute<Documento, Integer> quantidadepaginas;
    public static volatile SingularAttribute<Documento, String> nomedocumento;
    public static volatile SingularAttribute<Documento, Integer> iddocumento;
    public static volatile SingularAttribute<Documento, String> enderecodocumento;
    public static volatile SingularAttribute<Documento, Integer> quantidadecopias;
    public static volatile SingularAttribute<Documento, Solicitacao> solicitacaoIdsolicitacao;

}