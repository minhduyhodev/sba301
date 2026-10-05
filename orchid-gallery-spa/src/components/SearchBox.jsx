// src/components/SearchBox.jsx
import { Form } from 'react-bootstrap';

export default function SearchBox({ keyword, onChange }) {
  return (
    <Form.Control
      type="text"
      placeholder="Search orchid..."
      value={keyword}
      onChange={(e) => onChange(e.target.value)}
    />
  );
}
