package com.example.ApexTrack.Controller;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.Service.AssetService;
import com.example.ApexTrack.Service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetService;

    @MockitoBean
    private JWTService jwtService;   // needed for context

    @Autowired
    private ObjectMapper objectMapper;

    private Asset createTestAsset() {
        Employ employ = new Employ();
        employ.setId(1L);
        Asset asset = new Asset();
        asset.setId(1L);
        asset.setSerial_no("SN123");
        asset.setType("Laptop");
        asset.setAssigned_date(LocalDate.of(2025, 1, 1));
        asset.setDecommission_date(LocalDate.of(2029, 1, 1));
        asset.setStatus("ACTIVE");
        asset.setEmploy(employ);
        return asset;
    }

    @Test
    void testGetAllAssets() throws Exception {
        when(assetService.getAllAsset()).thenReturn(List.of(createTestAsset()));

        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].serial_no").value("SN123"));
    }

    @Test
    void testGetAssetById_Found() throws Exception {
        Asset asset = createTestAsset();
        when(assetService.getAssetById(1L)).thenReturn(asset);

        mockMvc.perform(get("/api/assets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetAssetById_NotFound() throws Exception {
        when(assetService.getAssetById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/assets/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAssetByEmployid() throws Exception {
        when(assetService.getAssetsByEmployid(1L)).thenReturn(List.of(createTestAsset()));

        mockMvc.perform(get("/api/assets/employ_id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testAddAsset_Success() throws Exception {
        Asset asset = createTestAsset();
        when(assetService.addAsset(any(Asset.class))).thenReturn(asset);

        // We need to send JSON with employ as { "id": 1 } because of @JsonProperty on employ field
        String json = "{\"serial_no\":\"SN123\",\"type\":\"Laptop\",\"assigned_date\":\"2025-01-01\",\"employ\":{\"id\":1}}";

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testAddAsset_AlreadyExists() throws Exception {
        when(assetService.addAsset(any(Asset.class))).thenReturn(null);

        String json = "{\"serial_no\":\"SN123\",\"type\":\"Laptop\",\"assigned_date\":\"2025-01-01\",\"employ\":{\"id\":1}}";

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isAlreadyReported());
    }

    @Test
    void testUpdateAsset_Success() throws Exception {
        Asset updated = createTestAsset();
        when(assetService.updateAsset(eq(1L), any(Asset.class))).thenReturn(updated);

        String json = "{\"serial_no\":\"SN123\",\"type\":\"Laptop\",\"assigned_date\":\"2025-01-01\"}";

        mockMvc.perform(put("/api/assets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateAsset_NotFound() throws Exception {
        when(assetService.updateAsset(eq(99L), any(Asset.class))).thenReturn(null);

        String json = "{\"serial_no\":\"SN123\",\"type\":\"Laptop\"}";

        mockMvc.perform(put("/api/assets/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAsset_Success() throws Exception {
        when(assetService.DeleteAssetById(1L)).thenReturn("Success");

        mockMvc.perform(delete("/api/assets/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void testDeleteAsset_NotFound() throws Exception {
        when(assetService.DeleteAssetById(99L)).thenReturn("Asset not Found or please enter correct details");

        mockMvc.perform(delete("/api/assets/99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Asset not Found or please enter correct details"));
    }

    @Test
    void testGetActiveAssets() throws Exception {
        when(assetService.getActiveAssets()).thenReturn(List.of(createTestAsset()));

        mockMvc.perform(get("/api/assets/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void testGetDecommissionedAssets() throws Exception {
        when(assetService.getDecommissionedAssets()).thenReturn(List.of(createTestAsset()));

        mockMvc.perform(get("/api/assets/decommissioned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }
}