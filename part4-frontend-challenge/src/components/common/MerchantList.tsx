import React, { useEffect, useState, useMemo } from "react";
import { getAllMerchants } from "@/services/merchantService";
import { Merchant } from "@/types/merchant";
import "./MerchantList.css";

function MerchantList() {
  const [merchants, setMerchants] = useState<Merchant[]>([]);
  const [loading, setLoading] = useState(true);

  // Filters
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("");

  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 100;

  useEffect(() => {
    const fetchMerchants = async () => {
      try {
        const response = await getAllMerchants();
        setMerchants(response.data.merchants);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchMerchants();
  }, []);

  // Reset page to 1 whenever filters/search change
  useEffect(() => {
    setCurrentPage(1);
  }, [search, statusFilter, categoryFilter]);

  const filteredMerchants = useMemo(() => {
    const normalizedSearch = search.toLowerCase().replace(/\s+/g, "");
    return merchants
      .filter((m) => {
        const normalizedName = m.merchantName.toLowerCase().replace(/\s+/g, "");
        return normalizedName.includes(normalizedSearch);
      })
      .filter((m) => (statusFilter ? m.merchantStatus === statusFilter : true))
      .filter((m) =>
        categoryFilter ? m.merchantCategory === categoryFilter : true
      );
  }, [merchants, search, statusFilter, categoryFilter]);

  const totalPages = Math.ceil(filteredMerchants.length / pageSize);

  // Ensure currentPage is within bounds if totalPages changes
  useEffect(() => {
    if (currentPage > totalPages) setCurrentPage(totalPages || 1);
  }, [totalPages, currentPage]);

  const paginatedData = filteredMerchants.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  if (loading)
    return <div className="loading">Loading merchants...</div>;

  const getPageNumbers = () => {
    const pages: (number | string)[] = [];
    if (totalPages <= 7) {
      for (let i = 1; i <= totalPages; i++) pages.push(i);
    } else {
      pages.push(1);
      if (currentPage > 4) pages.push("...");
      for (
        let i = Math.max(2, currentPage - 2);
        i <= Math.min(totalPages - 1, currentPage + 2);
        i++
      )
        pages.push(i);
      if (currentPage < totalPages - 3) pages.push("...");
      pages.push(totalPages);
    }
    return pages;
  };

  return (
    <div className="merchant-list-container">
      <h1 className="header">Merchant List</h1>

      {/* Filters */}
      <div className="filters">
        <input
          type="text"
          placeholder="Search merchants..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="search-input"
        />
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          className="filter-select"
        >
          <option value="">All Status</option>
          <option value="Active">Active</option>
          <option value="Inactive">Inactive</option>
          <option value="Suspended">Suspended</option>
        </select>
        <select
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
          className="filter-select"
        >
          <option value="">All Categories</option>
          {Array.from(new Set(merchants.map((m) => m.merchantCategory))).map(
            (cat) => (
              <option key={cat} value={cat}>
                {cat}
              </option>
            )
          )}
        </select>
      </div>

      {/* Table */}
      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Merchant</th>
              <th>Category</th>
              <th>Status</th>
              <th>Rating</th>
              <th>Orders</th>
            </tr>
          </thead>
          <tbody>
            {paginatedData.length === 0 ? (
              <tr>
                <td colSpan={5} className="no-data">
                  No merchants found.
                </td>
              </tr>
            ) : (
              paginatedData.map((m) => (
                <tr key={m.merchantId}>
                  <td className="merchant-info">
                    <img src={m.merchantLogo} alt="" />
                    <div>
                      <div className="merchant-name">{m.merchantName}</div>
                      <a
                        href={m.merchantWebsite}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="merchant-website"
                      >
                        Visit Website
                      </a>
                    </div>
                  </td>
                  <td>{m.merchantCategory}</td>
                  <td>
                    <span className={`status ${m.merchantStatus.toLowerCase()}`}>
                      {m.merchantStatus}
                    </span>
                  </td>
                  <td>{m.merchantRating} ⭐</td>
                  <td>{m.numOrders}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {totalPages > 1 && (
        <div className="pagination">
          <button
            disabled={currentPage === 1}
            onClick={() => setCurrentPage((prev) => prev - 1)}
          >
            Prev
          </button>

          {getPageNumbers().map((page, index) =>
            page === "..." ? (
              <span key={index} className="ellipsis">
                ...
              </span>
            ) : (
              <button
                key={index}
                className={currentPage === page ? "active" : ""}
                onClick={() => setCurrentPage(Number(page))}
              >
                {page}
              </button>
            )
          )}

          <button
            disabled={currentPage === totalPages}
            onClick={() => setCurrentPage((prev) => prev + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}

export default MerchantList;
