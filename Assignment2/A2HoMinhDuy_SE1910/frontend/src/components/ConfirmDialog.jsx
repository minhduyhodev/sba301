import React from 'react';
import { Modal } from './Modal';

export const ConfirmDialog = ({ isOpen, onClose, onConfirm, title = 'Confirm Action', message, confirmText = 'Delete', isDanger = true }) => {
  return (
    <Modal isOpen={isOpen} onClose={onClose} title={title} maxWidth="450px">
      <div className="confirm-dialog-content">
        <p className="confirm-message">{message}</p>
        <div className="modal-actions">
          <button type="button" className="btn btn-secondary" onClick={onClose}>
            Cancel
          </button>
          <button 
            type="button" 
            className={`btn ${isDanger ? 'btn-danger' : 'btn-primary'}`} 
            onClick={() => {
              onConfirm();
              onClose();
            }}
          >
            {confirmText}
          </button>
        </div>
      </div>
    </Modal>
  );
};
