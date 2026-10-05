import { useState } from "react";
import { Badge, Button, Card, Modal } from "react-bootstrap";

function OrchidCard({ orchid }) {

    const [show, setShow] = useState(false);

    return (
        <>
            <Card className="h-100 shadow-sm orchid-card">

                <Card.Img
                    variant="top"
                    src={orchid.image}
                    alt={orchid.orchidName}
                    className="orchid-card-image"
                />

                <Card.Body className="d-flex flex-column">

                    <Card.Title>
                        {orchid.orchidName}
                    </Card.Title>

                    <Card.Text className="mb-2">
                        Category: {orchid.category}
                    </Card.Text>

                    <div className="mb-3">
                        {orchid.isSpecial && (
                            <Badge bg="warning" text="dark">
                                Special
                            </Badge>
                        )}
                    </div>

                    <Button
                        size="sm"
                        className="mt-auto w-100"
                        onClick={() => setShow(true)}
                    >
                        View Detail
                    </Button>

                </Card.Body>
            </Card>


            <Modal
                show={show}
                onHide={() => setShow(false)}
                centered
            >

                <Modal.Header closeButton>
                    <Modal.Title>
                        {orchid.orchidName}
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>

                    <img
                        src={orchid.image}
                        alt={orchid.orchidName}
                        className="img-fluid rounded mb-3 orchid-modal-image"
                    />

                    <p><strong>Category:</strong> {orchid.category}</p>
                    {orchid.origin && <p><strong>Origin:</strong> {orchid.origin}</p>}
                    {orchid.color && <p><strong>Color:</strong> {orchid.color}</p>}
                    {orchid.rating !== undefined && (
                        <p><strong>Rating:</strong> {orchid.rating}</p>
                    )}

                </Modal.Body>

                <Modal.Footer>

                    <Button
                        variant="secondary"
                        onClick={() => setShow(false)}
                    >
                        Close
                    </Button>

                </Modal.Footer>

            </Modal>
        </>
    );
}

export default OrchidCard;
