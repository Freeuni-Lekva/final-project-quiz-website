package com.project.website.DAOs;

import com.project.website.Objects.Challenge;

import java.util.List;

public interface ChallengeDAO {
    String ATTR_NAME = "ChallengeDAO";

   boolean insertChallenge(Challenge challenge);

   boolean deleteChallenge(int id);

   List<Challenge> getChallengesTo(int toUserID);

}
