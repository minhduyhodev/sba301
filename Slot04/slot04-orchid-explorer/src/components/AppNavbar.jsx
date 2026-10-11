import { useContext } from "react";
import { Container, Navbar } from "react-bootstrap";

import UserContext from "../context/UserContext";

function AppNavbar() {
  const currentUser = useContext(UserContext);

  return (
    <Navbar bg="dark" data-bs-theme="dark" expand="md">
      <Container>
        <Navbar.Brand>Interactive Orchid Explorer</Navbar.Brand>
        <Navbar.Text className="ms-auto text-info">
          {currentUser ? `Hello, ${currentUser.name}` : "Guest"}
        </Navbar.Text>
      </Container>
    </Navbar>
  );
}

export default AppNavbar;
