import { Outlet } from "react-router-dom";
import AppNavbar from "../components/AppNavbar";

export default function MainLayout() {
  return (
    <>
      <AppNavbar />
      <main className="container">
        <Outlet />
      </main>
      <footer className="footer">
        SBA301 MiniStore SPA • React Router Demo • Hồ Minh Duy (CE190378)
      </footer>
    </>
  );
}
