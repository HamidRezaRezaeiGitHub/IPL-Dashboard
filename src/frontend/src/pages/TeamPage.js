import { React, useEffect, useState } from "react";
import { MatchDetailCard } from "../components/MatchDetailCard";
import { MatchSmallCard } from "../components/MatchSmallCard";

export const TeamPage = () => {
  // This line uses the useState module in a way that a variable named "team" gets created, initially out of an empty JSON objetc (only with
  // nessecary elements to prevent compilation error).
  // Then using the "setTeam" function, the argument will be assigned to the variable.
  const [team, setTeam] = useState({ matchesList: [] });

  useEffect(() => {
    // A function gets defined here, then will be called so it runs.
    // The main wrapper function of useEffect cannot be async (why?), so we're creating another function inside it (why?)
    const fetchMatches = async () => {
      const response = await fetch(
        "http://localhost:8080/team/Chennai%20Super%20Kings"
      );
      const data = await response.json();
      setTeam(data);
    };
    fetchMatches();
  }, []);
  // The array that we pass to the useEffect function defines the situations (dependencies) on which it should be trigerred. When empty, means
  // try the function only the first time that page is loaded.

  return (
    <div className="TeamPage">
      <h1>{team.teamName}</h1>

      <MatchDetailCard match={team.matchesList[0]} />

      {team.matchesList.slice(1).map((match) => (
        <MatchSmallCard match={match} />
      ))}
    </div>
  );
};
