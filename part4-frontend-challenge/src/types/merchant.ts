export interface Merchant {
    merchantId: number;
    merchantName: string;
    merchantStatus: 'Active' | 'Inactive';
    contactInfo: string;
    merchantCategory: string;
    merchantLocation: string;
    merchantRating: number;
    numOrders: number;
    paymentMethod: string;
    merchantLogo: string;
    merchantWebsite: string;
    merchantType: string;
}

export interface MerchantResponse {
    response_code: string;
    response_message: string;
    data: {
        merchants: Merchant[];
    };
}

export interface CreateMerchantPayload {
    merchantName: string;
    merchantStatus: 'Active' | 'Inactive' | 'Suspended';
    contactInfo: string;
    merchantCategory: string;
    merchantLocation: string;
    paymentMethod: string;
    merchantWebsite: string;
    merchantType: string;
    merchantTags: string[];
  }

  export interface CreateMerchantResponse {
    response_code: string;
    response_message: string;
    data: {
      merchantId: string;
      message: string;
    };
  }

  export interface UpdateMerchantPayload {
    merchantName: string;
    merchantStatus: "Active" | "Inactive" | "Suspended";
    contactInfo: string;
    merchantCategory: string;
    merchantLocation: string;
    merchantRating: number;
    numOrders: number;
    paymentMethod: string;
    merchantLogo: string;
    merchantWebsite: string;
    merchantType: "Individual" | "Business" | "Franchise";
    merchantTags: string[];
  }

  export interface UpdateMerchantResponse {
    response_code: string;
    response_message: string;
    data: {
      merchantId: string;
      message: string;
    };
  }


  export interface MerchantDetailResponse {
    merchantId: number;
    merchantName: string;
    merchantStatus: 'Active' | 'Inactive' | 'Suspended';
    contactInfo: string;
    merchantCategory: string;
    merchantLocation: string;
    merchantRating: number;
    numOrders: number;
    paymentMethod: string;
    merchantLogo: string;
    merchantWebsite: string;
    merchantType: 'Individual' | 'Business' | 'Franchise';
  }
  