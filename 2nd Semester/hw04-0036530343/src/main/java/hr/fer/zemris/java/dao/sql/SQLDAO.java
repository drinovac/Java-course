package hr.fer.zemris.java.dao.sql;

import hr.fer.zemris.java.dao.DAO;
import hr.fer.zemris.java.model.Band;
import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *
 * @author marcupic
 */
public class SQLDAO implements DAO {

    @Override
    public List<Poll> getPolls() {
        Connection connection = SQLConnectionProvider.getConnection();

        List<Poll> polls = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POLLS");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                polls.add(new Poll(resultSet.getLong("id"),
                        resultSet.getString("title"), resultSet.getString("message")));
            }
            return polls;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Band> getBands(Long pollId) {
        Connection connection = SQLConnectionProvider.getConnection();

        List<Band> bands = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POLLOPTIONS where pollid=?");
            preparedStatement.setLong(1, pollId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                bands.add(new Band(String.valueOf(resultSet.getLong("id")), resultSet.getString("optionTitle"),
                        resultSet.getString("optionLink")));
            }
            return bands;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Poll getPoll(Long pollId) {
        Connection connection = SQLConnectionProvider.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POLLS where id=?");
            preparedStatement.setLong(1, pollId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new Poll(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("message"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public boolean addVote(Long pollOptionID) {
        Connection connection = SQLConnectionProvider.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POLLOPTIONS where id=?");
            preparedStatement.setLong(1, pollOptionID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long numberOfVotes = resultSet.getLong("votesCount");

            preparedStatement = connection.prepareStatement("UPDATE POLLOPTIONS SET votesCount=? where id=?");
            preparedStatement.setLong(1, numberOfVotes + 1);
            preparedStatement.setLong(2, pollOptionID);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean addLike(Long pollOptionID) {
        Connection connection = SQLConnectionProvider.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE POLLOPTIONS SET likesCount = likesCount + 1 where id=?");
            preparedStatement.setLong(1, pollOptionID);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addDislike(Long pollOptionID) {
        Connection connection = SQLConnectionProvider.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE POLLOPTIONS SET dislikesCount = dislikesCount + 1 where id=?");
            preparedStatement.setLong(1, pollOptionID);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Result> getResults(Long pollId) {
        Connection connection = SQLConnectionProvider.getConnection();

        List<Result> results = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POLLOPTIONS where pollid=? order by votesCount desc");
            preparedStatement.setLong(1, pollId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(new Result(String.valueOf(resultSet.getLong("id")), resultSet.getString("optionTitle"),
                        resultSet.getString("optionLink"), resultSet.getString("votesCount"), resultSet.getString("likesCount"), resultSet.getString("dislikesCount")));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Result> getWinners(Long pollId) {
        Connection connection = SQLConnectionProvider.getConnection();

        List<Result> results = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM POLLOPTIONS where pollid=? and votesCount=(SELECT max(votescount) from polloptions as polloptoins1) order by votesCount desc");
            preparedStatement.setLong(1, pollId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(new Result(String.valueOf(resultSet.getLong("id")), resultSet.getString("optionTitle"),
                        resultSet.getString("optionLink"), resultSet.getString("votesCount"),  resultSet.getString("likesCount"), resultSet.getString("dislikesCount")));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}