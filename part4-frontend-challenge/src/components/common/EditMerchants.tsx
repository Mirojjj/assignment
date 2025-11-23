import React, { useState, useEffect } from "react";
import { getMerchantById, updateMerchant } from "@/services/merchantService";
import {
  UpdateMerchantPayload,
  UpdateMerchantResponse,
  MerchantDetailResponse
} from "@/types/merchant";
import "./EditMerchant.css";

const EditMerchants = () => {
  const [merchantId, setMerchantId] = useState<string>("3"); // default ID 3

  const [formData, setFormData] = useState<UpdateMerchantPayload>({
    merchantName: "",
    merchantStatus: "Active",
    contactInfo: "",
    merchantCategory: "",
    merchantLocation: "",
    merchantRating: 0,
    numOrders: 0,
    paymentMethod: "",
    merchantLogo: "",
    merchantWebsite: "",
    merchantType: "Business",
    merchantTags: [],
  });

  const [errors, setErrors] = useState<Partial<Record<keyof UpdateMerchantPayload, string>>>({});
  const [notification, setNotification] = useState("");
  const [apiError, setApiError] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);

  // Fetch merchant details whenever merchantId changes
  useEffect(() => {
    if (!merchantId) return;

    getMerchantById(merchantId)
      .then((res: { response_code: string; response_message: string; data: MerchantDetailResponse }) => {
        if (res.response_code === "200" && res.data) {
          const data = res.data;
          setFormData({
            merchantName: data.merchantName,
            merchantStatus: data.merchantStatus,
            contactInfo: data.contactInfo,
            merchantCategory: data.merchantCategory,
            merchantLocation: data.merchantLocation,
            merchantRating: data.merchantRating,
            numOrders: data.numOrders,
            paymentMethod: data.paymentMethod,
            merchantLogo: data.merchantLogo,
            merchantWebsite: data.merchantWebsite,
            merchantType: data.merchantType,
            merchantTags: data.merchantTags || [],
          });
          setApiError("");
        } else {
          setApiError(res.response_message || "Failed to fetch merchant details");
        }
      })
      .catch(() => setApiError("Failed to fetch merchant details"));
  }, [merchantId]);

  const handleMerchantIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMerchantId(e.target.value);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleTagsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const tags = e.target.value.split(",").map((t) => t.trim());
    setFormData((prev) => ({ ...prev, merchantTags: tags }));
  };

  const validate = () => {
    const newErrors: Partial<Record<keyof UpdateMerchantPayload, string>> = {};
    if (!formData.merchantName) newErrors.merchantName = "Merchant name required";
    if (!formData.contactInfo) newErrors.contactInfo = "Contact info required";
    if (!formData.merchantCategory) newErrors.merchantCategory = "Category required";
    if (!formData.merchantLocation) newErrors.merchantLocation = "Location required";
    if (!formData.paymentMethod) newErrors.paymentMethod = "Payment method required";
    if (!formData.merchantWebsite) newErrors.merchantWebsite = "Website required";
    if (!formData.merchantType) newErrors.merchantType = "Type required";
    return newErrors;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setApiError("");
    setNotification("");

    const validationErrors = validate();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length === 0) {
      setShowConfirm(true); // Show confirmation modal
    }
  };

  const confirmUpdate = async () => {
    setShowConfirm(false);
    if (!merchantId) return;

    try {
      const response: UpdateMerchantResponse = await updateMerchant(merchantId, formData);
      if (response.response_code === "200") {
        setNotification(`Merchant "${formData.merchantName}" updated successfully!`);
      } else {
        setApiError(response.response_message || "Failed to update merchant");
      }
    } catch (err: any) {
      setApiError(err.message || "An error occurred while updating merchant");
    }
  };

  return (
    <div className="edit-merchant-container">
      <h1>Edit Merchant</h1>

      <div className="merchant-id-input">
        <label>Merchant ID: </label>
        <input type="text" value={merchantId} onChange={handleMerchantIdChange} />
      </div>

      <br />

      {notification && <div className="notification">{notification}</div>}
      {apiError && <div className="error">{apiError}</div>}

      <form onSubmit={handleSubmit} className="merchant-form">
        {/* Row 1 */}
        <div className="form-row">
          <div className="form-group">
            <label>Merchant Name</label>
            <input type="text" name="merchantName" value={formData.merchantName} onChange={handleChange} />
            {errors.merchantName && <span className="error">{errors.merchantName}</span>}
          </div>

          <div className="form-group">
            <label>Status</label>
            <select name="merchantStatus" value={formData.merchantStatus} onChange={handleChange}>
              <option value="Active">Active</option>
              <option value="Inactive">Inactive</option>
              <option value="Suspended">Suspended</option>
            </select>
          </div>
        </div>

        {/* Row 2 */}
        <div className="form-row">
          <div className="form-group">
            <label>Contact Info</label>
            <input type="text" name="contactInfo" value={formData.contactInfo} onChange={handleChange} />
            {errors.contactInfo && <span className="error">{errors.contactInfo}</span>}
          </div>

          <div className="form-group">
            <label>Category</label>
            <input type="text" name="merchantCategory" value={formData.merchantCategory} onChange={handleChange} />
            {errors.merchantCategory && <span className="error">{errors.merchantCategory}</span>}
          </div>
        </div>

        {/* Row 3 */}
        <div className="form-row">
          <div className="form-group">
            <label>Location</label>
            <input type="text" name="merchantLocation" value={formData.merchantLocation} onChange={handleChange} />
            {errors.merchantLocation && <span className="error">{errors.merchantLocation}</span>}
          </div>

          <div className="form-group">
            <label>Rating</label>
            <input type="number" step="0.01" name="merchantRating" value={formData.merchantRating} onChange={handleChange} />
          </div>
        </div>

        {/* Row 4 */}
        <div className="form-row">
          <div className="form-group">
            <label>Orders</label>
            <input type="number" name="numOrders" value={formData.numOrders} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Payment Method</label>
            <input type="text" name="paymentMethod" value={formData.paymentMethod} onChange={handleChange} />
            {errors.paymentMethod && <span className="error">{errors.paymentMethod}</span>}
          </div>
        </div>

        {/* Row 5 */}
        <div className="form-row">
          <div className="form-group">
            <label>Logo URL</label>
            <input type="text" name="merchantLogo" value={formData.merchantLogo} onChange={handleChange} />
          </div>

          <div className="form-group">
            <label>Website</label>
            <input type="text" name="merchantWebsite" value={formData.merchantWebsite} onChange={handleChange} />
            {errors.merchantWebsite && <span className="error">{errors.merchantWebsite}</span>}
          </div>
        </div>

        {/* Row 6 */}
        <div className="form-row">
          <div className="form-group">
            <label>Type</label>
            <select name="merchantType" value={formData.merchantType} onChange={handleChange}>
              <option value="Individual">Individual</option>
              <option value="Business">Business</option>
              <option value="Franchise">Franchise</option>
            </select>
          </div>

          <div className="form-group">
            <label>Tags (comma separated)</label>
            <input type="text" value={formData.merchantTags.join(", ")} onChange={handleTagsChange} />
          </div>
        </div>

        <button type="submit" className="submit-btn">Update Merchant</button>
      </form>

      {/* Confirmation Modal */}
      {showConfirm && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>Confirm Update</h2>
            <p>Are you sure you want to update merchant "{formData.merchantName}"?</p>
            <div className="modal-buttons">
              <button className="confirm-btn" onClick={confirmUpdate}>Confirm</button>
              <button className="cancel-btn" onClick={() => setShowConfirm(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default EditMerchants;
