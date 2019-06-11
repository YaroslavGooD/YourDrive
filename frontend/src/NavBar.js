import React from "react";
import { logout } from "./Auth";

function NavBar() {
  return (
    <div className="NavBar">
      <button type="button" onClick={logout}>
        Logout
      </button>
    </div>
  );
}

export default NavBar;
