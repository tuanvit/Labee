-- Migration: Update Address table field names
-- From: address_line, city
-- To: detail_address, province

USE labee_db;

-- Rename columns
ALTER TABLE addresses 
    CHANGE COLUMN address_line detail_address VARCHAR(300) NOT NULL,
    CHANGE COLUMN city province VARCHAR(100) NOT NULL;

-- Verify the change
DESCRIBE addresses;
