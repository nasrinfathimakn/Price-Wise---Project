package com.pricewise.service;

import com.pricewise.entity.*;
import com.pricewise.enums.AvailabilityStatus;
import com.pricewise.enums.PriceUpdateStatus;
import com.pricewise.integration.ExternalOfferDTO;
import com.pricewise.integration.OfferNormalizationService;
import com.pricewise.integration.RetailerClient;
import com.pricewise.integration.RetailerClientFactory;
import com.pricewise.repository.PriceUpdateLogRepository;
import com.pricewise.repository.ProductOfferRepository;
import com.pricewise.repository.ProductRepository;
import com.pricewise.repository.RetailerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RetailerSyncService {

    private static final Logger log = LoggerFactory.getLogger(RetailerSyncService.class);

    private final RetailerRepository retailerRepository;
    private final ProductRepository productRepository;
    private final ProductOfferRepository offerRepository;
    private final PriceUpdateLogRepository logRepository;
    private final RetailerClientFactory clientFactory;
    private final OfferNormalizationService normalizationService;
    private final ProductMatchingService matchingService;
    private final PriceHistoryService priceHistoryService;

    public RetailerSyncService(
            RetailerRepository retailerRepository,
            ProductRepository productRepository,
            ProductOfferRepository offerRepository,
            PriceUpdateLogRepository logRepository,
            RetailerClientFactory clientFactory,
            OfferNormalizationService normalizationService,
            ProductMatchingService matchingService,
            PriceHistoryService priceHistoryService) {
        this.retailerRepository = retailerRepository;
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
        this.logRepository = logRepository;
        this.clientFactory = clientFactory;
        this.normalizationService = normalizationService;
        this.matchingService = matchingService;
        this.priceHistoryService = priceHistoryService;
    }

    @Transactional
    public PriceUpdateLog syncRetailer(Long retailerId) {
        Retailer retailer = retailerRepository.findById(retailerId)
                .orElseThrow(() -> new IllegalArgumentException("Retailer not found with id: " + retailerId));

        PriceUpdateLog updateLog = new PriceUpdateLog(null, retailerId, OffsetDateTime.now(), PriceUpdateStatus.RUNNING);
        updateLog = logRepository.save(updateLog);

        int processed = 0;
        try {
            RetailerClient client = clientFactory.getClient(retailer.getCode());
            List<Product> products = productRepository.findByActiveTrue();

            for (Product product : products) {
                List<ExternalOfferDTO> externalOffers = client.fetchOffers(
                        product.getProductIdentifier(),
                        product.getModelNumber(),
                        product.getName()
                );

                for (ExternalOfferDTO extOffer : externalOffers) {
                    ProductMatchingService.MatchResult matchResult = matchingService.matchOfferToProduct(extOffer);
                    if (matchResult.getMatchedProduct().isPresent() && !matchResult.isAmbiguous()) {
                        Product matchedProduct = matchResult.getMatchedProduct().get();

                        Optional<ProductOffer> existing = offerRepository.findByProductIdAndRetailerId(matchedProduct.getId(), retailerId);
                        ProductOffer offerToSave;
                        if (existing.isPresent()) {
                            offerToSave = existing.get();
                            offerToSave.setCurrentPrice(extOffer.getPrice());
                            if (extOffer.getOriginalPrice() != null) {
                                offerToSave.setOriginalPrice(extOffer.getOriginalPrice());
                            }
                            offerToSave.setDeliveryCharge(extOffer.getDeliveryCharge());
                            offerToSave.setAvailabilityStatus(extOffer.getAvailabilityStatus());
                            offerToSave.setLastFetchedAt(OffsetDateTime.now());
                            offerToSave.setLastSuccessfulFetchAt(OffsetDateTime.now());
                        } else {
                            offerToSave = normalizationService.normalizeToOffer(extOffer, matchedProduct.getId(), retailerId);
                        }

                        if (offerToSave != null) {
                            ProductOffer savedOffer = offerRepository.save(offerToSave);
                            priceHistoryService.savePriceObservation(
                                    savedOffer.getId(),
                                    savedOffer.getCurrentPrice(),
                                    savedOffer.getDeliveryCharge(),
                                    retailer.getCode()
                            );
                            processed++;
                        }
                    }
                }
            }

            updateLog.setStatus(PriceUpdateStatus.SUCCESS);
            updateLog.setOffersProcessed(processed);
            updateLog.setCompletedAt(OffsetDateTime.now());
        } catch (Exception ex) {
            log.error("Error during sync for retailer {}: {}", retailer.getName(), ex.getMessage());
            updateLog.setStatus(PriceUpdateStatus.FAILED);
            updateLog.setOffersProcessed(processed);
            updateLog.setErrorMessage("Sync failure: " + ex.getMessage());
            updateLog.setCompletedAt(OffsetDateTime.now());
        }

        return logRepository.save(updateLog);
    }

    @Transactional
    public void syncAllRetailers() {
        List<Retailer> retailers = retailerRepository.findByActiveTrue();
        log.info("Triggering background sync for {} active retailers...", retailers.size());
        for (Retailer retailer : retailers) {
            syncRetailer(retailer.getId());
        }
    }
}

