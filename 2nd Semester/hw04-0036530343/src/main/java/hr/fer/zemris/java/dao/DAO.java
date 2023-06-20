package hr.fer.zemris.java.dao;

import hr.fer.zemris.java.model.Band;
import hr.fer.zemris.java.model.Poll;
import hr.fer.zemris.java.model.Result;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 *
 * @author marcupic
 *
 */
public interface DAO {

    List<Poll> getPolls();

    List<Band> getBands(Long pollId);

    Poll getPoll(Long pollId);

    boolean addVote(Long pollOptionID);

    boolean addLike(Long pollOptionID);

    boolean addDislike(Long pollOptionID);

    List<Result> getResults(Long pollId);

    List<Result> getWinners(Long pollId);
}