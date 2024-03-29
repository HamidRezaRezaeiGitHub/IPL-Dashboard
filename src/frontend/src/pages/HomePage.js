import { React } from "react";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import "./HomePage.scss";
import { TeamTile } from "../components/TeamTile";

export const HomePage = () => {
  const [teams, setTeams] = useState([]);

  useEffect(() => {
    const fetchTeams = async () => {
      const response = await fetch(
        `${process.env.REACT_APP_API_ROOT_URL}/team`
      );
      const data = await response.json();
      setTeams(data);
    };
    fetchTeams();
  }, []);

  return (
    <div className="HomePage">
      <div className="header-section">
        <h1 className="app-name">IPL Dashboard</h1>
      </div>
      <div className="team-grid">
        {teams.map((team) => (
          <Link to={`/teams/${team.teamName}`} key={team.id}>
            <TeamTile teamName={team.teamName} />
          </Link>
        ))}
      </div>
    </div>
  );
};
