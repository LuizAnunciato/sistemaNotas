package sistemaNotas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    public List<Disciplina> listar()
            throws SQLException {

        List<Disciplina> disciplinas =
                new ArrayList<>();

        String sql =
                "SELECT id_disciplina, "
              + "nome_disciplina, carga_horaria "
              + "FROM disciplinas "
              + "ORDER BY nome_disciplina";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Disciplina disciplina =
                        new Disciplina(

                            rs.getInt(
                                    "id_disciplina"),

                            rs.getString(
                                    "nome_disciplina"),

                            rs.getInt(
                                    "carga_horaria")
                        );

                disciplinas.add(disciplina);
            }
        }

        return disciplinas;
    }
}