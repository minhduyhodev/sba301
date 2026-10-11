import AppNavbar from "./components/AppNavbar";
import HeroSection from "./components/HeroSection";
import OrchidExplorer from "./components/OrchidExplorer";
import AppFooter from "./components/AppFooter";
// Chú ý: Tại App.jsx, chỉ dùng 1 dấu chấm ./ vì thư mục context nằm ngay bên cạnh
import UserContext from "./context/UserContext";

const currentUser = {
    name: "SBA301 Student",
    role: "Learner"
};

function App() {
    return (
        <UserContext.Provider value={currentUser}>
            <div className="app-shell">
                <AppNavbar />
                <main>
                    <HeroSection />
                    <OrchidExplorer />
                </main>
                <AppFooter />
            </div>
        </UserContext.Provider>
    );
}

export default App;
