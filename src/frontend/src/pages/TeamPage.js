import { React, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { MatchDetailCard } from "../components/MatchDetailCard";
import { MatchSmallCard } from "../components/MatchSmallCard";
import { PieChart } from "react-minimal-pie-chart";
import "./TeamPage.scss";

export const TeamPage = () => {
  // This line uses the useState module in a way that a variable named "team" gets created, initially out of an empty JSON objetc (only with
  // nessecary elements to prevent compilation error).
  // Then using the "setTeam" function, the argument will be assigned to the variable.
  const [team, setTeam] = useState({ matchesList: [] });

  const { teamName } = useParams();

  useEffect(() => {
    // A function gets defined here, then will be called so it runs.
    // The main wrapper function of useEffect cannot be async (why?), so we're creating another function inside it (why?)
    const fetchMatches = async () => {
      const response = await fetch(`http://localhost:8080/team/${teamName}`);
      const data = await response.json();
      setTeam(data);
    };
    fetchMatches();
  }, [teamName]);
  // The array that we pass to the useEffect function defines the situations (dependencies) on which it should be trigerred. When empty, means
  // try the function only the first time that page is loaded.

  if (!team || !team.teamName) {
    return <h1>Team not found!</h1>;
  }

  return (
    <div className="TeamPage">
      <div className="team-name-section">
        <h1 className="team-name">{team.teamName}</h1>
      </div>
      <div className="win-loss-section">
        Wins / Losses
        <PieChart
          data={[
            {
              title: "Losses",
              value: team.totalMatches - team.totalWins,
              color: "#a34d5d",
            },
            { title: "Wins", value: team.totalWins, color: "#4da375" },
          ]}
        />
      </div>

      <div className="match-detail-section">
        <h3>Latest Matches</h3>
        <MatchDetailCard teamName={team.teamName} match={team.matchesList[0]} />
      </div>

      {team.matchesList.slice(1).map((match) => (
        <MatchSmallCard teamName={team.teamName} match={match} />
      ))}
      <div className="more-link">
        <a href="#">More ></a>
      </div>
    </div>
  );
};
