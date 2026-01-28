-- src/main/resources/data.sql
INSERT INTO asset (asset_serial_no, type, department, employ_name, purchase_date, decommission_date, status)
VALUES
('LAP-001', 'Laptop', 'Engineering', 'John Doe', '2023-01-15', '2027-01-15', 'ACTIVE'),
('SRV-001', 'Server', 'IT', 'Jane Smith', '2022-06-01', '2026-06-01', 'ACTIVE'),
('MON-001', 'Monitor', 'HR', 'Bob Johnson', '2024-03-10', '2028-03-10', 'ACTIVE');

---- You can also create indexes for better performance
--CREATE INDEX idx_asset_serial ON asset(asset_serial_no);
--CREATE INDEX idx_asset_status ON asset(status);
--CREATE INDEX idx_decommission_date ON asset(decommission_date);