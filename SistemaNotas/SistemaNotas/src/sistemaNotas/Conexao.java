package sistemaNotas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // ALTERE ZZZ0001 PARA O IDENTIFICADOR UTILIZADO NO SEU BANCO.
    private static final String URL =
            "jdbc:mariadb://localhost:3307/R712HI9";

    private static final String USUARIO = "R712HI9";
    private static final String SENHA = "R712HI9";

    private Conexao() {
        // Impede que a classe seja instanciada.
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USUARIO,
                SENHA
        );
    }
}