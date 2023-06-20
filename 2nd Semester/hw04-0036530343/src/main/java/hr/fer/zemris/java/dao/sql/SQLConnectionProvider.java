package hr.fer.zemris.java.dao.sql;

import java.sql.Connection;

public class SQLConnectionProvider {

    private static ThreadLocal<Connection> connections = new ThreadLocal<>();

    /**
     * Postavi vezu za trenutnu dretvu (ili obriši zapis iz mape ako je argument <code>null</code>).
     *
     * @param con veza prema bazi
     */
    public static void setConnection(Connection con) {
        if(con==null) {
            connections.remove();
        } else {
            connections.set(con);
        }
    }

    /**
     * Dohvati vezu koju trenutna dretva (pozivatelj) smije koristiti.
     *
     * @return vezu prema bazi podataka
     */
    public static Connection getConnection() {
        return connections.get();
    }

}