package sistemaNotas;

public class Disciplina {

    private int idDisciplina;
    private String nomeDisciplina;
    private int cargaHoraria;

    public Disciplina(int idDisciplina,
                      String nomeDisciplina,
                      int cargaHoraria) {

        this.idDisciplina = idDisciplina;
        this.nomeDisciplina = nomeDisciplina;
        this.cargaHoraria = cargaHoraria;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    @Override
    public String toString() {
        return nomeDisciplina;
    }
}