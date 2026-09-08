package sistemaNotas;

import java.math.BigDecimal;

public class ResultadoNota {

    private String ra;
    private String aluno;
    private String disciplina;
    private String prova;
    private BigDecimal nota;

    public ResultadoNota(
            String ra,
            String aluno,
            String disciplina,
            String prova,
            BigDecimal nota) {

        this.ra = ra;
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.prova = prova;
        this.nota = nota;
    }

    public String getRa() {
        return ra;
    }

    public String getAluno() {
        return aluno;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getProva() {
        return prova;
    }

    public BigDecimal getNota() {
        return nota;
    }
}