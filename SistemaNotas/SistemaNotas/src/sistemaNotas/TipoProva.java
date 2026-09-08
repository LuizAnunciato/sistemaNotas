package sistemaNotas;

public class TipoProva {

    private int idTipoProva;
    private String nomeProva;

    public TipoProva(int idTipoProva,
                     String nomeProva) {

        this.idTipoProva = idTipoProva;
        this.nomeProva = nomeProva;
    }

    public int getIdTipoProva() {
        return idTipoProva;
    }

    public String getNomeProva() {
        return nomeProva;
    }

    @Override
    public String toString() {
        return nomeProva;
    }
}