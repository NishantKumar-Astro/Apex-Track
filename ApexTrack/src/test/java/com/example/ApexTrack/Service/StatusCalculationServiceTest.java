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
class StatusCalculationServiceTest {

    @Mock
    private AssetRepo assetRepo;

    @InjectMocks
    private StatusCalculationService statusCalculationService;

    private Asset activeAsset;
    private Asset decommissionedAsset;

    @BeforeEach
    void setUp() {
        activeAsset = new Asset();
        activeAsset.setId(1L);
        activeAsset.setDecommission_date(LocalDate.now().plusDays(10));

        decommissionedAsset = new Asset();
        decommissionedAsset.setId(2L);
        decommissionedAsset.setDecommission_date(LocalDate.now().minusDays(1));
    }

    @Test
    void testCalculateStatus_Active() {
        Asset result = statusCalculationService.calculateStatus(activeAsset);
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testCalculateStatus_Decommissioned() {
        Asset result = statusCalculationService.calculateStatus(decommissionedAsset);
        assertThat(result.getStatus()).isEqualTo("DECOMMISSIONED");
    }

    @Test
    void testCalculateStatus_NullDate() {
        activeAsset.setDecommission_date(null);
        Asset result = statusCalculationService.calculateStatus(activeAsset);
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testGetAssetsNeedingUpgrade() {
        List<Asset> mockList = List.of(activeAsset);
        when(assetRepo.findAssetsNeedingUpgradeNative()).thenReturn(mockList);
        List<Asset> result = statusCalculationService.getAssetsNeedingUpgrade();
        assertThat(result).isEqualTo(mockList);
    }

    @Test
    void testIsUpgradeNeeded_True() {
        when(assetRepo.findAssetsNeedingUpgradeById(1L)).thenReturn(List.of(activeAsset));
        Boolean result = statusCalculationService.isUpgradeNeeded(1L);
        assertThat(result).isTrue();
    }

    @Test
    void testIsUpgradeNeeded_False() {
        when(assetRepo.findAssetsNeedingUpgradeById(2L)).thenReturn(null);
        Boolean result = statusCalculationService.isUpgradeNeeded(2L);
        assertThat(result).isFalse();
    }
}