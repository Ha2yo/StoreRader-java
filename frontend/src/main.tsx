import React from "react";
import './app/App.css';
import ReactDOM from "react-dom/client";
import Routing from "./app/Routing";
import LocationProvider from "./contexts/LocationContext";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <LocationProvider>
      <Routing />
    </LocationProvider>
  </React.StrictMode>,
);