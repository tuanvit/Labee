const axios = require('axios');

const BASE_URL = 'http://localhost:8080/api';

async function testAPI() {
    try {
        console.log('=== Testing Lazabee API ===\n');

        // Test Health Check
        console.log('1. Testing Health Check...');
        const health = await axios.get(`${BASE_URL}/health`);
        console.log('✅ Health:', health.data);
        console.log();

        // Test Database
        console.log('2. Testing Database Connection...');
        const db = await axios.get(`${BASE_URL}/test-db`);
        console.log('✅ Database:', db.data);
        console.log();

        // Test Register
        console.log('3. Testing User Registration...');
        const registerData = {
            username: 'testuser' + Date.now(),
            email: `test${Date.now()}@example.com`,
            password: '123456',
            full_name: 'Test User',
            phone: '0123456789'
        };

        const registerResponse = await axios.post(`${BASE_URL}/auth/register`, registerData);
        console.log('✅ Register:', registerResponse.data);
        console.log();

        // Test Login
        console.log('4. Testing User Login...');
        const loginData = {
            email: registerData.email,
            password: registerData.password
        };

        const loginResponse = await axios.post(`${BASE_URL}/auth/login`, loginData);
        console.log('✅ Login:', loginResponse.data);
        console.log();

        // Test Forgot Password
        console.log('5. Testing Forgot Password...');
        const forgotResponse = await axios.post(`${BASE_URL}/auth/forgot-password`, {
            email: registerData.email
        });
        console.log('✅ Forgot Password:', forgotResponse.data);
        console.log();

        // Test Verify Code
        console.log('6. Testing Verify Code...');
        const verifyResponse = await axios.post(`${BASE_URL}/auth/verify-code`, {
            code: '123456' // Mã cố định trong auth.js
        });
        console.log('✅ Verify Code:', verifyResponse.data);
        console.log();

        // Test Reset Password
        console.log('7. Testing Reset Password...');
        const resetResponse = await axios.post(`${BASE_URL}/auth/reset-password`, {
            newPassword: 'newpassword123'
        });
        console.log('✅ Reset Password:', resetResponse.data);
        console.log();

        console.log('🎉 All tests completed successfully!');

    } catch (error) {
        console.error('❌ Test failed:', error.response?.data || error.message);
    }
}

testAPI();