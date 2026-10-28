import { Outlet } from "react-router-dom";
import AppNavbar from "../components/AppNavbar";

export default function MainLayout() {
  return (
    <div className="app-shell">
      <AppNavbar />
      <main>
        <Outlet />
      </main>
      <footer className="border-top py-3 text-center text-muted bg-white mt-auto">
        SBA301 • Orchid Router Demo • Hồ Minh Duy (CE190378)
      </footer>
    </div>
  );
}
