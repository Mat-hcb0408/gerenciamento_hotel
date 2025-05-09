package org.skywalkerhotel.skywalkerhotel.Directory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/gerenciamento_hotel";
    private static final String USUARIO = "root";
    private static final String SENHA = null;  // Se a senha for nula, isso está correto, mas é recomendado sempre ter uma senha.

    public static Connection getConnection() {
        try {
            // Tente estabelecer uma conexão com o banco de dados
            return DriverManager.getConnection(URL, USUARIO, SENHA);  // Alterei para usar USUARIO e SENHA
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public static void desconectar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao desconectar do banco de dados: " + e.getMessage());
            }
        }
    }
}
