package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.repository.AssetRepo;
import com.example.ApexTrack.repository.EmployRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepo assetRepo;

    @Mock
    private EmployRepo employRepo;

    @Mock
    private StatusCalculationService statusCalculationService;

    @InjectMocks
    private AssetService assetService;

    private Employ testEmploy;
    private Asset testAsset;

    @BeforeEach
    void setUp() {
        testEmploy = new Employ();
        testEmploy.setId(1L);
        testEmploy.setUsername("john");

        testAsset = new Asset();
        testAsset.setId(1L);
        testAsset.setSerial_no("SN123");
        testAsset.setType("Laptop");
        testAsset.setAssigned_date(LocalDate.of(2025, 1, 1));
        testAsset.setDecommission_date(LocalDate.of(2029, 1, 1));
        testAsset.setStatus("ACTIVE");
        testAsset.setEmploy(testEmploy);
    }

    @Test
    void testGetAllAsset() {
        when(assetRepo.findAll()).thenReturn(List.of(testAsset));
        doAnswer(inv -> {
            Asset a = inv.getArgument(0);
            a.setStatus("ACTIVE");
            return a;
        }).when(statusCalculationService).calculateStatus(any(Asset.class));

        List<Asset> result = assetService.getAllAsset();
        assertThat(result).hasSize(1).contains(testAsset);
        verify(statusCalculationService, times(1)).calculateStatus(testAsset);
    }

    @Test
    void testGetAssetById_Found() {
        when(assetRepo.findById(1L)).thenReturn(Optional.of(testAsset));
        doAnswer(inv -> {
            Asset a = inv.getArgument(0);
            a.setStatus("ACTIVE");
            return a;
        }).when(statusCalculationService).calculateStatus(any(Asset.class));

        Asset result = assetService.getAssetById(1L);
        assertThat(result).isEqualTo(testAsset);
        verify(assetRepo).save(testAsset);
    }

    @Test
    void testGetAssetById_NotFound() {
        when(assetRepo.findById(99L)).thenReturn(Optional.empty());
        Asset result = assetService.getAssetById(99L);
        assertThat(result).isNull();
    }

    @Test
    void testAddAsset_Success() {
        Asset newAsset = new Asset();
        newAsset.setSerial_no("SN456");
        newAsset.setType("Monitor");
        newAsset.setEmploy(testEmploy);

        when(assetRepo.findBySerial_no("SN456")).thenReturn(null);
        when(employRepo.findById(1L)).thenReturn(Optional.of(testEmploy));
        when(assetRepo.save(any(Asset.class))).thenAnswer(inv -> inv.getArgument(0));

        Asset result = assetService.addAsset(newAsset);
        assertThat(result).isNotNull();
        assertThat(result.getAssigned_date()).isEqualTo(LocalDate.now());
        assertThat(result.getDecommission_date()).isEqualTo(LocalDate.now().plusYears(4));
        assertThat(result.getStatus()).isEqualTo("ACTIVE");
        assertThat(result.getEmploy()).isEqualTo(testEmploy);
        verify(assetRepo).save(any(Asset.class));
    }

    @Test
    void testAddAsset_SerialAlreadyExists() {
        when(assetRepo.findBySerial_no("SN123")).thenReturn(testAsset);
        Asset result = assetService.addAsset(testAsset);
        assertThat(result).isNull();
        verify(assetRepo, never()).save(any());
    }

    @Test
    void testAddAsset_EmployNotFound() {
        Asset newAsset = new Asset();
        newAsset.setSerial_no("SN789");
        newAsset.setEmploy(testEmploy);

        when(assetRepo.findBySerial_no("SN789")).thenReturn(null);
        when(employRepo.findById(1L)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> assetService.addAsset(newAsset));
        verify(assetRepo, never()).save(any());
    }

    @Test
    void testGetAssetsByEmployid() {
        when(assetRepo.findAssetsByEmploy_id(1L)).thenReturn(List.of(testAsset));
        List<Asset> result = assetService.getAssetsByEmployid(1L);
        assertThat(result).hasSize(1).contains(testAsset);
    }

    @Test
    void testDeleteAssetById_Success() {
        when(assetRepo.findById(1L)).thenReturn(Optional.of(testAsset));
        doNothing().when(assetRepo).deleteById(1L);
        String result = assetService.DeleteAssetById(1L);
        assertThat(result).isEqualTo("Success");
        verify(assetRepo).deleteById(1L);
    }

    @Test
    void testDeleteAssetById_NotFound() {
        when(assetRepo.findById(99L)).thenReturn(Optional.empty());
        String result = assetService.DeleteAssetById(99L);
        assertThat(result).isEqualTo("Asset not Found or please enter correct details");
        verify(assetRepo, never()).deleteById(any());
    }

    @Test
    void testUpdateAsset_Success() {
        Asset existing = new Asset();
        existing.setId(1L);
        existing.setSerial_no("OLD");
        existing.setType("OldType");
        existing.setAssigned_date(LocalDate.of(2020, 1, 1));
        existing.setDecommission_date(LocalDate.of(2024, 1, 1));
        existing.setEmploy(testEmploy);

        when(assetRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(assetRepo.save(any(Asset.class))).thenAnswer(inv -> inv.getArgument(0));
        doAnswer(inv -> inv.getArgument(0)).when(statusCalculationService).calculateStatus(any(Asset.class));

        Asset update = new Asset();
        update.setSerial_no("NEW_SN");
        update.setType("NewType");
        update.setAssigned_date(LocalDate.of(2025, 1, 1));

        Asset result = assetService.updateAsset(1L, update);
        assertThat(result).isNotNull();
        assertThat(result.getSerial_no()).isEqualTo("NEW_SN");
        assertThat(result.getType()).isEqualTo("NewType");
        assertThat(result.getAssigned_date()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(result.getDecommission_date()).isEqualTo(LocalDate.of(2029, 1, 1));
        assertThat(result.getEmploy()).isEqualTo(testEmploy);
    }

    @Test
    void testUpdateAsset_NotFound() {
        when(assetRepo.findById(99L)).thenReturn(Optional.empty());
        Asset result = assetService.updateAsset(99L, testAsset);
        assertThat(result).isNull();
        verify(assetRepo, never()).save(any());
    }

    @Test
    void testGetActiveAssets() {
        when(assetRepo.findAssetsWithStatusActive()).thenReturn(List.of(testAsset));
        List<Asset> result = assetService.getActiveAssets();
        assertThat(result).hasSize(1).contains(testAsset);
    }

    @Test
    void testGetDecommissionedAssets() {
        when(assetRepo.findAssetsWithStatusDecommissioned()).thenReturn(List.of(testAsset));
        List<Asset> result = assetService.getDecommissionedAssets();
        assertThat(result).hasSize(1).contains(testAsset);
    }
}