import { React } from "react";
import "./TeamTile.scss";

export const TeamTile = ({ teamName }) => {
  return (
    <div className="TeamTile">
      <h2>{teamName}</h2>
    </div>
  );
};
