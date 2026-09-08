package sistemaNotas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


public class AlunoDAO {

    public void inserir(Aluno aluno) throws SQLException {

        String sql =
                "INSERT INTO alunos "
              + "(ra, nome, data_nascimento, rg) "
              + "VALUES (?, ?, ?, ?)";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, aluno.getRa());
            stmt.setString(2, aluno.getNome());

            stmt.setDate(
                    3,
                    Date.valueOf(aluno.getDataNascimento())
            );

            stmt.setString(4, aluno.getRg());

            stmt.executeUpdate();
        }
    }

    public boolean existeRa(String ra)
            throws SQLException {

        String sql =
                "SELECT 1 FROM alunos "
              + "WHERE ra = ?";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, ra);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeRg(String rg)
            throws SQLException {

        String sql =
                "SELECT 1 FROM alunos "
              + "WHERE rg = ?";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, rg);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Aluno> listar()
            throws SQLException {

        List<Aluno> alunos = new ArrayList<>();

        String sql =
                "SELECT ra, nome, data_nascimento, rg "
              + "FROM alunos "
              + "ORDER BY nome";

        try (
            Connection conexao = Conexao.conectar();
            PreparedStatement stmt =
                    conexao.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Aluno aluno = new Aluno(
                        rs.getString("ra"),
                        rs.getString("nome"),

                        rs.getDate("data_nascimento")
                          .toLocalDate(),

                        rs.getString("rg")
                );

                alunos.add(aluno);
            }
        }

        return alunos;
    }
}