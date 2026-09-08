package sistemaNotas;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class NotaDAO {

    public boolean existeNota(
            String ra,
            int idDisciplina,
            int idTipoProva)
            throws SQLException {

        String sql =
                "SELECT 1 "
              + "FROM notas "
              + "WHERE ra = ? "
              + "AND id_disciplina = ? "
              + "AND id_tipo_prova = ?";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void inserir(
            String ra,
            int idDisciplina,
            int idTipoProva,
            BigDecimal nota)
            throws SQLException {

        String sql =
                "INSERT INTO notas "
              + "(ra, id_disciplina, "
              + "id_tipo_prova, nota) "
              + "VALUES (?, ?, ?, ?)";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);
            stmt.setBigDecimal(4, nota);

            stmt.executeUpdate();
        }
    }

    public ResultadoNota consultar(
            String ra,
            int idDisciplina,
            int idTipoProva)
            throws SQLException {

        String sql =
                "SELECT "
              + "a.ra, "
              + "a.nome AS aluno, "
              + "d.nome_disciplina AS disciplina, "
              + "tp.nome_prova AS prova, "
              + "n.nota "
              + "FROM notas AS n "

              + "INNER JOIN alunos AS a "
              + "ON a.ra = n.ra "

              + "INNER JOIN disciplinas AS d "
              + "ON d.id_disciplina = "
              + "n.id_disciplina "

              + "INNER JOIN tipos_provas AS tp "
              + "ON tp.id_tipo_prova = "
              + "n.id_tipo_prova "

              + "WHERE a.ra = ? "
              + "AND d.id_disciplina = ? "
              + "AND tp.id_tipo_prova = ?";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, ra);
            stmt.setInt(2, idDisciplina);
            stmt.setInt(3, idTipoProva);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new ResultadoNota(

                        rs.getString("ra"),
                        rs.getString("aluno"),
                        rs.getString("disciplina"),
                        rs.getString("prova"),
                        rs.getBigDecimal("nota")
                    );
                }
            }
        }

        return null;
    }

    public List<ResultadoNota> listarBoletim(
            String ra)
            throws SQLException {

        List<ResultadoNota> resultados =
                new ArrayList<>();

        String sql =
                "SELECT "
              + "a.ra, "
              + "a.nome AS aluno, "
              + "d.nome_disciplina AS disciplina, "
              + "tp.nome_prova AS prova, "
              + "n.nota "
              + "FROM notas AS n "

              + "INNER JOIN alunos AS a "
              + "ON a.ra = n.ra "

              + "INNER JOIN disciplinas AS d "
              + "ON d.id_disciplina = "
              + "n.id_disciplina "

              + "INNER JOIN tipos_provas AS tp "
              + "ON tp.id_tipo_prova = "
              + "n.id_tipo_prova "

              + "WHERE a.ra = ? "

              + "ORDER BY "
              + "d.id_disciplina, "
              + "tp.id_tipo_prova";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, ra);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    resultados.add(
                        new ResultadoNota(

                            rs.getString("ra"),
                            rs.getString("aluno"),

                            rs.getString(
                                    "disciplina"),

                            rs.getString("prova"),

                            rs.getBigDecimal(
                                    "nota")
                        )
                    );
                }
            }
        }

        return resultados;
    }
}