package sistemaNotas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class TipoProvaDAO {

    public List<TipoProva> listar()
            throws SQLException {

        List<TipoProva> tipos =
                new ArrayList<>();

        String sql =
                "SELECT id_tipo_prova, nome_prova "
              + "FROM tipos_provas "
              + "ORDER BY id_tipo_prova";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                TipoProva tipo =
                        new TipoProva(

                            rs.getInt(
                                    "id_tipo_prova"),

                            rs.getString(
                                    "nome_prova")
                        );

                tipos.add(tipo);
            }
        }

        return tipos;
    }
}