import { get,post, put } from './api';
import { MerchantResponse, CreateMerchantPayload, CreateMerchantResponse, UpdateMerchantPayload, UpdateMerchantResponse, MerchantDetailResponse } from '../types/merchant';

export const getAllMerchants = async (): Promise<MerchantResponse> => {
    return get<MerchantResponse>('/merchants/getAllMerchants');
};

export const createMerchant = async (
    payload: CreateMerchantPayload
  ): Promise<CreateMerchantResponse> => {
    return post<CreateMerchantResponse>('/merchants', payload);
  };

  export const getMerchantById = async (merchantId: string):Promise<MerchantDetailResponse> => {
    return get<MerchantDetailResponse>(`/merchants/${merchantId}`);
  };

  export const updateMerchant = async (merchantId: string, payload: UpdateMerchantPayload):Promise<UpdateMerchantResponse> => {
    return put<UpdateMerchantResponse>(`/merchants/${merchantId}`, payload);
  };

  