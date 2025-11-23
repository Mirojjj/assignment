import { Routes, Route } from 'react-router-dom';
import { Header } from './components/layout/Header';
import { Transactions } from './pages/Transactions';
import { Merchants } from './pages/Merchants';
import { Reports } from './pages/Reports';
import './App.css';
import MerchantList from './components/common/MerchantList';
import AddMerchant from './components/common/AddMerchant';
import EditMerchants from './components/common/EditMerchants';
import MerchantDetail from './components/common/MerchantDetail';

/**
 * Main Application Component
 * 
 * Current status: Basic implementation with data fetching
 * 
 * Implemented:
 * 1. ✅ Data fetching using custom hook
 * 2. ✅ TransactionSummary component
 * 3. ✅ TransactionList component
 * 
 * TODO for junior developer:
 * 1. Add TransactionFilters component
 * 2. Add Pagination component
 * 3. Enhance error handling
 */

function App() {
  return (
    <div className="app">
      <Header />

      <Routes>
        <Route path="/" element={<Transactions />} />
        <Route path="/merchants" element={<Merchants />} />
        <Route path="/reports" element={<Reports />} />
        <Route path="/merchantlist" element={<MerchantList />} />
        <Route path='/addMerchant' element={<AddMerchant/>}/>
        <Route path='/editMerchant' element={<EditMerchants/>} />
        <Route path="/merchantDetails" element={<MerchantDetail/>}/>
      </Routes>
    </div>
  );
}

export default App;
