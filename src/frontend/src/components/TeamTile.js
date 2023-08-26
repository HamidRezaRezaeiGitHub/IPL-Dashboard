import { React } from "react";
import { Link } from "react-router-dom";
import "./TeamTile.scss";

export const TeamTile = ({ teamName }) => {
  return (
    <div className="TeamTile">
      <h2>{teamName}</h2>
    </div>
  );
};
