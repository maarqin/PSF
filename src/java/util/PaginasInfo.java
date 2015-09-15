package util;

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

    /**
     *
     */
    public PaginasInfo() {
    }

    /**
     *
     * @param qtTotal
     * @param nomeProfessor
     * @param emailProfessor
     */
    public PaginasInfo(Long qtTotal, String nomeProfessor, String emailProfessor) {
        this.qtTotal = qtTotal;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
    }

    /**
     *
     * @param qtPaginas
     * @param nomeProfessor
     * @param emailProfessor
     * @param qtCopias
     */
    public PaginasInfo(Integer qtPaginas, String nomeProfessor, String emailProfessor, Integer qtCopias) {
        this.qtPaginas = qtPaginas;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
        this.qtCopias = qtCopias;
    }

    /**
     *
     * @param qtPaginas
     * @param nomeProfessor
     * @param emailProfessor
     */
    public PaginasInfo(Integer qtPaginas, String nomeProfessor, String emailProfessor) {
        this.qtPaginas = qtPaginas;
        this.nomeProfessor = nomeProfessor;
        this.emailProfessor = emailProfessor;
    }

    /**
     *
     * @return
     */
    public Integer getQtPaginas() {
        return qtPaginas;
    }

    /**
     *
     * @param qtPaginas
     */
    public void setQtPaginas(Integer qtPaginas) {
        this.qtPaginas = qtPaginas;
    }

    /**
     *
     * @return
     */
    public String getNomeProfessor() {
        return nomeProfessor;
    }

    /**
     *
     * @param nomeProfessor
     */
    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    /**
     *
     * @return
     */
    public String getEmailProfessor() {
        return emailProfessor;
    }

    /**
     *
     * @param emailProfessor
     */
    public void setEmailProfessor(String emailProfessor) {
        this.emailProfessor = emailProfessor;
    }

    /**
     *
     * @return
     */
    public Integer getQtCopias() {
        return qtCopias;
    }

    /**
     *
     * @param qtCopias
     */
    public void setQtCopias(Integer qtCopias) {
        this.qtCopias = qtCopias;
    }

    /**
     *
     * @return
     */
    public Long getQtTotal() {
        return qtTotal;
    }

    @Override
    public String toString() {
        return "PaginasInfo{" + "qtCopias=" + qtCopias + ", qtPaginas=" +
                qtPaginas + ", qtTotal=" + qtTotal + ", nomeProfessor=" + 
                nomeProfessor + ", emailProfessor=" + emailProfessor + '}';
    }

}
