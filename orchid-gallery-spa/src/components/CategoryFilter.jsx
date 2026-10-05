import { Form } from 'react-bootstrap';

export default function CategoryFilter({
  categories = [],
  selectedCategory,
  onSelectCategory,
  specialOnly,
  onToggleSpecial
}) {
  return (
    <div className="d-flex align-items-center gap-3">
      <Form.Select
        value={selectedCategory}
        onChange={(e) => onSelectCategory(e.target.value)}
      >
        <option value="ALL">All Categories</option>
        {categories.map((cat) => (
          <option key={cat} value={cat}>
            {cat}
          </option>
        ))}
      </Form.Select>

      <Form.Check
        type="switch"
        id="special-switch"
        label="Special only"
        checked={specialOnly}
        onChange={(e) => onToggleSpecial(e.target.checked)}
      />
    </div>
  );
}
