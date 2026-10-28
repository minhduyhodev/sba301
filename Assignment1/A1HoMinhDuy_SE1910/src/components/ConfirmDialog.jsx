import React from 'react';
import Modal from './Modal.jsx';

export default function ConfirmDialog({ isOpen, title = 'Confirm Delete', message, onConfirm, onCancel }) {
  return (
    <Modal isOpen={isOpen} title={title} onClose={onCancel}>
      <div className="modal-body">
        <p>{message || 'Are you sure you want to delete this record? This action cannot be undone.'}</p>
      </div>
      <div className="modal-footer">
        <button type="button" className="btn btn-secondary" onClick={onCancel}>
          Cancel
        </button>
        <button type="button" className="btn btn-danger" onClick={onConfirm}>
          Confirm Delete
        </button>
      </div>
    </Modal>
  );
}
