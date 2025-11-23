import React, { useState, useEffect } from "react";
import "./AddMerchant.css";
import { createMerchant } from "@/services/merchantService";
import { CreateMerchantPayload, CreateMerchantResponse } from "@/types/merchant";

interface MerchantForm {
  merchantName: string;
  merchantStatus: "Active" | "Inactive" | "Suspended";
  contactInfo: string;
  merchantCategory: string;
  merchantLocation: string;
  paymentMethod: string;
  merchantWebsite: string;
  merchantType: "Individual" | "Business" | "Franchise";
  merchantTags: string[];
}

interface FormErrors {
  merchantName?: string;
  merchantStatus?: string;
  contactInfo?: string;
  merchantCategory?: string;
  merchantLocation?: string;
  paymentMethod?: string;
  merchantWebsite?: string;
  merchantType?: string;
  merchantTags?: string;
}

const AddMerchant = () => {
  const [formData, setFormData] = useState<MerchantForm>({
    merchantName: "",
    merchantStatus: "Active",
    contactInfo: "",
    merchantCategory: "",
    merchantLocation: "",
    paymentMethod: "",
    merchantWebsite: "",
    merchantType: "Business",
    merchantTags: [],
  });

  const [errors, setErrors] = useState<FormErrors>({});
  const [apiError, setApiError] = useState("");
  const [notification, setNotification] = useState("");

  // Regex for email and website validation
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const websiteRegex = /^(https?:\/\/)?([\w-]+\.)+[\w-]{2,}(\/[\w-./?%&=]*)?$/i;

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    // Real-time validation
    setErrors((prev) => {
      const newErrors = { ...prev };

      if (name === "merchantName" && !value) newErrors.merchantName = "Merchant name required";
      else if (name === "merchantName") delete newErrors.merchantName;

      if (name === "contactInfo" && !value) newErrors.contactInfo = "Contact info required";
      else if (name === "contactInfo" && !emailRegex.test(value)) newErrors.contactInfo = "Invalid email format";
      else delete newErrors.contactInfo;

      if (name === "merchantWebsite" && !value) newErrors.merchantWebsite = "Website required";
      else if (name === "merchantWebsite" && !websiteRegex.test(value)) newErrors.merchantWebsite = "Invalid URL";
      else delete newErrors.merchantWebsite;

      if (name === "merchantType" && !value) newErrors.merchantType = "Type required";
      else delete newErrors.merchantType;

      return newErrors;
    });
  };

  const handleTagsChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const tags = e.target.value.split(",").map((t) => t.trim());
    setFormData((prev) => ({ ...prev, merchantTags: tags }));
  };

  const validate = (): FormErrors => {
    const newErrors: FormErrors = {};
    if (!formData.merchantName) newErrors.merchantName = "Merchant name required";
    if (!formData.contactInfo) newErrors.contactInfo = !emailRegex.test(formData.contactInfo) ? "Invalid email format" : undefined;
    if (!formData.merchantCategory) newErrors.merchantCategory = "Category required";
    if (!formData.merchantLocation) newErrors.merchantLocation = "Location required";
    if (!formData.paymentMethod) newErrors.paymentMethod = "Payment method required";
    if (!formData.merchantWebsite) newErrors.merchantWebsite = !websiteRegex.test(formData.merchantWebsite) ? "Invalid URL" : undefined;
    if (!formData.merchantType) newErrors.merchantType = "Type required";
    return newErrors;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setApiError("");
    setNotification("");

    const validationErrors = validate();
    setErrors(validationErrors);

    if (Object.keys(validationErrors).length === 0) {
      try {
        const payload: CreateMerchantPayload = { ...formData };
        const response: CreateMerchantResponse = await createMerchant(payload);

        if (response.response_code === "200") {
          setNotification(`Merchant "${formData.merchantName}" created successfully!`);
          setFormData({
            merchantName: "",
            merchantStatus: "Active",
            contactInfo: "",
            merchantCategory: "",
            merchantLocation: "",
            paymentMethod: "",
            merchantWebsite: "",
            merchantType: "Business",
            merchantTags: [],
          });
        } else {
          setApiError(response.response_message || "Failed to create merchant");
        }
      } catch (err: any) {
        setApiError(err.message || "An error occurred while creating merchant");
      }
    }
  };

  return (
    <div className="add-merchant-container">
      <h1>Add New Merchant</h1>

      {notification && <div className="notification">{notification}</div>}
      {apiError && <div className="error">{apiError}</div>}

      <form onSubmit={handleSubmit} className="merchant-form">
        <div className="form-row">
          <div className="form-group">
            <label>Merchant Name</label>
            <input
              type="text"
              name="merchantName"
              value={formData.merchantName}
              onChange={handleChange}
              placeholder="ABC Store"
            />
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

        <div className="form-row">
          <div className="form-group">
            <label>Email</label>
            <input
              type="text"
              name="contactInfo"
              value={formData.contactInfo}
              onChange={handleChange}
              placeholder="info@abcstore.com"
            />
            {errors.contactInfo && <span className="error">{errors.contactInfo}</span>}
          </div>

          <div className="form-group">
            <label>Category</label>
            <input
              type="text"
              name="merchantCategory"
              value={formData.merchantCategory}
              onChange={handleChange}
              placeholder="Retail"
            />
            {errors.merchantCategory && <span className="error">{errors.merchantCategory}</span>}
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Location</label>
            <input
              type="text"
              name="merchantLocation"
              value={formData.merchantLocation}
              onChange={handleChange}
              placeholder="Kathmandu, Nepal"
            />
            {errors.merchantLocation && <span className="error">{errors.merchantLocation}</span>}
          </div>

          <div className="form-group">
            <label>Payment Method</label>
            <input
              type="text"
              name="paymentMethod"
              value={formData.paymentMethod}
              onChange={handleChange}
              placeholder="Card, Cash, Online"
            />
            {errors.paymentMethod && <span className="error">{errors.paymentMethod}</span>}
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Website</label>
            <input
              type="text"
              name="merchantWebsite"
              value={formData.merchantWebsite}
              onChange={handleChange}
              placeholder="https://www.abcstore.com"
            />
            {errors.merchantWebsite && <span className="error">{errors.merchantWebsite}</span>}
          </div>

          <div className="form-group">
            <label>Type</label>
            <select name="merchantType" value={formData.merchantType} onChange={handleChange}>
              <option value="Individual">Individual</option>
              <option value="Business">Business</option>
              <option value="Franchise">Franchise</option>
            </select>
            {errors.merchantType && <span className="error">{errors.merchantType}</span>}
          </div>
        </div>

        <div className="form-group">
          <label>Tags (comma separated)</label>
          <input
            type="text"
            name="merchantTags"
            value={formData.merchantTags.join(", ")}
            onChange={handleTagsChange}
            placeholder="electronics, gadgets, accessories"
          />
        </div>

        <button type="submit" className="submit-btn">
          Add Merchant
        </button>
      </form>
    </div>
  );
};

export default AddMerchant;
