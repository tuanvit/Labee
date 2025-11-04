const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const authRoutes = require('./routes/auth-simple');

dotenv.config();

const app = express();
const PORT = process.env.PORT || 8080;

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Routes
app.use('/api/auth', authRoutes);

// Health check endpoint
app.get('/api/health', (req, res) => {
    res.json({
        status: 'OK',
        message: 'Lazabee API is running',
        timestamp: new Date().toISOString()
    });
});

// Test database connection
app.get('/api/test-db', async (req, res) => {
    try {
        const pool = require('./config/database-test');
        const result = await pool.query('SELECT NOW()');
        res.json({
            success: true,
            message: 'Database connection successful',
            timestamp: result.rows[0].now
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            message: 'Database connection failed',
            error: error.message
        });
    }
});

// Test simple register
app.post('/api/test-register', async (req, res) => {
    try {
        console.log('Test register request:', req.body);
        const pool = require('./config/database-test');

        // Test query
        const result = await pool.query('SELECT COUNT(*) FROM users');
        console.log('Current users count:', result.rows[0].count);

        res.json({
            success: true,
            message: 'Test register endpoint working',
            usersCount: result.rows[0].count,
            receivedData: req.body
        });
    } catch (error) {
        console.error('Test register error:', error);
        res.status(500).json({
            success: false,
            message: 'Test register failed',
            error: error.message
        });
    }
});

// Error handling middleware
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        success: false,
        message: 'Có lỗi xảy ra trên server'
    });
});

// 404 handler
app.use('*', (req, res) => {
    res.status(404).json({
        success: false,
        message: 'Endpoint không tồn tại'
    });
});

app.listen(PORT, () => {
    console.log(`Server đang chạy trên port ${PORT}`);
    console.log(`Health check: http://localhost:${PORT}/api/health`);
});