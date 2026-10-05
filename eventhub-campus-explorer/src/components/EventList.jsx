import { useState } from "react";
import { Col, Container, Form, Row } from "react-bootstrap";
import { events } from "../data/events";
import EventCard from "./EventCard";

function EventList() {
    const [searchText, setSearchText] = useState("");
    const [category, setCategory] = useState("All");
    const [featuredOnly, setFeaturedOnly] = useState(false);

    const categories = ["All", ...new Set(events.map((event) => event.category))];

    const filteredEvents = events.filter((event) => {
        const matchesText = event.title
            .toLowerCase()
            .includes(searchText.trim().toLowerCase());
        const matchesCategory = category === "All" || event.category === category;
        const matchesFeatured = !featuredOnly || event.featured;
        return matchesText && matchesCategory && matchesFeatured;
    });

    return (
        <Container id="events" className="py-5">
            <div className="d-flex flex-column flex-lg-row justify-content-between gap-3 mb-4">
                <div>
                    <h2 className="mb-1">Upcoming Events</h2>
                    <p className="text-muted mb-0">
                        Showing {filteredEvents.length} of {events.length} events
                    </p>
                </div>

                <div className="d-flex flex-column flex-md-row gap-2">
                    <Form.Control
                        type="search"
                        placeholder="Search events..."
                        value={searchText}
                        onChange={(e) => setSearchText(e.target.value)}
                    />
                    <Form.Select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                    >
                        {categories.map((item) => (
                            <option key={item} value={item}>{item}</option>
                        ))}
                    </Form.Select>
                    <Form.Check
                        type="switch"
                        id="featured-only"
                        label="Featured only"
                        checked={featuredOnly}
                        onChange={(e) => setFeaturedOnly(e.target.checked)}
                    />
                </div>
            </div>

            {filteredEvents.length === 0 ? (
                <div className="text-center py-5 border rounded bg-light">
                    <h4>No events found</h4>
                    <p className="text-muted mb-0">Try changing your search or filters.</p>
                </div>
            ) : (
                <Row className="g-4">
                    {filteredEvents.map((event) => (
                        <Col key={event.id} xs={12} md={6} lg={4}>
                            <EventCard event={event} />
                        </Col>
                    ))}
                </Row>
            )}
        </Container>
    );
}

export default EventList;
