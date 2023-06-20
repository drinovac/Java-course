package hr.fer.zemris.java;

import java.beans.PropertyVetoException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import hr.fer.zemris.java.dao.sql.SQLConnectionProvider;
import hr.fer.zemris.java.model.Result;

@WebListener
public class Inicijalizacija implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        Map<String, String> properties = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(sce.getServletContext().getResourceAsStream("/WEB-INF/dbsettings.properties")));
            String line;
            while((line = br.readLine()) != null) {
                String[] elements = line.split("=");
                properties.put(elements[0], elements[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while reading properties");
        }

        if(!properties.containsKey("host") || !properties.containsKey("port") ||
                !properties.containsKey("name") || !properties.containsKey("user") || !properties.containsKey("password")) {
            throw new RuntimeException("One or more properties in dbsettings.properties are missing");
        }

        String connectionURL = "jdbc:derby://" + properties.get("host") + ":" + properties.get("port") + "/"
                                    + properties.get("name") + ";user=" + properties.get("user") +
                                    ";password=" + properties.get("password");


        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
        } catch (PropertyVetoException e1) {
            throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
        }
        cpds.setJdbcUrl(connectionURL);

        Connection con;
        try {
            con = cpds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SQLConnectionProvider.setConnection(con);

        Map<String, String> tablesCommands = new HashMap<>();
        tablesCommands.put("POlLS",
                """ 
                CREATE TABLE Polls 
                (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                title VARCHAR(150) NOT NULL, 
                message CLOB(2048) NOT NULL)""");
        tablesCommands.put("POOLOPTIONS",
                """
                CREATE TABLE PollOptions
                 (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                 optionTitle VARCHAR(100) NOT NULL,
                 optionLink VARCHAR(150) NOT NULL,
                 pollID BIGINT,
                 votesCount BIGINT,
                 likesCount BIGINT,
                 dislikesCount BIGINT,   
                 FOREIGN KEY (pollID) REFERENCES Polls(id)
                )""");

        for (Map.Entry<String, String> entry: tablesCommands.entrySet()) {
            createTable(con, entry.getValue());
        }


        if(!votingPollsExists(con)) {
            try {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT into POLLS (TITLE, MESSAGE)" +
                        "values (?, ?)");
                preparedStatement.setString(1, "Glasanje za omiljeni bend:");
                preparedStatement.setString(2, "Od sljedećih bendova, koji Vam je bend najdraži? " +
                                "Kliknite na link kako biste glasali!");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if(!pollOptionsExists(con)) {
            List<Result> bandList = null;
            try {
                bandList = getResults(sce);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for(Result result: bandList) {
                System.out.println(result.getNumberOfLikes() + " " + result.getNumberOfDislikes());
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = con.prepareStatement("INSERT INTO POLLOPTIONS (OPTIONTITLE, OPTIONLINK, POLLID, VOTESCOUNT, LIKESCOUNT, DISLIKESCOUNT)" +
                            " VALUES (?, ?, ?, ?, ?, ?)");
                    preparedStatement.setString(1, result.getName());
                    preparedStatement.setString(2, result.getSongLink());
                    preparedStatement.setLong(3, 1);
                    preparedStatement.setLong(4, Long.parseLong(result.getNumberOfVotes()));
                    preparedStatement.setLong(5, Long.parseLong(result.getNumberOfLikes()));
                    preparedStatement.setLong(6, Long.parseLong(result.getNumberOfDislikes()));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
    }

    private boolean pollOptionsExists(Connection con) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM POLLOPTIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Result> getResults(ServletContextEvent sce) throws IOException {
        String fileNameRezultati = sce.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        String fileNameDefinicija = sce.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");


        Map<String, String> rezultati = new HashMap<>();
        List<String> rezLines = Files.readAllLines(Paths.get(fileNameRezultati));

        for (String line: rezLines) {
            rezultati.put(line.split("\\t")[0], line.split("\\t")[1]);
        }

        List<String> lines = Files.readAllLines(Paths.get(fileNameDefinicija));

        List<Result> results = new ArrayList<>();
        for (String line: lines) {
            String[] parts = line.split("\\t");

            String numberOfVotes = rezultati.get(parts[0]).split(" ")[0];
            String numberOfLikes = rezultati.get(parts[0]).split(" ")[1];
            String numberOfDislikes = rezultati.get(parts[0]).split(" ")[2];

            results.add(new Result(parts[0], parts[1], parts[2], numberOfVotes, numberOfLikes, numberOfDislikes));
        }
        results.sort((o1, o2) -> o2.getNumberOfVotes().compareTo(o1.getNumberOfVotes()));
        return results;
    }

    private boolean votingPollsExists(Connection con) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM POLLS");
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTable(Connection con, String command) {

        PreparedStatement preparedStatement;

        try {
            preparedStatement = con.prepareStatement(command);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if(!e.getSQLState().equals("X0Y32")) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
        if(cpds!=null) {
            try {
                DataSources.destroy(cpds);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}