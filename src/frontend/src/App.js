import "./App.scss";
import { TeamPage } from "./pages/TeamPage";
import { MatchPage } from "./pages/MatchPage";
import { HomePage } from "./pages/HomePage";
import { HashRouter as Router, Routes, Route } from "react-router-dom";

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/teams">
            <Route index element={<HomePage />} />
            <Route path=":teamName" element={<TeamPage />} />
            <Route path=":teamName/matches/:year" element={<MatchPage />} />
          </Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
