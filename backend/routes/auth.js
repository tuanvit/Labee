const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const nodemailer = require('nodemailer');
const pool = require('../config/database-test');

const router = express.Router();

// Tạo transporter cho email
const transporter = nodemailer.createTransport({
    host: process.env.EMAIL_HOST,
    port: process.env.EMAIL_PORT,
    secure: false,
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASSWORD
    }
});

// Đăng ký
router.post('/register', async (req, res) => {
    try {
        const { username, email, password, full_name, phone } = req.body;

        // Kiểm tra user đã tồn tại
        const existingUser = await pool.query(
            'SELECT * FROM users WHERE email = $1 OR username = $2',
            [email, username]
        );

        if (existingUser.rows.length > 0) {
            return res.status(400).json({
                success: false,
                message: 'Email hoặc tên đăng nhập đã tồn tại'
            });
        }

        // Hash password
        const hashedPassword = await bcrypt.hash(password, 10);

        // Tạo user mới
        const newUser = await pool.query(
            'INSERT INTO users (username, email, password, full_name, phone) VALUES ($1, $2, $3, $4, $5) RETURNING id, username, email, full_name, phone, created_at',
            [username, email, hashedPassword, full_name, phone]
        );

        // Tạo JWT token
        const token = jwt.sign(
            { userId: newUser.rows[0].id },
            process.env.JWT_SECRET,
            { expiresIn: process.env.JWT_EXPIRES_IN }
        );

        res.status(201).json({
            success: true,
            message: 'Đăng ký thành công',
            token,
            user: newUser.rows[0]
        });

    } catch (error) {
        console.error('Lỗi đăng ký:', error);
        res.status(500).json({
            success: false,
            message: 'Lỗi server'
        });
    }
});

// Đăng nhập
router.post('/login', async (req, res) => {
    try {
        const { email, password } = req.body;

        // Tìm user
        const user = await pool.query(
            'SELECT * FROM users WHERE email = $1',
            [email]
        );

        if (user.rows.length === 0) {
            return res.status(401).json({
                success: false,
                message: 'Email hoặc mật khẩu không đúng'
            });
        }

        // Kiểm tra password
        const validPassword = await bcrypt.compare(password, user.rows[0].password);

        if (!validPassword) {
            return res.status(401).json({
                success: false,
                message: 'Email hoặc mật khẩu không đúng'
            });
        }

        // Tạo JWT token
        const token = jwt.sign(
            { userId: user.rows[0].id },
            process.env.JWT_SECRET,
            { expiresIn: process.env.JWT_EXPIRES_IN }
        );

        // Loại bỏ password khỏi response
        const { password: _, ...userWithoutPassword } = user.rows[0];

        res.json({
            success: true,
            message: 'Đăng nhập thành công',
            token,
            user: userWithoutPassword
        });

    } catch (error) {
        console.error('Lỗi đăng nhập:', error);
        res.status(500).json({
            success: false,
            message: 'Lỗi server'
        });
    }
});

// Quên mật khẩu
router.post('/forgot-password', async (req, res) => {
    try {
        const { email } = req.body;

        // Kiểm tra email tồn tại
        const user = await pool.query(
            'SELECT * FROM users WHERE email = $1',
            [email]
        );

        if (user.rows.length === 0) {
            return res.status(404).json({
                success: false,
                message: 'Email không tồn tại trong hệ thống'
            });
        }

        // Tạo mã xác nhận 6 số
        const verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
        const expiresAt = new Date(Date.now() + 15 * 60 * 1000); // 15 phút

        // Lưu mã vào database
        await pool.query(
            'INSERT INTO password_resets (email, code, expires_at) VALUES ($1, $2, $3)',
            [email, verificationCode, expiresAt]
        );

        // Gửi email
        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: email,
            subject: 'Mã xác nhận đặt lại mật khẩu - Lazabee',
            html: `
                <h2>Đặt lại mật khẩu</h2>
                <p>Mã xác nhận của bạn là: <strong>${verificationCode}</strong></p>
                <p>Mã này sẽ hết hạn sau 15 phút.</p>
                <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
            `
        };

        await transporter.sendMail(mailOptions);

        res.json({
            success: true,
            message: 'Mã xác nhận đã được gửi đến email của bạn'
        });

    } catch (error) {
        console.error('Lỗi quên mật khẩu:', error);
        res.status(500).json({
            success: false,
            message: 'Lỗi server'
        });
    }
});

// Xác nhận mã
router.post('/verify-code', async (req, res) => {
    try {
        const { code } = req.body;

        // Tìm mã xác nhận
        const resetRecord = await pool.query(
            'SELECT * FROM password_resets WHERE code = $1 AND used = false AND expires_at > NOW()',
            [code]
        );

        if (resetRecord.rows.length === 0) {
            return res.status(400).json({
                success: false,
                message: 'Mã xác nhận không hợp lệ hoặc đã hết hạn'
            });
        }

        res.json({
            success: true,
            message: 'Mã xác nhận hợp lệ'
        });

    } catch (error) {
        console.error('Lỗi xác nhận mã:', error);
        res.status(500).json({
            success: false,
            message: 'Lỗi server'
        });
    }
});

// Đặt lại mật khẩu
router.post('/reset-password', async (req, res) => {
    try {
        const { newPassword } = req.body;

        // Tìm mã xác nhận gần nhất chưa sử dụng
        const resetRecord = await pool.query(
            'SELECT * FROM password_resets WHERE used = false AND expires_at > NOW() ORDER BY created_at DESC LIMIT 1'
        );

        if (resetRecord.rows.length === 0) {
            return res.status(400).json({
                success: false,
                message: 'Không tìm thấy yêu cầu đặt lại mật khẩu hợp lệ'
            });
        }

        const email = resetRecord.rows[0].email;

        // Hash mật khẩu mới
        const hashedPassword = await bcrypt.hash(newPassword, 10);

        // Cập nhật mật khẩu
        await pool.query(
            'UPDATE users SET password = $1, updated_at = NOW() WHERE email = $2',
            [hashedPassword, email]
        );

        // Đánh dấu mã đã sử dụng
        await pool.query(
            'UPDATE password_resets SET used = true WHERE id = $1',
            [resetRecord.rows[0].id]
        );

        res.json({
            success: true,
            message: 'Đặt lại mật khẩu thành công'
        });

    } catch (error) {
        console.error('Lỗi đặt lại mật khẩu:', error);
        res.status(500).json({
            success: false,
            message: 'Lỗi server'
        });
    }
});

// Đăng xuất
router.post('/logout', (req, res) => {
    res.json({
        success: true,
        message: 'Đăng xuất thành công'
    });
});

module.exports = router;