import { React, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { MatchDetailCard } from "../components/MatchDetailCard";
import { MatchSmallCard } from "../components/MatchSmallCard";
import { PieChart } from "react-minimal-pie-chart";
import { Link } from "react-router-dom";
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
    const fetchTeam = async () => {
      const response = await fetch(
        `${process.env.REACT_APP_API_ROOT_URL}/team/${teamName}`
      );
      const data = await response.json();
      setTeam(data);
    };
    fetchTeam();
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
        <MatchSmallCard key={match.id} teamName={team.teamName} match={match} />
      ))}
      <div className="more-link">
        <Link
          to={`/teams/${teamName}/matches/${process.env.REACT_APP_DATA_END_YEAR}`}
        >
          More &gt;
        </Link>
      </div>
    </div>
  );
};
