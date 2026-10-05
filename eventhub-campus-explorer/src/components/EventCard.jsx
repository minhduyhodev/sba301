import { useState } from "react";
import { Badge, Button, Card, Modal } from "react-bootstrap";

function EventCard({ event }) {
    const [showDetail, setShowDetail] = useState(false);

    return (
        <>
            <Card className="h-100 shadow-sm">
                <Card.Img className="event-image" variant="top" src={event.image} alt={event.title} />
                <Card.Body className="d-flex flex-column">
                    <div className="d-flex justify-content-between align-items-start gap-2">
                        <Card.Title>{event.title}</Card.Title>
                        {event.featured && <Badge bg="warning" text="dark">Featured</Badge>}
                    </div>
                    <Card.Text className="mb-1"><strong>Category:</strong> {event.category}</Card.Text>
                    <Card.Text className="mb-1"><strong>Date:</strong> {event.date}</Card.Text>
                    <Card.Text className="mb-3"><strong>Location:</strong> {event.location}</Card.Text>

                    <Button className="mt-auto" onClick={() => setShowDetail(true)}>
                        View Detail
                    </Button>
                </Card.Body>
            </Card>

            <Modal show={showDetail} onHide={() => setShowDetail(false)} centered>
                <Modal.Header closeButton>
                    <Modal.Title>{event.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p><strong>Category:</strong> {event.category}</p>
                    <p><strong>Date:</strong> {event.date}</p>
                    <p><strong>Location:</strong> {event.location}</p>
                    <p><strong>Seats:</strong> {event.seats}</p>
                    <p className="mb-0">{event.description}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDetail(false)}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default EventCard;
