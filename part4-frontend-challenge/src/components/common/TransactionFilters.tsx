import React from 'react';
import { FilterState } from '../../types/transaction';
import { Input } from './Input';
import { Button } from './Button';
import './TransactionFilters.css';

interface TransactionFiltersProps {
    filters: FilterState;
    onFilterChange: (newFilters: Partial<FilterState>) => void;
}

export const TransactionFilters: React.FC<TransactionFiltersProps> = ({
    filters,
    onFilterChange,
}) => {
    const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        onFilterChange({ [name]: value });
    };

    const handleStatusChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const value = e.target.value;
        onFilterChange({ status: value === 'all' ? undefined : value });
    };

    const handleReset = () => {
        // Reset to defaults but keep pagination if needed, or reset completely
        // Here we reset to a reasonable default state
        onFilterChange({
            startDate: '2025-11-16',
            endDate: '2025-11-18',
            status: undefined,
            page: 0
        });
    };

    return (
        <div className="filters-container">
            <div className="filter-group">
                <Input
                    type="date"
                    label="Start Date"
                    name="startDate"
                    value={filters.startDate}
                    onChange={handleDateChange}
                />
                <Input
                    type="date"
                    label="End Date"
                    name="endDate"
                    value={filters.endDate}
                    onChange={handleDateChange}
                />
            </div>

            <div className="status-select-wrapper">
                <label htmlFor="status-select" className="status-label">
                    Status
                </label>
                <select
                    id="status-select"
                    className="status-select"
                    value={filters.status || 'all'}
                    onChange={handleStatusChange}
                >
                    <option value="all">All Statuses</option>
                    <option value="completed">Completed</option>
                    <option value="pending">Pending</option>
                    <option value="failed">Failed</option>
                    <option value="reversed">Reversed</option>
                </select>
            </div>

            <div className="filter-actions">
                <Button variant="outline" onClick={handleReset}>
                    Reset Filters
                </Button>
            </div>
        </div>
    );
};
