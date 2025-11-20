import React from 'react';
import './Table.css';

interface TableProps {
  children: React.ReactNode;
}

export const Table: React.FC<TableProps> = ({ children }) => {
  return (
    <div className="table-wrapper">
      <table className="table">{children}</table>
    </div>
  );
};
