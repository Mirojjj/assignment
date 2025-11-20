import { NavLink } from 'react-router-dom';
import './Header.css';

export const Header = () => {
  return (
    <header className="header">
      <div className="header-content">
        <div className="header-logo">
          <h2>💳 Payment Platform</h2>
        </div>
        <nav className="header-nav">
          <NavLink 
            to="/" 
            className={({ isActive }) => `header-link ${isActive ? 'active' : ''}`}
          >
            Transactions
          </NavLink>
          <NavLink 
            to="/merchants" 
            className={({ isActive }) => `header-link ${isActive ? 'active' : ''}`}
          >
            Merchants
          </NavLink>
          <NavLink 
            to="/reports" 
            className={({ isActive }) => `header-link ${isActive ? 'active' : ''}`}
          >
            Reports
          </NavLink>
        </nav>
      </div>
    </header>
  );
};
