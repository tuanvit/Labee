const { Pool } = require('pg');

const pool = new Pool({
    host: 'localhost',
    port: 5432,
    database: 'lazabee_db',
    user: 'postgres',
    password: '100904', // Hardcode để test
    max: 20,
    idleTimeoutMillis: 30000,
    connectionTimeoutMillis: 2000,
});

// Test connection
pool.on('connect', () => {
    console.log('Kết nối PostgreSQL thành công');
});

pool.on('error', (err) => {
    console.error('Lỗi kết nối PostgreSQL:', err);
});

module.exports = pool;