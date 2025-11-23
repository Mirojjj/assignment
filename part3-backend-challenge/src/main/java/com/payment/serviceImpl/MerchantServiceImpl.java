package com.payment.serviceImpl;

import com.payment.dto.merchantDto.MerchantDto;
import com.payment.entity.Merchant;
import com.payment.payloads.CreateMerchantPayload;
import com.payment.payloads.UpdateMerchantPayloadWithMerchantId;
import com.payment.repository.MerchantRepository;
import com.payment.responses.*;
import com.payment.services.MerchantService;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class MerchantServiceImpl implements MerchantService {

    private final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);
    private final MerchantRepository merchantRepository;

    @Inject
    public MerchantServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    public MerchantListResponse getAllMerchants() {
        var result = this.merchantRepository.findAll();

        logger.info("resulttt" + result);

        List<MerchantDto> merchantDtos = result.stream()
                .map(tx -> new MerchantDto(
                        tx.getMerchantId(),
                        tx.getMerchantName(),
                        tx.getMerchantStatus(),
                        tx.getContactInfo(),
                        tx.getMerchantCategory(),
                        tx.getMerchantLocation(),
                        tx.getMerchantRating(),
                        tx.getNumOrders(),
                        tx.getPaymentMethod(),
                        tx.getMerchantLogo(),
                        tx.getMerchantWebsite(),
                        tx.getMerchantType(),
                        tx.getMerchantTags()
                )).toList();

        return new MerchantListResponse(merchantDtos);
    }

    @Override
    public MerchantDetailsResponse getMerchantDetails(String merchantId) {
        logger.info("Merchant details id" + merchantId);
        var result = this.merchantRepository.findByMerchantId(Integer.parseInt(merchantId));
        logger.info("merchant detail result" + result);
        return MerchantDetailsResponseBuilder.builder()
                .merchantId(result.getMerchantId())
                .merchantName(result.getMerchantName())
                .merchantStatus(result.getMerchantStatus())
                .contactInfo(result.getContactInfo())
                .merchantLocation(result.getMerchantLocation())
                .merchantRating(result.getMerchantRating())
                .merchantWebsite(result.getMerchantWebsite())
                .numOrders(result.getNumOrders())
                .merchantLogo(result.getMerchantLogo())
                .merchantTags(result.getMerchantTags())
                .merchantCategory(result.getMerchantCategory())
                .merchantType(result.getMerchantType())
                .build();
    }

    @Override
    public CreateMerchantResponse createNewMerchant(CreateMerchantPayload createMerchantPayload) {
        Merchant entity =  toMerchantEntity(createMerchantPayload);

        var result = this.merchantRepository.save(entity);

        logger.info("New merchant created Successfully");

        return CreateMerchantResponseBuilder.builder()
                .merchantId(result.getMerchantId().toString())
                .message("New merchant created Successfully")
                .build();
    }

    private Merchant toMerchantEntity(CreateMerchantPayload request) {
     Merchant merchant = new Merchant();
     merchant.setMerchantId(java.util.concurrent.ThreadLocalRandom.current().nextInt());
     merchant.setMerchantName(request.merchantName());
     merchant.setMerchantStatus(request.merchantStatus());
     merchant.setContactInfo(request.contactInfo());
     merchant.setMerchantCategory(request.merchantCategory());
     merchant.setMerchantLocation(request.merchantLocation());
     merchant.setMerchantRating(new BigDecimal(0.0));
     merchant.setNumOrders(0);
     merchant.setPaymentMethod(request.paymentMethod());
     merchant.setMerchantLogo("logourl");
     merchant.setMerchantWebsite(request.merchantWebsite());
     merchant.setMerchantType(request.merchantType());
     merchant.setMerchantTags(request.merchantTags());
     return merchant;
    }

    @Override
    public UpdateMerchantResponse updateMerchant(UpdateMerchantPayloadWithMerchantId p) {
        long updated = merchantRepository.updateMerchant(
                p.merchantId(),
                p.merchantName(),
                p.merchantStatus(),
                p.contactInfo(),
                p.merchantCategory(),
                p.merchantLocation(),
                p.merchantRating(),
                p.numOrders(),
                p.paymentMethod(),
                p.merchantLogo(),
                p.merchantWebsite(),
                p.merchantType(),
                p.merchantTags()
        );

        if (updated == 0) {
            return UpdateMerchantResponseBuilder.builder()
                    .merchantId(p.merchantId().toString())
                    .message( "Merchant not found")
                    .build();
        }

        return  UpdateMerchantResponseBuilder.builder()
                .merchantId(p.merchantId().toString())
                .message( "Merchant Updated Successfully")
                .build();

    }

    @Override
    public DeleteMerchantResponse deleteMerchant(String merchantId) {

        long deleted = merchantRepository.deleteMerhcantByMerchantId(Integer.parseInt(merchantId));

        if (deleted == 0) {
            return DeleteMerchantResponseBuilder.builder()
                    .merchantId(merchantId)
                    .message( "Merchant not found")
                    .build();
        }
        return DeleteMerchantResponseBuilder.builder()
                .merchantId(merchantId)
                .message( "Merchant deltedSuccessfully")
                .build();
    }
}
