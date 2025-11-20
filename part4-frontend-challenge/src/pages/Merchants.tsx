import './Merchants.css';

/**
 * Merchants Page Component
 * 
 * This page is a placeholder for future merchant management functionality.
 * 
 * ASSIGNMENT: Implement the following features:
 * 
 * 1. **Merchant List View**
 *    - Display all merchants in a table or card layout
 *    - Show merchant ID, name, status, and contact information
 *    - Add search/filter functionality
 * 
 * 2. **Merchant Details**
 *    - Click on a merchant to view detailed information
 *    - Show merchant settings, payment methods, and statistics
 * 
 * 3. **Add/Edit Merchant**
 *    - Form to create new merchants
 *    - Edit existing merchant information
 *    - Validate merchant data before submission
 * 
 * 4. **API Integration**
 *    - Create merchant service API calls
 *    - GET /api/v1/merchants - List all merchants
 *    - GET /api/v1/merchants/{id} - Get merchant details
 *    - POST /api/v1/merchants - Create new merchant
 *    - PUT /api/v1/merchants/{id} - Update merchant
 */
export const Merchants = () => {
  return (
    <main className="container">
      <div className="placeholder-page">
        <div className="placeholder-header">
          <h1>🏢 Merchants Management</h1>
          <p className="subtitle">Manage merchant accounts and settings</p>
          <div style={{ marginTop: '20px', padding: '15px', backgroundColor: '#f0f4ff', borderRadius: '8px', border: '2px solid #667eea' }}>
            <h3 style={{ margin: '0 0 10px 0', color: '#667eea' }}>🎯 Target: 100 Points</h3>
            <p style={{ margin: 0, fontSize: '14px' }}>Complete any combination of tasks below to reach 100 points. You don't need to complete all tasks.</p>
          </div>
        </div>

        <div className="feature-grid">
          <div className="feature-card">
            <div className="feature-icon">📋</div>
            <h3>Merchant List <span style={{ backgroundColor: '#667eea', color: 'white', padding: '2px 8px', borderRadius: '8px', fontSize: '12px', marginLeft: '8px' }}>30 pts</span></h3>
            <p>View and search all registered merchants</p>
            <ul className="feature-list">
              <li>Display merchant information in table format (10 pts)</li>
              <li>Search and filter by name, ID, or status (5 pts)</li>
              <li>Sort by various criteria (5 pts)</li>
              <li>Pagination for large datasets (5 pts)</li>
              <li>Loading states and error handling (5 pts)</li>
            </ul>
          </div>

          <div className="feature-card">
            <div className="feature-icon">➕</div>
            <h3>Add New Merchant <span style={{ backgroundColor: '#667eea', color: 'white', padding: '2px 8px', borderRadius: '8px', fontSize: '12px', marginLeft: '8px' }}>25 pts</span></h3>
            <p>Register new merchants to the platform</p>
            <ul className="feature-list">
              <li>Form with merchant details (name, email, phone) (8 pts)</li>
              <li>Business information and registration (5 pts)</li>
              <li>Submit to POST /api/v1/merchants (5 pts)</li>
              <li>Input validation and error handling (4 pts)</li>
              <li>Success notifications and form reset (3 pts)</li>
            </ul>
          </div>

          <div className="feature-card">
            <div className="feature-icon">✏️</div>
            <h3>Edit Merchant Details <span style={{ backgroundColor: '#667eea', color: 'white', padding: '2px 8px', borderRadius: '8px', fontSize: '12px', marginLeft: '8px' }}>20 pts</span></h3>
            <p>Update merchant information and settings</p>
            <ul className="feature-list">
              <li>Pre-populate form with existing data (5 pts)</li>
              <li>Update contact details and address (5 pts)</li>
              <li>Manage merchant status (active/inactive) (5 pts)</li>
              <li>Submit to PUT /api/v1/merchants/:id (3 pts)</li>
              <li>Confirmation dialogs (2 pts)</li>
            </ul>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🔍</div>
            <h3>Merchant Details View <span style={{ backgroundColor: '#667eea', color: 'white', padding: '2px 8px', borderRadius: '8px', fontSize: '12px', marginLeft: '8px' }}>25 pts</span></h3>
            <p>View comprehensive merchant information</p>
            <ul className="feature-list">
              <li>Display complete merchant profile (5 pts)</li>
              <li>Show transaction statistics (8 pts)</li>
              <li>List recent transactions (7 pts)</li>
              <li>View merchant activity timeline (3 pts)</li>
              <li>Export transaction history (2 pts)</li>
            </ul>
          </div>
        </div>

        <div className="technical-notes">
          <h2>📝 Technical Implementation Notes</h2>
          <div className="notes-content">
            <div className="note-section">
              <h3>API Endpoints to Implement:</h3>
              <ul>
                <li><code>GET /api/v1/merchants</code> - List all merchants (with pagination)</li>
                <li><code>GET /api/v1/merchants/{'{id}'}</code> - Get merchant details</li>
                <li><code>POST /api/v1/merchants</code> - Create new merchant</li>
                <li><code>PUT /api/v1/merchants/{'{id}'}</code> - Update merchant</li>
                <li><code>DELETE /api/v1/merchants/{'{id}'}</code> - Deactivate merchant</li>
              </ul>
            </div>

            <div className="note-section">
              <h3>Components to Create:</h3>
              <ul>
                <li><code>MerchantList.tsx</code> - Table/list view of merchants</li>
                <li><code>MerchantCard.tsx</code> - Individual merchant card component</li>
                <li><code>MerchantForm.tsx</code> - Form for add/edit merchant</li>
                <li><code>MerchantDetails.tsx</code> - Detailed merchant view</li>
                <li><code>MerchantFilters.tsx</code> - Search and filter controls</li>
              </ul>
            </div>

            <div className="note-section">
              <h3>State Management:</h3>
              <ul>
                <li>Create custom hook: <code>useMerchants()</code></li>
                <li>Manage merchant list state and loading states</li>
                <li>Handle form validation and submission</li>
                <li>Implement error handling and success notifications</li>
              </ul>
            </div>
          </div>
        </div>

        <div className="getting-started">
          <h2>🚀 Getting Started</h2>
          <p>To implement this feature:</p>
          <ol>
            <li>Create the merchant type definitions in <code>src/types/merchant.ts</code></li>
            <li>Implement the merchant API service in <code>src/services/merchantService.ts</code></li>
            <li>Create the custom hook <code>src/hooks/useMerchants.ts</code></li>
            <li>Build the UI components in <code>src/components/merchants/</code></li>
            <li>Update this page to use the new components</li>
          </ol>
        </div>
      </div>
    </main>
  );
};
