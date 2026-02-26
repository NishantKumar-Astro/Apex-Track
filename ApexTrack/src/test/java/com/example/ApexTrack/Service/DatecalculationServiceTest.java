package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.repository.AssetRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DatecalculationServiceTest {

    @Mock
    private AssetRepo assetRepo;

    @InjectMocks
    private DatecalculationService dateService;

    private Asset asset;

    @BeforeEach
    void setUp() {
        asset = new Asset();
        asset.setId(1L);
        asset.setAssigned_date(LocalDate.of(2023, 1, 1));
        asset.setDecommission_date(LocalDate.of(2027, 1, 1));
    }

    @Test
    void getDaysRemaining_FutureDate_ShouldReturnPositive() {
        Integer days = dateService.getDaysRemaining(asset);
        // Assuming today is before 2027-01-01, days should be >0
        assertThat(days).isNotNull().isPositive();
    }

    @Test
    void getDaysRemaining_PastDate_ShouldReturnZero() {
        asset.setDecommission_date(LocalDate.now().minusDays(5));
        Integer days = dateService.getDaysRemaining(asset);
        assertThat(days).isZero();
    }

    @Test
    void getEquipmentAgeMonths_ShouldReturnPositive() {
        Integer months = dateService.getEquipmentAgeMonths(asset);
        assertThat(months).isNotNull().isPositive();
    }

    @Test
    void getActiveAssets_ShouldReturnList() {
        when(assetRepo.findAssetsWithStatusActive()).thenReturn(List.of(asset));
        List<Asset> result = dateService.getActiveAssets();
        assertThat(result).hasSize(1);
    }

    @Test
    void getDecommissionedAssets_ShouldReturnList() {
        when(assetRepo.findAssetsWithStatusDecommissioned()).thenReturn(List.of(asset));
        List<Asset> result = dateService.getDecommissionedAssets();
        assertThat(result).hasSize(1);
    }
}